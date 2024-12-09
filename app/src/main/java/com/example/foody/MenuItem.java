package com.example.foody;

import java.io.Serializable;

// Model class representing a menu item with its details and quantity
public class MenuItem implements Serializable {
    // Basic item information
    private final String id;
    private final String name;
    private final String description;
    private final double price;
    private final String imageUrl;
    private int quantity;

    // Constructor to initialize all menu item properties
    public MenuItem(String id, String name, String description, double price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = 0;  // Initialize quantity to zero
    }

    // Getter methods for accessing item properties
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }
    public int getQuantity() { return quantity; }

    // Setter method for updating item quantity in cart
    public void setQuantity(int quantity) { this.quantity = quantity; }
}