# Smart Library Management System

A command-line Java application for managing books, library members, borrowing, returns, transactions, fines, and library reports.

---

## Overview

The **Smart Library Management System** is a standalone terminal application designed to streamline core library administration tasks. It enables library staff to manage a catalog of physical and reference books, register student and staff members, track book checkouts and returns, compute overdue fines automatically, and export operational reports.

The application addresses common operational challenges by providing automated quota enforcement, stock management, and concurrency control. Special reference materials are automatically flagged to restrict checkouts, while borrowable physical inventory is tracked dynamically as items are issued and returned. Overdue returns automatically trigger daily fine calculations based on loan parameters.

All library records—including member profiles, catalog entries, and transaction histories—are persisted locally using an embedded SQLite database. The application initializes database structures automatically on startup, eliminating the need to install or configure external database servers.

Built entirely as a command-line interface, the system operates in any standard terminal environment without requiring graphical desktop frameworks or complex IDE setups.

---

## Features

- **Catalog Management:** Add and manage physical borrowable books and reading-room reference books.
- **Member Registration:** Register students and librarians with automated borrowing quota limits (e.g., 3 books for students, 10 for librarians).
- **Multi-Criteria Search:** Search catalog items by title, author, ISBN keyword, unique Book ID, or Category enum.
- **Borrowing & Returns:** Issue books to registered members and process returns with automatic fine calculation.
- **Transaction History:** Track active checkouts, overdue items, and completed returns across the system.
- **Multithreading Concurrency Safety:** Safe concurrent transaction processing using thread synchronization to prevent race conditions during simultaneous book checkouts.
- **Persistent Data Storage:** Automatic schema initialization and local data persistence using SQLite via JDBC and JPA/Hibernate.
- **Reporting & Data Export:** Generate formatted text summary reports, export books and transactions to CSV files, and perform binary database backups.
- **Class Reflection Inspector:** Dynamically inspect class structures, methods, fields, and annotations at runtime.
- **Input Validation & Error Prevention:** Comprehensive input validation to catch invalid menu choices, bad email formats, or non-numeric entries without crashing.

---

## How It Works

### Application Workflow

```text
Start Application
       ↓
Main Menu
       ↓
Select Library Operation
       ↓
Validate Input
       ↓
Perform Operation
       ↓
Update Database
       ↓
Display Result
       ↓
Return to Main Menu
```

### Key Workflows

#### 1. Adding a Book
The user selects Option 2 from the main menu, specifies the title, author, ISBN, category, and type (Physical vs. Reference). For physical books, the system records shelf location and stock quantity. For reference books, the system logs reading room section placement and marks the item as non-borrowable.

#### 2. Registering a Member
The user selects Option 1 to enter student details (name, email, 10-digit phone, registration number, and department). The system validates email and phone syntax using regular expressions before generating a unique user ID.

#### 3. Issuing a Book
The user selects Option 5 and enters the member ID and book ID:
1. The system verifies that both member and book records exist.
2. It checks whether the book is a Reference Book (which cannot be checked out).
3. It checks if inventory quantity is greater than 0.
4. It checks whether the member has reached their borrowing quota limit.
5. If all checks pass, a transaction record is created with an issue date and a 14-day due date, and the available book quantity is decremented.

#### 4. Returning a Book
The user selects Option 6 and inputs the transaction ID:
1. The system locates the active transaction.
2. It calculates the return date against the due date.
3. If returned late, an overdue fine (₹2.50/day) is computed.
4. The transaction is marked as completed, and book inventory stock is incremented.

---

## Technology Stack

| Technology | Purpose |
| :--- | :--- |
| **Java 17** | Core programming language and runtime environment |
| **Apache Maven** | Project build, dependency management, and test execution |
| **SQLite 3** | Local embedded relational database |
| **JDBC** | Direct database persistence via PreparedStatements |
| **JPA / Hibernate 6** | Object-Relational Mapping (ORM) and JPQL queries |
| **JUnit 5** | Automated unit testing framework |
| **SLF4J** | Logging abstraction |

---

## Requirements

To build and run this application, ensure your environment meets the following requirements:

- **Java Development Kit (JDK):** Version 17 or higher
- **Apache Maven:** Version 3.8 or higher
- **Git:** Version 2.x or higher
- **Terminal / Command Prompt:** Standard console terminal (Linux, macOS, or Windows)

