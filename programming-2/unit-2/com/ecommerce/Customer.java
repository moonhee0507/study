package com.ecommerce;

import com.ecommerce.orders.Order;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a customer in the e-commerce system.
 * @author Hee Moon
 */
public class Customer {

    private int customerID;
    private String name;
    private List<Product> shoppingCart;

    /**
     * Constructs a Customer with the specified details.
     * @param customerID the unique identifier for the customer
     * @param name the name of the customer
     */
    public Customer(int customerID, String name) {
        if (customerID <= 0) {
            throw new IllegalArgumentException("Customer ID must be positive.");
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Customer name cannot be empty.");
        }
        this.customerID = customerID;
        this.name = name;
        this.shoppingCart = new ArrayList<>();
    }

    /** @return the customer ID */
    public int getCustomerID() {
        return customerID;
    }

    /** @return the customer name */
    public String getName() {
        return name;
    }

    /** @return the current shopping cart */
    public List<Product> getShoppingCart() {
        return shoppingCart;
    }

    /**
     * Adds a product to the shopping cart.
     * @param product the product to add
     */
    public void addToCart(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }
        shoppingCart.add(product);
        System.out.println("\"" + product.getName() + "\" has been added to the cart.");
    }

    /**
     * Removes a product from the shopping cart.
     * @param product the product to remove
     */
    public void removeFromCart(Product product) {
        if (shoppingCart.remove(product)) {
            System.out.println("\"" + product.getName() + "\" has been removed from the cart.");
        } else {
            System.out.println("Product not found in cart.");
        }
    }

    /**
     * Calculates the total cost of items in the shopping cart.
     * @return the total cost
     */
    public double calculateTotal() {
        double total = 0;
        for (Product p : shoppingCart) {
            total += p.getPrice();
        }
        return total;
    }

    /**
     * Places an order with the current shopping cart contents.
     * @param orderID the unique identifier for the new order
     * @return the placed Order
     */
    public Order placeOrder(int orderID) {
        if (shoppingCart.isEmpty()) {
            throw new IllegalStateException("Cannot place order. Shopping cart is empty.");
        }
        List<Product> orderedProducts = new ArrayList<>(shoppingCart);
        Order order = new Order(orderID, this, orderedProducts);
        shoppingCart.clear();
        System.out.println("Order placed successfully! Order ID: " + orderID);
        return order;
    }

    /**
     * Displays all products currently in the shopping cart.
     */
    public void displayCart() {
        System.out.println("===== " + name + "'s Shopping Cart =====");
        if (shoppingCart.isEmpty()) {
            System.out.println("Cart is empty.");
        } else {
            for (Product p : shoppingCart) {
                System.out.println("  " + p.toString());
            }
            System.out.printf("Total: $%.2f%n", calculateTotal());
        }
        System.out.println("=====================================");
    }
}
