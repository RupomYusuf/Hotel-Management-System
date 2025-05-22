//package com.company;

import java.io.Serializable;

public class Room implements Serializable {
    private int roomNumber;
    private String roomType; // LUXURY_DOUBLE, DELUXE_DOUBLE, LUXURY_SINGLE, DELUXE_SINGLE
    private double pricePerDay;
    private boolean isAC;
    private boolean hasBreakfast;
    private boolean isOccupied;
    private int numberOfBeds;

    public Room(int roomNumber, String roomType) {
        this.roomNumber = roomNumber;
        if (!isValidRoomType(roomType)) {
            throw new IllegalArgumentException("Invalid room type: " + roomType);
        }
        this.roomType = roomType;
        this.pricePerDay = calculatePricePerDay();
        this.isOccupied = false;
        
        // Set features based on room type
        switch(roomType) {
            case "LUXURY_DOUBLE":
                this.isAC = true;
                this.hasBreakfast = true;
                this.numberOfBeds = 2;
                break;
            case "DELUXE_DOUBLE":
                this.isAC = false;
                this.hasBreakfast = true;
                this.numberOfBeds = 2;
                break;
            case "LUXURY_SINGLE":
                this.isAC = true;
                this.hasBreakfast = true;
                this.numberOfBeds = 1;
                break;
            case "DELUXE_SINGLE":
                this.isAC = false;
                this.hasBreakfast = true;
                this.numberOfBeds = 1;
                break;
        }
    }

    private boolean isValidRoomType(String roomType) {
        return roomType != null && (
            roomType.equals("LUXURY_SINGLE") ||
            roomType.equals("LUXURY_DOUBLE") ||
            roomType.equals("DELUXE_SINGLE") ||
            roomType.equals("DELUXE_DOUBLE")
        );
    }

    Room(int i, String deluxe, double d) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    // Getters
    public int getRoomNumber() { return roomNumber; }
    public String getRoomType() { return roomType; }
    public boolean isOccupied() { return isOccupied; }
    public double getPricePerDay() { return pricePerDay; }
    public boolean isAC() { return isAC; }
    public boolean hasBreakfast() { return hasBreakfast; }
    public int getNumberOfBeds() { return numberOfBeds; }

    // Setters
    public void setOccupied(boolean occupied) { this.isOccupied = occupied; }

    private double calculatePricePerDay() {
        switch(roomType) {
            case "LUXURY_DOUBLE":
                return 4000;
            case "DELUXE_DOUBLE":
                return 3000;
            case "LUXURY_SINGLE":
                return 2200;
            case "DELUXE_SINGLE":
                return 1200;
            default:
                throw new IllegalArgumentException("Invalid room type: " + roomType);
        }
    }
} 