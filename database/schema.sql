
-- Vehicle Rental Management System
-- Database Schema

CREATE DATABASE IF NOT EXISTS vehicle_rental;
USE vehicle_rental;

-- 1. Vehicles Table
CREATE TABLE IF NOT EXISTS vehicles (
                                        vehicle_id INT PRIMARY KEY AUTO_INCREMENT,
                                        brand VARCHAR(50) NOT NULL,
    model VARCHAR(50) NOT NULL,
    vehicle_type VARCHAR(30) NOT NULL,
    registration_number VARCHAR(20) UNIQUE NOT NULL,
    rent_per_day DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) DEFAULT 'AVAILABLE'
    );

-- 2. Customers Table
CREATE TABLE IF NOT EXISTS customers (
                                         customer_id INT PRIMARY KEY AUTO_INCREMENT,
                                         name VARCHAR(100) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    email VARCHAR(100) UNIQUE,
    license_number VARCHAR(50) UNIQUE NOT NULL
    );

-- 3. Rentals Table
CREATE TABLE IF NOT EXISTS rentals (
                                       rental_id INT PRIMARY KEY AUTO_INCREMENT,
                                       customer_id INT NOT NULL,
                                       vehicle_id INT NOT NULL,
                                       rental_date DATE NOT NULL,
                                       due_date DATE NOT NULL,
                                       actual_return_date DATE,
                                       total_amount DECIMAL(10,2) DEFAULT 0.00,
    status VARCHAR(20) DEFAULT 'ACTIVE',

    CONSTRAINT fk_rental_customer
    FOREIGN KEY (customer_id)
    REFERENCES customers(customer_id),

    CONSTRAINT fk_rental_vehicle
    FOREIGN KEY (vehicle_id)
    REFERENCES vehicles(vehicle_id)
    );
