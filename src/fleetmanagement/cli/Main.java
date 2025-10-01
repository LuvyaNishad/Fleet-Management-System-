package fleetmanagement.cli;

import fleetmanagement.fleet.FleetManager;
import fleetmanagement.exceptions.InvalidOperationException;
import fleetmanagement.exceptions.OverloadException;
import fleetmanagement.exceptions.InsufficientFuelException;
import fleetmanagement.vehicles.Vehicle;
import fleetmanagement.vehicles.Car;
import fleetmanagement.vehicles.Truck;
import fleetmanagement.vehicles.Bus;
import fleetmanagement.vehicles.Airplane;
import fleetmanagement.vehicles.CargoShip;
import fleetmanagement.interfaces.FuelConsumable;

import java.util.Scanner;
import java.util.List;

public class Main {
    private static FleetManager fleetManager = new FleetManager();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("   Transportation Fleet Management System   ");

        createSampleVehicles();

        boolean running = true;
        while (running) {
            displayMenu();
            int choice = getIntInput("Enter your choice: ");

            try {
                switch (choice) {
                    case 1: addVehicle(); break;
                    case 2: removeVehicle(); break;
                    case 3: startJourney(); break;
                    case 4: refuelAll(); break;
                    case 5: maintainAll(); break;
                    case 6: generateReport(); break;
                    case 7: saveFleet(); break;
                    case 8: loadFleet(); break;
                    case 9: searchByType(); break;
                    case 10: listMaintenanceNeeds(); break;
                    case 11:
                        running = false;
                        System.out.println("Thank you for using the Fleet Management System!");
                        break;
                    default: System.out.println("Invalid choice! Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
            System.out.println();
        }

        scanner.close();
    }

    private static void createSampleVehicles() {
        try {
            System.out.println("  Creating sample vehicles");

            Car car = new Car("C001", "Toyota Camry", 180.0, 4);
            Truck truck = new Truck("T001", "Ford F-150", 120.0, 6);
            Bus bus = new Bus("B001", "Volvo Bus", 100.0, 6);
            Airplane airplane = new Airplane("A001", "Boeing 737", 850.0, 35000.0);
            CargoShip cargoShip = new CargoShip("S001", "Maersk Container", 50.0, false);

            car.refuel(50.0);
            truck.refuel(100.0);
            bus.refuel(150.0);
            airplane.refuel(5000.0);
            cargoShip.refuel(2000.0);

            fleetManager.addVehicle(car);
            fleetManager.addVehicle(truck);
            fleetManager.addVehicle(bus);
            fleetManager.addVehicle(airplane);
            fleetManager.addVehicle(cargoShip);

            System.out.println("  Vehicles created and added to fleet");

            System.out.println("\n   Running Test Cases   ");

            car.boardPassengers(3);
            car.move(60);
            car.disembarkPassengers(1);
            System.out.println("Car test complete. Fuel left: " + car.getFuelLevel());

            truck.loadCargo(2000);
            truck.move(100);
            truck.unloadCargo(500);
            System.out.println("Truck test complete. Cargo left: " + truck.getCurrentCargo());

            bus.boardPassengers(30);
            bus.move(50);
            bus.disembarkPassengers(10);
            System.out.println("Bus test complete. Passengers left: " + bus.getCurrentPassengers());

            airplane.boardPassengers(100);
            airplane.loadCargo(2000);
            airplane.move(500);
            airplane.scheduleMaintenance();
            if (airplane.needsMaintenance()) {
                airplane.performMaintenance();
            }
            System.out.println("Airplane test complete. Maintenance performed.");

            cargoShip.loadCargo(10000);
            cargoShip.move(200);
            cargoShip.unloadCargo(2000);
            System.out.println("CargoShip test complete. Cargo left: " + cargoShip.getCurrentCargo());

            System.out.println("\n   Fleet Report After Test Cases   ");
            System.out.println(fleetManager.generateReport());

            System.out.println("   Test Cases Completed   \n");

        } catch (Exception e) {
            System.out.println("Error : " + e.getMessage());
        }
    }

    private static void displayMenu() {
        System.out.println("\n   MAIN MENU   ");
        System.out.println("1. Add Vehicle");
        System.out.println("2. Remove Vehicle");
        System.out.println("3. Start Journey");
        System.out.println("4. Refuel All");
        System.out.println("5. Perform Maintenance");
        System.out.println("6. Generate Report");
        System.out.println("7. Save Fleet");
        System.out.println("8. Load Fleet");
        System.out.println("9. Search by Type");
        System.out.println("10. List Vehicles Needing Maintenance");
        System.out.println("11. Exit");
    }

    private static void addVehicle() {
        try {
            System.out.println("\n   ADD VEHICLE   ");
            System.out.println("Select vehicle type:");
            System.out.println("1. Car");
            System.out.println("2. Truck");
            System.out.println("3. Bus");
            System.out.println("4. Airplane");
            System.out.println("5. Cargo Ship");

            int typeChoice = getIntInput("Enter vehicle type (1-5): ");
            if (typeChoice < 1 || typeChoice > 5) {
                System.out.println("Invalid vehicle type!");
                return;
            }

            String id = getStringInput("Enter vehicle ID: ");
            String model = getStringInput("Enter vehicle model: ");
            double maxSpeed = getDoubleInput("Enter max speed (km/h): ");

            Vehicle vehicle = null;
            switch (typeChoice) {
                case 1:
                    int wheels = getIntInput("Enter number of wheels: ");
                    vehicle = new Car(id, model, maxSpeed, wheels);
                    break;
                case 2:
                    wheels = getIntInput("Enter number of wheels: ");
                    vehicle = new Truck(id, model, maxSpeed, wheels);
                    break;
                case 3:
                    wheels = getIntInput("Enter number of wheels: ");
                    vehicle = new Bus(id, model, maxSpeed, wheels);
                    break;
                case 4:
                    double maxAltitude = getDoubleInput("Enter max altitude (feet): ");
                    vehicle = new Airplane(id, model, maxSpeed, maxAltitude);
                    break;
                case 5:
                    boolean hasSail = getBooleanInput("Does it have sail? (true/false): ");
                    vehicle = new CargoShip(id, model, maxSpeed, hasSail);
                    break;
            }

            if (vehicle != null) {
                fleetManager.addVehicle(vehicle);
                System.out.println("Vehicle added successfully!");

                if (vehicle instanceof FuelConsumable) {
                    double fuelAmount = getDoubleInput("Enter initial fuel amount: ");
                    ((FuelConsumable) vehicle).refuel(fuelAmount);
                }
            }
        } catch (Exception e) {
            System.out.println("Error adding vehicle: " + e.getMessage());
        }
    }

    private static void removeVehicle() {
        try {
            System.out.println("\n   REMOVE VEHICLE   ");
            String id = getStringInput("Enter vehicle ID to remove: ");
            fleetManager.removeVehicle(id);
            System.out.println("Vehicle removed successfully!");
        } catch (Exception e) {
            System.out.println("Error removing vehicle: " + e.getMessage());
        }
    }

    private static void startJourney() {
        try {
            System.out.println("\n   START JOURNEY   ");
            double distance = getDoubleInput("Enter journey distance (km): ");
            if (distance <= 0) {
                System.out.println("Distance must be positive!");
                return;
            }
            fleetManager.startAllJourneys(distance);
            System.out.println("Journey completed for all vehicles!");
        } catch (Exception e) {
            System.out.println("Error starting journey: " + e.getMessage());
        }
    }

    private static void refuelAll() {
        try {
            System.out.println("\n   REFUEL ALL VEHICLES   ");
            double amount = getDoubleInput("Enter fuel amount for all vehicles: ");
            if (amount <= 0) {
                System.out.println("Fuel amount must be positive!");
                return;
            }
            fleetManager.refuelAll(amount);
            System.out.println("All vehicles refueled!");
        } catch (Exception e) {
            System.out.println("Error refueling vehicles: " + e.getMessage());
        }
    }

    private static void maintainAll() {
        try {
            System.out.println("\n   PERFORM MAINTENANCE   ");
            fleetManager.maintainAll();
            System.out.println("Maintenance completed for all vehicles needing it!");
        } catch (Exception e) {
            System.out.println("Error performing maintenance: " + e.getMessage());
        }
    }

    private static void generateReport() {
        String report = fleetManager.generateReport();
        System.out.println(report);
    }

    private static void saveFleet() {
        try {
            System.out.println("\n   SAVE FLEET   ");
            String filename = getStringInput("Enter filename : ");
            fleetManager.saveToFile(filename);
            System.out.println("Fleet saved successfully to " + filename);
        } catch (Exception e) {
            System.out.println("Error saving fleet: " + e.getMessage());
        }
    }

    private static void loadFleet() {
        try {
            System.out.println("\n   LOAD FLEET   ");
            String filename = getStringInput("Enter filename to load: ");
            fleetManager.loadFromFile(filename);
            System.out.println("Fleet loaded successfully from " + filename);
        } catch (Exception e) {
            System.out.println("Error loading fleet: " + e.getMessage());
        }
    }

    private static void searchByType() {
        try {
            System.out.println("\n   SEARCH BY TYPE   ");
            System.out.println("Search for:");
            System.out.println("1. Cars");
            System.out.println("2. Trucks");
            System.out.println("3. Buses");
            System.out.println("4. Airplanes");
            System.out.println("5. Cargo Ships");
            System.out.println("6. Fuel Consumable Vehicles");
            System.out.println("7. Vehicles Needing Maintenance");

            int choice = getIntInput("Enter search type (1-7): ");

            List<Vehicle> results = null;
            switch (choice) {
                case 1: results = fleetManager.searchByType(Car.class); break;
                case 2: results = fleetManager.searchByType(Truck.class); break;
                case 3: results = fleetManager.searchByType(Bus.class); break;
                case 4: results = fleetManager.searchByType(Airplane.class); break;
                case 5: results = fleetManager.searchByType(CargoShip.class); break;
                case 6: results = fleetManager.searchByType(FuelConsumable.class); break;
                case 7: results = fleetManager.getVehiclesNeedingMaintenance(); break;
                default:
                    System.out.println("Invalid choice!");
                    return;
            }

            if (results.isEmpty()) {
                System.out.println("No vehicles found.");
            } else {
                System.out.println("Found " + results.size() + " vehicles:");
                for (int i = 0; i < results.size(); i++) {
                    System.out.println((i + 1) + ". " + results.get(i).getDetails());
                }
            }
        } catch (Exception e) {
            System.out.println("Error searching: " + e.getMessage());
        }
    }

    private static void listMaintenanceNeeds() {
        try {
            System.out.println("\n   VEHICLES NEEDING MAINTENANCE   ");
            List<Vehicle> vehicles = fleetManager.getVehiclesNeedingMaintenance();

            if (vehicles.isEmpty()) {
                System.out.println("No vehicles need maintenance at this time.");
            } else {
                System.out.println("Vehicles needing maintenance (" + vehicles.size() + "):");
                for (int i = 0; i < vehicles.size(); i++) {
                    Vehicle v = vehicles.get(i);
                    System.out.println((i + 1) + ". " + v.getId() + " - " + v.getModel() +
                            " (Mileage: " + v.getCurrentMileage() + " km)");
                }
            }
        } catch (Exception e) {
            System.out.println("Error listing maintenance needs: " + e.getMessage());
        }
    }

    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer!");
            }
        }
    }

    private static double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number!");
            }
        }
    }

    private static boolean getBooleanInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("true") || input.equals("yes") || input.equals("y")) {
                return true;
            } else if (input.equals("false") || input.equals("no") || input.equals("n")) {
                return false;
            } else {
                System.out.println("Please enter 'true' or 'false'!");
            }
        }
    }
}
