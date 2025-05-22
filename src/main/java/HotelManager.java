import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;
import java.io.Serializable;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

public class HotelManager implements Serializable {
    private List<Room> rooms;
    private Map<String, Customer> customers;
    private Map<String, Reservation> reservations;
    private Map<String, Billing> billings;
    private Map<String, User> users;
    private List<MenuItem> menuItems;

    public HotelManager() {
        this.rooms = new ArrayList<>();
        this.customers = new HashMap<>();
        this.reservations = new HashMap<>();
        this.billings = new HashMap<>();
        this.users = new HashMap<>();
        this.menuItems = new ArrayList<>();
        // Add default admin user
        users.put("admin", new User("admin", "admin123", User.UserRole.ADMIN));
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public Room findRoomByNumber(int roomNumber) {
        return rooms.stream()
            .filter(room -> room.getRoomNumber() == roomNumber)
            .findFirst()
            .orElse(null);
    }

    public User findUser(String username) {
        return users.get(username);
    }

    public Reservation createReservation(Customer customer, int roomNumber, 
                                      Date checkIn, Date checkOut) {
        Room room = findRoomByNumber(roomNumber);
        if (room != null && !room.isOccupied()) {
            customers.putIfAbsent(customer.getId(), customer);
            Reservation reservation = new Reservation(customer, room, checkIn, checkOut);
            reservations.put(reservation.getReservationId(), reservation);
            room.setOccupied(true);
            customer.addReservation(reservation);
            return reservation;
        }
        return null;
    }

    public void checkOut(String reservationId) {
        Reservation reservation = reservations.get(reservationId);
        if (reservation != null && reservation.isActive()) {
            reservation.setActive(false);
            reservation.getRoom().setOccupied(false);
            Billing bill = generateBill(reservationId);
            billings.put(bill.getBillId(), bill);
        }
    }

    public Billing generateBill(String reservationId) {
        Reservation reservation = reservations.get(reservationId);
        if (reservation != null) {
            Billing bill = new Billing(reservation);
            billings.put(bill.getBillId(), bill);
            return bill;
        }
        return null;
    }

    public ArrayList<MenuItem> getMenuItems() {
        return new ArrayList<>(menuItems);
    }

    public Map<String, Billing> getBillings() {
        return billings;
    }

    public Customer findCustomerByContact(String contact) {
        return customers.values().stream()
                       .filter(c -> c.getContact().equals(contact))
                       .findFirst()
                       .orElse(null);
    }

    public List<Customer> getFrequentCustomers() {
        Map<Customer, Integer> customerReservationCount = new HashMap<>();
        
        // Count reservations for each customer
        for (Reservation reservation : reservations.values()) {
            Customer customer = reservation.getCustomer();
            customerReservationCount.put(customer, 
                customerReservationCount.getOrDefault(customer, 0) + 1);
        }
        
        // Filter customers with more than 1 reservation
        return customerReservationCount.entrySet().stream()
            .filter(entry -> entry.getValue() > 1)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
    }

    public List<Reservation> getCustomerHistory(String customerId) {
        Customer customer = customers.get(customerId);
        return customer != null ? customer.getReservations() : new ArrayList<>();
    }

    public Map<String, Integer> getCustomerStatistics() {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("totalCustomers", customers.size());
        stats.put("activeCustomers", (int) customers.values().stream()
                 .filter(c -> c.getReservations().stream()
                             .anyMatch(Reservation::isActive))
                 .count());
        return stats;
    }

    public void addFoodToReservation(int roomNumber, Food food) {
        rooms.stream()
             .filter(room -> room.getRoomNumber() == roomNumber && room.isOccupied())
             .findFirst()
             .ifPresent(room -> {
                 reservations.values().stream()
                           .filter(res -> res.getRoom().equals(room) && res.isActive())
                           .findFirst()
                           .ifPresent(res -> res.addFoodOrder(food));
             });
    }

    public Reservation findReservationByRoom(int roomNumber) {
        return reservations.values().stream()
                         .filter(res -> res.getRoom().getRoomNumber() == roomNumber 
                                      && res.isActive())
                         .findFirst()
                         .orElse(null);
    }
} 