package com.ecommerce;

/**
 * Represents a product available for purchase in the e-commerce system.
 * @author Hee Moon
 */
public class Product {

    private int productID;
    private String name;
    private double price;
    private int stock;

    /**
     * Constructs a Product with the specified details.
     * @param productID the unique identifier for the product
     * @param name the name of the product
     * @param price the price of the product
     * @param stock the available stock quantity
     */
    public Product(int productID, String name, double price, int stock) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative.");
        }
        this.productID = productID;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    /** @return the product ID */
    public int getProductID() {
        return productID;
    }

    /** @return the product name */
    public String getName() {
        return name;
    }

    /** @return the product price */
    public double getPrice() {
        return price;
    }

    /** @return the available stock */
    public int getStock() {
        return stock;
    }

    /**
     * Sets the price of the product.
     * @param price the new price (must be non-negative)
     */
    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
        this.price = price;
    }

    /**
     * Sets the stock quantity of the product.
     * @param stock the new stock quantity (must be non-negative)
     */
    public void setStock(int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative.");
        }
        this.stock = stock;
    }

    /**
     * Returns a string representation of the product.
     * @return formatted product information
     */
    @Override
    public String toString() {
        return String.format("[ID: %d] %s - $%.2f (Stock: %d)",
                productID, name, price, stock);
    }
}
