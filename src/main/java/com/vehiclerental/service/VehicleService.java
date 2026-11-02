
package com.vehiclerental.service;

import com.vehiclerental.dao.VehicleDAO;
import com.vehiclerental.model.Vehicle;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class VehicleService {

    private final VehicleDAO vehicleDAO;

    // Constructor injection
    public VehicleService() {
        this.vehicleDAO = new VehicleDAO();
    }

    // Add vehicle with validation
    public boolean addVehicle(Vehicle vehicle) throws SQLException {

        validateVehicle(vehicle);

        vehicle.setBrand(vehicle.getBrand().trim());
        vehicle.setModel(vehicle.getModel().trim());
        vehicle.setVehicleType(vehicle.getVehicleType().trim());
        vehicle.setRegistrationNumber(
                vehicle.getRegistrationNumber().trim().toUpperCase());

        return vehicleDAO.addVehicle(vehicle);
    }

    // Retrieve all vehicles
    public List<Vehicle> getAllVehicles() throws SQLException {

        return vehicleDAO.getAllVehicles();
    }

    // Update vehicle
    public boolean updateVehicle(Vehicle vehicle) throws SQLException {

        if (vehicle.getVehicleId() <= 0) {
            throw new IllegalArgumentException("Invalid vehicle ID!");
        }

        validateVehicle(vehicle);

        return vehicleDAO.updateVehicle(vehicle);
    }

    // Delete vehicle
    public boolean deleteVehicle(int vehicleId) throws SQLException {

        if (vehicleId <= 0) {
            throw new IllegalArgumentException("Invalid vehicle ID!");
        }

        return vehicleDAO.deleteVehicle(vehicleId);
    }

    // Business validation
    private void validateVehicle(Vehicle vehicle) {

        if (vehicle == null) {
            throw new IllegalArgumentException(
                    "Vehicle details cannot be null!");
        }

        if (isBlank(vehicle.getBrand())) {
            throw new IllegalArgumentException(
                    "Brand is required!");
        }

        if (isBlank(vehicle.getModel())) {
            throw new IllegalArgumentException(
                    "Model is required!");
        }

        if (isBlank(vehicle.getVehicleType())) {
            throw new IllegalArgumentException(
                    "Vehicle type is required!");
        }

        if (isBlank(vehicle.getRegistrationNumber())) {
            throw new IllegalArgumentException(
                    "Registration number is required!");
        }

        BigDecimal rent = vehicle.getRentPerDay();

        if (rent == null || rent.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Rent must be greater than zero!");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
