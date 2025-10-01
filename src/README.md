# Transportation Fleet Management System

This project is my AP-M25 assignment written in Java.  
It is a **Command-Line Fleet Management System** that demonstrates **Object-Oriented Programming** concepts such as:

- **Inheritance** (Vehicle → LandVehicle, AirVehicle, WaterVehicle → Car, Truck, Bus, Airplane, CargoShip)
- **Abstract Classes** (`Vehicle`, `LandVehicle`, `AirVehicle`, `WaterVehicle`)
- **Interfaces** (`FuelConsumable`, `PassengerCarrier`, `CargoCarrier`, `Maintainable`)
- **Polymorphism** (common `move()` and `calculateFuelEfficiency()` methods implemented differently in each subclass)
- **Exception Handling** (custom exceptions for overload, invalid operations, insufficient fuel)
- **Factory Method Pattern** (`VehicleFactory` to create vehicles from CSV or CLI input)
- **Persistence** (save and load fleet data from CSV)

---

## How to Compile and Run

### IntelliJ IDEA
1. Open the project in IntelliJ IDEA.
2. Mark `src/` as your **Source Root**.
3. Right-click `Main.java` (`fleetmanagement.cli.Main`) and select **Run**.

### Command Line
From inside the `src/` folder:  
javac fleetmanagement/**/*.java  
java fleetmanagement.cli.Main

---

## Features
- Add or remove vehicles (Car, Truck, Bus, Airplane, CargoShip).
- Board/disembark passengers and load/unload cargo.
- Start journeys and simulate fuel consumption.
- Refuel all vehicles at once.
- Perform and track maintenance.
- Save and load fleet data using CSV persistence.
- Generate a full fleet report.
- Search vehicles by type or by maintenance needs.
- Demonstration test cases included in `Main.java`.

---

## Example Run (CLI)
=== Transportation Fleet Management System ===
1. Add Vehicle
2. Remove Vehicle
3. Start Journey
4. Refuel All
5. Perform Maintenance
6. Generate Report
7. Save Fleet
8. Load Fleet
9. Search by Type
10. List Vehicles Needing Maintenance
11. Exit

Example: Selecting **6. Generate Report** will output a summary of all vehicles, their fuel, cargo, passengers, mileage, and maintenance status.

---

## Files Included
- `src/` → all Java source code (organized into packages: cli, fleet, vehicles, interfaces, exceptions).
- `README.txt` → detailed explanation for assignment submission.
- `README.md` → GitHub-friendly overview.
- `.gitignore` → excludes build and IDE files.
- `UML.png` → UML class diagram (optional).
- `fleet.csv` → sample fleet persistence file (optional).  
