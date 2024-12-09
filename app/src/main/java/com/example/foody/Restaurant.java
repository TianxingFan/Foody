package com.example.foody;

import java.io.Serializable;

// Model class representing a restaurant with its basic information
public class Restaurant implements Serializable {
    // Restaurant details
    private final String name;
    private final String imageUrl;
    private final float rating;
    private final String cuisine;
    private final String deliveryTime;
    private final String priceRange;

    // Constructor to initialize all restaurant properties
    public Restaurant(String name, String imageUrl, float rating, String cuisine, String deliveryTime, String priceRange) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.rating = rating;
        this.cuisine = cuisine;
        this.deliveryTime = deliveryTime;
        this.priceRange = priceRange;
    }

    // Getter methods for accessing restaurant properties
    public String getName() { return name; }
    public String getImageUrl() { return imageUrl; }
    public float getRating() { return rating; }
    public String getCuisine() { return cuisine; }
    public String getDeliveryTime() { return deliveryTime; }
    public String getPriceRange() { return priceRange; }
}