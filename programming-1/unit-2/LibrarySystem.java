import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class Book {
    String title;
    String author;
    int quantity;

    Book(String title, String author, int quantity) {
        this.title = title;
        this.author = author;
        this.quantity = quantity;
    }
}

public class LibrarySystem {

    private static Map<String, Book> library = new HashMap<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            displayMenu();
            int choice = getIntInput("Choose an option: ");

            switch (choice) {
                case 1:
                    addBook();
                    break;
                case 2:
                    borrowBook();
                    break;
                case 3:
                    returnBook();
                    break;
                case 4:
                    System.out.println("Exiting the program. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option. Please choose between 1 and 4.");
            }
        }
    }

    private static void displayMenu() {
        System.out.println("\n=== Library System Menu ===");
        System.out.println("1. Add Books");
        System.out.println("2. Borrow Books");
        System.out.println("3. Return Books");
        System.out.println("4. Exit");
    }

    private static void addBook() {
        System.out.print("Enter book title: ");
        String title = scanner.nextLine().trim();

        System.out.print("Enter author name: ");
        String author = scanner.nextLine().trim();

        int quantity = getIntInput("Enter quantity: ");

        if (quantity <= 0) {
            System.out.println("Quantity must be greater than zero.");
            return;
        }

        if (library.containsKey(title)) {
            Book existingBook = library.get(title);
            existingBook.quantity += quantity;
            System.out.println("Book already exists. Quantity updated.");
        } else {
            library.put(title, new Book(title, author, quantity));
            System.out.println("New book added successfully.");
        }
    }

    private static void borrowBook() {
        System.out.print("Enter book title to borrow: ");
        String title = scanner.nextLine().trim();

        if (!library.containsKey(title)) {
            System.out.println("Error: Book not found in the library.");
            return;
        }

        int quantity = getIntInput("Enter number of books to borrow: ");

        if (quantity <= 0) {
            System.out.println("Quantity must be greater than zero.");
            return;
        }

        Book book = library.get(title);

        if (book.quantity >= quantity) {
            book.quantity -= quantity;
            System.out.println("Book borrowed successfully.");
        } else {
            System.out.println("Error: Not enough books available.");
        }
    }

    private static void returnBook() {
        System.out.print("Enter book title to return: ");
        String title = scanner.nextLine().trim();

        if (!library.containsKey(title)) {
            System.out.println("Error: This book does not belong to the library.");
            return;
        }

        int quantity = getIntInput("Enter number of books to return: ");

        if (quantity <= 0) {
            System.out.println("Quantity must be greater than zero.");
            return;
        }

        Book book = library.get(title);
        book.quantity += quantity;
        System.out.println("Book returned successfully.");
    }

    private static int getIntInput(String message) {
        while (true) {
            try {
                System.out.print(message);
                int value = Integer.parseInt(scanner.nextLine().trim());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }
}