import java.util.*;
import java.io.*;
public class a {
    public static void main(String[] args) {
        // Initialize menu
        HashMap<String, Integer> menu = new HashMap<>();
        menu.put("Pizza", 200);
        menu.put("Burger", 150);
        menu.put("Pasta", 250);

        // Initialize hotels
        List<Hotel> hotels = new ArrayList<>();
        hotels.add(new Hotel(1, "Hotel A", 0, 0, menu));
        hotels.add(new Hotel(2, "Hotel B", 5, 5, menu));

        // Initialize drivers
        List<Driver> drivers = new ArrayList<>();m
        drivers.add(new Driver(1, "Driver A", 2, 2));
        drivers.add(new Driver(2, "Driver B", 6, 6));

        DriverManager driverManager = new DriverManager(drivers);

        // Dynamic user interaction
        Scanner scanner = new Scanner(System.in);
        Cart cart = new Cart();

        Driver assignedDriver = null; // Variable to track the assigned driver
int userX = 0, userY = 0; // Variables to store user's location dynamically
boolean isOrderPlaced = false; // Flag to check if there's an active order

while (true) {
    System.out.println("\nWelcome to Swiggy! Choose an option:");
    System.out.println("1. View Hotels");
    System.out.println("2. View Menu and Add Items to Cart");
    System.out.println("3. Place Order");
    System.out.println("4. Confirm Order Delivery");
    System.out.println("5. Exit");

    int choice = scanner.nextInt();
    switch (choice) {
        case 1:
            System.out.println("Available Hotels:");
            for (Hotel hotel : hotels) {
                System.out.println(hotel.getId() + ". " + hotel.getName());
            }
            break;

        case 2:
            System.out.println("Choose a hotel by ID:");
            int hotelId = scanner.nextInt();
            Hotel selectedHotel = hotels.stream().filter(h -> h.getId() == hotelId).findFirst().orElse(null);

            if (selectedHotel != null) {
                System.out.println("Menu for " + selectedHotel.getName() + ":");
                selectedHotel.getMenu().forEach((item, price) -> System.out.println(item + " - " + price));

                System.out.println("Enter the item name to add to the cart (or type 'done' to finish):");
                scanner.nextLine(); // Consume newline
                while (true) {
                    String item = scanner.nextLine();
                    if (item.equalsIgnoreCase("done")) break;

                    if (selectedHotel.getMenu().containsKey(item)) {
                        cart.addItem(item, selectedHotel.getMenu().get(item));
                        System.out.println(item + " added to the cart.");
                    } else {
                        System.out.println("Invalid item. Please choose from the menu.");
                    }
                }
            } else {
                System.out.println("Invalid hotel ID. Please try again.");
            }
            break;

        case 3:
            if (cart.getTotalAmount() < 500) {
                System.out.println("Minimum order amount is 500. Please add more items.");
            } else {
                System.out.println("Enter your current location (x and y coordinates):");
                userX = scanner.nextInt();
                userY = scanner.nextInt();

                System.out.println("Order placed successfully!");
                System.out.println("Cart Items: " + cart.getItems());
                System.out.println("Total Amount: " + cart.getTotalAmount());

                // Find the nearest hotel to the user
                Hotel nearestHotel = null;
                double minDistance = Double.MAX_VALUE;

                for (Hotel hotel : hotels) {
                    double distance = Math.sqrt(Math.pow(userX - hotel.getX(), 2) + Math.pow(userY - hotel.getY(), 2));
                    if (distance < minDistance) {
                        minDistance = distance;
                        nearestHotel = hotel;
                    }
                }

                if (nearestHotel != null) {
                    System.out.println("Nearest hotel is " + nearestHotel.getName() + " located at (" + nearestHotel.getX() + ", " + nearestHotel.getY() + ").");

                    // Assign a driver
                    assignedDriver = driverManager.assignDriver(nearestHotel.getX(), nearestHotel.getY());

                    if (assignedDriver != null) {
                        System.out.println("Driver " + assignedDriver.getName() + " assigned to deliver your order.");
                        isOrderPlaced = true; // Mark that an order is placed
                    } else {
                        System.out.println("No drivers available at the moment.");
                    }
                } else {
                    System.out.println("No hotels found nearby.");
                }
            }
            break;

        case 4:
            if (isOrderPlaced && assignedDriver != null) {
                System.out.println("Driver " + assignedDriver.getName() + " is en route. Confirm delivery once you receive your order (type 'yes'):");

                scanner.nextLine(); // Consume leftover newline
                String confirmation = scanner.nextLine();

                if (confirmation.equalsIgnoreCase("yes")) {
                    assignedDriver.updateLocation(userX, userY); // Driver reaches the user's location
                    System.out.println("Order delivered successfully. Driver status updated.");
                    assignedDriver.setAvailable(true); // Mark driver as available
                    assignedDriver = null; // Reset driver assignment
                    isOrderPlaced = false; // Reset order status
                    cart.clearCart(); // Clear the cart after delivery
                } else {
                    System.out.println("Order delivery not confirmed. Driver status remains unavailable.");
                }
            } else {
                System.out.println("No active order to confirm delivery.");
            }
            break;

        case 5:
            System.out.println("Thank you for using Swiggy! Goodbye.");
            scanner.close();
            return;

        default:
            System.out.println("Invalid choice. Please try again.");
    }
}

    }
}