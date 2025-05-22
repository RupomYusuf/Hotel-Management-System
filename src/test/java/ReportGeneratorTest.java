import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class ReportGeneratorTest {
    private HotelManager hotelManager;
    private ReportGenerator reportGenerator;
    private Customer customer;
    private Room room;
    private Reservation reservation;
    private Billing billing;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Before
    public void setUp() {
        // Redirect System.out to capture output
        System.setOut(new PrintStream(outContent));
        
        // Initialize test objects
        hotelManager = new HotelManager();
        reportGenerator = new ReportGenerator(hotelManager);
        
        // Create test customer
        customer = new Customer("John Doe", "1234567890", "Male");
        
        // Create test room
        room = new Room(101, "LUXURY_DOUBLE");
        hotelManager.getRooms().add(room);
        
        // Create test reservation
        Calendar cal = Calendar.getInstance();
        Date checkIn = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 3);
        Date checkOut = cal.getTime();
        
        reservation = new Reservation(customer, room, checkIn, checkOut);
        hotelManager.createReservation(customer, room.getRoomNumber(), checkIn, checkOut);
        
        // Create test billing
        billing = new Billing(reservation);
        hotelManager.getBillings().put(billing.getBillId(), billing);
    }

    @Test
    public void testGenerateDailyRevenue() {
        // Test with today's date
        Date today = new Date();
        Map<String, Double> revenue = reportGenerator.generateDailyRevenue(today);
        
        assertNotNull(revenue);
        assertTrue(revenue.containsKey("roomRevenue"));
        assertTrue(revenue.containsKey("foodRevenue"));
        assertTrue(revenue.containsKey("totalRevenue"));
        
        // Verify room revenue calculation
        double expectedRoomRevenue = room.getPricePerDay() * 3; // 3 days stay
        assertEquals(expectedRoomRevenue, revenue.get("roomRevenue"), 0.001);
        
        // Verify food revenue is initially 0
        assertEquals(0.0, revenue.get("foodRevenue"), 0.001);
        
        // Verify total revenue
        assertEquals(expectedRoomRevenue, revenue.get("totalRevenue"), 0.001);
    }

    @Test
    public void testGenerateDailyRevenueWithFood() {
        // Add food orders to the reservation
        Food food1 = new Food("Burger", 200.0);
        Food food2 = new Food("Pizza", 300.0);
        reservation.addFoodOrder(food1);
        reservation.addFoodOrder(food2);
        
        // Test with today's date
        Date today = new Date();
        Map<String, Double> revenue = reportGenerator.generateDailyRevenue(today);
        
        // Verify food revenue
        assertEquals(500.0, revenue.get("foodRevenue"), 0.001);
        
        // Verify total revenue includes both room and food
        double expectedTotal = (room.getPricePerDay() * 3) + 500.0;
        assertEquals(expectedTotal, revenue.get("totalRevenue"), 0.001);
    }

    @Test
    public void testGenerateMonthlyRevenue() {
        Calendar cal = Calendar.getInstance();
        int currentMonth = cal.get(Calendar.MONTH);
        int currentYear = cal.get(Calendar.YEAR);
        
        Map<String, Double> revenue = reportGenerator.generateMonthlyRevenue(currentMonth, currentYear);
        
        assertNotNull(revenue);
        assertTrue(revenue.containsKey("roomRevenue"));
        assertTrue(revenue.containsKey("foodRevenue"));
        assertTrue(revenue.containsKey("totalRevenue"));
        
        // Verify room revenue calculation
        double expectedRoomRevenue = room.getPricePerDay() * 3; // 3 days stay
        assertEquals(expectedRoomRevenue, revenue.get("roomRevenue"), 0.001);
    }

    @Test
    public void testGenerateOccupancyReport() {
        Map<String, Object> report = reportGenerator.generateOccupancyReport();
        
        assertNotNull(report);
        assertTrue(report.containsKey("totalRooms"));
        assertTrue(report.containsKey("occupiedRooms"));
        assertTrue(report.containsKey("availableRooms"));
        assertTrue(report.containsKey("occupancyRate"));
        assertTrue(report.containsKey("roomTypeOccupancy"));
        
        // Verify room counts
        assertEquals(1, report.get("totalRooms"));
        assertEquals(1, report.get("occupiedRooms"));
        assertEquals(0, report.get("availableRooms"));
        
        // Verify occupancy rate
        assertEquals(100.0, report.get("occupancyRate"));
        
        // Verify room type occupancy
        @SuppressWarnings("unchecked")
        Map<String, Integer> roomTypeOccupancy = (Map<String, Integer>) report.get("roomTypeOccupancy");
        assertEquals(1, roomTypeOccupancy.get("LUXURY_DOUBLE").intValue());
    }

    @Test
    public void testGenerateDailyRevenueNoBills() {
        // Create a date far in the future
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 1);
        Date futureDate = cal.getTime();
        
        Map<String, Double> revenue = reportGenerator.generateDailyRevenue(futureDate);
        
        assertEquals(0.0, revenue.get("roomRevenue"), 0.001);
        assertEquals(0.0, revenue.get("foodRevenue"), 0.001);
        assertEquals(0.0, revenue.get("totalRevenue"), 0.001);
    }

    @Test
    public void testGenerateMonthlyRevenueNoBills() {
        // Create a month far in the future
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 1);
        int futureMonth = cal.get(Calendar.MONTH);
        int futureYear = cal.get(Calendar.YEAR);
        
        Map<String, Double> revenue = reportGenerator.generateMonthlyRevenue(futureMonth, futureYear);
        
        assertEquals(0.0, revenue.get("roomRevenue"), 0.001);
        assertEquals(0.0, revenue.get("foodRevenue"), 0.001);
        assertEquals(0.0, revenue.get("totalRevenue"), 0.001);
    }

    @Test
    public void testGenerateOccupancyReportNoRooms() {
        // Create a new hotel manager with no rooms
        HotelManager emptyManager = new HotelManager();
        ReportGenerator emptyGenerator = new ReportGenerator(emptyManager);
        
        Map<String, Object> report = emptyGenerator.generateOccupancyReport();
        
        assertEquals(0, report.get("totalRooms"));
        assertEquals(0, report.get("occupiedRooms"));
        assertEquals(0, report.get("availableRooms"));
        assertEquals(0.0, report.get("occupancyRate"));
    }
} 