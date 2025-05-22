import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Date;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class HotelManagerTest {
    private HotelManager hotelManager;
    private Customer customer;
    private Room room;
    private Date checkIn;
    private Date checkOut;

    @Before
    public void setUp() {
        hotelManager = new HotelManager();
        customer = new Customer("John Doe", "1234567890", "Male");
        
        // Add a room
        room = new Room(101, "LUXURY_DOUBLE");
        hotelManager.getRooms().add(room);
        
        // Set up dates
        Calendar cal = Calendar.getInstance();
        checkIn = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 3);
        checkOut = cal.getTime();
    }

    @Test
    public void testInitialization() {
        HotelManager newManager = new HotelManager();
        assertNotNull(newManager.getRooms());
        assertNotNull(newManager.getBillings());
        assertTrue(newManager.getRooms().isEmpty());
        assertTrue(newManager.getBillings().isEmpty());
    }

    @Test
    public void testCreateReservation() {
        Reservation reservation = hotelManager.createReservation(customer, 101, checkIn, checkOut);
        assertNotNull(reservation);
        assertEquals(customer, reservation.getCustomer());
        assertEquals(room, reservation.getRoom());
        assertTrue(room.isOccupied());
    }

    @Test
    public void testCreateReservationWithOccupiedRoom() {
        hotelManager.createReservation(customer, 101, checkIn, checkOut);
        Customer customer2 = new Customer("Jane Doe", "0987654321", "Female");
        Reservation reservation2 = hotelManager.createReservation(customer2, 101, checkIn, checkOut);
        assertNull(reservation2); // Should be null as room is occupied
    }

    @Test
    public void testCheckOut() {
        Reservation reservation = hotelManager.createReservation(customer, 101, checkIn, checkOut);
        assertNotNull(reservation);
        assertTrue(room.isOccupied());
        
        hotelManager.checkOut(reservation.getReservationId());
        assertFalse(room.isOccupied());
        assertFalse(reservation.isActive());
    }

    @Test
    public void testFindRoomByNumber() {
        Room foundRoom = hotelManager.findRoomByNumber(101);
        assertNotNull(foundRoom);
        assertEquals(101, foundRoom.getRoomNumber());
        
        Room notFoundRoom = hotelManager.findRoomByNumber(999);
        assertNull(notFoundRoom);
    }

    @Test
    public void testFindCustomerByContact() {
        hotelManager.createReservation(customer, 101, checkIn, checkOut);
        Customer foundCustomer = hotelManager.findCustomerByContact("1234567890");
        assertNotNull(foundCustomer);
        assertEquals("John Doe", foundCustomer.getName());
        
        Customer notFoundCustomer = hotelManager.findCustomerByContact("9999999999");
        assertNull(notFoundCustomer);
    }

    @Test
    public void testGetFrequentCustomers() {
        // Add all required rooms first
        Room room2 = new Room(102, "LUXURY_DOUBLE");
        Room room3 = new Room(103, "LUXURY_DOUBLE");
        hotelManager.getRooms().add(room2);
        hotelManager.getRooms().add(room3);
        
        // Create multiple reservations for the same customer
        hotelManager.createReservation(customer, 101, checkIn, checkOut);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 5);
        hotelManager.createReservation(customer, 102, cal.getTime(), checkOut);
        cal.add(Calendar.DAY_OF_MONTH, 5);
        hotelManager.createReservation(customer, 103, cal.getTime(), checkOut);
        
        List<Customer> frequentCustomers = hotelManager.getFrequentCustomers();
        assertEquals(1, frequentCustomers.size());
        assertEquals(customer, frequentCustomers.get(0));
        
        // Verify customer has 3 reservations
        List<Reservation> customerReservations = hotelManager.getCustomerHistory(customer.getId());
        assertEquals(3, customerReservations.size());
    }

    @Test
    public void testGetCustomerHistory() {
        Reservation reservation = hotelManager.createReservation(customer, 101, checkIn, checkOut);
        List<Reservation> history = hotelManager.getCustomerHistory(customer.getId());
        assertEquals(1, history.size());
        assertEquals(reservation, history.get(0));
    }

    @Test
    public void testGetCustomerStatistics() {
        hotelManager.createReservation(customer, 101, checkIn, checkOut);
        Map<String, Integer> stats = hotelManager.getCustomerStatistics();
        assertEquals(1, stats.get("totalCustomers").intValue());
        assertEquals(1, stats.get("activeCustomers").intValue());
    }

    @Test
    public void testAddFoodToReservation() {
        Reservation reservation = hotelManager.createReservation(customer, 101, checkIn, checkOut);
        Food food = new Food("Burger", 200.0);
        hotelManager.addFoodToReservation(101, food);
        
        List<Food> orders = reservation.getFoodOrders();
        assertEquals(1, orders.size());
        assertEquals(food, orders.get(0));
    }

    @Test
    public void testFindReservationByRoom() {
        Reservation reservation = hotelManager.createReservation(customer, 101, checkIn, checkOut);
        Reservation foundReservation = hotelManager.findReservationByRoom(101);
        assertEquals(reservation, foundReservation);
        
        Reservation notFoundReservation = hotelManager.findReservationByRoom(999);
        assertNull(notFoundReservation);
    }

    @Test
    public void testInvalidRoomScenarios() {
        // Test creating reservation with non-existent room
        Reservation invalidReservation = hotelManager.createReservation(customer, 999, checkIn, checkOut);
        assertNull(invalidReservation);

        // Test adding food to non-existent reservation
        Food food = new Food("Burger", 200.0);
        hotelManager.addFoodToReservation(999, food);
        assertTrue(hotelManager.getBillings().isEmpty());

        // Test finding room with invalid number
        Room invalidRoom = hotelManager.findRoomByNumber(999);
        assertNull(invalidRoom);
    }
} 