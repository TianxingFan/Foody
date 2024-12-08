package com.example.foody;

import java.io.Serializable;

public class MenuItem implements Serializable {
    private final String id;
    private final String name;
    private final String description;
    private final double price;
    private final String imageUrl;
    private int quantity;

    public MenuItem(String id, String name, String description, double price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = 0;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}