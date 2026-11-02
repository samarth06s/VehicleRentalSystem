
package com.vehiclerental.model;

import java.math.BigDecimal;

public class Vehicle {

    private int vehicleId;
    private String brand;
    private String model;
    private String vehicleType;
    private String registrationNumber;
    private BigDecimal rentPerDay;
    private String status;

    // Default constructor
    public Vehicle() {
    }

    // Parameterized constructor
    public Vehicle(
            String brand,
            String model,
            String vehicleType,
            String registrationNumber,
            BigDecimal rentPerDay,
            String status) {

        this.brand = brand;
        this.model = model;
        this.vehicleType = vehicleType;
        this.registrationNumber = registrationNumber;
        this.rentPerDay = rentPerDay;
        this.status = status;
    }

    // Getters and Setters
    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public BigDecimal getRentPerDay() {
        return rentPerDay;
    }

    public void setRentPerDay(BigDecimal rentPerDay) {
        this.rentPerDay = rentPerDay;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return brand + " " + model +
                " (" + registrationNumber + ")";
    }
}
