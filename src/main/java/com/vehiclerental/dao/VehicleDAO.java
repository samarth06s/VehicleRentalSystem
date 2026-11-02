
package com.vehiclerental.dao;

import com.vehiclerental.config.DBConnection;
import com.vehiclerental.model.Vehicle;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleDAO {

    // CREATE: Add a new vehicle
    public boolean addVehicle(Vehicle vehicle) throws SQLException {

        String sql = """
                INSERT INTO vehicles
                (brand, model, vehicle_type, registration_number,
                 rent_per_day, status)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, vehicle.getBrand());
            statement.setString(2, vehicle.getModel());
            statement.setString(3, vehicle.getVehicleType());
            statement.setString(4, vehicle.getRegistrationNumber());
            statement.setBigDecimal(5, vehicle.getRentPerDay());
            statement.setString(6, vehicle.getStatus());

            return statement.executeUpdate() > 0;
        }
    }

    // READ: Get all vehicles
    public List<Vehicle> getAllVehicles() throws SQLException {

        List<Vehicle> vehicles = new ArrayList<>();

        String sql = "SELECT * FROM vehicles ORDER BY vehicle_id DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Vehicle vehicle = new Vehicle();

                vehicle.setVehicleId(resultSet.getInt("vehicle_id"));
                vehicle.setBrand(resultSet.getString("brand"));
                vehicle.setModel(resultSet.getString("model"));
                vehicle.setVehicleType(resultSet.getString("vehicle_type"));
                vehicle.setRegistrationNumber(
                        resultSet.getString("registration_number"));
                vehicle.setRentPerDay(
                        resultSet.getBigDecimal("rent_per_day"));
                vehicle.setStatus(resultSet.getString("status"));

                vehicles.add(vehicle);
            }
        }

        return vehicles;
    }

    // UPDATE: Update vehicle information
    public boolean updateVehicle(Vehicle vehicle) throws SQLException {

        String sql = """
                UPDATE vehicles
                SET brand = ?, model = ?, vehicle_type = ?,
                    registration_number = ?, rent_per_day = ?
                WHERE vehicle_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, vehicle.getBrand());
            statement.setString(2, vehicle.getModel());
            statement.setString(3, vehicle.getVehicleType());
            statement.setString(4, vehicle.getRegistrationNumber());
            statement.setBigDecimal(5, vehicle.getRentPerDay());
            statement.setInt(6, vehicle.getVehicleId());

            return statement.executeUpdate() > 0;
        }
    }

    // DELETE: Delete a vehicle
    public boolean deleteVehicle(int vehicleId) throws SQLException {

        String sql = "DELETE FROM vehicles WHERE vehicle_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, vehicleId);

            return statement.executeUpdate() > 0;
        }
    }

}
