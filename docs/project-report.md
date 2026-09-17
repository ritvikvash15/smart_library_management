# PROJECT REPORT

## SMART LIBRARY MANAGEMENT SYSTEM – A COMMAND-LINE JAVA APPLICATION

---

### 1. TITLE
**Smart Library Management System – A Command-Line Java Application**

---

### 2. ABSTRACT
The Smart Library Management System is a robust command-line application designed to manage core library operations, including book inventory, member registration, issue/return transactions, fine calculations, data export, and automated multi-user synchronization. Developed using Java 17, Maven, SQLite, JDBC, JPA/Hibernate, and JUnit 5, the application provides a modular software architecture demonstrating Object-Oriented Programming (OOP) principles, custom exception handling, thread-safe concurrent transactions, character/byte I/O streams, and hybrid database persistence via JDBC and JPA.

---

### 3. INTRODUCTION
In institutional resource centers, effective management of library assets is vital. While web-based library applications exist, a solid understanding of core Java runtime behavior, object inheritance, multi-threaded state consistency, and database connectivity is fundamental for software engineers. This project delivers a menu-driven, self-contained terminal application that requires no external database server administration and runs cleanly across operating systems.

---

### 4. PROBLEM STATEMENT
Traditional library management tools often suffer from:
- Lack of thread synchronization when multiple users attempt to issue limited book stock concurrently.
- Failure to enforce specific inheritance rules (e.g., distinguishing borrowable physical books from reading-room reference books).
- Reliance on complex manual database server configuration.
- Inadequate exception handling resulting in unexpected runtime crashes during invalid console input.

This project addresses these challenges by building a robust command-line system with native Java architecture.

---

### 5. OBJECTIVES
- To design a clean, menu-driven command-line interface for managing library users, books, and transactions.
- To implement OOP principles: Encapsulation, Polymorphism, Inheritance, Abstract Classes, Interfaces, Enums, Static Members, Inner/Nested Classes, and Design Patterns (Singleton).
- To engineer a multithreaded transaction simulator verifying synchronization locks during simultaneous checkouts.
- To implement custom checked exceptions and input validation preventing runtime application crashes.
- To utilize the Java Collections Framework (`ArrayList`, `HashMap`, `Stack`, `Vector`) for efficient data manipulation.
- To provide I/O stream mechanisms for exporting reports (`.txt`), catalog data (`.csv`), and binary database backups (`.db`).
- To demonstrate hybrid data access using JDBC PreparedStatements and JPA/Hibernate ORM with JPQL queries on an embedded SQLite database.

---

### 6. SCOPE
The application handles:
- Student and Librarian registration.
- Adding Physical Books and Reference Books with stock control.
- Searching catalog items via overloaded methods (Keyword, Book ID, Category).
- Issuing and returning books with automated overdue fine calculation.
- Live demonstration of multithreaded concurrency and Java Reflection metadata inspection.
- Report generation, CSV data export, and database backup creation.

---

### 7. TECHNOLOGIES USED
- **Core Platform:** Java 17 LTS (OpenJDK)
- **Build & Dependency Tool:** Apache Maven 3.8+
- **Database Engine:** SQLite 3 (Embedded, self-contained)
- **Data Access:** JDBC (`org.xerial:sqlite-jdbc`) & JPA 3.1 / Hibernate ORM 6.4 (`hibernate-community-dialects`)
- **Testing Framework:** JUnit 5 (Jupiter)
- **Logging:** SLF4J Simple Logger

---

### 8. SYSTEM REQUIREMENTS
#### Hardware Requirements:
- Processor: Dual-core 1.5 GHz or higher
- RAM: Minimum 2 GB (4 GB recommended)
- Storage: 100 MB available disk space

#### Software Requirements:
- Operating System: Linux, macOS, or Windows
- JDK: Java 17+ installed and configured in system `PATH`
- Maven: Apache Maven 3.8+

---

### 9. SYSTEM ARCHITECTURE
The system follows a modular layered architecture:

