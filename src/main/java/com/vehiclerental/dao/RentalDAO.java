
package com.vehiclerental.dao;

import com.vehiclerental.config.DBConnection;
import com.vehiclerental.model.Rental;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class RentalDAO {

    // CREATE: Book a vehicle
    public boolean createRental(Rental rental) throws SQLException {

        String vehicleSql = """
                SELECT rent_per_day, status
                FROM vehicles
                WHERE vehicle_id = ?
                FOR UPDATE
                """;

        String rentalSql = """
                INSERT INTO rentals
                (customer_id, vehicle_id, rental_date,
                 due_date, total_amount, status)
                VALUES (?, ?, ?, ?, ?, 'ACTIVE')
                """;

        String updateVehicleSql = """
                UPDATE vehicles
                SET status = 'RENTED'
                WHERE vehicle_id = ?
                """;

        try (Connection connection = DBConnection.getConnection()) {

            connection.setAutoCommit(false);

            try {
                BigDecimal rentPerDay;

                // Lock and verify the vehicle
                try (PreparedStatement statement =
                             connection.prepareStatement(vehicleSql)) {

                    statement.setInt(1, rental.getVehicleId());

                    try (ResultSet rs = statement.executeQuery()) {

                        if (!rs.next()) {
                            throw new SQLException("Vehicle not found!");
                        }

                        if (!"AVAILABLE".equals(rs.getString("status"))) {
                            throw new SQLException("Vehicle is not available!");
                        }

                        rentPerDay = rs.getBigDecimal("rent_per_day");
                    }
                }

                // Calculate rental amount
                long days = ChronoUnit.DAYS.between(
                        rental.getRentalDate(),
                        rental.getDueDate()
                );

                if (days <= 0) {
                    throw new SQLException(
                            "Due date must be after rental date!");
                }

                BigDecimal amount = rentPerDay.multiply(
                        BigDecimal.valueOf(days)
                );

                // Insert rental record
                try (PreparedStatement statement =
                             connection.prepareStatement(rentalSql)) {

                    statement.setInt(1, rental.getCustomerId());
                    statement.setInt(2, rental.getVehicleId());
                    statement.setDate(3,
                            Date.valueOf(rental.getRentalDate()));
                    statement.setDate(4,
                            Date.valueOf(rental.getDueDate()));
                    statement.setBigDecimal(5, amount);

                    statement.executeUpdate();
                }

                // Mark vehicle as rented
                try (PreparedStatement statement =
                             connection.prepareStatement(updateVehicleSql)) {

                    statement.setInt(1, rental.getVehicleId());
                    statement.executeUpdate();
                }

                connection.commit();
                return true;

            } catch (SQLException | RuntimeException e) {
                connection.rollback();
                throw e;
            }
        }
    }

    // READ: Retrieve all rental records
    public List<Rental> getAllRentals() throws SQLException {

        List<Rental> rentals = new ArrayList<>();

        String sql = """
                SELECT * FROM rentals
                ORDER BY rental_id DESC
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {

                Rental rental = new Rental();

                rental.setRentalId(rs.getInt("rental_id"));
                rental.setCustomerId(rs.getInt("customer_id"));
                rental.setVehicleId(rs.getInt("vehicle_id"));
                rental.setRentalDate(
                        rs.getDate("rental_date").toLocalDate());
                rental.setDueDate(
                        rs.getDate("due_date").toLocalDate());

                Date returnDate = rs.getDate("actual_return_date");

                if (returnDate != null) {
                    rental.setActualReturnDate(
                            returnDate.toLocalDate());
                }

                rental.setTotalAmount(
                        rs.getBigDecimal("total_amount"));
                rental.setStatus(rs.getString("status"));

                rentals.add(rental);
            }
        }

        return rentals;
    }

    // UPDATE: Return a rented vehicle
    public boolean returnVehicle(int rentalId,
                                 LocalDate actualReturnDate)
            throws SQLException {

        String findSql = """
        SELECT r.vehicle_id,
               r.rental_date,
               r.due_date,
               r.status,
               v.rent_per_day
        FROM rentals r
        JOIN vehicles v
        ON r.vehicle_id = v.vehicle_id
        WHERE r.rental_id = ?
        FOR UPDATE
        """;

        String updateRentalSql = """
                UPDATE rentals
                SET actual_return_date = ?,
                    total_amount = ?,
                    status = 'COMPLETED'
                WHERE rental_id = ?
                """;

        String updateVehicleSql = """
                UPDATE vehicles
                SET status = 'AVAILABLE'
                WHERE vehicle_id = ?
                """;

        try (Connection connection = DBConnection.getConnection()) {

            connection.setAutoCommit(false);

            try {
                int vehicleId;
                LocalDate rentalDate;
                LocalDate dueDate;
                BigDecimal rentPerDay;

                // Lock rental and vehicle records
                try (PreparedStatement statement =
                             connection.prepareStatement(findSql)) {

                    statement.setInt(1, rentalId);

                    try (ResultSet rs = statement.executeQuery()) {

                        if (!rs.next()) {
                            throw new SQLException("Rental not found!");
                        }

                        if (!"ACTIVE".equals(rs.getString("status"))) {
                            throw new SQLException(
                                    "Rental is already completed!");
                        }

                        vehicleId = rs.getInt("vehicle_id");

                        rentalDate = rs.getDate("rental_date").toLocalDate();

                        dueDate = rs.getDate("due_date").toLocalDate();

                        rentPerDay = rs.getBigDecimal("rent_per_day");
                    }
                }

                if (actualReturnDate.isBefore(rentalDate)) {
                    throw new SQLException(
                            "Return date cannot be before rental date!");
                }

                // Calculate rental days
                long rentalDays = ChronoUnit.DAYS.between(
                        rentalDate, actualReturnDate
                );

                rentalDays = Math.max(1, rentalDays);

// Calculate base rental amount
                BigDecimal baseAmount = rentPerDay.multiply(
                        BigDecimal.valueOf(rentalDays)
                );

// Calculate overdue days
                long overdueDays = Math.max(
                        0,
                        ChronoUnit.DAYS.between(dueDate, actualReturnDate)
                );

// Fixed late fee: Rs. 200 per overdue day
                BigDecimal lateFee = BigDecimal.valueOf(200).multiply(
                        BigDecimal.valueOf(overdueDays)
                );

// Calculate final bill
                BigDecimal finalAmount = baseAmount.add(lateFee);

                // Update rental record
                try (PreparedStatement statement =
                             connection.prepareStatement(updateRentalSql)) {

                    statement.setDate(1,
                            Date.valueOf(actualReturnDate));
                    statement.setBigDecimal(2, finalAmount);
                    statement.setInt(3, rentalId);

                    statement.executeUpdate();
                }

                // Make vehicle available again
                try (PreparedStatement statement =
                             connection.prepareStatement(updateVehicleSql)) {

                    statement.setInt(1, vehicleId);
                    statement.executeUpdate();
                }

                connection.commit();
                return true;

            } catch (SQLException | RuntimeException e) {
                connection.rollback();
                throw e;
            }
        }
    }
}
