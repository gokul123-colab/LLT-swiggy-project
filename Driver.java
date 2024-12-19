class Driver {
    private int id;
    private String name;
    private int x;
    private int y;
    private boolean isAvailable;

    public Driver(int id, String name, int x, int y) {
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
        this.isAvailable = true;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void updateLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String getName() {
        return name;
    }
}

