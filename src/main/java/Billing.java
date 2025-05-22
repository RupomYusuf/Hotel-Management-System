//package com.company;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class Billing implements Serializable {
    private String billId;
    private Reservation reservation;
    private double totalAmount;
    private boolean isPaid;
    private Date billingDate;

    public Billing(Reservation reservation) {
        this.billId = generateBillId();
        this.reservation = reservation;
        this.totalAmount = calculateTotalAmount();
        this.isPaid = false;
        this.billingDate = new Date();
    }

    private String generateBillId() {
        return "BILL" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 8);
    }

    private double calculateTotalAmount() {
        // Calculate room charges based on duration
        long duration = reservation.getCheckOutDate().getTime() - reservation.getCheckInDate().getTime();
        int days = (int) (duration / (1000 * 60 * 60 * 24));
        double roomCharges = days * reservation.getRoom().getPricePerDay();
        
        // Calculate food charges
        double foodCharges = getFoodCharges();
        
        // Return total amount including both room and food charges
        return roomCharges + foodCharges;
    }

    public double getRoomCharges() {
        // Calculate room charges based on duration
        long duration = reservation.getCheckOutDate().getTime() - reservation.getCheckInDate().getTime();
        int days = (int) (duration / (1000 * 60 * 60 * 24));
        return days * reservation.getRoom().getPricePerDay();
    }

    public double getFoodCharges() {
        return reservation.getFoodOrders().stream()
            .mapToDouble(Food::getPrice)
            .sum();
    }

    public Date getBillingDate() {
        return billingDate;
    }

    // Getters
    public String getBillId() { return billId; }
    public Reservation getReservation() { return reservation; }
    public double getTotalAmount() { return totalAmount; }
    public boolean isPaid() { return isPaid; }

    // Setters
    public void setPaid(boolean paid) { this.isPaid = paid; }
} 