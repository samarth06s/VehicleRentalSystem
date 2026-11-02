
package com.vehiclerental.service;

import com.vehiclerental.dao.CustomerDAO;
import com.vehiclerental.model.Customer;

import java.sql.SQLException;
import java.util.List;

public class CustomerService {

    private final CustomerDAO customerDAO;

    public CustomerService() {
        this.customerDAO = new CustomerDAO();
    }

    // Register customer
    public boolean addCustomer(Customer customer)
            throws SQLException {

        validateCustomer(customer);

        customer.setName(customer.getName().trim());
        customer.setPhone(customer.getPhone().trim());
        customer.setEmail(normalizeEmail(customer.getEmail()));
        customer.setLicenseNumber(
                customer.getLicenseNumber().trim().toUpperCase());

        return customerDAO.addCustomer(customer);
    }

    // Retrieve all customers
    public List<Customer> getAllCustomers() throws SQLException {

        return customerDAO.getAllCustomers();
    }

    // Update customer
    public boolean updateCustomer(Customer customer)
            throws SQLException {

        if (customer == null || customer.getCustomerId() <= 0) {
            throw new IllegalArgumentException(
                    "Invalid customer information!");
        }

        validateCustomer(customer);

        return customerDAO.updateCustomer(customer);
    }

    // Delete customer
    public boolean deleteCustomer(int customerId)
            throws SQLException {

        if (customerId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid customer ID!");
        }

        return customerDAO.deleteCustomer(customerId);
    }

    // Customer validation
    private void validateCustomer(Customer customer) {

        if (customer == null) {
            throw new IllegalArgumentException(
                    "Customer details cannot be null!");
        }

        if (isBlank(customer.getName())) {
            throw new IllegalArgumentException(
                    "Customer name is required!");
        }

        if (isBlank(customer.getPhone())) {
            throw new IllegalArgumentException(
                    "Phone number is required!");
        }

        if (!customer.getPhone().trim().matches("\\+?[0-9]{10,15}")) {
            throw new IllegalArgumentException(
                    "Enter a valid phone number!");
        }

        if (isBlank(customer.getLicenseNumber())) {
            throw new IllegalArgumentException(
                    "Driving license number is required!");
        }

        if (!isBlank(customer.getEmail())
                && !customer.getEmail().trim().matches(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {

            throw new IllegalArgumentException(
                    "Enter a valid email address!");
        }
    }

    private String normalizeEmail(String email) {
        return isBlank(email) ? null : email.trim().toLowerCase();
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
