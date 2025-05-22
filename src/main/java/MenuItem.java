//package com.company;

import java.io.Serializable;

public class MenuItem implements Serializable {
    private int id;
    private String name;
    private double price;
    private String description;
    private String category;
    private boolean available;

    public MenuItem(int id, String name, double price, String category) {
        this(id, name, price, category, ""); // Call the full constructor with empty description
    }

    public MenuItem(int id, String name, double price, String category, String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.description = description;
        this.available = true;
    }

    // Getters and setters
    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    @Override
    public String toString() {
        return String.format("%s - Rs.%.2f%s%s", 
            name, 
            price, 
            description.isEmpty() ? "" : " (" + description + ")",
            available ? "" : " [Not Available]"
        );
    }
} 