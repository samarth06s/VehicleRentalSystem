# Vehicle Rental Management System

A desktop-based Vehicle Rental Management System developed using Java Swing and MySQL. The application helps manage vehicles, customers, rentals, returns, and invoices through a user-friendly graphical interface.

## Features

- **Dashboard:** View vehicle statistics, customer counts, and rental summaries.
- **Vehicle Management:** Add, update, delete, search, and view vehicles.
- **Customer Management:** Maintain customer information and driving licence details.
- **Rental Management:** Book vehicles, process returns, and calculate rental charges.
- **Late Fee Calculation:** Automatically calculate overdue charges at ₹200 per day.
- **Invoice Generation:** View, print, and export completed rental invoices as PDF.
- **Database Integration:** Store and manage information using MySQL.

## Technologies Used

- Java 21
- Java Swing
- MySQL 8
- JDBC
- Maven
- FlatLaf
- Apache PDFBox
- IntelliJ IDEA
- Git and GitHub

## Project Structure

```text
VehicleRentalSystem/
├── src/main/java/com/vehiclerental/
│   ├── config/
│   ├── dao/
│   ├── model/
│   ├── service/
│   ├── ui/
│   ├── util/
│   └── Main.java
├── src/main/resources/
├── pom.xml
└── README.md
```

## Database Setup

1. Install MySQL Server and MySQL Workbench.
2. Create a database named `vehicle_rental`.
3. Create the required tables: `vehicles`, `customers`, and `rentals`.
4. Configure your MySQL credentials in `src/main/resources/db.properties`.

Example configuration:

```properties
db.url=jdbc:mysql://localhost:3306/vehicle_rental
db.username=YOUR_USERNAME
db.password=YOUR_PASSWORD
```

**Important:** Never upload your actual database credentials to GitHub.

## How to Run

1. Clone the repository:

```bash
git clone https://github.com/samarth06s/VehicleRentalSystem.git
```

2. Open the project in IntelliJ IDEA.
3. Allow Maven to download the dependencies.
4. Configure the MySQL database and `db.properties`.
5. Run `Main.java`.

## Developer

**Samarth Shikari** \
**Saurabh Fatangade**\
**Jay Jadhav**\
**Pruthviraj Nimbalkar**\
B.Tech Computer Science and Engineering  
Sanjivani University

---

*Developed as a Java-based desktop application project.*
