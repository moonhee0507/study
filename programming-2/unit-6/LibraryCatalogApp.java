import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Exception thrown when a requested library item cannot be found in the catalog.
 *
 * <p>This exception is used to handle scenarios where an operation
 * such as removal or retrieval is attempted on an item that does
 * not exist in the catalog.</p>
 *
 * @author Hee Moon
 */
class ItemNotFoundException extends Exception {

    /**
     * Constructs a new ItemNotFoundException with a detail message.
     *
     * @param itemID the ID of the item that was not found
     */
    ItemNotFoundException(String itemID) {
        super("Item with ID \"" + itemID + "\" was not found in the catalog.");
    }
}


/**
 * Represents a book in the library.
 *
 * <p>Used as a concrete type for {@code LibraryItem<Book>}.</p>
 *
 * @author Hee Moon
 */
class Book {

    /** ISBN of the book. */
    private final String isbn;

    /** Genre of the book. */
    private final String genre;

    /**
     * Constructs a new Book.
     *
     * @param isbn  the ISBN of the book
     * @param genre the genre of the book
     */
    Book(String isbn, String genre) {
        this.isbn  = isbn;
        this.genre = genre;
    }

    /** @return the ISBN of the book */
    public String getIsbn()  { return isbn; }

    /** @return the genre of the book */
    public String getGenre() { return genre; }

    /**
     * Returns a string representation of the book.
     *
     * @return formatted string with ISBN and genre
     */
    @Override
    public String toString() {
        return String.format("Book(ISBN: %s, Genre: %s)", isbn, genre);
    }
}


/**
 * Represents a DVD in the library.
 *
 * <p>Used as a concrete type for {@code LibraryItem<DVD>}.</p>
 *
 * @author Hee Moon
 */
class DVD {

    /** Duration of the DVD in minutes. */
    private final int durationMinutes;

    /** Director of the DVD. */
    private final String director;

    /**
     * Constructs a new DVD.
     *
     * @param durationMinutes the duration of the DVD in minutes
     * @param director        the director of the DVD
     */
    DVD(int durationMinutes, String director) {
        this.durationMinutes = durationMinutes;
        this.director        = director;
    }

    /** @return the duration of the DVD in minutes */
    public int    getDurationMinutes() { return durationMinutes; }

    /** @return the director of the DVD */
    public String getDirector()        { return director; }

    /**
     * Returns a string representation of the DVD.
     *
     * @return formatted string with duration and director
     */
    @Override
    public String toString() {
        return String.format("DVD(Duration: %d min, Director: %s)", durationMinutes, director);
    }
}


/**
 * Represents a magazine in the library.
 *
 * <p>Used as a concrete type for {@code LibraryItem<Magazine>}.</p>
 *
 * @author Hee Moon
 */
class Magazine {

    /** Issue number of the magazine. */
    private final int issueNumber;

    /** Publication month of the magazine. */
    private final String month;

    /**
     * Constructs a new Magazine.
     *
     * @param issueNumber the issue number of the magazine
     * @param month       the publication month
     */
    Magazine(int issueNumber, String month) {
        this.issueNumber = issueNumber;
        this.month       = month;
    }

    /** @return the issue number of the magazine */
    public int    getIssueNumber() { return issueNumber; }

    /** @return the publication month */
    public String getMonth()       { return month; }

    /**
     * Returns a string representation of the magazine.
     *
     * @return formatted string with issue number and month
     */
    @Override
    public String toString() {
        return String.format("Magazine(Issue: %d, Month: %s)", issueNumber, month);
    }
}


/**
 * A generic class representing a single library item.
 *
 * <p>The type parameter {@code T} represents the category of the item
 * (e.g., Book, DVD, Magazine). This allows the catalog to store
 * different item types while maintaining compile-time type safety.</p>
 *
 * @param <T> the type of the library item category
 * @author Hee Moon
 */
class LibraryItem<T> {

    /** Unique identifier for the library item. */
    private final String itemID;

    /** Title of the library item. */
    private final String title;

    /** Author or creator of the library item. */
    private final String author;

    /** The actual item object (e.g., Book, DVD, Magazine). */
    private final T item;

    /**
     * Constructs a new LibraryItem with the given attributes.
     *
     * @param itemID unique identifier for the item
     * @param title  title of the item
     * @param author author or creator of the item
     * @param item   the item object of type T
     */
    LibraryItem(String itemID, String title, String author, T item) {
        this.itemID  = itemID;
        this.title   = title;
        this.author  = author;
        this.item    = item;
    }