No IDE (such as IntelliJ, Eclipse, or NetBeans) or external database server (such as MySQL or PostgreSQL) is required.

---

## Installation

Clone the repository to your local computer:

```bash
git clone https://github.com/<your-username>/smart-library-management.git
cd smart-library-management
```

*(Note: Replace `<your-username>` with your actual GitHub username).*

---

## Building the Project

Compile the application and run all automated unit tests using Maven:

```bash
mvn clean package
```

This command performs the following actions:
1. Cleans previous build artifacts.
2. Compiles all Java source code under `src/main/java`.
3. Runs all 8 automated JUnit 5 test cases under `src/test/java`.
4. Packages the compiled application into an executable Fat JAR in the `target/` directory:
   `target/smart-library-management-1.0-SNAPSHOT-jar-with-dependencies.jar`

---

## Running the Application

You can execute the application in either of two ways:

### Method 1: Run via Maven Exec Plugin (Recommended for Development)

```bash
mvn exec:java
```

### Method 2: Run the Standalone Packaged JAR

```bash
java -jar target/smart-library-management-1.0-SNAPSHOT-jar-with-dependencies.jar
```

---

## Using the Application

When launched, the system presents an interactive command-line menu:

```text
========================================
       SMART LIBRARY MANAGEMENT         
========================================
1.  Register Student
2.  Add Book
3.  View All Books
4.  Search Book
5.  Issue Book
6.  Return Book
7.  View Student Transactions
8.  View Overdue & Active Transactions
9.  Generate Library Report (.txt)
10. Export Data (.csv, .txt & DB Backup)
11. Run Concurrent Transaction Demo (Multithreading)
12. Inspect Class Metadata (Reflection Demo)
13. Run JPA / Hibernate Operations Demo
14. Exit
========================================
Enter your choice (1-14):
```

### Example 1: Registering a Student and Issuing a Book

```text
Enter your choice (1-14): 1

--- 📝 REGISTER NEW STUDENT ---
Enter Name: Alice Smith
Enter Email: alice@university.edu
Enter 10-Digit Phone: 9876543210
Enter Registration Number (e.g., 21BCE0003): 21BCE0001
Enter Department: Computer Science
✅ Student registered successfully with System User ID #1

Enter your choice (1-14): 5

--- 📤 ISSUE BOOK ---
Enter User ID (e.g. 1): 1
Enter Book ID (e.g. 1): 1
✅ Book Issued Successfully!
   Transaction ID: #1
   Due Date: 2026-09-27
```

### Example 2: Running Concurrent Transaction Demo

```text
Enter your choice (1-14): 11

====================================================
   CONCURRENT TRANSACTION DEMO (MULTITHREADING)     
====================================================
Scenario: Creating a high-demand single-copy book and spawning
2 concurrent threads (Student-1 & Student-2) trying to check
it out at the exact same millisecond.

📚 Created Test Book: ID #3 | Title: 'Limited Edition Java Handbook' | Quantity: 1

🚀 Starting Concurrent Threads simultaneously...
⚡ [Thread-Student-1] Attempting to issue Book #3 for Student #1...
⚡ [Thread-Student-2] Attempting to issue Book #3 for Student #2...
✅ [Thread-Student-1] Successfully issued Book #3! Tx #1
❌ [Thread-Student-2] Failed to issue Book #3: Book 'Limited Edition Java Handbook' is currently unavailable or out of stock.

Thread 1 Result: SUCCESS: Transaction #1 created for Thread-Student-1
Thread 2 Result: FAILED: Book 'Limited Edition Java Handbook' is currently unavailable or out of stock.
Remaining Quantity in Inventory: 0 copy (Status: ISSUED)
```

---

## Database

The Smart Library Management System uses **SQLite 3** for embedded data persistence.

### Database Architecture & File Locations

- **JDBC Database File:** `data/library.db`  
  Stores core operational data for users, books, and transaction records. Created and initialized automatically by `DatabaseManager.java`.
- **JPA / Hibernate Database File:** `data/library_jpa.db`  
  Managed by Hibernate ORM for entity mapping and JPQL query execution.

### Schema Tables

