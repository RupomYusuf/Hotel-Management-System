import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Customer implements Serializable {
    private String name;
    private String contact;
    private String gender;
    private String email;
    private String id; // for unique identification
    private ArrayList<Reservation> reservations;

    // Constructor with ID given explicitly
    public Customer(String id, String name, String email, String gender, String contact) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.contact = contact;
        this.reservations = new ArrayList<>();
    }

    // Constructor without ID - generates ID internally
    public Customer(String name, String contact, String gender) {
        this.name = name;
        this.contact = contact;
        this.gender = gender;
        this.email = ""; // Default empty email
        this.id = generateId();
        this.reservations = new ArrayList<>();
    }

    private String generateId() {
        return "CUST" + UUID.randomUUID().toString().replace("-", "");
    }

    // Getters and setters
    public String getName() { return name; }
    public String getContact() { return contact; }
    public String getGender() { return gender; }
    public String getEmail() { return email; }
    public String getId() { return id; }
    public List<Reservation> getReservations() { return reservations; }

    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.name = name;
    }

    public void setContact(String contact) {
        if (contact == null || contact.trim().isEmpty()) {
            throw new IllegalArgumentException("Contact cannot be null or empty");
        }
        if (!contact.matches("\\d{10}")) {
            throw new IllegalArgumentException("Contact number must be exactly 10 digits");
        }
        this.contact = contact;
    }

    public void setGender(String gender) {
        if (gender == null || gender.trim().isEmpty()) {
            throw new IllegalArgumentException("Gender cannot be null or empty");
        }
        if (!gender.equalsIgnoreCase("Male") && !gender.equalsIgnoreCase("Female") && !gender.equalsIgnoreCase("Other")) {
            throw new IllegalArgumentException("Gender must be Male, Female, or Other");
        }
        this.gender = gender;
    }

    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (!email.matches("^[A-Za-z0-9+_.'\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        this.email = email;
    }

    public void setContactNumber(String contactNumber) {
        if (contactNumber == null || contactNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Contact number cannot be null or empty");
        }
        if (!contactNumber.matches("\\d{10}")) {
            throw new IllegalArgumentException("Contact number must be 10 digits");
        }
        this.contact = contactNumber;
    }
}