    /** @return the unique identifier of this item */
    public String getItemID() { return itemID; }

    /** @return the title of this item */
    public String getTitle()  { return title; }

    /** @return the author of this item */
    public String getAuthor() { return author; }

    /** @return the item object of type T */
    public T      getItem()   { return item; }

    /**
     * Returns a formatted string representation of the library item.
     *
     * @return string containing itemID, title, author, and item type
     */
    @Override
    public String toString() {
        return String.format(
            "[ID: %s] Title: \"%s\" | Author: %s | Type: %s",
            itemID, title, author, item.getClass().getSimpleName()
        );
    }
}


/**
 * A generic catalog class that stores and manages library items.
 *
 * <p>The type parameter {@code T} represents the category of library items
 * stored in this catalog (e.g., Book, DVD, Magazine). The same catalog
 * implementation can be reused for any item type while maintaining
 * compile-time type safety.</p>
 *
 * <p>Supported operations:</p>
 * <ul>
 *   <li>Add a new library item</li>
 *   <li>Remove an existing item by ID</li>
 *   <li>Retrieve item details by ID</li>
 *   <li>Display all items in the catalog</li>
 * </ul>
 *
 * @param <T> the type of items stored in this catalog
 * @author Hee Moon
 */
class Catalog<T> {

    /** Internal list for storing library items. */
    private final List<LibraryItem<T>> items;

    /** Display name of this catalog (e.g., "Book Catalog"). */
    private final String catalogName;

    /**
     * Constructs a new empty Catalog with the given name.
     *
     * @param catalogName the display name of this catalog
     */
    Catalog(String catalogName) {
        this.catalogName = catalogName;
        this.items       = new ArrayList<>();
    }

    /**
     * Adds a new library item to the catalog.
     *
     * <p>If an item with the same ID already exists, the item will
     * not be added and a warning message will be printed.</p>
     *
     * @param item the LibraryItem to add
     */
    public void addItem(LibraryItem<T> item) {
        for (LibraryItem<T> existing : items) {
            if (existing.getItemID().equals(item.getItemID())) {
                System.out.println(
                    "[Warning] Item with ID \"" + item.getItemID()
                    + "\" already exists. Skipping."
                );
                return;
            }
        }
        items.add(item);
        System.out.println("[Added] " + item);
    }

    /**
     * Removes a library item from the catalog by its item ID.
     *
     * @param itemID the unique ID of the item to remove
     * @throws ItemNotFoundException if no item with the given ID exists
     */
    public void removeItem(String itemID) throws ItemNotFoundException {
        LibraryItem<T> target = findByID(itemID);
        items.remove(target);
        System.out.println("[Removed] " + target);
    }

    /**
     * Retrieves and displays the details of a library item by its ID.
     *
     * @param itemID the unique ID of the item to retrieve
     * @throws ItemNotFoundException if no item with the given ID exists
     */
    public void getItemDetails(String itemID) throws ItemNotFoundException {
        LibraryItem<T> target = findByID(itemID);
        System.out.println("[Details] " + target);
        System.out.println("          Item Info: " + target.getItem());
    }

    /**
     * Displays all items currently stored in the catalog.
     *
     * <p>If the catalog is empty, a message is printed to indicate so.</p>
     */
    public void displayAll() {
        System.out.println("\n===== " + catalogName + " =====");
        if (items.isEmpty()) {
            System.out.println("  (The catalog is empty.)");
        } else {
            for (int i = 0; i < items.size(); i++) {
                System.out.println("  " + (i + 1) + ". " + items.get(i));
            }
        }
        System.out.println("==============================\n");
    }

    /**
     * Returns the number of items currently in the catalog.
     *
     * @return the item count
     */
    public int size() {
        return items.size();
    }

    /**
     * Finds a library item by its ID.
     *
     * <p>This is a private helper method used internally by
     * {@link #removeItem(String)} and {@link #getItemDetails(String)}.</p>
     *
     * @param itemID the unique ID to search for
     * @return the matching LibraryItem
     * @throws ItemNotFoundException if no item with the given ID is found
     */
    private LibraryItem<T> findByID(String itemID) throws ItemNotFoundException {
        for (LibraryItem<T> item : items) {
            if (item.getItemID().equals(itemID)) {
                return item;
            }
        }
        throw new ItemNotFoundException(itemID);
    }
}


