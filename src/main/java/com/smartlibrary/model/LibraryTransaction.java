package com.smartlibrary.model;

import com.smartlibrary.annotation.CourseConcept;
import com.smartlibrary.interface_contract.Exportable;

import java.time.LocalDate;

/**
 * Model class for Library Transactions (Issue / Return / Fines).
 * Demonstrates: Encapsulation, Date operations, Static Nested Class.
 */
@CourseConcept(unit = 2, concept = "Classes, Encapsulation & Static Nested Class")
public class LibraryTransaction implements Exportable {
    private int transactionId;
    private int userId;
    private int bookId;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private double fineAmount;
    private TransactionType transactionType;
    private boolean returned;

    public LibraryTransaction() {}

    public LibraryTransaction(int transactionId, int userId, int bookId, LocalDate issueDate, LocalDate dueDate, TransactionType transactionType) {
        this.transactionId = transactionId;
        this.userId = userId;
        this.bookId = bookId;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.transactionType = transactionType;
        this.fineAmount = 0.0;
        this.returned = false;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public double getFineAmount() {
        return fineAmount;
    }

    public void setFineAmount(double fineAmount) {
        this.fineAmount = fineAmount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public boolean isReturned() {
        return returned;
    }

    public void setReturned(boolean returned) {
        this.returned = returned;
    }

    @Override
    public String toCsvRow() {
        return String.format("%d,%d,%d,%s,%s,%s,%.2f,%s,%b",
                transactionId, userId, bookId,
                issueDate != null ? issueDate.toString() : "N/A",
                dueDate != null ? dueDate.toString() : "N/A",
                returnDate != null ? returnDate.toString() : "N/A",
                fineAmount, transactionType.name(), returned);
    }

    @Override
    public String toFormattedText() {
        return String.format("TxID: #%d | UserID: %d | BookID: %d | Issued: %s | Due: %s | Returned: %s | Fine: ₹%.2f | Status: %s",
                transactionId, userId, bookId,
                issueDate, dueDate,
                returned ? returnDate.toString() : "PENDING",
                fineAmount, returned ? "COMPLETED" : "ACTIVE");
    }

    @Override
    public String toString() {
        return toFormattedText();
    }

    /**
     * Static Nested Class demonstrating Unit 2 Nested & Inner Classes.
     */
    @CourseConcept(unit = 2, concept = "Static Nested Class")
    public static class TransactionSummary {
        private final int totalTransactions;
        private final int activeIssues;
        private final double totalFinesCollected;

        public TransactionSummary(int totalTransactions, int activeIssues, double totalFinesCollected) {
            this.totalTransactions = totalTransactions;
            this.activeIssues = activeIssues;
            this.totalFinesCollected = totalFinesCollected;
        }

        public int getTotalTransactions() { return totalTransactions; }
        public int getActiveIssues() { return activeIssues; }
        public double getTotalFinesCollected() { return totalFinesCollected; }

        @Override
        public String toString() {
            return String.format("Summary -> Total Transactions: %d | Active Issues: %d | Total Fines: ₹%.2f",
                    totalTransactions, activeIssues, totalFinesCollected);
        }
    }
}
