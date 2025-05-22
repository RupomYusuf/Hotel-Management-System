//package com.company;

import java.util.Date;
import java.util.ArrayList;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public class Reservation implements Serializable {
    private String reservationId;
    private Customer customer;
    private Room room;
    private Date checkInDate;
    private Date checkOutDate;
    private boolean isActive;
    private List<Food> foodOrders;

    public Reservation() {
        this.reservationId = generateReservationId();
        this.isActive = true;
        this.foodOrders = new ArrayList<>();
    }

    public Reservation(Customer customer, Room room, Date checkInDate, Date checkOutDate) {
        this.reservationId = generateReservationId();
        this.customer = customer;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.isActive = true;
        this.foodOrders = new ArrayList<>();
    }

    private String generateReservationId() {
        return "RES" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }

    public void addFoodOrder(Food food) {
        foodOrders.add(food);
    }

    // Getters
    public String getReservationId() { return reservationId; }
    public Customer getCustomer() { return customer; }
    public Room getRoom() { return room; }
    public Date getCheckInDate() { return checkInDate; }
    public Date getCheckOutDate() { return checkOutDate; }
    public boolean isActive() { return isActive; }
    public List<Food> getFoodOrders() { return foodOrders; }

    // Setters
    public void setActive(boolean active) { this.isActive = active; }

} 