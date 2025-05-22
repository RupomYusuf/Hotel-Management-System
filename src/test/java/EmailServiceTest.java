import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Date;
import java.util.Calendar;

public class EmailServiceTest {
    private Customer customer;
    private Room room;
    private Reservation reservation;
    private Billing billing;
    private Payment payment;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Before
    public void setUp() {
        // Redirect System.out to capture email content
        System.setOut(new PrintStream(outContent));
        
        // Set up test data
        customer = new Customer("John Doe", "1234567890", "Male");
        customer.setEmail("john@example.com");
        room = new Room(101, "LUXURY_DOUBLE");
        
        Calendar cal = Calendar.getInstance();
        Date checkIn = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 3);
        Date checkOut = cal.getTime();
        
        reservation = new Reservation(customer, room, checkIn, checkOut);
        billing = new Billing(reservation);
        payment = new Payment(billing.getBillId(), billing.getTotalAmount(), Payment.PaymentMethod.CREDIT_CARD);
    }

    @After
    public void tearDown() {
        // Restore original System.out
        System.setOut(originalOut);
        
        // Clear the output buffer
        outContent.reset();
        
        // Clean up test objects
        customer = null;
        room = null;
        reservation = null;
        billing = null;
        payment = null;
    }

    @Test
    public void testSendBookingConfirmation() {
        EmailService.sendBookingConfirmation(reservation, customer);
        String emailContent = outContent.toString();
        
        assertTrue(emailContent.contains("Sending booking confirmation email to: john@example.com"));
        assertTrue(emailContent.contains("From: hotel@example.com"));
        assertTrue(emailContent.contains("Subject: Booking Confirmation - " + reservation.getReservationId()));
        assertTrue(emailContent.contains("Dear John Doe"));
        assertTrue(emailContent.contains("Reservation ID: " + reservation.getReservationId()));
        assertTrue(emailContent.contains("Room Number: 101"));
    }

    @Test
    public void testSendPaymentConfirmation() {
        payment.processPayment();
        EmailService.sendPaymentConfirmation(payment, billing);
        String emailContent = outContent.toString();
        
        assertTrue(emailContent.contains("Sending payment confirmation email to: john@example.com"));
        assertTrue(emailContent.contains("From: hotel@example.com"));
        assertTrue(emailContent.contains("Subject: Payment Confirmation - " + payment.getPaymentId()));
        assertTrue(emailContent.contains("Dear John Doe"));
        assertTrue(emailContent.contains("Payment ID: " + payment.getPaymentId()));
        assertTrue(emailContent.contains("Transaction ID: " + payment.getTransactionId()));
        assertTrue(emailContent.contains("Amount: Rs." + String.format("%.2f", billing.getTotalAmount())));
    }

    @Test
    public void testSendBookingConfirmationWithNoEmail() {
        Customer customerNoEmail = new Customer("Jane Doe", "0987654321", "Female");
        EmailService.sendBookingConfirmation(reservation, customerNoEmail);
        String emailContent = outContent.toString();
        
        assertTrue(emailContent.contains("Sending booking confirmation email to: "));
        assertTrue(emailContent.contains("Dear Jane Doe"));
    }

    @Test
    public void testSendPaymentConfirmationWithFailedPayment() {
        Payment failedPayment = new Payment(billing.getBillId(), billing.getTotalAmount(), Payment.PaymentMethod.CREDIT_CARD);
        // Payment not processed, so status remains PENDING
        EmailService.sendPaymentConfirmation(failedPayment, billing);
        String emailContent = outContent.toString();
        
        assertTrue(emailContent.contains("Sending payment confirmation email to: john@example.com"));
        assertTrue(emailContent.contains("Payment ID: " + failedPayment.getPaymentId()));
    }

    @Test
    public void testEmailContentFormat() {
        EmailService.sendBookingConfirmation(reservation, customer);
        String emailContent = outContent.toString();
        
        // Check for proper formatting
        assertTrue(emailContent.contains("\n\n")); // Double line breaks
        assertTrue(emailContent.contains("Reservation Details:"));
        assertTrue(emailContent.contains("Thank you for choosing our hotel!"));
    }

    @Test
    public void testEmailWithSpecialCharacters() {
        Customer specialCustomer = new Customer("John O'Doe", "1234567890", "Male");
        specialCustomer.setEmail("john.o'doe@example.com");
        EmailService.sendBookingConfirmation(reservation, specialCustomer);
        String emailContent = outContent.toString();
        
        assertTrue(emailContent.contains("Dear John O'Doe"));
        assertTrue(emailContent.contains("john.o'doe@example.com"));
    }

    @Test
    public void testEmailWithLongContent() {
        // Create a reservation with a long stay
        Calendar cal = Calendar.getInstance();
        Date longStayCheckIn = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 30);
        Date longStayCheckOut = cal.getTime();
        
        // Create a new reservation with the long stay dates
        Reservation longStayReservation = new Reservation(customer, room, longStayCheckIn, longStayCheckOut);
        
        // Add some food orders to make content longer
        Food food1 = new Food("Breakfast Package", 500.0);
        Food food2 = new Food("Lunch Package", 800.0);
        Food food3 = new Food("Dinner Package", 1000.0);
        longStayReservation.addFoodOrder(food1);
        longStayReservation.addFoodOrder(food2);
        longStayReservation.addFoodOrder(food3);
        
        EmailService.sendBookingConfirmation(longStayReservation, customer);
        String emailContent = outContent.toString();
        
        // Verify long stay details
        assertTrue(emailContent.contains("30")); // Should mention the long stay
        assertTrue(emailContent.contains("Breakfast Package"));
        assertTrue(emailContent.contains("Lunch Package"));
        assertTrue(emailContent.contains("Dinner Package"));
        
        // Check for dates in the email content
        assertTrue(emailContent.contains("Check-in: " + longStayCheckIn));
        assertTrue(emailContent.contains("Check-out: " + longStayCheckOut));
    }

    @Test
    public void testEmailWithMultipleFoodOrders() {
        // Add multiple food orders to the reservation
        Food food1 = new Food("Burger", 200.0);
        Food food2 = new Food("Pizza", 300.0);
        Food food3 = new Food("Ice Cream", 100.0);
        reservation.addFoodOrder(food1);
        reservation.addFoodOrder(food2);
        reservation.addFoodOrder(food3);
        
        EmailService.sendBookingConfirmation(reservation, customer);
        String emailContent = outContent.toString();
        
        assertTrue(emailContent.contains("Burger"));
        assertTrue(emailContent.contains("Pizza"));
        assertTrue(emailContent.contains("Ice Cream"));
    }

    @Test
    public void testEmailWithZeroAmountPayment() {
        Payment zeroPayment = new Payment(billing.getBillId(), 0.0, Payment.PaymentMethod.CASH);
        EmailService.sendPaymentConfirmation(zeroPayment, billing);
        String emailContent = outContent.toString();
        
        assertTrue(emailContent.contains("Amount: Rs.0.00"));
    }

    @Test
    public void testEmailWithLargeAmountPayment() {
        // Create a reservation with a large amount
        Room luxuryRoom = new Room(101, "LUXURY_DOUBLE");
        Calendar cal = Calendar.getInstance();
        Date checkIn = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 30); // 30 days stay
        Date checkOut = cal.getTime();
        Reservation longStayReservation = new Reservation(customer, luxuryRoom, checkIn, checkOut);
        Billing largeBilling = new Billing(longStayReservation);
        
        Payment largePayment = new Payment(largeBilling.getBillId(), largeBilling.getTotalAmount(), Payment.PaymentMethod.CREDIT_CARD);
        largePayment.processPayment();
        
        EmailService.sendPaymentConfirmation(largePayment, largeBilling);
        String emailContent = outContent.toString();
        
        assertTrue(emailContent.contains("Amount: Rs." + String.format("%.2f", largeBilling.getTotalAmount())));
        assertTrue(emailContent.contains("Payment ID: " + largePayment.getPaymentId()));
        assertTrue(emailContent.contains("Transaction ID: " + largePayment.getTransactionId()));
    }

    @Test
    public void testEmailWithDifferentPaymentMethods() {
        // Create test data
        Customer customer = new Customer("John Doe", "1234567890", "Male");
        customer.setEmail("john@example.com");
        Room room = new Room(101, "LUXURY_DOUBLE");
        Reservation reservation = new Reservation(customer, room, new Date(), new Date());
        Billing billing = new Billing(reservation);
        
        // Test with different payment methods
        String[] paymentMethods = {"CREDIT_CARD", "DEBIT_CARD", "CASH", "UPI"};
        for (String method : paymentMethods) {
            String emailContent = EmailService.generatePaymentConfirmationEmail(
                customer, 
                billing, 
                method
            );
            
            assertNotNull(emailContent);
            assertTrue(emailContent.contains(customer.getName()));
            assertTrue(emailContent.contains(billing.getBillId()));
            assertTrue(emailContent.contains(method));
            assertTrue(emailContent.contains(String.valueOf(billing.getTotalAmount())));
        }
    }

    @Test
    public void testEmailWithNullValues() {
        Customer nullCustomer = new Customer(null, null, null);
        Reservation nullReservation = new Reservation(nullCustomer, null, null, null);
        EmailService.sendBookingConfirmation(nullReservation, nullCustomer);
        String emailContent = outContent.toString();
        
        assertTrue(emailContent.contains("Dear null"));
        assertTrue(emailContent.contains("Room Number: 0"));
    }
} 