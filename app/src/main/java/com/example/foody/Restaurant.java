package com.example.foody;

import java.io.Serializable;

public class Restaurant implements Serializable {
    private final String name;
    private final String imageUrl;
    private final float rating;
    private final String cuisine;
    private final String deliveryTime;
    private final String priceRange;

    public Restaurant(String name, String imageUrl, float rating, String cuisine, String deliveryTime, String priceRange) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.rating = rating;
        this.cuisine = cuisine;
        this.deliveryTime = deliveryTime;
        this.priceRange = priceRange;
    }

    public String getName() { return name; }
    public String getImageUrl() { return imageUrl; }
    public float getRating() { return rating; }
    public String getCuisine() { return cuisine; }
    public String getDeliveryTime() { return deliveryTime; }
    public String getPriceRange() { return priceRange; }
}