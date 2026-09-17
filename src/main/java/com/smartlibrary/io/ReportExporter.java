package com.smartlibrary.io;

import com.smartlibrary.annotation.CourseConcept;
import com.smartlibrary.exception.LibraryException;
import com.smartlibrary.interface_contract.Reportable;
import com.smartlibrary.model.Book;
import com.smartlibrary.model.LibraryTransaction;
import com.smartlibrary.model.User;
import com.smartlibrary.service.LibraryService;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Report Exporter class for generating .txt and .csv reports.
 * Demonstrates Unit 4: Character Streams (FileWriter, PrintWriter, BufferedReader, FileReader).
 */
@CourseConcept(unit = 4, concept = "Character-Oriented I/O Streams & Report Export")
public class ReportExporter implements Reportable {

    private final LibraryService libraryService;
    private static final String REPORTS_DIR = "reports";

    public ReportExporter(LibraryService libraryService) {
        this.libraryService = libraryService;
        File dir = new File(REPORTS_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @Override
    public String generateSummaryReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("====================================================\n");
        sb.append("         SMART LIBRARY MANAGEMENT SYSTEM            \n");
        sb.append("                 SUMMARY REPORT                     \n");
        sb.append("  Generated At: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\n");
        sb.append("====================================================\n\n");

        List<Book> books = libraryService.getAllBooks();
        sb.append("--- BOOKS SUMMARY (Total: ").append(books.size()).append(") ---\n");
        for (Book b : books) {
            sb.append(b.toFormattedText()).append("\n");
        }

        try {
            List<User> users = libraryService.getAllUsers();
            sb.append("\n--- REGISTERED USERS (Total: ").append(users.size()).append(") ---\n");
            for (User u : users) {
                sb.append(u.toFormattedText()).append("\n");
            }

            List<LibraryTransaction> txs = libraryService.getAllTransactions();
            sb.append("\n--- RECENT TRANSACTIONS (Total: ").append(txs.size()).append(") ---\n");
            for (LibraryTransaction t : txs) {
                sb.append(t.toFormattedText()).append("\n");
            }
        } catch (LibraryException e) {
            sb.append("Error retrieving report data: ").append(e.getMessage()).append("\n");
        }

        sb.append("\n====================================================\n");
        return sb.toString();
    }

    public File exportSummaryReportTxt() throws IOException {
        File file = new File(REPORTS_DIR, "library_report.txt");
        // Demonstrates Character Stream PrintWriter & BufferedWriter
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file)))) {
            writer.write(generateSummaryReport());
        }
        return file;
    }

    public File exportBooksCsv() throws IOException {
        File file = new File(REPORTS_DIR, "books.csv");
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file)))) {
            writer.println("ID,ISBN,Title,Author,Category,Status,BookType");
            for (Book b : libraryService.getAllBooks()) {
                writer.println(b.toCsvRow());
            }
        }
        return file;
    }

    public File exportTransactionsCsv() throws IOException, LibraryException {
        File file = new File(REPORTS_DIR, "transactions.csv");
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file)))) {
            writer.println("TxID,UserID,BookID,IssueDate,DueDate,ReturnDate,Fine,Type,Returned");
            for (LibraryTransaction t : libraryService.getAllTransactions()) {
                writer.println(t.toCsvRow());
            }
        }
        return file;
    }

    /**
     * Demonstrates Reading from an Exported File using FileReader and BufferedReader.
     */
    @CourseConcept(unit = 4, concept = "Character-Oriented Stream Input Reading")
    public String readReportFile(File file) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }
}
