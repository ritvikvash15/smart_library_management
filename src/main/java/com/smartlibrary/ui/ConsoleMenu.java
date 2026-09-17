package com.smartlibrary.ui;

import com.smartlibrary.annotation.CourseConcept;
import com.smartlibrary.exception.LibraryException;
import com.smartlibrary.io.BackupManager;
import com.smartlibrary.io.ReportExporter;
import com.smartlibrary.model.*;
import com.smartlibrary.reflection.ReflectionInspector;
import com.smartlibrary.repository.JpaLibraryRepository;
import com.smartlibrary.service.LibraryService;
import com.smartlibrary.thread.MultithreadingDemoManager;
import com.smartlibrary.util.InputValidator;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Command Line Interface Menu loop for Smart Library Management System.
 * Demonstrates Unit 1 Control Flow, User Input/Output, Scanner, Exception Prevention.
 */
@CourseConcept(unit = 1, concept = "CLI Menu Loop, Control Flow & I/O")
public class ConsoleMenu {

    private final LibraryService libraryService;
    private final ReportExporter reportExporter;
    private final MultithreadingDemoManager threadDemoManager;
    private final JpaLibraryRepository jpaRepository;
    private final Scanner scanner;

    public ConsoleMenu() {
        this.libraryService = LibraryServiceHolder.getInstance();
        this.reportExporter = new ReportExporter(libraryService);
        this.threadDemoManager = new MultithreadingDemoManager(libraryService);
        this.jpaRepository = new JpaLibraryRepository();
        this.scanner = new Scanner(System.in);
    }

    /**
     * Singleton Lazy Holder pattern for LibraryService
     */
    private static class LibraryServiceHolder {
        private static final LibraryService INSTANCE = new LibraryService();
        public static LibraryService getInstance() {
            return INSTANCE;
        }
    }

    public void start() {
        boolean running = true;
        System.out.println("\n🚀 Smart Library Management System initialized successfully.");

        while (running) {
            displayMenu();
            int choice = InputValidator.readInt(scanner, "Enter your choice (1-14): ");

            switch (choice) {
                case 1 -> registerStudent();
                case 2 -> addBook();
                case 3 -> viewAllBooks();
                case 4 -> searchBook();
                case 5 -> issueBook();
                case 6 -> returnBook();
                case 7 -> viewStudentTransactions();
                case 8 -> viewOverdueBooks();
                case 9 -> generateLibraryReport();
                case 10 -> exportData();
                case 11 -> threadDemoManager.runConcurrencyDemo();
                case 12 -> inspectClassMetadata();
                case 13 -> jpaRepository.runJpaDemonstration();
                case 14 -> {
                    System.out.println("\n========================================");
                    System.out.println("   Thank you for using Smart Library!   ");
                    System.out.println("========================================\n");
                    running = false;
                }
                default -> System.out.println("❌ Invalid option! Please select a number between 1 and 14.");
            }
        }
    }

    private void displayMenu() {
        System.out.println("\n========================================");
        System.out.println("       SMART LIBRARY MANAGEMENT         ");
        System.out.println("========================================");
        System.out.println("1.  Register Student");
        System.out.println("2.  Add Book");
        System.out.println("3.  View All Books");
        System.out.println("4.  Search Book");
        System.out.println("5.  Issue Book");
        System.out.println("6.  Return Book");
        System.out.println("7.  View Student Transactions");
        System.out.println("8.  View Overdue & Active Transactions");
        System.out.println("9.  Generate Library Report (.txt)");
        System.out.println("10. Export Data (.csv, .txt & DB Backup)");
        System.out.println("11. Run Concurrent Transaction Demo (Multithreading)");
        System.out.println("12. Inspect Class Metadata (Reflection Demo)");
        System.out.println("13. Run JPA / Hibernate Operations Demo");
        System.out.println("14. Exit");
        System.out.println("========================================");
    }

    private void registerStudent() {
        System.out.println("\n--- 📝 REGISTER NEW STUDENT ---");
        try {
            String name = InputValidator.readNonEmptyString(scanner, "Enter Name: ");
            String email = InputValidator.readEmail(scanner, "Enter Email: ");
            String phone = InputValidator.readPhone(scanner, "Enter 10-Digit Phone: ");
            String regNo = InputValidator.readNonEmptyString(scanner, "Enter Registration Number (e.g., 21BCE0003): ");
            String dept = InputValidator.readNonEmptyString(scanner, "Enter Department: ");

            Student student = new Student(0, name, email, phone, regNo, dept);
            int id = libraryService.registerUser(student);
            System.out.println("✅ Student registered successfully with System User ID #" + id);
        } catch (LibraryException e) {
            System.out.println("❌ Registration Failed: " + e.getMessage());
        }
    }

