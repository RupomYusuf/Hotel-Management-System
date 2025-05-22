import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Date;

public class PaymentTest {
    private Payment payment;
    private static final String BILL_ID = "BILL123";
    private static final double AMOUNT = 1000.0;

    @Before
    public void setUp() {
        payment = new Payment(BILL_ID, AMOUNT, Payment.PaymentMethod.CREDIT_CARD);
    }

    @Test
    public void testPaymentInitialization() {
        assertEquals(BILL_ID, payment.getBillId());
        assertEquals(AMOUNT, payment.getAmount(), 0.001);
        assertEquals(Payment.PaymentMethod.CREDIT_CARD, payment.getMethod());
        assertEquals(Payment.PaymentStatus.PENDING, payment.getStatus());
        assertNotNull(payment.getPaymentDate());
        assertTrue(payment.getPaymentId().startsWith("PAY-"));
        assertTrue(payment.getTransactionId().startsWith("TXN-"));
    }

    @Test
    public void testProcessPayment() {
        assertTrue(payment.processPayment());
        assertEquals(Payment.PaymentStatus.COMPLETED, payment.getStatus());
    }

    @Test
    public void testProcessPaymentInterrupted() {
        Thread.currentThread().interrupt();
        assertFalse(payment.processPayment());
        assertEquals(Payment.PaymentStatus.FAILED, payment.getStatus());
        Thread.interrupted(); // Clear the interrupt status
    }

    @Test
    public void testPaymentIdUniqueness() {
        Payment payment2 = new Payment(BILL_ID, AMOUNT, Payment.PaymentMethod.CREDIT_CARD);
        assertNotEquals(payment.getPaymentId(), payment2.getPaymentId());
    }

    @Test
    public void testTransactionIdUniqueness() {
        Payment payment2 = new Payment(BILL_ID, AMOUNT, Payment.PaymentMethod.CREDIT_CARD);
        assertNotEquals(payment.getTransactionId(), payment2.getTransactionId());
    }

    @Test
    public void testDifferentPaymentMethods() {
        Payment cashPayment = new Payment(BILL_ID, AMOUNT, Payment.PaymentMethod.CASH);
        Payment upiPayment = new Payment(BILL_ID, AMOUNT, Payment.PaymentMethod.UPI);
        Payment debitPayment = new Payment(BILL_ID, AMOUNT, Payment.PaymentMethod.DEBIT_CARD);
        
        assertEquals(Payment.PaymentMethod.CASH, cashPayment.getMethod());
        assertEquals(Payment.PaymentMethod.UPI, upiPayment.getMethod());
        assertEquals(Payment.PaymentMethod.DEBIT_CARD, debitPayment.getMethod());
    }

    @Test
    public void testPaymentDate() {
        Date beforePayment = new Date();
        Payment newPayment = new Payment(BILL_ID, AMOUNT, Payment.PaymentMethod.CREDIT_CARD);
        Date afterPayment = new Date();
        
        assertTrue(newPayment.getPaymentDate().after(beforePayment) || 
                  newPayment.getPaymentDate().equals(beforePayment));
        assertTrue(newPayment.getPaymentDate().before(afterPayment) || 
                  newPayment.getPaymentDate().equals(afterPayment));
    }

    @Test
    public void testPaymentStatusTransitions() {
        assertEquals(Payment.PaymentStatus.PENDING, payment.getStatus());
        assertTrue(payment.processPayment());
        assertEquals(Payment.PaymentStatus.COMPLETED, payment.getStatus());
    }

    @Test
    public void testPaymentAmountPrecision() {
        Payment precisePayment = new Payment(BILL_ID, 1000.999, Payment.PaymentMethod.CREDIT_CARD);
        assertEquals(1000.999, precisePayment.getAmount(), 0.001);
    }
} 