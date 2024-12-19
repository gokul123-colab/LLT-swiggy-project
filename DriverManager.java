import java.util.*;
class DriverManager {
    private List<Driver> drivers;

    public DriverManager(List<Driver> drivers) {
        this.drivers = drivers;
    }

    public Driver assignDriver(int hotelX, int hotelY) {
        Driver assignedDriver = null;
        double minDistance = Double.MAX_VALUE;

        for (Driver driver : drivers) {
            if (driver.isAvailable()) {
                double distance = Math.sqrt(Math.pow(hotelX - driver.getX(), 2) + Math.pow(hotelY - driver.getY(), 2));
                if (distance < minDistance) {
                    minDistance = distance;
                    assignedDriver = driver;
                }
            }
        }

        if (assignedDriver != null) {
            assignedDriver.setAvailable(false);
        }

        return assignedDriver;
    }
}