    private void addBook() {
        System.out.println("\n--- 📚 ADD NEW BOOK ---");
        String title = InputValidator.readNonEmptyString(scanner, "Enter Title: ");
        String author = InputValidator.readNonEmptyString(scanner, "Enter Author: ");
        String isbn = InputValidator.readNonEmptyString(scanner, "Enter ISBN: ");

        System.out.println("Categories: 1. CS, 2. MATH, 3. PHYSICS, 4. FICTION, 5. NON_FICTION, 6. REFERENCE");
        int catChoice = InputValidator.readInt(scanner, "Select Category (1-6): ");
        BookCategory category = switch (catChoice) {
            case 2 -> BookCategory.MATHEMATICS;
            case 3 -> BookCategory.PHYSICS;
            case 4 -> BookCategory.FICTION;
            case 5 -> BookCategory.NON_FICTION;
            case 6 -> BookCategory.REFERENCE;
            default -> BookCategory.COMPUTER_SCIENCE;
        };

        System.out.println("Book Type: 1. Physical Borrowable Book, 2. Reference Reading-Room Book");
        int typeChoice = InputValidator.readInt(scanner, "Select Type (1-2): ");

        try {
            Book book;
            if (typeChoice == 2) {
                String roomSec = InputValidator.readNonEmptyString(scanner, "Enter Reading Room Section: ");
                book = new ReferenceBook(0, isbn, title, author, category, roomSec);
            } else {
                String shelf = InputValidator.readNonEmptyString(scanner, "Enter Shelf Location (e.g., Rack CS-05): ");
                int qty = InputValidator.readInt(scanner, "Enter Quantity: ");
                book = new PhysicalBook(0, isbn, title, author, category, shelf, qty);
            }

            int id = libraryService.addBook(book);
            System.out.println("✅ Book added successfully with Book ID #" + id);
        } catch (LibraryException e) {
            System.out.println("❌ Add Book Failed: " + e.getMessage());
        }
    }

    private void viewAllBooks() {
        System.out.println("\n--- 📖 VIEW ALL BOOKS ---");
        System.out.println("1. Standard View");
        System.out.println("2. Sorted by Title (Demonstrates Anonymous Inner Class Comparator)");
        int choice = InputValidator.readInt(scanner, "Select View Option (1-2): ");

        List<Book> books = (choice == 2) ? libraryService.getBooksSortedByTitle() : libraryService.getAllBooks();

        if (books.isEmpty()) {
            System.out.println("No books found in library catalog.");
            return;
        }

        for (Book b : books) {
            System.out.println(b.toFormattedText());
        }
    }

    private void searchBook() {
        System.out.println("\n--- 🔍 SEARCH BOOKS (DEMONSTRATES METHOD OVERLOADING) ---");
        System.out.println("1. Search by Keyword (Title / Author / ISBN)");
        System.out.println("2. Search by Book ID");
        System.out.println("3. Search by Category");
        int choice = InputValidator.readInt(scanner, "Select Search Method (1-3): ");

        switch (choice) {
            case 1 -> {
                String kw = InputValidator.readNonEmptyString(scanner, "Enter Search Keyword: ");
                List<Book> results = libraryService.searchBook(kw);
                displaySearchResults(results);
            }
            case 2 -> {
                int id = InputValidator.readInt(scanner, "Enter Book ID: ");
                Optional<Book> result = libraryService.searchBook(id);
                result.ifPresentOrElse(
                        b -> System.out.println("✅ Found: " + b.toFormattedText()),
                        () -> System.out.println("❌ No book found with ID #" + id)
                );
            }
            case 3 -> {
                System.out.println("Categories: 1. CS, 2. MATH, 3. PHYSICS, 4. FICTION, 5. NON_FICTION, 6. REFERENCE");
                int catChoice = InputValidator.readInt(scanner, "Select Category (1-6): ");
                BookCategory category = switch (catChoice) {
                    case 2 -> BookCategory.MATHEMATICS;
                    case 3 -> BookCategory.PHYSICS;
                    case 4 -> BookCategory.FICTION;
                    case 5 -> BookCategory.NON_FICTION;
                    case 6 -> BookCategory.REFERENCE;
                    default -> BookCategory.COMPUTER_SCIENCE;
                };
                List<Book> results = libraryService.searchBook(category);
                displaySearchResults(results);
            }
            default -> System.out.println("Invalid search choice.");
        }
    }

    private void displaySearchResults(List<Book> results) {
        if (results.isEmpty()) {
            System.out.println("❌ No matching books found.");
        } else {
            System.out.println("Found " + results.size() + " matching book(s):");
            for (Book b : results) {
                System.out.println("   -> " + b.toFormattedText());
            }
        }
    }

