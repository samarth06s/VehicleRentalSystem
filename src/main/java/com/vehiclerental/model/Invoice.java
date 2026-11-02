package com.vehiclerental.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Invoice(
        int rentalId,
        LocalDate rentalDate,
        LocalDate dueDate,
        LocalDate actualReturnDate,
        BigDecimal totalAmount,
        String status,
        String customerName,
        String phone,
        String email,
        String licenseNumber,
        String brand,
        String model,
        String vehicleType,
        String registrationNumber,
        BigDecimal rentPerDay
) {
}