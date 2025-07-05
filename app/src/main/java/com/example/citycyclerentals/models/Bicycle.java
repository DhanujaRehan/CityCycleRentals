package com.example.citycyclerentals.models;

public class Bicycle {
    private int id;
    private String image;
    private double price;
    private String description;

    public Bicycle() {}

    public Bicycle(String image, double price, String description) {
        this.image = image;
        this.price = price;
        this.description = description;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}