    private void issueBook() {
        System.out.println("\n--- 📤 ISSUE BOOK ---");
        int userId = InputValidator.readInt(scanner, "Enter User ID (e.g. 1): ");
        int bookId = InputValidator.readInt(scanner, "Enter Book ID (e.g. 1): ");

        try {
            LibraryTransaction tx = libraryService.issueBook(userId, bookId);
            System.out.println("✅ Book Issued Successfully!");
            System.out.println("   Transaction ID: #" + tx.getTransactionId());
            System.out.println("   Due Date: " + tx.getDueDate());
        } catch (LibraryException e) {
            System.out.println("❌ Issue Failed: " + e.getMessage());
        }
    }

    private void returnBook() {
        System.out.println("\n--- 📥 RETURN BOOK ---");
        int txId = InputValidator.readInt(scanner, "Enter Transaction ID to return: ");

        try {
            double fine = libraryService.returnBook(txId);
            System.out.println("✅ Book Returned Successfully!");
            if (fine > 0) {
                System.out.printf("⚠️ Overdue Fine Assessed: ₹%.2f\n", fine);
            } else {
                System.out.println("🎉 No overdue fine! Returned on time.");
            }
        } catch (LibraryException e) {
            System.out.println("❌ Return Failed: " + e.getMessage());
        }
    }

    private void viewStudentTransactions() {
        System.out.println("\n--- 📋 VIEW STUDENT TRANSACTIONS ---");
        int userId = InputValidator.readInt(scanner, "Enter User ID: ");

        try {
            List<LibraryTransaction> txs = libraryService.getUserTransactions(userId);
            if (txs.isEmpty()) {
                System.out.println("No active issue transactions found for User ID #" + userId);
            } else {
                System.out.println("Active Transactions for User #" + userId + ":");
                for (LibraryTransaction t : txs) {
                    System.out.println("   -> " + t.toFormattedText());
                }
            }
        } catch (LibraryException e) {
            System.out.println("❌ Error loading transactions: " + e.getMessage());
        }
    }

    private void viewOverdueBooks() {
        System.out.println("\n--- ⏰ ALL TRANSACTIONS ---");
        try {
            List<LibraryTransaction> txs = libraryService.getAllTransactions();
            if (txs.isEmpty()) {
                System.out.println("No transactions found.");
            } else {
                for (LibraryTransaction t : txs) {
                    System.out.println(t.toFormattedText());
                }
            }
        } catch (LibraryException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    private void generateLibraryReport() {
        System.out.println("\n--- 📄 GENERATE LIBRARY REPORT ---");
        String reportText = reportExporter.generateSummaryReport();
        System.out.println(reportText);
    }

    private void exportData() {
        System.out.println("\n--- 💾 EXPORT DATA (CHARACTER & BYTE I/O STREAMS) ---");
        try {
            File txtFile = reportExporter.exportSummaryReportTxt();
            File booksCsv = reportExporter.exportBooksCsv();
            File txCsv = reportExporter.exportTransactionsCsv();
            boolean backupSuccess = BackupManager.createDatabaseBackup("data/library.db", "reports/library_backup.db");

            System.out.println("✅ Data Exported Successfully:");
            System.out.println("   -> Summary Report Text File: " + txtFile.getAbsolutePath());
            System.out.println("   -> Books CSV File: " + booksCsv.getAbsolutePath());
            System.out.println("   -> Transactions CSV File: " + txCsv.getAbsolutePath());
            if (backupSuccess) {
                System.out.println("   -> Binary Database Backup File: reports/library_backup.db");
            }
        } catch (Exception e) {
            System.out.println("❌ Export Failed: " + e.getMessage());
        }
    }

    private void inspectClassMetadata() {
        System.out.println("\n--- 🔬 JAVA REFLECTION API INSPECTOR ---");
        System.out.println("1. Inspect Book (com.smartlibrary.model.Book)");
        System.out.println("2. Inspect Student (com.smartlibrary.model.Student)");
        System.out.println("3. Inspect LibraryTransaction (com.smartlibrary.model.LibraryTransaction)");
        System.out.println("4. Custom Class Name");
        int choice = InputValidator.readInt(scanner, "Select Class (1-4): ");

        String className = switch (choice) {
            case 2 -> "com.smartlibrary.model.Student";
            case 3 -> "com.smartlibrary.model.LibraryTransaction";
            case 4 -> InputValidator.readNonEmptyString(scanner, "Enter fully qualified class name: ");
            default -> "com.smartlibrary.model.Book";
        };

        ReflectionInspector.inspectClass(className);
    }
}
