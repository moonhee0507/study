package com.ecommerce.orders;

import com.ecommerce.Customer;
import com.ecommerce.Product;
import java.util.List;

/**
 * Represents an order placed by a customer.
 * @author Hee Moon
 */
public class Order {

    private int orderID;
    private Customer customer;
    private List<Product> products;
    private double orderTotal;
    private String status;

    /**
     * Constructs an Order with the specified details.
     * @param orderID the unique identifier for the order
     * @param customer the customer who placed the order
     * @param products the list of products in the order
     */
    public Order(int orderID, Customer customer, List<Product> products) {
        if (orderID <= 0) {
            throw new IllegalArgumentException("Order ID must be positive.");
        }
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null.");
        }
        if (products == null || products.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one product.");
        }
        this.orderID = orderID;
        this.customer = customer;
        this.products = products;
        this.status = "Pending";
        this.orderTotal = calculateTotal();
    }

    /** @return the order ID */
    public int getOrderID() {
        return orderID;
    }

    /** @return the order status */
    public String getStatus() {
        return status;
    }

    /** @return the order total */
    public double getOrderTotal() {
        return orderTotal;
    }

    /**
     * Updates the status of the order.
     * @param status the new status of the order
     */
    public void updateStatus(String status) {
        this.status = status;
    }

    /**
     * Calculates the total cost of the order.
     * @return the sum of all product prices
     */
    private double calculateTotal() {
        double total = 0;
        for (Product p : products) {
            total += p.getPrice();
        }
        return total;
    }

    /**
     * Generates and prints a summary of the order.
     */
    public void generateOrderSummary() {
        System.out.println("========== Order Summary ==========");
        System.out.println("Order ID   : " + orderID);
        System.out.println("Customer   : " + customer.getName());
        System.out.println("Status     : " + status);
        System.out.println("-----------------------------------");
        System.out.println("Products   :");
        for (Product p : products) {
            System.out.println("  " + p.toString());
        }
        System.out.println("-----------------------------------");
        System.out.printf("Order Total: $%.2f%n", orderTotal);
        System.out.println("===================================");
    }
}
