
package com.vehiclerental.dao;

import com.vehiclerental.config.DBConnection;
import com.vehiclerental.model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    // CREATE: Register a new customer
    public boolean addCustomer(Customer customer) throws SQLException {

        String sql = """
                INSERT INTO customers
                (name, phone, email, license_number)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, customer.getName());
            statement.setString(2, customer.getPhone());
            statement.setString(3, customer.getEmail());
            statement.setString(4, customer.getLicenseNumber());

            return statement.executeUpdate() > 0;
        }
    }

    // READ: Retrieve all customers
    public List<Customer> getAllCustomers() throws SQLException {

        List<Customer> customers = new ArrayList<>();

        String sql = "SELECT * FROM customers ORDER BY customer_id DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Customer customer = new Customer();

                customer.setCustomerId(
                        resultSet.getInt("customer_id"));
                customer.setName(resultSet.getString("name"));
                customer.setPhone(resultSet.getString("phone"));
                customer.setEmail(resultSet.getString("email"));
                customer.setLicenseNumber(
                        resultSet.getString("license_number"));

                customers.add(customer);
            }
        }

        return customers;
    }

    // UPDATE: Modify customer details
    public boolean updateCustomer(Customer customer)
            throws SQLException {

        String sql = """
                UPDATE customers
                SET name = ?, phone = ?, email = ?,
                    license_number = ?
                WHERE customer_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, customer.getName());
            statement.setString(2, customer.getPhone());
            statement.setString(3, customer.getEmail());
            statement.setString(4, customer.getLicenseNumber());
            statement.setInt(5, customer.getCustomerId());

            return statement.executeUpdate() > 0;
        }
    }

    // DELETE: Remove a customer
    public boolean deleteCustomer(int customerId)
            throws SQLException {

        String sql = "DELETE FROM customers WHERE customer_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, customerId);

            return statement.executeUpdate() > 0;
        }
    }
}
