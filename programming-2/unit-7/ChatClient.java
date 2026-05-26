import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * Online Chat Application — Client
 *
 * Connects to ChatServer on localhost:12345 using a TCP socket.
 * Once connected, the server assigns a unique user ID and the client
 * can send and receive messages in real time.
 *
 * Java's socket API makes this straightforward: as Eck (2022,
 * Section 11.4) explains, a Socket connection gives both sides a
 * standard InputStream and OutputStream, so network communication
 * works the same way as reading from or writing to a file.
 *
 * Start ChatServer before running this class.
 *
 * @author Hee Moon
 */
public class ChatClient {

    private static final String HOST = "localhost";
    private static final int PORT = 12345;

    public static void main(String[] args) {
        System.out.println("Connecting to " + HOST + ":" + PORT + "...\n");

        // try-with-resources closes the socket, streams, and scanner
        // automatically when the block exits, even if an exception occurs.
        // This is the pattern recommended in Eck (2022, Section 11.2) for
        // ensuring I/O resources are always released.
        try (
                Socket socket = new Socket(HOST, PORT);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                Scanner scanner = new Scanner(System.in)) {
            // Two threads are needed here: one to send (this thread) and one
            // to receive (MessageReceiver). Without a separate receiver thread,
            // the input prompt would block while waiting for the user to type,
            // and incoming messages would never be displayed.
            Thread receiver = new Thread(new MessageReceiver(in));
            receiver.setDaemon(true);
            receiver.start();

            while (true) {
                String input = scanner.nextLine();
                out.println(input);
                if (input.equalsIgnoreCase("exit")) {
                    System.out.println("Disconnecting...");
                    break;
                }
            }

        } catch (ConnectException e) {
            // ConnectException is a subclass of IOException thrown specifically
            // when the connection is refused — i.e. no server is listening on
            // the given host and port. Catching it separately gives a clearer
            // error message than the generic IOException handler below.
            System.err.println("Could not connect. Is ChatServer running?");
        } catch (IOException e) {
            System.err.println("Connection error: " + e.getMessage());
        }

        System.out.println("Goodbye!");
    }
}

/**
 * Reads messages from the server and prints them to the console.
 *
 * Runs in a background daemon thread so incoming messages are displayed
 * immediately without blocking the main thread's input loop.
 * Exits when the server closes the connection (readLine() returns null)
 * or when an IOException is thrown — the same null-check pattern used
 * for reading text files in Eck (2022, Section 11.2).
 *
 * @author Hee Moon
 */
class MessageReceiver implements Runnable {

    private final BufferedReader in;

    MessageReceiver(BufferedReader in) {
        this.in = in;
    }

    @Override
    public void run() {
        try {
            String message;
            // readLine() returns null at end-of-stream, which happens when
            // the server closes the connection. This is the standard loop
            // for reading all lines from a stream (Eck, 2022, Section 11.2).
            while ((message = in.readLine()) != null) {
                System.out.println(message);
            }
        } catch (IOException e) {
            System.out.println("Server connection closed.");
        }
    }
}