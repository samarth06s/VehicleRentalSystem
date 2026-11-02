
package com.vehiclerental.dao;

import com.vehiclerental.config.DBConnection;
import com.vehiclerental.model.Invoice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class InvoiceDAO {

    public Invoice getInvoiceData(int rentalId) throws SQLException {

        String sql = """
                SELECT
                    r.rental_id,
                    r.rental_date,
                    r.due_date,
                    r.actual_return_date,
                    r.total_amount,
                    r.status,
                    c.name AS customer_name,
                    c.phone,
                    c.email,
                    c.license_number,
                    v.brand,
                    v.model,
                    v.vehicle_type,
                    v.registration_number,
                    v.rent_per_day
                FROM rentals r
                JOIN customers c ON r.customer_id = c.customer_id
                JOIN vehicles v ON r.vehicle_id = v.vehicle_id
                WHERE r.rental_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, rentalId);

            try (ResultSet rs = statement.executeQuery()) {

                if (!rs.next()) {
                    throw new SQLException("Invoice record not found!");
                }

                java.sql.Date rentalSqlDate = rs.getDate("rental_date");
                java.sql.Date dueSqlDate = rs.getDate("due_date");
                java.sql.Date returnSqlDate =
                        rs.getDate("actual_return_date");

                LocalDate rentalDate = rentalSqlDate.toLocalDate();
                LocalDate dueDate = dueSqlDate.toLocalDate();

                LocalDate actualReturnDate = returnSqlDate == null
                        ? null
                        : returnSqlDate.toLocalDate();

                return new Invoice(
                        rs.getInt("rental_id"),
                        rentalDate,
                        dueDate,
                        actualReturnDate,
                        rs.getBigDecimal("total_amount"),
                        rs.getString("status"),
                        rs.getString("customer_name"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("license_number"),
                        rs.getString("brand"),
                        rs.getString("model"),
                        rs.getString("vehicle_type"),
                        rs.getString("registration_number"),
                        rs.getBigDecimal("rent_per_day")
                );
            }
        }
    }
}
