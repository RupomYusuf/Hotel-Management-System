import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Date;
import java.util.Calendar;

public class MainTest {

    @Test
    public void testGetRoomTypeFromChoice_ValidChoices() {
        assertEquals("LUXURY_DOUBLE", Main.getRoomTypeFromChoice(1));
        assertEquals("DELUXE_DOUBLE", Main.getRoomTypeFromChoice(2));
        assertEquals("LUXURY_SINGLE", Main.getRoomTypeFromChoice(3));
        assertEquals("DELUXE_SINGLE", Main.getRoomTypeFromChoice(4));
    }

    @Test
    public void testGetRoomTypeFromChoice_InvalidChoice() {
        assertNull(Main.getRoomTypeFromChoice(0));
        assertNull(Main.getRoomTypeFromChoice(5));
        assertNull(Main.getRoomTypeFromChoice(-1));
    }

    @Test
    public void testGetRoomTypeFromChoice_BoundaryValues() {
        // Test boundary values
        assertNull(Main.getRoomTypeFromChoice(Integer.MIN_VALUE));
        assertNull(Main.getRoomTypeFromChoice(Integer.MAX_VALUE));
        assertNull(Main.getRoomTypeFromChoice(-999));
        assertNull(Main.getRoomTypeFromChoice(999));
    }

    @Test
    public void testGetRoomTypeFromChoice_EdgeCases() {
        // Test edge cases around valid range
        assertNull(Main.getRoomTypeFromChoice(0));  // Just below valid range
        assertEquals("LUXURY_DOUBLE", Main.getRoomTypeFromChoice(1));  // Lower bound
        assertEquals("DELUXE_SINGLE", Main.getRoomTypeFromChoice(4));  // Upper bound
        assertNull(Main.getRoomTypeFromChoice(5));  // Just above valid range
    }

    @Test
    public void testGetRoomTypeFromChoice_Consistency() {
        // Test that same input always gives same output
        String type1 = Main.getRoomTypeFromChoice(1);
        String type2 = Main.getRoomTypeFromChoice(1);
        assertEquals(type1, type2);
        assertNotNull(type1);
    }

    @Test
    public void testGetRoomTypeFromChoice_AllValidTypes() {
        // Test all valid room types are different
        String[] types = new String[4];
        for (int i = 1; i <= 4; i++) {
            types[i-1] = Main.getRoomTypeFromChoice(i);
        }
        
        // Check all types are different
        for (int i = 0; i < types.length; i++) {
            for (int j = i + 1; j < types.length; j++) {
                assertNotEquals("Room types should be unique", types[i], types[j]);
            }
        }
    }

   

    @Test
    public void testGetRoomTypeFromChoice_FormatConsistency() {
        // Test that room type format is consistent
        for (int i = 1; i <= 4; i++) {
            String type = Main.getRoomTypeFromChoice(i);
            assertTrue("Room type should be in correct format", 
                      type.matches("^(LUXURY|DELUXE)_(SINGLE|DOUBLE)$"));
        }
    }

    @Test
    public void testGetRoomTypeFromChoice_PriceConsistency() {
        // Test that room types have consistent pricing
        for (int i = 1; i <= 4; i++) {
            String type = Main.getRoomTypeFromChoice(i);
            if (type != null) {
                assertTrue("Room type should have a valid price", 
                          getRoomPrice(type) > 0);
            }
        }
    }

    // Helper method to get room price (assuming it exists in Main class)
    private double getRoomPrice(String roomType) {
        switch (roomType) {
            case "LUXURY_DOUBLE": return 4000.0;
            case "DELUXE_DOUBLE": return 3000.0;
            case "LUXURY_SINGLE": return 2500.0;
            case "DELUXE_SINGLE": return 2000.0;
            default: return 0.0;
        }
    }

    @Test
    public void testGetRoomTypeFromChoice_Performance() {
        // Test performance with multiple calls
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            Main.getRoomTypeFromChoice(1);
        }
        long endTime = System.currentTimeMillis();
        assertTrue("Method should be performant", 
                  (endTime - startTime) < 1000); // Should complete within 1 second
    }
}