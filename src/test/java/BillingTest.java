import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Date;
import java.util.Calendar;

public class BillingTest {
    private Customer customer;
    private Room room;
    private Reservation reservation;
    private Billing billing;
    private Date checkIn;
    private Date checkOut;

    @Before
    public void setUp() {
        customer = new Customer("John Doe", "1234567890", "Male");
        room = new Room(101, "LUXURY_DOUBLE");
        
        // Set up dates
        Calendar cal = Calendar.getInstance();
        checkIn = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 3); // 3 days stay
        checkOut = cal.getTime();
        
        reservation = new Reservation(customer, room, checkIn, checkOut);
        billing = new Billing(reservation);
    }

    @Test
    public void testBillingInitialization() {
        assertNotNull(billing.getBillId());
        assertTrue(billing.getBillId().startsWith("BILL"));
        assertEquals(reservation, billing.getReservation());
        assertFalse(billing.isPaid());
    }

    @Test
    public void testTotalAmountCalculation() {
        // 3 days * 4000 (LUXURY_DOUBLE price) = 12000
        assertEquals(12000.0, billing.getTotalAmount(), 0.001);
    }

    @Test
    public void testPaymentStatus() {
        assertFalse(billing.isPaid());
        billing.setPaid(true);
        assertTrue(billing.isPaid());
    }

    @Test
    public void testBillIdUniqueness() {
        Billing billing2 = new Billing(reservation);
        assertNotEquals(billing.getBillId(), billing2.getBillId());
    }

    @Test
    public void testSingleDayStay() {
        Calendar cal = Calendar.getInstance();
        Date singleDayCheckIn = cal.getTime();
        cal.add(Calendar.HOUR, 24);
        Date singleDayCheckOut = cal.getTime();
        
        Reservation singleDayReservation = new Reservation(customer, room, singleDayCheckIn, singleDayCheckOut);
        Billing singleDayBilling = new Billing(singleDayReservation);
        
        assertEquals(4000.0, singleDayBilling.getTotalAmount(), 0.001);
    }

    @Test
    public void testLongStay() {
        Calendar cal = Calendar.getInstance();
        Date longStayCheckIn = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 30); // 30 days stay
        Date longStayCheckOut = cal.getTime();
        
        Reservation longStayReservation = new Reservation(customer, room, longStayCheckIn, longStayCheckOut);
        Billing longStayBilling = new Billing(longStayReservation);
        
        assertEquals(120000.0, longStayBilling.getTotalAmount(), 0.001); // 30 * 4000
    }

    @Test
    public void testReservationAssociation() {
        assertEquals(reservation, billing.getReservation());
        assertEquals(customer, billing.getReservation().getCustomer());
        assertEquals(room, billing.getReservation().getRoom());
    }

    @Test
    public void testFoodRevenueCalculation() {
        // Add food orders to the reservation
        Food food1 = new Food("Burger", 200.0);
        Food food2 = new Food("Pizza", 300.0);
        reservation.addFoodOrder(food1);
        reservation.addFoodOrder(food2);
        
        // Create new billing with food orders
        Billing billingWithFood = new Billing(reservation);
        
        // Total should be room cost (12000) + food cost (500)
        assertEquals(12500.0, billingWithFood.getTotalAmount(), 0.001);
    }
} 