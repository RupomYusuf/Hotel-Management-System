import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MenuItemTest {
    private MenuItem menuItem;
    private MenuItem menuItemWithDescription;

    @Before
    public void setUp() {
        menuItem = new MenuItem(1, "Burger", 200.0, "Main Course");
        menuItemWithDescription = new MenuItem(2, "Pizza", 300.0, "Main Course", "Delicious cheese pizza");
    }

    @Test
    public void testMenuItemInitialization() {
        assertEquals(1, menuItem.getId());
        assertEquals("Burger", menuItem.getName());
        assertEquals(200.0, menuItem.getPrice(), 0.001);
        assertEquals("Main Course", menuItem.getCategory());
        assertEquals("", menuItem.getDescription());
        assertTrue(menuItem.isAvailable());
    }

    @Test
    public void testMenuItemWithDescriptionInitialization() {
        assertEquals(2, menuItemWithDescription.getId());
        assertEquals("Pizza", menuItemWithDescription.getName());
        assertEquals(300.0, menuItemWithDescription.getPrice(), 0.001);
        assertEquals("Main Course", menuItemWithDescription.getCategory());
        assertEquals("Delicious cheese pizza", menuItemWithDescription.getDescription());
        assertTrue(menuItemWithDescription.isAvailable());
    }

    @Test
    public void testSetDescription() {
        menuItem.setDescription("Juicy beef burger");
        assertEquals("Juicy beef burger", menuItem.getDescription());
    }

    @Test
    public void testSetAvailability() {
        menuItem.setAvailable(false);
        assertFalse(menuItem.isAvailable());
        menuItem.setAvailable(true);
        assertTrue(menuItem.isAvailable());
    }

    @Test
    public void testToString() {
        String expected = "Burger - Rs.200.00";
        assertEquals(expected, menuItem.toString());
    }

    @Test
    public void testToStringWithDescription() {
        String expected = "Pizza - Rs.300.00 (Delicious cheese pizza)";
        assertEquals(expected, menuItemWithDescription.toString());
    }

    @Test
    public void testToStringWhenNotAvailable() {
        menuItem.setAvailable(false);
        String expected = "Burger - Rs.200.00 [Not Available]";
        assertEquals(expected, menuItem.toString());
    }

    @Test
    public void testToStringWithDescriptionWhenNotAvailable() {
        menuItemWithDescription.setAvailable(false);
        String expected = "Pizza - Rs.300.00 (Delicious cheese pizza) [Not Available]";
        assertEquals(expected, menuItemWithDescription.toString());
    }

    @Test
    public void testMenuItemEquality() {
        MenuItem sameItem = new MenuItem(1, "Burger", 200.0, "Main Course");
        MenuItem differentItem = new MenuItem(2, "Burger", 200.0, "Main Course");
        
        assertNotEquals(menuItem, sameItem); // Different instances
        assertNotEquals(menuItem, differentItem); // Different IDs
    }

    @Test
    public void testMenuItemWithSpecialCharacters() {
        MenuItem item = new MenuItem(3, "Chicken & Rice", 250.0, "Main Course", "Grilled chicken with steamed rice");
        assertEquals("Chicken & Rice", item.getName());
        assertEquals("Grilled chicken with steamed rice", item.getDescription());
    }

    @Test
    public void testMenuItemWithLongDescription() {
        String longDescription = "A delicious combination of fresh ingredients including " +
            "locally sourced vegetables, premium quality meat, and special herbs and spices " +
            "prepared by our expert chefs using traditional cooking methods.";
        MenuItem item = new MenuItem(4, "Special Platter", 500.0, "Main Course", longDescription);
        assertEquals(longDescription, item.getDescription());
    }

    @Test
    public void testMenuItemWithZeroPrice() {
        MenuItem item = new MenuItem(5, "Complimentary Item", 0.0, "Special", "Free item");
        assertEquals(0.0, item.getPrice(), 0.001);
    }

    @Test
    public void testMenuItemWithHighPrice() {
        MenuItem item = new MenuItem(6, "Premium Item", 9999.99, "Luxury", "Luxury item");
        assertEquals(9999.99, item.getPrice(), 0.001);
    }

    @Test
    public void testMenuItemWithEmptyName() {
        MenuItem item = new MenuItem(7, "", 100.0, "Special", "No name item");
        assertEquals("", item.getName());
    }

    @Test
    public void testMenuItemWithEmptyDescription() {
        MenuItem item = new MenuItem(8, "Simple Item", 100.0, "Main Course", "");
        assertEquals("", item.getDescription());
    }

    @Test
    public void testMenuItemAvailabilityToggle() {
        MenuItem item = new MenuItem(9, "Test Item", 100.0, "Main Course", "Test Description");
        assertTrue(item.isAvailable());
        
        item.setAvailable(false);
        assertFalse(item.isAvailable());
        
        item.setAvailable(true);
        assertTrue(item.isAvailable());
    }

    @Test
    public void testMenuItemHashCode() {
        MenuItem item1 = new MenuItem(10, "Same Item", 100.0, "Main Course", "Same Description");
        MenuItem item2 = new MenuItem(11, "Same Item", 100.0, "Main Course", "Same Description");
        MenuItem item3 = new MenuItem(12, "Different Item", 200.0, "Main Course", "Different Description");
        
        assertNotEquals(item1.hashCode(), item2.hashCode()); // Different IDs
        assertNotEquals(item1.hashCode(), item3.hashCode());
    }

    @Test
    public void testMenuItemToStringWithAllFields() {
        MenuItem item = new MenuItem(13, "Test Item", 100.0, "Main Course", "Test Description");
        String toString = item.toString();
        
        assertTrue(toString.contains("Test Item"));
        assertTrue(toString.contains("Test Description"));
        assertTrue(toString.contains("100.00"));
    }
} 