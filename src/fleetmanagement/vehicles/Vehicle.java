package fleetmanagement.vehicles;

import fleetmanagement.exceptions.InvalidOperationException;

public abstract class Vehicle implements Comparable<Vehicle> {
    private String id;
    private String model;
    private double maxSpeed;
    private double currentMileage;

    public Vehicle(String id, String model, double maxSpeed) throws InvalidOperationException {
        if (id == null || id.trim().isEmpty()) {
            throw new InvalidOperationException("Vehicle ID cannot be empty");
        }
        this.id = id;
        this.model = model;
        this.maxSpeed = maxSpeed;
        this.currentMileage = 0.0;
    }

    public String getId() { return id; }
    public String getModel() { return model; }
    public double getMaxSpeed() { return maxSpeed; }
    public double getCurrentMileage() { return currentMileage; }

    protected void addMileage(double distance) {
        if (distance > 0) {
            currentMileage += distance;
        }
    }

    public abstract void move(double distance) throws InvalidOperationException;
    public abstract double calculateFuelEfficiency();
    public abstract double estimateJourneyTime(double distance);

    public void displayInfo() {
        System.out.println("   VEHICLE INFORMATION   ");
        System.out.println("ID: " + id);
        System.out.println("Model: " + model);
        System.out.println("Max Speed: " + maxSpeed + " km/h");
        System.out.println("Current Mileage: " + currentMileage + " km");
    }

    @Override
    public int compareTo(Vehicle other) {
        return Double.compare(this.calculateFuelEfficiency(), other.calculateFuelEfficiency());
    }

    public abstract String toCSVString();

    public String getDetails() {
        return String.format("%s: %s (ID: %s) - %.1f km/h, %.1f km mileage",
                getClass().getSimpleName(), model, id, maxSpeed, currentMileage);
    }
}
