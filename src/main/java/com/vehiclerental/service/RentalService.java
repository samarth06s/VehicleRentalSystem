
package com.vehiclerental.service;

import com.vehiclerental.dao.RentalDAO;
import com.vehiclerental.model.Rental;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class RentalService {

    private final RentalDAO rentalDAO;

    public RentalService() {
        this.rentalDAO = new RentalDAO();
    }

    // Book a vehicle
    public boolean bookVehicle(Rental rental) throws SQLException {

        validateRental(rental);

        if (rental.getRentalDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Rental date cannot be in the past!");
        }

        // Rental date validation
        if (rental.getRentalDate() == null ||
                rental.getDueDate() == null) {

            throw new IllegalArgumentException(
                    "Rental date and due date are required!"
            );
        }

// Due date must be after rental date
        if (!rental.getDueDate().isAfter(rental.getRentalDate())) {

            throw new IllegalArgumentException(
                    "Due date must be after rental date!"
            );
        }

// Rental date cannot be in the past
        if (rental.getRentalDate().isBefore(LocalDate.now())) {

            throw new IllegalArgumentException(
                    "Rental date cannot be in the past!"
            );
        }
        return rentalDAO.createRental(rental);
    }

    // Retrieve all rental records
    public List<Rental> getAllRentals() throws SQLException {

        return rentalDAO.getAllRentals();
    }

    // Return a vehicle
    public boolean returnVehicle(int rentalId,
                                 LocalDate actualReturnDate)
            throws SQLException {

        if (rentalId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid rental ID!");
        }

        if (actualReturnDate == null) {
            throw new IllegalArgumentException(
                    "Return date is required!");
        }

        if (actualReturnDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Return date cannot be in the future!");
        }

        return rentalDAO.returnVehicle(
                rentalId, actualReturnDate);
    }

    // Rental validation
    private void validateRental(Rental rental) {

        if (rental == null) {
            throw new IllegalArgumentException(
                    "Rental details cannot be null!");
        }

        if (rental.getCustomerId() <= 0) {
            throw new IllegalArgumentException(
                    "Select a valid customer!");
        }

        if (rental.getVehicleId() <= 0) {
            throw new IllegalArgumentException(
                    "Select a valid vehicle!");
        }

        if (rental.getRentalDate() == null
                || rental.getDueDate() == null) {
            throw new IllegalArgumentException(
                    "Rental and due dates are required!");
        }

        if (!rental.getDueDate().isAfter(
                rental.getRentalDate())) {
            throw new IllegalArgumentException(
                    "Due date must be after rental date!");
        }
    }
}