1. **`users`**: Stores user ID, name, unique email, phone, role (`STUDENT`/`LIBRARIAN`), registration number, department, and employee ID.
2. **`books`**: Stores book ID, ISBN, title, author, category, status (`AVAILABLE`/`ISSUED`/`REFERENCE_ONLY`), book type, quantity, and location info.
3. **`transactions`**: Stores transaction ID, user ID (foreign key), book ID (foreign key), issue date, due date, return date, fine amount, transaction type, and return status flag.

Because SQLite runs in-process, no database server installation, user creation, or port configuration is required.

---

## Project Structure

```text
smart-library-management/
├── pom.xml                                 # Maven project configuration and dependencies
├── README.md                               # Project documentation
├── LICENSE                                 # MIT License
├── .gitignore                              # Git exclusion rules
├── docs/                                   # Documentation resources
│   └── project-report.md                   # System design & architecture report
├── data/                                   # Auto-generated SQLite database files
│   ├── library.db                          # Primary JDBC database
│   └── library_jpa.db                      # JPA Hibernate database
├── reports/                                # Export directory for text/CSV/backup files
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/
    │   │       └── smartlibrary/
    │   │           ├── Main.java           # Application entry point
    │   │           ├── annotation/         # Custom metadata annotations (@CourseConcept)
    │   │           ├── model/              # OOP Domain model (Book, User, Transaction, Enums)
    │   │           ├── interface_contract/ # Service interfaces (Searchable, Borrowable, etc.)
    │   │           ├── entity/             # JPA Entities (BookEntity, UserEntity, TransactionEntity)
    │   │           ├── exception/          # Custom checked exception hierarchy
    │   │           ├── util/               # Singleton DB managers and input validators
    │   │           ├── repository/         # JDBC and JPA repository DAOs
    │   │           ├── service/            # Core business logic & fine calculation
    │   │           ├── io/                 # Character and Byte stream exporters
    │   │           ├── thread/             # Multithreading concurrency simulator
    │   │           ├── reflection/         # Dynamic class structure inspector
    │   │           └── ui/                 # Command-line menu interface
    │   └── resources/
    │       ├── META-INF/persistence.xml    # JPA persistence unit configuration
    │       └── simplelogger.properties     # SLF4J logger configuration
    └── test/
        └── java/
            └── com/
                └── smartlibrary/           # Automated JUnit 5 test suite
                    ├── model/
                    ├── service/
                    └── thread/
```

---

## Testing

Automated testing is implemented using **JUnit 5**. The test suite covers domain logic, inheritance rules, search overloading, exception throwing, and thread synchronization.

### Running Tests

To run the automated tests:

```bash
mvn test
```

### Implemented Test Suites

- **`ModelTest.java`**: Tests inheritance, non-borrowable reference book rules, keyword search matching, and fine calculation math.
- **`LibraryServiceTest.java`**: Tests overloaded search logic, missing book checks, and exception throwing when attempting to borrow reference books.
- **`SynchronizationTest.java`**: Tests concurrent thread execution on single-copy inventory to verify that synchronization locks prevent duplicate checkouts.

---

## Troubleshooting

### 1. `mvn: command not found`
- **Cause:** Maven is not installed or its `bin` folder is not added to your system `PATH`.
- **Solution:** Install Maven 3.8+ and add Maven to your environment `PATH`.

### 2. `Java version error / UnsupportedClassVersionError`
- **Cause:** The system default Java version is older than Java 17.
- **Solution:** Set your `JAVA_HOME` environment variable to JDK 17 or higher and verify with `java -version`.

### 3. File Creation or Database Permission Errors
- **Cause:** Restricted folder permissions preventing creation of the `data/` or `reports/` directories.
- **Solution:** Run the terminal with appropriate write permissions in the project working directory.

---

## Future Improvements

- **Email & SMS Notifications:** Integrate an automated mail dispatch service to notify members 2 days prior to book due dates.
- **Barcode & RFID Scanning:** Support USB barcode readers via console input streams for fast checkouts.
- **RESTful Web API:** Extend the service layer with a Spring Boot or Lightweight HTTP server module to expose API endpoints for web/mobile applications.

---

## Project Documentation

For additional technical details and course documentation, see the following files in the `docs/` folder:

- [Project Report](docs/project-report.md): Comprehensive technical documentation covering software architecture, module design, database ER diagrams, and result analysis.