```text
+-------------------------------------------------------+
|                    CLI Console Menu                   |
+-------------------------------------------------------+
                           |
                           v
+-------------------------------------------------------+
|            Library Service / Business Logic           |
+-------------------------------------------------------+
    |                  |                    |
    v                  v                    v
+-----------+  +---------------+  +-------------------+
|  File I/O |  | Multithread   |  | Reflection API    |
| Stream    |  | Synchronization|  | Metadata Inspector|
+-----------+  +---------------+  +-------------------+
    |                  |                    |
    +------------------+--------------------+
                           |
                           v
+-------------------------------------------------------+
|           Persistence Layer (JDBC & JPA/Hibernate)    |
+-------------------------------------------------------+
                           |
                           v
+-------------------------------------------------------+
|              SQLite Database (data/library.db)        |
+-------------------------------------------------------+
```

---

### 10. MODULE DESCRIPTION
1. **Domain Model Module:** Defines `Book`, `User`, `LibraryTransaction`, and associated enums/interfaces.
2. **Repository & Data Access Module:** Handles direct SQLite SQL queries via JDBC and ORM mappings via JPA.
3. **Business Service Module:** Enforces business rules, quota checks, fine calculations, and overloaded search logic.
4. **Multithreading Module:** Spawns concurrent student threads targeting identical book records to test synchronization locks.
5. **File I/O Module:** Formats data into text/CSV files using character streams and performs binary database backups using byte streams.
6. **Reflection Module:** Dynamically queries class signatures, annotations, methods, and constructors at runtime.
7. **User Interface Module:** Manages interactive console prompts and input validation loops.

---

### 11. CLASS / PACKAGE DESCRIPTION

- `com.smartlibrary.annotation.CourseConcept`: Custom annotation mapping code elements to architectural components.
- `com.smartlibrary.model.Book`: Abstract base class implementing `Borrowable`, `Searchable`, and `Exportable`.
- `com.smartlibrary.model.PhysicalBook`: Concrete subclass for borrowable physical inventory.
- `com.smartlibrary.model.ReferenceBook`: Concrete subclass for non-borrowable reading room items.
- `com.smartlibrary.model.User`: Abstract base class for library users.
- `com.smartlibrary.model.Student`: Concrete subclass defining student borrowing quota (3 books).
- `com.smartlibrary.model.Librarian`: Concrete subclass defining librarian borrowing quota (10 books).
- `com.smartlibrary.model.LibraryTransaction`: Transaction entity containing static nested summary class.
- `com.smartlibrary.util.DatabaseManager`: Singleton connection manager and SQLite table initializer.
- `com.smartlibrary.util.JpaUtil`: Singleton `EntityManagerFactory` manager.
- `com.smartlibrary.util.InputValidator`: Exception-safe console input reader.
- `com.smartlibrary.repository.BookJdbcRepository`: JDBC DAO for book operations.
- `com.smartlibrary.repository.UserJdbcRepository`: JDBC DAO for user operations.
- `com.smartlibrary.repository.TransactionJdbcRepository`: JDBC DAO for transaction records.
- `com.smartlibrary.repository.JpaLibraryRepository`: JPA DAO executing JPQL queries.
- `com.smartlibrary.service.LibraryService`: Service facade providing thread-safe business methods.
- `com.smartlibrary.thread.ConcurrentCheckoutTask`: Runnable task for thread testing.
- `com.smartlibrary.thread.MultithreadingDemoManager`: Concurrency runner demonstrating synchronized lock behavior.
- `com.smartlibrary.reflection.ReflectionInspector`: Reflection API dynamic class inspector.
- `com.smartlibrary.io.ReportExporter`: Character stream report exporter.
- `com.smartlibrary.io.BackupManager`: Byte stream binary backup utility.
- `com.smartlibrary.ui.ConsoleMenu`: Command-line loop controller.

---

### 12. DATABASE DESIGN
The SQLite database consists of three relational tables:

