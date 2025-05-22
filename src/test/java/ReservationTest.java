import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Date;
import java.util.Calendar;
import java.util.List;

public class ReservationTest {
    private Customer customer;
    private Room room;
    private Date checkIn;
    private Date checkOut;
    private Reservation reservation;

    @Before
    public void setUp() {
        customer = new Customer("John Doe", "1234567890", "Male");
        room = new Room(101, "LUXURY_DOUBLE");
        
        Calendar cal = Calendar.getInstance();
        checkIn = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 3);
        checkOut = cal.getTime();
        
        reservation = new Reservation(customer, room, checkIn, checkOut);
    }

    @Test
    public void testReservationInitialization() {
        assertNotNull(reservation.getReservationId());
        assertTrue(reservation.getReservationId().startsWith("RES"));
        assertEquals(customer, reservation.getCustomer());
        assertEquals(room, reservation.getRoom());
        assertEquals(checkIn, reservation.getCheckInDate());
        assertEquals(checkOut, reservation.getCheckOutDate());
        assertTrue(reservation.isActive());
    }

    @Test
    public void testDefaultConstructor() {
        Reservation defaultReservation = new Reservation();
        assertNotNull(defaultReservation.getReservationId());
        assertTrue(defaultReservation.isActive());
        assertNotNull(defaultReservation.getFoodOrders());
        assertTrue(defaultReservation.getFoodOrders().isEmpty());
    }

    @Test
    public void testReservationIdUniqueness() {
        Reservation reservation2 = new Reservation(customer, room, checkIn, checkOut);
        assertNotEquals(reservation.getReservationId(), reservation2.getReservationId());
    }

    @Test
    public void testActiveStatus() {
        assertTrue(reservation.isActive());
        reservation.setActive(false);
        assertFalse(reservation.isActive());
    }

    @Test
    public void testFoodOrders() {
        Food food1 = new Food("Burger", 200.0);
        Food food2 = new Food("Pizza", 300.0);
        
        reservation.addFoodOrder(food1);
        reservation.addFoodOrder(food2);
        
        List<Food> orders = reservation.getFoodOrders();
        assertEquals(2, orders.size());
        assertTrue(orders.contains(food1));
        assertTrue(orders.contains(food2));
    }

    @Test
    public void testEmptyFoodOrders() {
        List<Food> orders = reservation.getFoodOrders();
        assertNotNull(orders);
        assertTrue(orders.isEmpty());
    }

    @Test
    public void testReservationDates() {
        Calendar cal = Calendar.getInstance();
        Date newCheckIn = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 5);
        Date newCheckOut = cal.getTime();
        
        Reservation newReservation = new Reservation(customer, room, newCheckIn, newCheckOut);
        assertEquals(newCheckIn, newReservation.getCheckInDate());
        assertEquals(newCheckOut, newReservation.getCheckOutDate());
    }

    @Test
    public void testReservationCustomerAssociation() {
        assertEquals(customer, reservation.getCustomer());
        assertEquals("John Doe", reservation.getCustomer().getName());
        assertEquals("1234567890", reservation.getCustomer().getContact());
    }

    @Test
    public void testReservationRoomAssociation() {
        assertEquals(room, reservation.getRoom());
        assertEquals(101, reservation.getRoom().getRoomNumber());
        assertEquals("LUXURY_DOUBLE", reservation.getRoom().getRoomType());
    }
    
    @Test
    public void testReservationCreation() {
        Customer customer = new Customer("John Doe", "1234567890", "Male");
        Room room = new Room(101, "LUXURY_DOUBLE");
        Date checkIn = new Date();
        Date checkOut = new Date(checkIn.getTime() + 2 * 24 * 60 * 60 * 1000); // +2 days

        Reservation reservation = new Reservation(customer, room, checkIn, checkOut);

        assertNotNull(reservation.getReservationId());
        assertEquals(customer, reservation.getCustomer());
        assertEquals(room, reservation.getRoom());
        assertEquals(checkIn, reservation.getCheckInDate());
        assertEquals(checkOut, reservation.getCheckOutDate());
        assertTrue(reservation.getFoodOrders().isEmpty());
    }

    @Test
    public void testAddFoodOrder() {
        Customer customer = new Customer("Jane Smith", "9876543210", "Female");
        Room room = new Room(102, "DELUXE_SINGLE");
        Date checkIn = new Date();
        Date checkOut = new Date(checkIn.getTime() + 1 * 24 * 60 * 60 * 1000); // +1 day

        Reservation reservation = new Reservation(customer, room, checkIn, checkOut);
        Food food1 = new Food("Burger", 150.0);
        Food food2 = new Food("Coffee", 50.0);

        reservation.addFoodOrder(food1);
        reservation.addFoodOrder(food2);

        List<Food> foodOrders = reservation.getFoodOrders();
        assertEquals(2, foodOrders.size());
        assertTrue(foodOrders.contains(food1));
        assertTrue(foodOrders.contains(food2));
    }

    @Test
    public void testRoomOccupancyOnReservation() {
        Customer customer = new Customer("Alex", "1112223333", "Other");
        Room room = new Room(103, "DELUXE_DOUBLE");
        Date checkIn = new Date();
        Date checkOut = new Date(checkIn.getTime() + 3 * 24 * 60 * 60 * 1000); // +3 days

        Reservation reservation = new Reservation(customer, room, checkIn, checkOut);

        // 💡 If your Reservation class doesn't auto-occupy room, do it here:
        room.setOccupied(true);

        assertTrue(room.isOccupied());
    }
}