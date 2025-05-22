import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FoodTest {
    private Food food;
    private Food sameFood;
    private Food differentFood;

    @Before
    public void setUp() {
        food = new Food("Burger", 200.0);
        sameFood = new Food("Burger", 200.0);
        differentFood = new Food("Pizza", 300.0);
    }

    @Test
    public void testFoodInitialization() {
        assertEquals("Burger", food.getName());
        assertEquals(200.0, food.getPrice(), 0.001);
    }

    @Test
    public void testFoodWithZeroPrice() {
        Food freeFood = new Food("Complimentary Item", 0.0);
        assertEquals("Complimentary Item", freeFood.getName());
        assertEquals(0.0, freeFood.getPrice(), 0.001);
    }

    @Test
    public void testFoodWithHighPrice() {
        Food expensiveFood = new Food("Premium Item", 9999.99);
        assertEquals("Premium Item", expensiveFood.getName());
        assertEquals(9999.99, expensiveFood.getPrice(), 0.001);
    }

    @Test
    public void testFoodWithSpecialCharacters() {
        Food specialFood = new Food("Chicken & Rice", 250.0);
        assertEquals("Chicken & Rice", specialFood.getName());
        assertEquals(250.0, specialFood.getPrice(), 0.001);
    }

    @Test
    public void testFoodWithEmptyName() {
        Food emptyNameFood = new Food("", 100.0);
        assertEquals("", emptyNameFood.getName());
        assertEquals(100.0, emptyNameFood.getPrice(), 0.001);
    }

    @Test
    public void testFoodEquality() {
        // Same object
        assertTrue(food.equals(food));
        
        // Different object with same values
        assertTrue(food.equals(sameFood));
        assertTrue(sameFood.equals(food));
        
        // Different object with different values
        assertFalse(food.equals(differentFood));
        assertFalse(differentFood.equals(food));
        
        // Null comparison
        assertFalse(food.equals(null));
        
        // Different type
        assertFalse(food.equals(new Object()));
    }

    @Test
    public void testFoodHashCode() {
        // Same object should have same hash code
        assertEquals(food.hashCode(), food.hashCode());
        
        // Equal objects should have same hash code
        assertEquals(food.hashCode(), sameFood.hashCode());
        
        // Different objects should have different hash codes
        assertNotEquals(food.hashCode(), differentFood.hashCode());
    }

    @Test
    public void testFoodWithNegativePrice() {
        Food negativePriceFood = new Food("Discount Item", -50.0);
        assertEquals("Discount Item", negativePriceFood.getName());
        assertEquals(-50.0, negativePriceFood.getPrice(), 0.001);
    }

    @Test
    public void testFoodWithVeryLongName() {
        String longName = "Super Deluxe Extra Large Double Cheese Triple Patty Burger with Special Sauce";
        Food longNameFood = new Food(longName, 500.0);
        assertEquals(longName, longNameFood.getName());
        assertEquals(500.0, longNameFood.getPrice(), 0.001);
    }

    @Test
    public void testFoodWithPrecisionPrice() {
        Food precisePriceFood = new Food("Precise Item", 199.999);
        assertEquals("Precise Item", precisePriceFood.getName());
        assertEquals(199.999, precisePriceFood.getPrice(), 0.001);
    }

    @Test
    public void testFoodWithUnicodeCharacters() {
        Food unicodeFood = new Food("Sushi 寿司", 400.0);
        assertEquals("Sushi 寿司", unicodeFood.getName());
        assertEquals(400.0, unicodeFood.getPrice(), 0.001);
    }
} 