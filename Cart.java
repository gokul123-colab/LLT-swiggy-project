import java.util.*;
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