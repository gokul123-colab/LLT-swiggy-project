import java.util.*;
class Hotel {
    private int id;
    private String name;
    private int x;
    private int y;
    private HashMap<String, Integer> menu;

    public Hotel(int id, String name, int x, int y, HashMap<String, Integer> menu) {
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
        this.menu = menu;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public HashMap<String, Integer> getMenu() {
        return menu;
    }
}