```text
+-------------------+       +-------------------+       +------------------------+
|       USERS       |       |       BOOKS       |       |      TRANSACTIONS      |
+-------------------+       +-------------------+       +------------------------+
| id (PK)           |<-----\| id (PK)           |<-----\| id (PK)                |
| name              |       | isbn              |       | user_id (FK -> USERS)  |
| email (UNIQUE)    |       | title             |       | book_id (FK -> BOOKS)  |
| phone             |       | author            |       | issue_date             |
| role              |       | category          |       | due_date               |
| registration_no   |       | status            |       | return_date            |
| department        |       | book_type         |       | fine_amount            |
| employee_id       |       | quantity          |       | transaction_type       |
+-------------------+       | location_info     |       | returned               |
                            +-------------------+       +------------------------+
```

---

### 13. JDBC IMPLEMENTATION
JDBC operations utilize `PreparedStatement` with parameterized placeholders to eliminate SQL injection vulnerabilities. Connections are opened on demand via `DatabaseManager.getInstance().getConnection()` inside try-with-resources blocks. Generated keys are retrieved via `Statement.RETURN_GENERATED_KEYS` to update model identifiers automatically upon creation.

---

### 14. JPA IMPLEMENTATION
The JPA implementation uses Jakarta Persistence 3.1 with Hibernate ORM 6.4. Entities (`UserEntity`, `BookEntity`, `TransactionEntity`) map relational columns using annotations (`@Entity`, `@Table`, `@Id`, `@GeneratedValue`, `@Column`, `@Enumerated`, `@ManyToOne`). Executable JPQL queries (e.g. `SELECT b FROM BookEntity b WHERE b.status = :status`) are queried through `EntityManager`.

---

### 15. OOP CONCEPTS USED
- **Encapsulation:** All model properties are declared `private` and accessed exclusively through getters/setters.
- **Inheritance:** `PhysicalBook` and `ReferenceBook` extend `Book`; `Student` and `Librarian` extend `User`.
- **Polymorphism:** Method overriding of `isBorrowable()` and `calculateFine()` allows uniform treatment of `Book` references.
- **Method Overloading:** `LibraryService.searchBook` is overloaded for `String`, `int`, and `BookCategory` parameters.
- **Abstract Classes:** `Book` and `User` define abstract methods (`getBookType()`, `getMaxBorrowLimit()`).
- **Interfaces:** `Searchable`, `Borrowable`, `Exportable`, `Reportable` decouple contracts from implementation.
- **Static Members & Singleton:** `DatabaseManager` and `JpaUtil` enforce single global connection instances.
- **Nested & Inner Classes:** `LibraryTransaction.TransactionSummary` (Static Nested Class) and `LibraryServiceHolder` (Lazy Holder Inner Class).
- **Anonymous Classes:** `Comparator<Book>` in `getBooksSortedByTitle()`.

---

### 16. EXCEPTION HANDLING
Custom checked exceptions extend `LibraryException`:
- `BookNotAvailableException`: Thrown when a book is out of stock or is a non-borrowable reference book.
- `BookNotFoundException`: Thrown when an invalid book ID is requested.
- `UserNotFoundException`: Thrown when a user ID is missing.
- `MaxLimitExceededException`: Thrown when a student attempts to check out more than 3 books.
- `InvalidInputException`: Thrown when input validation regex fails.
- `DatabaseOperationException`: Wraps low-level SQL exceptions.

Console input errors (e.g., entering letters for numeric IDs) are safely caught by `InputValidator` loops without terminating the application.

---

### 17. MULTITHREADING AND SYNCHRONIZATION
To demonstrate thread safety, menu Option 11 spawns two parallel threads (`Student-Thread-1` and `Student-Thread-2`) targeting a single-copy physical book simultaneously. The `issueBook()` method in `LibraryService` is declared `synchronized`. When Thread-1 acquires the object lock, Thread-2 waits. Thread-1 decrements inventory to 0 and succeeds; when Thread-2 resumes execution, it catches a `BookNotAvailableException`, preventing inventory corruption.

