import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;

public class Main {
    private static HotelManager hotelManager = new HotelManager();
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int choice;
        do {
            displayMainMenu();
            choice = sc.nextInt();
            handleMenuChoice(choice);
        } while (choice != 5);
    }

    public static void displayMainMenu() {
        System.out.println("\nHotel Management System");
        System.out.println("1. Display room details");
        System.out.println("2. Display room availability");
        System.out.println("3. Book a room");
        System.out.println("4. Check out");
        System.out.println("5. Exit");
        System.out.print("Enter your choice: ");
    }

    static void handleMenuChoice(int choice) {
        switch (choice) {
            case 1:
                displayRoomDetails();
                break;
            case 2:
                displayRoomAvailability();
                break;
            case 3:
                bookRoom();
                break;
            case 4:
                checkOut();
                break;
            case 5:
                System.out.println("Thank you for using our system!");
                break;
            default:
                System.out.println("Invalid choice!");
        }
    }

    public static void displayRoomDetails() {
        System.out.println("\nRoom Types and Features:");
        System.out.println("1. Luxury Double Room");
        System.out.println("2. Deluxe Double Room");
        System.out.println("3. Luxury Single Room");
        System.out.println("4. Deluxe Single Room");
        
        int choice = sc.nextInt();
        String roomType = getRoomTypeFromChoice(choice);
        if (roomType != null) {
            Room room = new Room(0, roomType);
            System.out.println("\nFeatures:");
            System.out.println("Number of beds: " + room.getNumberOfBeds());
            System.out.println("AC: " + (room.isAC() ? "Yes" : "No"));
            System.out.println("Breakfast: " + (room.hasBreakfast() ? "Yes" : "No"));
            System.out.println("Price per day: Rs." + room.getPricePerDay());
        }
    }

    public static String getRoomTypeFromChoice(int choice) {
        switch (choice) {
            case 1: return "LUXURY_DOUBLE";
            case 2: return "DELUXE_DOUBLE";
            case 3: return "LUXURY_SINGLE";
            case 4: return "DELUXE_SINGLE";
            default: return null;
        }
    }

    public static void displayRoomAvailability() {
        System.out.println("\nRoom Availability:");
        for (Room room : hotelManager.getRooms()) {
            System.out.printf("Room %d: %s - %s%n", 
                room.getRoomNumber(),
                room.getRoomType(),
                room.isOccupied() ? "Occupied" : "Available");
        }
    }

    private static void bookRoom() {
        System.out.println("\nBooking a Room");
        System.out.println("Available Room Types:");
        System.out.println("1. Luxury Double Room");
        System.out.println("2. Deluxe Double Room");
        System.out.println("3. Luxury Single Room");
        System.out.println("4. Deluxe Single Room");
        System.out.print("Select room type (1-4): ");
        
        int roomTypeChoice = sc.nextInt();
        String roomType = getRoomTypeFromChoice(roomTypeChoice);
        if (roomType == null) {
            System.out.println("Invalid room type selection!");
            return;
        }

        System.out.print("Enter customer name: ");
        sc.nextLine(); // Consume newline
        String name = sc.nextLine();
        
        System.out.print("Enter contact number: ");
        String contact = sc.nextLine();
        
        System.out.print("Enter gender (Male/Female): ");
        String gender = sc.nextLine();
        
        Customer customer = new Customer(name, contact, gender);
        
        System.out.print("Enter check-in date (dd/MM/yyyy): ");
        String checkInStr = sc.nextLine();
        System.out.print("Enter check-out date (dd/MM/yyyy): ");
        String checkOutStr = sc.nextLine();
        
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date checkIn = dateFormat.parse(checkInStr);
            Date checkOut = dateFormat.parse(checkOutStr);
            
            // Find an available room of the selected type
            Room availableRoom = hotelManager.getRooms().stream()
                .filter(room -> room.getRoomType().equals(roomType) && !room.isOccupied())
                .findFirst()
                .orElse(null);
                
            if (availableRoom == null) {
                System.out.println("No available rooms of selected type!");
                return;
            }
            
            Reservation reservation = hotelManager.createReservation(customer, availableRoom.getRoomNumber(), checkIn, checkOut);
            if (reservation != null) {
                System.out.println("Room booked successfully!");
                System.out.println("Reservation ID: " + reservation.getReservationId());
                System.out.println("Room Number: " + availableRoom.getRoomNumber());
            } else {
                System.out.println("Failed to book room!");
            }
        } catch (ParseException e) {
            System.out.println("Invalid date format! Please use dd/MM/yyyy");
        }
    }

    private static void checkOut() {
        System.out.println("\nCheckout Process");
        System.out.print("Enter room number: ");
        int roomNumber = sc.nextInt();
        
        Reservation reservation = hotelManager.findReservationByRoom(roomNumber);
        if (reservation == null) {
            System.out.println("No active reservation found for this room!");
            return;
        }
        
        hotelManager.checkOut(reservation.getReservationId());
        Billing bill = hotelManager.generateBill(reservation.getReservationId());
        
        System.out.println("Checkout successful!");
        System.out.println("Total amount: Rs." + String.format("%.2f", bill.getTotalAmount()));
    }
}

