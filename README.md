<p align="center">
  <img src="screenshots/project-banner.png"
       width="100%"
       alt="Vehicle Rental Management System">
</p>

# 🚗 Vehicle Rental Management System

A Java-based desktop application developed using **Java Swing and MySQL** to simplify vehicle rental operations. The system provides an integrated interface for managing vehicles, customers, bookings, returns, rental charges, and invoices.

## ✨ Features

- **Dashboard:** View vehicle statistics, customer counts, and rental summaries.
- **Vehicle Management:** Add, update, delete, search, and view vehicle records.
- **Customer Management:** Manage customer information and driving licence details.
- **Rental Management:** Book vehicles, process returns, and track rental records.
- **Automatic Late Fee Calculation:** Calculate overdue charges at ₹200 per day.
- **Invoice Generation:** View, print, and export completed rental invoices as PDF.
- **MySQL Database Integration:** Store and manage application data.
- **Modern GUI:** Clean desktop interface built with Java Swing and FlatLaf.

## 🛠️ Technologies Used

| Technology | Purpose |
|---|---|
| Java 21 | Core programming language |
| Java Swing | Desktop GUI |
| MySQL 8 | Database management |
| JDBC | Database connectivity |
| Maven | Dependency and build management |
| FlatLaf | Modern Swing interface |
| Apache PDFBox | PDF invoice generation |
| IntelliJ IDEA | Development environment |
| Git & GitHub | Version control |

## 📁 Project Structure

```text
VehicleRentalSystem/
│
├── src/
│   └── main/
│       ├── java/com/vehiclerental/
│       │   ├── config/
│       │   ├── dao/
│       │   ├── model/
│       │   ├── service/
│       │   ├── ui/
│       │   ├── util/
│       │   └── Main.java
│       │
│       └── resources/
│           └── db.properties.example
│
├── database/
│   └── schema.sql
│
├── pom.xml
├── README.md
└── .gitignore
```


## 📸 Application Screenshots

### Dashboard

![Dashboard](screenshots/dashboard.png)

### Vehicle Management
![Vehicle Management](screenshots/vehicle_management.png)

### Customer Management
![Customer Management](screenshots/customer_management.png)

### Rental Management
![Rental Management](screenshots/rental_management.png)

### Invoice Generation
![Invoice PDF](screenshots/invoice_pdf.png)


## 🗄️ Database Setup

1. Install MySQL Server and MySQL Workbench.
2. Open the project in IntelliJ IDEA.
3. Open `database/schema.sql` in MySQL Workbench.
4. Execute the SQL script to create the `vehicle_rental` database and required tables.
5. Configure your database connection.

### Database Configuration

The application loads database credentials from an external configuration file.

**1. Create the configuration directory**

On macOS or Linux, run:

```bash
mkdir -p ~/.vehicle-rental-system
```

**2. Create the configuration file**

Create a file named `db.properties` inside:

`~/.vehicle-rental-system/`

Add the following:

```properties
db.url=jdbc:mysql://localhost:3306/vehicle_rental
db.username=YOUR_MYSQL_USERNAME
db.password=YOUR_MYSQL_PASSWORD
```

Replace the placeholders with your own MySQL credentials.

**Important:** Do not upload this file or share your database password. The configuration file is intentionally kept outside the project.

### Running the Downloaded Application

1. Download the latest executable JAR from the [GitHub Releases](https://github.com/samarth06s/VehicleRentalSystem/releases/latest) page.
2. Ensure MySQL is running and the database is configured.
3. Open Terminal in the downloaded JAR's folder.
4. Run:

```bash
java -jar vehicle-rental-system-1.0.1.jar
```

**Requirements:** Java 21 or later and MySQL 8.0.

## ⚙️ Installation & Setup

### Prerequisites
- Java 21 or later
- MySQL 8.0
- MySQL Workbench (recommended)

### Database Setup
1. Open MySQL Workbench.
2. Execute the SQL script from `database/schema.sql`.
3. Create the database and tables.

### Database Configuration
Create the configuration directory:

`~/.vehicle-rental-system/`

Inside it, create `db.properties`:

```properties
db.url=jdbc:mysql://localhost:3306/vehicle_rental
db.username=YOUR_MYSQL_USERNAME
db.password=YOUR_MYSQL_PASSWORD
```

Replace the values with your local MySQL credentials.

**Important:** Never commit your actual `db.properties` file or share your database password.

### Run the Application

Run the executable JAR:

```bash
java -jar vehicle-rental-system-1.0-SNAPSHOT.jar
```

## ▶️ How to Run

### Option 1: IntelliJ IDEA

1. Clone the repository:

```bash
git clone https://github.com/samarth06s/VehicleRentalSystem.git
```

2. Open the project in IntelliJ IDEA.
3. Allow Maven to download the dependencies.
4. Set up the MySQL database.
5. Configure `db.properties`.
6. Run `Main.java`.

### Option 2: Maven Build

Open the Maven panel in IntelliJ IDEA and run:

- `clean`
- `package`

The generated JAR will be available inside the `target/` directory.

The application requires Java 21, a running MySQL server, and a valid database configuration.

## 👨‍💻 Development Team

- **Samarth Shikari**
- **Saurabh Fatangade**
- **Jay Jadhav**
- **Pruthviraj Nimbalkar**

B.Tech – Computer Science and Engineering

**Sanjivani University**

---

*Developed as a Java-based desktop application project.*