/**
 * Main application class for the Generic Library Catalog.
 *
 * <p>Provides a command-line interface (CLI) that allows users to:</p>
 * <ul>
 *   <li>Add a new library item (Book, DVD, or Magazine)</li>
 *   <li>Remove an item by ID</li>
 *   <li>Retrieve item details by ID</li>
 *   <li>View all items in a selected catalog</li>
 * </ul>
 *
 * <p>Three separate catalogs are maintained — one for each item type —
 * demonstrating the flexibility of the generic {@link Catalog} class.</p>
 *
 * @author Hee Moon
 */
public class LibraryCatalogApp {

    /** Catalog for Book items. */
    private static final Catalog<Book>     bookCatalog     = new Catalog<>("Book Catalog");

    /** Catalog for DVD items. */
    private static final Catalog<DVD>      dvdCatalog      = new Catalog<>("DVD Catalog");

    /** Catalog for Magazine items. */
    private static final Catalog<Magazine> magazineCatalog = new Catalog<>("Magazine Catalog");

    /** Scanner for reading user input from the console. */
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Application entry point.
     *
     * <p>Loads sample data, then starts the main CLI loop.</p>
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        loadSampleData();
        runMenu();
        scanner.close();
    }

    /**
     * Displays the main menu and routes user input to the appropriate
     * handler until the user chooses to exit.
     */
    private static void runMenu() {
        boolean running = true;

        while (running) {
            System.out.println("========================================");
            System.out.println("       Generic Library Catalog          ");
            System.out.println("========================================");
            System.out.println("  1. Add a new item");
            System.out.println("  2. Remove an item");
            System.out.println("  3. Get item details");
            System.out.println("  4. View catalog");
            System.out.println("  5. Exit");
            System.out.println("----------------------------------------");
            System.out.print("Select an option: ");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1" -> handleAdd();
                case "2" -> handleRemove();
                case "3" -> handleGetDetails();
                case "4" -> handleView();
                case "5" -> {
                    System.out.println("Goodbye!");
                    running = false;
                }
                default -> System.out.println("[Error] Invalid option. Please enter 1-5.\n");
            }
        }
    }

    /**
     * Prompts the user to select an item type and enter item details,
     * then adds the new item to the appropriate catalog.
     */
    private static void handleAdd() {
        System.out.println("\n-- Add Item --");
        System.out.println("  1. Book");
        System.out.println("  2. DVD");
        System.out.println("  3. Magazine");
        System.out.print("Select type: ");
        String typeInput = scanner.nextLine().trim();

        System.out.print("Enter Item ID : ");
        String itemID = scanner.nextLine().trim();

        System.out.print("Enter Title   : ");
        String title = scanner.nextLine().trim();

        System.out.print("Enter Author  : ");
        String author = scanner.nextLine().trim();

        switch (typeInput) {
            case "1" -> {
                System.out.print("Enter ISBN    : ");
                String isbn = scanner.nextLine().trim();
                System.out.print("Enter Genre   : ");
                String genre = scanner.nextLine().trim();
                bookCatalog.addItem(
                    new LibraryItem<>(itemID, title, author, new Book(isbn, genre))
                );
            }
            case "2" -> {
                System.out.print("Enter Duration (minutes): ");
                int duration = parseIntSafely(scanner.nextLine().trim(), 0);
                System.out.print("Enter Director           : ");
                String director = scanner.nextLine().trim();
                dvdCatalog.addItem(
                    new LibraryItem<>(itemID, title, author, new DVD(duration, director))
                );
            }
            case "3" -> {
                System.out.print("Enter Issue Number: ");
                int issue = parseIntSafely(scanner.nextLine().trim(), 0);
                System.out.print("Enter Month       : ");
                String month = scanner.nextLine().trim();
                magazineCatalog.addItem(
                    new LibraryItem<>(itemID, title, author, new Magazine(issue, month))
                );
            }
            default -> System.out.println("[Error] Invalid item type.\n");
        }
        System.out.println();
    }

    /**
     * Prompts the user to select a catalog and enter an item ID,
     * then attempts to remove the item from that catalog.
     *
     * <p>If the item is not found, an appropriate error message is
     * displayed without crashing the program.</p>
     */
    private static void handleRemove() {
        System.out.println("\n-- Remove Item --");
        int catalogChoice = selectCatalog();
        if (catalogChoice == -1) return;

        System.out.print("Enter Item ID to remove: ");
        String itemID = scanner.nextLine().trim();

        try {
            switch (catalogChoice) {
                case 1 -> bookCatalog.removeItem(itemID);
                case 2 -> dvdCatalog.removeItem(itemID);
                case 3 -> magazineCatalog.removeItem(itemID);
            }
        } catch (ItemNotFoundException e) {
            System.out.println("[Error] " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * Prompts the user to select a catalog and enter an item ID,
     * then displays the details of the matching item.
     *
     * <p>If the item is not found, an appropriate error message is
     * displayed without crashing the program.</p>
     */
    private static void handleGetDetails() {
        System.out.println("\n-- Get Item Details --");
        int catalogChoice = selectCatalog();
        if (catalogChoice == -1) return;

        System.out.print("Enter Item ID: ");
        String itemID = scanner.nextLine().trim();

        try {
            switch (catalogChoice) {
                case 1 -> bookCatalog.getItemDetails(itemID);
                case 2 -> dvdCatalog.getItemDetails(itemID);
                case 3 -> magazineCatalog.getItemDetails(itemID);
            }
        } catch (ItemNotFoundException e) {
            System.out.println("[Error] " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * Prompts the user to select a catalog and displays all items in it.
     */
    private static void handleView() {
        System.out.println("\n-- View Catalog --");
        int catalogChoice = selectCatalog();
        if (catalogChoice == -1) return;

        switch (catalogChoice) {
            case 1 -> bookCatalog.displayAll();
            case 2 -> dvdCatalog.displayAll();
            case 3 -> magazineCatalog.displayAll();
        }
    }

    /**
     * Displays a catalog selection prompt and returns the user's choice.
     *
     * @return 1 for Books, 2 for DVDs, 3 for Magazines, -1 for invalid input
     */
    private static int selectCatalog() {
        System.out.println("  1. Book Catalog");
        System.out.println("  2. DVD Catalog");
        System.out.println("  3. Magazine Catalog");
        System.out.print("Select catalog: ");
        String input = scanner.nextLine().trim();

        return switch (input) {
            case "1" -> 1;
            case "2" -> 2;
            case "3" -> 3;
            default  -> {
                System.out.println("[Error] Invalid catalog selection.\n");
                yield -1;
            }
        };
    }

    /**
     * Safely parses an integer from a string, returning a default value
     * if parsing fails.
     *
     * @param input        the string to parse
     * @param defaultValue the value to return if parsing fails
     * @return the parsed integer, or defaultValue on failure
     */
    private static int parseIntSafely(String input, int defaultValue) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("[Warning] Invalid number. Using default: " + defaultValue);
            return defaultValue;
        }
    }

    /**
     * Loads a set of pre-defined sample items into each catalog.
     *
     * <p>Called once at startup to populate the catalogs with initial data,
     * making it easy to test all operations immediately.</p>
     */
    private static void loadSampleData() {
        System.out.println("======================================");
        System.out.println("             Sample Data             ");
        System.out.println("======================================");

        // Books
        bookCatalog.addItem(new LibraryItem<>(
            "B001",
            "Effective Java",
            "Joshua Bloch",
            new Book("978-0134685991", "Programming")
        ));

        bookCatalog.addItem(new LibraryItem<>(
            "B002",
            "Core Java, Volume I: Fundamentals",
            "Cay S. Horstmann",
            new Book("978-0137673629", "Programming")
        ));

        bookCatalog.addItem(new LibraryItem<>(
            "B003",
            "Project Hail Mary",
            "Andy Weir",
            new Book("978-0593135204", "Science Fiction")
        ));

        // DVDs
        dvdCatalog.addItem(new LibraryItem<>(
            "D001",
            "Oppenheimer",
            "Christopher Nolan",
            new DVD(180, "Christopher Nolan")
        ));

        dvdCatalog.addItem(new LibraryItem<>(
            "D002",
            "Dune: Part Two",
            "Denis Villeneuve",
            new DVD(166, "Denis Villeneuve")
        ));

        dvdCatalog.addItem(new LibraryItem<>(
            "D003",
            "Spider-Man: Across the Spider-Verse",
            "Joaquim Dos Santos, Kemp Powers, Justin K. Thompson",
            new DVD(140, "Joaquim Dos Santos, Kemp Powers, Justin K. Thompson")
        ));

        // Magazines
        magazineCatalog.addItem(new LibraryItem<>(
            "M001",
            "National Geographic",
            "National Geographic Editorial Team",
            new Magazine(245, "May")
        ));

        magazineCatalog.addItem(new LibraryItem<>(
            "M002",
            "Scientific American",
            "Scientific American Editors",
            new Magazine(312, "April")
        ));

        magazineCatalog.addItem(new LibraryItem<>(
            "M003",
            "TIME",
            "TIME Editorial Staff",
            new Magazine(198, "May")
        ));

        System.out.println("Sample data loaded successfully.\n");
    }
}
