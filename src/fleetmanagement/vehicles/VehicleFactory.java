package fleetmanagement.vehicles;

import fleetmanagement.exceptions.InvalidOperationException;

public class VehicleFactory {

    public static Vehicle createVehicle(String type, String[] data) throws InvalidOperationException {
        switch (type) {
            case "Car":
                return Car.fromCSV(data);
            case "Truck":
                return Truck.fromCSV(data);
            case "Bus":
                return Bus.fromCSV(data);
            case "Airplane":
                return Airplane.fromCSV(data);
            case "CargoShip":
                return CargoShip.fromCSV(data);
            default:
                throw new InvalidOperationException("Unknown vehicle type: " + type);
        }
    }
}
