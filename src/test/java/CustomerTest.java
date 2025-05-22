/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */

import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Comprehensive test cases for Customer class including corner cases.
 */
public class CustomerTest {

    private Customer customer;
    private Customer customerWithEmail;

    @Before
    public void setUp() {
        customer = new Customer("John Doe", "1234567890", "Male");
        customerWithEmail = new Customer("CUST123", "Jane Doe", "jane@example.com", "Female", "0987654321");
    }

    @After
    public void tearDown() {
        // Cleanup if needed after each test
        customer = null;
        customerWithEmail = null;
    }

    @Test
    public void testCustomerInitialization() {
        assertNotNull(customer.getId());
        assertTrue(customer.getId().startsWith("CUST"));
        assertEquals("John Doe", customer.getName());
        assertEquals("1234567890", customer.getContact());
        assertEquals("Male", customer.getGender());
        assertEquals("", customer.getEmail());
        assertNotNull(customer.getReservations());
        assertTrue(customer.getReservations().isEmpty());
    }

    @Test
    public void testCustomerWithEmailInitialization() {
        assertEquals("CUST123", customerWithEmail.getId());
        assertEquals("Jane Doe", customerWithEmail.getName());
        assertEquals("jane@example.com", customerWithEmail.getEmail());
        assertEquals("Female", customerWithEmail.getGender());
        assertEquals("0987654321", customerWithEmail.getContact());
    }

    @Test
    public void testSetValidName() {
        customer.setName("John Smith");
        assertEquals("John Smith", customer.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNullName() {
        customer.setName(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetEmptyName() {
        customer.setName("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetBlankName() {
        customer.setName("   ");
    }

    @Test
    public void testSetValidContact() {
        customer.setContact("9876543210");
        assertEquals("9876543210", customer.getContact());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNullContact() {
        customer.setContact(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetEmptyContact() {
        customer.setContact("");
    }

    @Test
    public void testSetValidGender() {
        customer.setGender("Female");
        assertEquals("Female", customer.getGender());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNullGender() {
        customer.setGender(null);
    }

    @Test
    public void testSetValidEmail() {
        customer.setEmail("john@example.com");
        assertEquals("john@example.com", customer.getEmail());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNullEmail() {
        customer.setEmail(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetEmptyEmail() {
        customer.setEmail("");
    }

    @Test
    public void testAddReservation() {
        Reservation reservation = new Reservation();
        customer.addReservation(reservation);
        List<Reservation> reservations = customer.getReservations();
        assertEquals(1, reservations.size());
        assertEquals(reservation, reservations.get(0));
    }

    @Test
    public void testMultipleReservations() {
        Reservation reservation1 = new Reservation();
        Reservation reservation2 = new Reservation();
        customer.addReservation(reservation1);
        customer.addReservation(reservation2);
        List<Reservation> reservations = customer.getReservations();
        assertEquals(2, reservations.size());
        assertTrue(reservations.contains(reservation1));
        assertTrue(reservations.contains(reservation2));
    }

    @Test
    public void testCustomerIdUniqueness() {
        Customer customer2 = new Customer("John Doe", "1234567890", "Male");
        assertNotEquals(customer.getId(), customer2.getId());
    }

    @Test
    public void testReservationListModification() {
        List<Reservation> reservations = customer.getReservations();
        Reservation reservation = new Reservation();
        reservations.add(reservation); // Try to modify the list directly
        assertEquals(1, customer.getReservations().size()); // Should not be modified
    }

   @Test
    public void testEmailFormatValidation() {
    // Initialize customer if not already done
    Customer customer = new Customer("CUST124", "Jane Do4e", "janwe@example.com", "male", "0987654321");

    // Valid email formats - should not throw exception
    try {
        customer.setEmail("user@example.com");
        customer.setEmail("user.name@example.com");
        customer.setEmail("user+tag@example.com");
        customer.setEmail("user@sub.example.com");
    } catch (IllegalArgumentException e) {
        fail("Valid email threw exception: " + e.getMessage());
    }

    // Invalid email formats should throw IllegalArgumentException
    String[] invalidEmails = { "invalid.email", "user@", "@example.com" };
    for (String email : invalidEmails) {
        try {
            customer.setEmail(email);
            fail("Should throw IllegalArgumentException for invalid email: " + email);
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
}

    @Test
    public void testContactNumberFormat() {
        // Valid contact numbers
        customer.setContact("1234567890");
        
        // Invalid contact numbers should throw exception
        String[] invalidContacts = {
            "123",           // Too short
            "12345678901",   // Too long
            "123456789a",    // Contains non-digit
            "123-456-7890",  // Contains special characters
            "+1234567890"    // Contains special characters
        };
        
        for (String invalidContact : invalidContacts) {
            try {
                customer.setContact(invalidContact);
                fail("Should throw IllegalArgumentException for invalid contact: " + invalidContact);
            } catch (IllegalArgumentException e) {
                // Expected exception
            }
        }
    }

    @Test
    public void testGenderValidation() {
        // Valid genders
        customer.setGender("Male");
        customer.setGender("Female");
        customer.setGender("Other");
        
        // Invalid gender should throw exception
        try {
            customer.setGender("Invalid");
            fail("Should throw IllegalArgumentException for invalid gender");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testConcurrentReservationAddition() {
        Reservation reservation1 = new Reservation();
        Reservation reservation2 = new Reservation();
        
        // Add reservations in different order
        customer.addReservation(reservation2);
        customer.addReservation(reservation1);
        
        List<Reservation> reservations = customer.getReservations();
        assertEquals(2, reservations.size());
        assertEquals(reservation2, reservations.get(0));
        assertEquals(reservation1, reservations.get(1));
    }

    @Test
    public void testCustomerEquality() {
        Customer customer1 = new Customer("John Doe", "1234567890", "Male");
        Customer customer2 = new Customer("John Doe", "1234567890", "Male");
        Customer customer3 = new Customer("Jane Doe", "0987654321", "Female");
        
        assertNotEquals(customer1, customer2); // Different instances
        assertNotEquals(customer1, customer3); // Different attributes
        assertNotEquals(customer1, null); // Null comparison
        assertNotEquals(customer1, new Object()); // Different type
    }

    @Test
    public void testCustomerHashCode() {
        Customer customer1 = new Customer("John Doe", "1234567890", "Male");
        Customer customer2 = new Customer("John Doe", "1234567890", "Male");
        
        assertNotEquals(customer1.hashCode(), customer2.hashCode()); // Different instances
    }
}
