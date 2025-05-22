//package com.company;

import java.util.Date;

public class EmailService {
    private static final String FROM_EMAIL = "hotel@example.com";
    
    public static void sendBookingConfirmation(Reservation reservation, Customer customer) {
        // In a real implementation, this would send an actual email
        System.out.println("\nSending booking confirmation email to: " + customer.getEmail());
        System.out.println("From: " + FROM_EMAIL);
        System.out.println("Subject: Booking Confirmation - " + reservation.getReservationId());
        System.out.println(generateBookingEmail(reservation, customer));
    }
    
    public static void sendPaymentConfirmation(Payment payment, Billing billing) {
        // In a real application, this would send an actual email
        System.out.println("\nSending payment confirmation email to: " + 
            billing.getReservation().getCustomer().getEmail());
        System.out.println("From: " + FROM_EMAIL);
        System.out.println("Subject: Payment Confirmation - " + payment.getPaymentId());
        System.out.println(generatePaymentEmail(payment, billing));
    }
    
    private static String generateBookingEmail(Reservation reservation, Customer customer) {
        String customerName = customer != null ? customer.getName() : "Guest";
        String reservationId = reservation != null ? reservation.getReservationId() : "N/A";
        int roomNumber = reservation != null && reservation.getRoom() != null ? 
            reservation.getRoom().getRoomNumber() : 0;
        Date checkIn = reservation != null ? reservation.getCheckInDate() : null;
        Date checkOut = reservation != null ? reservation.getCheckOutDate() : null;
        
        // Format dates if they exist
        String checkInStr = checkIn != null ? checkIn.toString() : "N/A";
        String checkOutStr = checkOut != null ? checkOut.toString() : "N/A";
        
        // Calculate stay duration
        long duration = 0;
        if (checkIn != null && checkOut != null) {
            duration = (checkOut.getTime() - checkIn.getTime()) / (1000 * 60 * 60 * 24);
        }
        
        // Build food orders section
        StringBuilder foodOrders = new StringBuilder();
        if (reservation != null && !reservation.getFoodOrders().isEmpty()) {
            foodOrders.append("\nFood Orders:\n");
            for (Food food : reservation.getFoodOrders()) {
                foodOrders.append("- ").append(food.getName()).append("\n");
            }
        }
        
        return String.format(
            "Dear %s,\n\n" +
            "Your booking has been confirmed!\n\n" +
            "Reservation Details:\n" +
            "Reservation ID: %s\n" +
            "Room Number: %d\n" +
            "Stay Duration: %d days\n" +
            "Check-in: %s\n" +
            "Check-out: %s%s\n\n" +
            "Thank you for choosing our hotel!",
            customerName,
            reservationId,
            roomNumber,
            duration,
            checkInStr,
            checkOutStr,
            foodOrders.toString()
        );
    }
    
    private static String generatePaymentEmail(Payment payment, Billing billing) {
        return String.format(
            "Dear %s,\n\n" +
            "Your payment has been processed successfully!\n\n" +
            "Payment Details:\n" +
            "Payment ID: %s\n" +
            "Transaction ID: %s\n" +
            "Amount: Rs.%.2f\n\n" +
            "Thank you for your payment!",
            billing.getReservation().getCustomer().getName(),
            payment.getPaymentId(),
            payment.getTransactionId(),
            payment.getAmount()
        );
    }

    public static String generatePaymentConfirmationEmail(Customer customer, Billing billing, String paymentMethod) {
        StringBuilder email = new StringBuilder();
        email.append("Dear ").append(customer.getName()).append(",\n\n");
        email.append("Thank you for your payment. Here are your payment details:\n\n");
        email.append("Bill ID: ").append(billing.getBillId()).append("\n");
        email.append("Amount: $").append(billing.getTotalAmount()).append("\n");
        email.append("Payment Method: ").append(paymentMethod).append("\n");
        email.append("Date: ").append(new Date()).append("\n\n");
        email.append("Best regards,\nHotel Management System");
        
        return email.toString();
    }
} 