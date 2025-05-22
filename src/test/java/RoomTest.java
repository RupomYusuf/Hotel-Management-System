import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RoomTest {
    private Room luxuryDouble;
    private Room deluxeDouble;
    private Room luxurySingle;
    private Room deluxeSingle;

    @Before
    public void setUp() {
        luxuryDouble = new Room(101, "LUXURY_DOUBLE");
        deluxeDouble = new Room(102, "DELUXE_DOUBLE");
        luxurySingle = new Room(201, "LUXURY_SINGLE");
        deluxeSingle = new Room(202, "DELUXE_SINGLE");
    }

    @Test
    public void testRoomInitialization() {
        assertEquals(101, luxuryDouble.getRoomNumber());
        assertEquals("LUXURY_DOUBLE", luxuryDouble.getRoomType());
        assertFalse(luxuryDouble.isOccupied());
    }

    @Test
    public void testLuxuryDoubleFeatures() {
        assertEquals(4000.0, luxuryDouble.getPricePerDay(), 0.001);
        assertTrue(luxuryDouble.isAC());
        assertTrue(luxuryDouble.hasBreakfast());
        assertEquals(2, luxuryDouble.getNumberOfBeds());
    }

    @Test
    public void testDeluxeDoubleFeatures() {
        assertEquals(3000.0, deluxeDouble.getPricePerDay(), 0.001);
        assertFalse(deluxeDouble.isAC());
        assertTrue(deluxeDouble.hasBreakfast());
        assertEquals(2, deluxeDouble.getNumberOfBeds());
    }

    @Test
    public void testLuxurySingleFeatures() {
        assertEquals(2200.0, luxurySingle.getPricePerDay(), 0.001);
        assertTrue(luxurySingle.isAC());
        assertTrue(luxurySingle.hasBreakfast());
        assertEquals(1, luxurySingle.getNumberOfBeds());
    }

    @Test
    public void testDeluxeSingleFeatures() {
        assertEquals(1200.0, deluxeSingle.getPricePerDay(), 0.001);
        assertFalse(deluxeSingle.isAC());
        assertTrue(deluxeSingle.hasBreakfast());
        assertEquals(1, deluxeSingle.getNumberOfBeds());
    }

    @Test
    public void testOccupancyStatus() {
        assertFalse(luxuryDouble.isOccupied());
        luxuryDouble.setOccupied(true);
        assertTrue(luxuryDouble.isOccupied());
        luxuryDouble.setOccupied(false);
        assertFalse(luxuryDouble.isOccupied());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidRoomType() {
        new Room(301, "INVALID_TYPE");
    }

    @Test
    public void testRoomEquality() {
        Room room1 = new Room(101, "LUXURY_DOUBLE");
        Room room2 = new Room(101, "LUXURY_DOUBLE");
        Room room3 = new Room(102, "LUXURY_DOUBLE");
        
        assertNotEquals(room1, room2); // Different instances
        assertNotEquals(room1, room3); // Different room numbers
    }
} 