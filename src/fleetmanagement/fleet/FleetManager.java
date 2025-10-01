package fleetmanagement.fleet;

import fleetmanagement.exceptions.InvalidOperationException;
import fleetmanagement.interfaces.FuelConsumable;
import fleetmanagement.interfaces.Maintainable;
import fleetmanagement.vehicles.Vehicle;
import fleetmanagement.vehicles.VehicleFactory;

import java.io.*;
import java.util.*;

public class FleetManager {
    private List<Vehicle> fleet;

    public FleetManager() {
        this.fleet = new ArrayList<>();
    }

    public void addVehicle(Vehicle vehicle) throws InvalidOperationException {
        // Check for duplicate ID
        for (Vehicle v : fleet) {
            if (v.getId().equals(vehicle.getId())) {
                throw new InvalidOperationException("Vehicle with ID " + vehicle.getId() + " already exists");
            }
        }
        fleet.add(vehicle);
    }

    public void removeVehicle(String id) throws InvalidOperationException {
        Iterator<Vehicle> iterator = fleet.iterator();
        while (iterator.hasNext()) {
            Vehicle vehicle = iterator.next();
            if (vehicle.getId().equals(id)) {
                iterator.remove();
                return;
            }
        }
        throw new InvalidOperationException("Vehicle with ID " + id + " not found");
    }

    public void startAllJourneys(double distance) {
        for (Vehicle vehicle : fleet) {
            try {
                vehicle.move(distance);
            } catch (InvalidOperationException e) {
                System.err.println("Error moving vehicle " + vehicle.getId() + ": " + e.getMessage());
            }
        }
    }

    public double getTotalFuelConsumption(double distance) {
        double totalFuel = 0;
        for (Vehicle vehicle : fleet) {
            if (vehicle instanceof FuelConsumable) {
                try {
                    FuelConsumable fuelVehicle = (FuelConsumable) vehicle;
                    double fuelNeeded = distance / vehicle.calculateFuelEfficiency();

                    if (fuelNeeded > fuelVehicle.getFuelLevel()) {
                        System.err.println("Insufficient fuel for " + vehicle.getId() +
                                ". Needed: " + fuelNeeded + ", Available: " + fuelVehicle.getFuelLevel());
                        continue;
                    }

                    totalFuel += fuelNeeded;
                } catch (Exception e) {
                    System.err.println("Error calculating fuel for " + vehicle.getId() + ": " + e.getMessage());
                }
            }
        }
        return totalFuel;
    }

    public void maintainAll() {
        for (Vehicle vehicle : fleet) {
            if (vehicle instanceof Maintainable) {
                Maintainable maintainable = (Maintainable) vehicle;
                if (maintainable.needsMaintenance()) {
                    maintainable.performMaintenance();
                }
            }
        }
    }

    public List<Vehicle> searchByType(Class<?> type) {
        List<Vehicle> result = new ArrayList<>();
        for (Vehicle vehicle : fleet) {
            if (type.isInstance(vehicle)) {
                result.add(vehicle);
            }
        }
        return result;
    }

    public void sortFleetByEfficiency() {
        Collections.sort(fleet, (v1, v2) -> {
            double eff1 = v1.calculateFuelEfficiency();
            double eff2 = v2.calculateFuelEfficiency();
            return Double.compare(eff2, eff1);
        });
    }

    public String generateReport() {
        StringBuilder report = new StringBuilder();
        report.append("   FLEET MANAGEMENT REPORT   \n");
        report.append("Total Vehicles: ").append(fleet.size()).append("\n");

        Map<String, Integer> typeCounts = new HashMap<>();
        for (Vehicle vehicle : fleet) {
            String typeName = vehicle.getClass().getSimpleName();
            typeCounts.put(typeName, typeCounts.getOrDefault(typeName, 0) + 1);
        }

        report.append("\nVehicle Count by Type:\n");
        for (Map.Entry<String, Integer> entry : typeCounts.entrySet()) {
            report.append("  ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }

        double totalEfficiency = 0;
        int fuelVehicleCount = 0;
        for (Vehicle vehicle : fleet) {
            if (vehicle instanceof FuelConsumable) {
                totalEfficiency += vehicle.calculateFuelEfficiency();
                fuelVehicleCount++;
            }
        }
        double avgEfficiency = fuelVehicleCount > 0 ? totalEfficiency / fuelVehicleCount : 0;
        report.append("Average Fuel Efficiency: ").append(String.format("%.2f", avgEfficiency)).append(" km/l\n");

        double totalMileage = 0;
        for (Vehicle vehicle : fleet) {
            totalMileage += vehicle.getCurrentMileage();
        }
        report.append("Total Mileage: ").append(String.format("%.2f", totalMileage)).append(" km\n");

        int needMaintenance = 0;
        for (Vehicle vehicle : fleet) {
            if (vehicle instanceof Maintainable && ((Maintainable) vehicle).needsMaintenance()) {
                needMaintenance++;
            }
        }
        report.append("Vehicles Needing Maintenance: ").append(needMaintenance).append("/").append(fleet.size()).append("\n");

        return report.toString();
    }

    public List<Vehicle> getVehiclesNeedingMaintenance() {
        List<Vehicle> result = new ArrayList<>();
        for (Vehicle vehicle : fleet) {
            if (vehicle instanceof Maintainable && ((Maintainable) vehicle).needsMaintenance()) {
                result.add(vehicle);
            }
        }
        return result;
    }

    public void saveToFile(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Vehicle vehicle : fleet) {
                String csvLine = vehicle.toCSVString();
                writer.println(csvLine);
            }
            System.out.println("Fleet saved to " + filename + " (" + fleet.size() + " vehicles)");
        } catch (IOException e) {
            System.err.println("Error saving fleet to file: " + e.getMessage());
        }
    }

    public void loadFromFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            List<Vehicle> loadedFleet = new ArrayList<>();
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                try {
                    String[] data = line.split(",");
                    if (data.length < 2) {
                        System.err.println("Skipping invalid line " + lineNumber + ": " + line);
                        continue;
                    }

                    String vehicleType = data[0];
                    Vehicle vehicle = VehicleFactory.createVehicle(vehicleType, data);
                    if (vehicle != null) {
                        loadedFleet.add(vehicle);
                    }
                } catch (Exception e) {
                    System.err.println("Error parsing line " + lineNumber + ": " + e.getMessage());
                }
            }

            this.fleet = loadedFleet;
            System.out.println("Loaded " + loadedFleet.size() + " vehicles from " + filename);

        } catch (IOException e) {
            System.err.println("Error loading fleet from file: " + e.getMessage());
        }
    }

    public List<Vehicle> getFleet() {
        return new ArrayList<>(fleet);
    }

    public int getFleetSize() {
        return fleet.size();
    }

    public void displayAllVehicles() {
        if (fleet.isEmpty()) {
            System.out.println("No vehicles in fleet");
            return;
        }

        System.out.println("   ALL VEHICLES IN FLEET   ");
        for (int i = 0; i < fleet.size(); i++) {
            Vehicle vehicle = fleet.get(i);
            System.out.println((i + 1) + ". " + vehicle.getId() + " - " +
                    vehicle.getClass().getSimpleName() + " - " +
                    vehicle.getModel());
        }
    }

    public void refuelAll(double amount) throws InvalidOperationException {
        for (Vehicle v : fleet) {
            if (v instanceof FuelConsumable) {
                ((FuelConsumable) v).refuel(amount);
            }
        }
    }
}