---

### 18. COLLECTIONS FRAMEWORK
- `ArrayList<Book>`: Stores cached library catalog for dynamic search and retrieval.
- `HashMap<Integer, User>`: Provides O(1) key-value lookup of users by ID.
- `Stack<LibraryTransaction>`: Tracks transaction history.
- `Vector<String>`: Provides a thread-safe synchronized audit log.

---

### 19. FILE I/O
- **Character Streams:** `ReportExporter.java` uses `FileWriter`, `BufferedWriter`, `PrintWriter`, `FileReader`, and `BufferedReader` to write and read `reports/library_report.txt`, `reports/books.csv`, and `reports/transactions.csv`.
- **Byte Streams:** `BackupManager.java` uses `FileInputStream`, `FileOutputStream`, `BufferedInputStream`, and `BufferedOutputStream` to create binary copies of `data/library.db` into `reports/library_backup.db`.

---

### 20. REFLECTION AND ANNOTATIONS
- **Custom Annotation:** `@CourseConcept(unit, concept, description)` annotates key methods and classes.
- **Reflection Inspector:** `ReflectionInspector.java` uses `Class.forName()`, `getDeclaredFields()`, `getConstructors()`, `getDeclaredMethods()`, and `getDeclaredAnnotations()` to inspect class metadata dynamically from the console menu.

---

### 21. PROGRAM FLOW
1. Application starts via `Main.java`.
2. `DatabaseManager` checks and creates `data/library.db` and tables.
3. `ConsoleMenu` displays text menu options.
4. User selects an option (1-14).
5. `InputValidator` parses console input safely.
6. `LibraryService` executes business logic, database queries, or multithreading tasks.
7. Results are formatted and printed to the terminal.
8. Application loops until user selects Option 14 (Exit).

---

### 22. SAMPLE INPUT/OUTPUT
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

### 23. TESTING
All automated unit tests were created using JUnit 5 in `src/test/java`:
- `ModelTest`: Verified inheritance, `isBorrowable()`, and fine calculation math.
- `LibraryServiceTest`: Verified overloaded search methods and exception throwing on reference books.
- `SynchronizationTest`: Verified multithreading thread safety preventing double checkout.

**Test Execution Command:** `mvn clean test`  
**Result:** 8 tests executed, 0 failures, 0 errors.

---

### 24. RESULTS
The application successfully builds into a single fat JAR artifact (`smart-library-management-1.0-SNAPSHOT-jar-with-dependencies.jar`). All 14 CLI menu features, database persistences, thread synchronization locks, file stream exports, and reflection inspections function without errors.

---

### 25. ADVANTAGES
- Self-contained SQLite database requiring zero external database configuration.
- Robust exception handling preventing application crashes.
- True multithreading demonstration explaining race conditions and locks.
- Clean separation of concerns across layered architecture.

---

### 26. LIMITATIONS
- Command-line interface only (no graphical interface).
- Local SQLite database file store (single node).

---

### 27. FUTURE ENHANCEMENTS
- Adding email notifications for overdue books.
- Implementing REST API web services for remote clients.
- Adding barcode scanner integration via terminal input streams.

---

### 28. CONCLUSION
The Smart Library Management System demonstrates a complete, working, core Java implementation. By combining Object-Oriented principles, multithreaded synchronization, custom exceptions, Collections, file streams, JDBC, and JPA Hibernate ORM, the project provides a real-world software solution.

---

### 29. REFERENCES
1. Herbert Schildt, *Java: The Complete Reference*, 11th Edition, Oracle Press, 2018.
2. Joshua Bloch, *Effective Java*, 3rd Edition, Addison-Wesley, 2018.
3. Brian Goetz et al., *Java Concurrency in Practice*, Addison-Wesley, 2006.
4. Oracle University Java Documentation & Jakarta Persistence Specification.
