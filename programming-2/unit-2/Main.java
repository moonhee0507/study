import com.ecommerce.Customer;
import com.ecommerce.Product;
import com.ecommerce.orders.Order;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Main program demonstrating the e-commerce system with interactive menu.
 * 
 * @author Hee Moon
 */
public class Main {

    private static List<Product> productCatalog = new ArrayList<>();
    private static Customer customer = null;
    private static List<Order> orderHistory = new ArrayList<>();
    private static int orderCounter = 1001;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        initializeProducts();
        System.out.println("[E-Commerce System]");
        System.out.print("- Enter your name: ");
        String name = scanner.nextLine().trim();

        try {
            customer = new Customer(1, name);
            System.out.println("Hello, " + customer.getName() + "!\n");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }

        boolean running = true;
        while (running) {
            printMainMenu();
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    browseProduts();
                    break;
                case "2":
                    addProductToCart();
                    break;
                case "3":
                    removeProductFromCart();
                    break;
                case "4":
                    customer.displayCart();
                    break;
                case "5":
                    placeOrder();
                    break;
                case "6":
                    viewOrderHistory();
                    break;
                case "7":
                    running = false;
                    System.out.println("Session ended.");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
        scanner.close();
    }

    /**
     * Initializes the product catalog with sample products.
     */
    private static void initializeProducts() {
        productCatalog.add(new Product(101, "Laptop", 999.99, 10));
        productCatalog.add(new Product(102, "Mouse", 29.99, 50));
        productCatalog.add(new Product(103, "Keyboard", 49.99, 30));
        productCatalog.add(new Product(104, "Monitor", 299.99, 15));
    }

    /**
     * Prints the main menu options.
     */
    private static void printMainMenu() {
        System.out.println("[1] Browse     [2] Add to Cart  [3] Remove from Cart");
        System.out.println("[4] View Cart  [5] Place Order  [6] Order History");
        System.out.println("[7] Exit");
        System.out.print("Select an option: ");
    }

    /**
     * Displays all available products in the catalog.
     */
    private static void browseProduts() {
        System.out.println("\n===== Available Products =====");
        for (Product p : productCatalog) {
            System.out.println(p);
        }
        System.out.println();
    }

    /**
     * Allows the customer to add a product to the cart by product ID.
     */
    private static void addProductToCart() {
        browseProduts();
        System.out.print("Enter Product ID to add: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Product found = findProductByID(id);
            if (found != null) {
                customer.addToCart(found);
            } else {
                System.out.println("Product ID " + id + " not found.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter a valid numeric Product ID.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * Allows the customer to remove a product from the cart by product ID.
     */
    private static void removeProductFromCart() {
        customer.displayCart();
        System.out.print("Enter Product ID to remove: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Product found = findProductByID(id);
            if (found != null) {
                customer.removeFromCart(found);
            } else {
                System.out.println("Product ID " + id + " not found.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter a valid numeric Product ID.");
        }
        System.out.println();
    }

    /**
     * Places an order with the current cart contents.
     */
    private static void placeOrder() {
        try {
            customer.displayCart();
            System.out.print("Confirm order? (yes/no): ");
            String confirm = scanner.nextLine().trim().toLowerCase();
            if (confirm.equals("yes")) {
                Order order = customer.placeOrder(orderCounter++);
                orderHistory.add(order);
                System.out.println();
                order.generateOrderSummary();
            } else {
                System.out.println("Order cancelled.\n");
            }
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage() + "\n");
        }
    }

    /**
     * Displays all past orders placed by the customer.
     */
    private static void viewOrderHistory() {
        System.out.println("\n===== Order History =====");
        if (orderHistory.isEmpty()) {
            System.out.println("No orders placed yet.");
        } else {
            for (Order o : orderHistory) {
                o.generateOrderSummary();
            }
        }
        System.out.println();
    }

    /**
     * Finds a product in the catalog by its ID.
     * 
     * @param id the product ID to search for
     * @return the matching Product, or null if not found
     */
    private static Product findProductByID(int id) {
        for (Product p : productCatalog) {
            if (p.getProductID() == id) {
                return p;
            }
        }
        return null;
    }
}
