
package com.vehiclerental.dao;

import com.vehiclerental.config.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DashboardDAO {

    public int getRentedVehicles() throws SQLException {
        return getCount(
                "SELECT COUNT(*) FROM vehicles WHERE status = 'RENTED'"
        );
    }

    public int getCompletedRentals() throws SQLException {
        return getCount(
                "SELECT COUNT(*) FROM rentals WHERE status = 'COMPLETED'"
        );
    }

    // Get total number of vehicles
    public int getTotalVehicles() throws SQLException {

        String sql = "SELECT COUNT(*) FROM vehicles";

        return getCount(sql);
    }

    // Get available vehicles
    public int getAvailableVehicles() throws SQLException {

        String sql = """
                SELECT COUNT(*) FROM vehicles
                WHERE status = 'AVAILABLE'
                """;

        return getCount(sql);
    }

    // Get total registered customers
    public int getTotalCustomers() throws SQLException {

        String sql = "SELECT COUNT(*) FROM customers";

        return getCount(sql);
    }

    // Get active rentals
    public int getActiveRentals() throws SQLException {

        String sql = """
                SELECT COUNT(*) FROM rentals
                WHERE status = 'ACTIVE'
                """;

        return getCount(sql);
    }

    // Reusable count method
    private int getCount(String sql) throws SQLException {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }

            return 0;
        }
    }
}
