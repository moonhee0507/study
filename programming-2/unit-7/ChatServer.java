import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * Online Chat Application — Server
 *
 * Listens on port 12345 for incoming client connections.
 * Each client is assigned a unique user ID and handled in its own thread.
 * Messages from any client are broadcast to all connected clients.
 *
 * As Eck (2022, Section 11.4) explains, network communication in Java
 * uses the same stream-based model as file I/O: once a connection is
 * established, the server reads from and writes to the socket through
 * standard input/output streams.
 *
 * Run this class before starting any ChatClient instances.
 *
 * @author Hee Moon
 */
public class ChatServer {

    private static final int PORT = 12345;

    // Maps each connected client's userID to its output stream.
    // ConcurrentHashMap is used instead of HashMap to provide
    // thread-safe access to the client registry. This is appropriate
    // because multiple ClientHandler threads may register clients,
    // remove clients, and iterate over the registry during broadcasting.
    private static final Map<String, PrintWriter> clients = new ConcurrentHashMap<>();

    private static int userCounter = 0;

    public static void main(String[] args) {
        System.out.println("Chat Server started on port " + PORT);
        System.out.println("Waiting for connections...\n");

        // ServerSocket listens on the given port. When a client calls
        // new Socket(host, port), serverSocket.accept() unblocks and
        // returns a new Socket representing that specific connection.
        // This mirrors the client-server model described in
        // Eck (2022, Section 11.4) and demonstrated in Pankaj (2022).
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                String userID = nextUserID();
                System.out.println("[+] " + userID + " connected from "
                        + clientSocket.getInetAddress().getHostAddress());

                // Each client gets its own thread so the server can handle
                // multiple connections simultaneously. Eck (2022, Section 11.4)
                // notes that a single-threaded server can only serve one client
                // at a time; spawning a thread per connection is the standard
                // fix for this limitation.
                Thread t = new Thread(new ClientHandler(clientSocket, userID));
                t.setDaemon(true); // daemon threads exit automatically when main() ends
                t.start();
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    // synchronized so that userCounter increments atomically across threads,
    // ensuring no two clients ever receive the same ID.
    private static synchronized String nextUserID() {
        return "User" + (++userCounter);
    }

    static synchronized void registerClient(String userID, PrintWriter writer) {
        clients.put(userID, writer);
    }

    static synchronized void removeClient(String userID) {
        clients.remove(userID);
    }

    /**
     * Sends a message to every connected client.
     *
     * Synchronized to prevent interleaved output when multiple threads
     * call broadcast() at the same time.
     *
     * @param message the message to send
     */
    static synchronized void broadcast(String message) {
        System.out.println(message);
        for (PrintWriter writer : clients.values()) {
            writer.println(message);
        }
    }

    static int getClientCount() {
        return clients.size();
    }

    // -------------------------------------------------------------------------

    /**
     * Handles one connected client: registers it, relays its messages,
     * and cleans up when it disconnects.
     *
     * Implements Runnable so each instance can run in its own thread,
     * following the multithreaded server pattern described in
     * Eck (2022, Section 11.4).
     *
     * @author Hee Moon
     */
    static class ClientHandler implements Runnable {

        private final Socket socket;
        private final String userID;

        ClientHandler(Socket socket, String userID) {
            this.socket = socket;
            this.userID = userID;
        }

        @Override
        public void run() {
            // Special handling: getInputStream() and getOutputStream() throw
            // a checked IOException, but Runnable.run() cannot declare
            // "throws IOException". Placing stream setup inside the try block
            // lets the existing catch(IOException) clause handle it naturally,
            // without needing a separate try-with-resources header.
            try {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(
                        socket.getOutputStream(), true);

                ChatServer.registerClient(userID, out);

                out.println("Welcome! You joined as " + userID);
                out.println("Online: " + ChatServer.getClientCount() + " user(s)");
                out.println("Type a message and press Enter. Type 'exit' to quit.\n");

                ChatServer.broadcast("[Server] " + userID + " joined the chat.");

                // readLine() returns null when the client closes the connection,
                // which is how we detect a disconnect without an explicit signal.
                // This is the same pattern shown in Eck (2022, Section 11.2)
                // for reading text files line by line.
                String line;
                while ((line = in.readLine()) != null) {
                    if (line.equalsIgnoreCase("exit"))
                        break;
                    ChatServer.broadcast("[" + userID + "] " + line);
                }

            } catch (IOException e) {
                System.out.println("[-] Connection lost: " + userID);
            } finally {
                // finally ensures cleanup runs whether we exited the loop
                // normally (exit command), via null (client disconnected),
                // or via an IOException (network error).
                ChatServer.removeClient(userID);
                ChatServer.broadcast("[Server] " + userID + " left the chat.");
                System.out.println("[-] " + userID + " disconnected. "
                        + "Active: " + ChatServer.getClientCount());
                // Closing the socket also closes its InputStream and
                // OutputStream, so no need to close in and out separately.
                try {
                    socket.close();
                } catch (IOException ignored) {
                }
            }
        }
    }
}