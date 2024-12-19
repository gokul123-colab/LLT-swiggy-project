import java.util.*;
import java.io.*;

public class a {
    public static void main(String[] args) {
        // Initialize menu
        HashMap<String, Integer> menuA = new HashMap<>();
        menuA.put("Pizza", 200);
        menuA.put("Burger", 150);
        menuA.put("Pasta", 250);

        // Initialize menu for Hotel B
        HashMap<String, Integer> menuB = new HashMap<>();
        menuB.put("Sandwich", 120);
        menuB.put("Fries", 100);
        menuB.put("Wrap", 180);

        // Initialize hotels
        List<Hotel> hotels = new ArrayList<>();
        hotels.add(new Hotel(1, "Hotel A", 0, 0, menuA));
        hotels.add(new Hotel(2, "Hotel B", 5, 5, menuB));

        // Initialize drivers
        List<Driver> drivers = new ArrayList<>();
        drivers.add(new Driver(1, "Driver A", 2, 2));
        drivers.add(new Driver(2, "Driver B", 6, 6));

        DriverManager driverManager = new DriverManager(drivers);

        // Dynamic user interaction
        Scanner scanner = new Scanner(System.in);
        Cart cart = new Cart();

        Driver assignedDriver = null;
        int userX = 0, userY = 0;
        boolean isOrderPlaced = false;

        while (true) {
            System.out.println("\nWelcome to Swiggy! Choose an option:");
            System.out.println("1. View Hotels");
            System.out.println("2. View Menu and Add Items to Cart");
            System.out.println("3. Remove Items from Cart");
            System.out.println("4. Place Order");
            System.out.println("5. Confirm Order Delivery");
            System.out.println("6. Exit");

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
                    System.out.println("Current Cart Items: " + cart.getItems());
                    System.out.println("Enter the item name to remove from the cart (or type 'done' to finish):");
                    scanner.nextLine(); // Consume newline
                    while (true) {
                        String item = scanner.nextLine();
                        if (item.equalsIgnoreCase("done")) break;

                        if (cart.removeItem(item)) {
                            System.out.println(item + " removed from the cart.");
                        } else {
                            System.out.println("Item not found in the cart. Please try again.");
                        }
                    }
                    break;

                case 4:
                    if (cart.getTotalAmount() < 500) {
                        System.out.println("Minimum order amount is 500. Please add more items.");
                    } else {
                        System.out.println("Enter your current location (x and y coordinates):");
                        userX = scanner.nextInt();
                        userY = scanner.nextInt();

                        System.out.println("Order placed successfully!");
                        System.out.println("Cart Items: " + cart.getItems());
                        System.out.println("Total Amount: " + cart.getTotalAmount());

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

                            assignedDriver = driverManager.assignDriver(nearestHotel.getX(), nearestHotel.getY());

                            if (assignedDriver != null) {
                                System.out.println("Driver " + assignedDriver.getName() + " assigned to deliver your order.");
                                isOrderPlaced = true;
                            } else {
                                System.out.println("No drivers available at the moment.");
                            }
                        } else {
                            System.out.println("No hotels found nearby.");
                        }
                    }
                    break;

                case 5:
                    if (isOrderPlaced && assignedDriver != null) {
                        System.out.println("Driver " + assignedDriver.getName() + " is en route. Confirm delivery once you receive your order (type 'yes'):");

                        scanner.nextLine(); // Consume leftover newline
                        String confirmation = scanner.nextLine();

                        if (confirmation.equalsIgnoreCase("yes")) {
                            assignedDriver.updateLocation(userX, userY);
                            System.out.println("Order delivered successfully. Driver status updated.");
                            assignedDriver.setAvailable(true);
                            assignedDriver = null;
                            isOrderPlaced = false;
                            cart.clearCart();
                        } else {
                            System.out.println("Order delivery not confirmed. Driver status remains unavailable.");
                        }
                    } else {
                        System.out.println("No active order to confirm delivery.");
                    }
                    break;

                case 6:
                    System.out.println("Thank you for using Swiggy! Goodbye.");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}

class Cart {
    private HashMap<String, Integer> items;
    private int totalAmount;

    public Cart() {
        this.items = new HashMap<>();
        this.totalAmount = 0;
    }

    public void addItem(String itemName, int price) {
        items.put(itemName, items.getOrDefault(itemName, 0) + 1);
        totalAmount += price;
    }

    public boolean removeItem(String itemName) {
        if (items.containsKey(itemName)) {
            totalAmount -= items.get(itemName); // Decrease total amount
            items.remove(itemName); // Remove item from cart
            return true;
        }
        return false;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public HashMap<String, Integer> getItems() {
        return items;
    }

    public void clearCart() {
        items.clear();
        totalAmount = 0;
    }
}
