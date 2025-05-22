//package com.company;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class Payment implements Serializable {
    private String billId;
    private String paymentId;
    private String transactionId;
    private double amount;
    private PaymentStatus status;
    private PaymentMethod method;
    private Date paymentDate;

    public enum PaymentStatus {
        PENDING,
        COMPLETED,
        FAILED
    }

    public enum PaymentMethod {
        CREDIT_CARD,
        DEBIT_CARD,
        CASH,
        UPI
    }

    public Payment(String billId, double amount, PaymentMethod method) {
        this.billId = billId;
        this.amount = amount;
        this.method = method;
        this.status = PaymentStatus.PENDING;
        this.paymentDate = new Date();
        this.paymentId = "PAY-" + UUID.randomUUID().toString().substring(0, 8);
        this.transactionId = "TXN-" + UUID.randomUUID().toString().substring(0, 8);
    }

    public boolean processPayment() {
        // Simulate payment processing
        try {
            Thread.sleep(1000); // Simulate processing time
            this.status = PaymentStatus.COMPLETED;
            return true;
        } catch (InterruptedException e) {
            this.status = PaymentStatus.FAILED;
            return false;
        }
    }

    // Getters
    public String getBillId() { return billId; }
    public String getPaymentId() { return paymentId; }
    public String getTransactionId() { return transactionId; }
    public double getAmount() { return amount; }
    public PaymentStatus getStatus() { return status; }
    public PaymentMethod getMethod() { return method; }
    public Date getPaymentDate() { return paymentDate; }
} 