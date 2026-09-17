package com.smartlibrary.service;

import com.smartlibrary.annotation.CourseConcept;
import com.smartlibrary.exception.*;
import com.smartlibrary.model.*;
import com.smartlibrary.repository.BookJdbcRepository;
import com.smartlibrary.repository.TransactionJdbcRepository;
import com.smartlibrary.repository.UserJdbcRepository;

import java.time.LocalDate;
import java.util.*;

/**
 * Core Service facade for Smart Library Management operations.
 * Demonstrates: Collections Framework (ArrayList, HashMap, Stack, Vector), Method Overloading,
 * Exception Handling, Thread Synchronization, Business Rules.
 */
@CourseConcept(unit = 4, concept = "Collections Framework & Overloaded Business Logic")
public class LibraryService {

    private final BookJdbcRepository bookRepository;
    private final UserJdbcRepository userRepository;
    private final TransactionJdbcRepository transactionRepository;

    // Unit 4 Collections Framework Demonstrations
    private final List<Book> cachedBooks = new ArrayList<>();                   // ArrayList
    private final Map<Integer, User> userCache = new HashMap<>();                // HashMap
    private final Stack<LibraryTransaction> recentTransactions = new Stack<>(); // Stack
    private final Vector<String> auditLogs = new Vector<>();                    // Vector

    public LibraryService() {
        this.bookRepository = new BookJdbcRepository();
        this.userRepository = new UserJdbcRepository();
        this.transactionRepository = new TransactionJdbcRepository();
        refreshCache();
    }

    public synchronized void refreshCache() {
        try {
            cachedBooks.clear();
            cachedBooks.addAll(bookRepository.findAllBooks());

            userCache.clear();
            for (User u : userRepository.findAllUsers()) {
                userCache.put(u.getId(), u);
            }

            recentTransactions.clear();
            List<LibraryTransaction> allTx = transactionRepository.findAllTransactions();
            for (int i = Math.max(0, allTx.size() - 10); i < allTx.size(); i++) {
                recentTransactions.push(allTx.get(i));
            }
            auditLogs.add("Cache refreshed at " + LocalDate.now());
        } catch (DatabaseOperationException e) {
            System.err.println("Warning: Cache initialization error: " + e.getMessage());
        }
    }

    // --- METHOD OVERLOADING DEMONSTRATIONS (UNIT 2) ---

    @CourseConcept(unit = 2, concept = "Method Overloading - Search by Keyword")
    public List<Book> searchBook(String keyword) {
        List<Book> result = new ArrayList<>();
        for (Book b : cachedBooks) {
            if (b.matchesKeyword(keyword)) {
                result.add(b);
            }
        }
        return result;
    }

    @CourseConcept(unit = 2, concept = "Method Overloading - Search by ID")
    public Optional<Book> searchBook(int id) {
        for (Book b : cachedBooks) {
            if (b.getId() == id) {
                return Optional.of(b);
            }
        }
        return Optional.empty();
    }

    @CourseConcept(unit = 2, concept = "Method Overloading - Search by Category")
    public List<Book> searchBook(BookCategory category) {
        List<Book> result = new ArrayList<>();
        for (Book b : cachedBooks) {
            if (b.getCategory() == category) {
                result.add(b);
            }
        }
        return result;
    }

    // --- USER MANAGEMENT ---

    public int registerUser(User user) throws LibraryException {
        if (user == null) {
            throw new InvalidInputException("User data cannot be null.");
        }
        int id = userRepository.saveUser(user);
        userCache.put(id, user);
        auditLogs.add("Registered user: " + user.getName() + " [ID=" + id + "]");
        return id;
    }

    public List<User> getAllUsers() throws DatabaseOperationException {
        return userRepository.findAllUsers();
    }

    // --- BOOK MANAGEMENT ---

    public int addBook(Book book) throws LibraryException {
        if (book == null) {
            throw new InvalidInputException("Book data cannot be null.");
        }
        int id = bookRepository.saveBook(book);
        cachedBooks.add(book);
        auditLogs.add("Added book: " + book.getTitle() + " [ID=" + id + "]");
        return id;
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(cachedBooks);
    }

    // --- THREAD-SAFE BOOK ISSUE OPERATION (UNIT 3 SYNCHRONIZATION) ---

    @CourseConcept(unit = 3, concept = "Synchronization & Thread Safety")
    public synchronized LibraryTransaction issueBook(int userId, int bookId) throws LibraryException {
        // 1. Verify User existence
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User ID #" + userId + " not found."));

        // 2. Verify Book existence
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book ID #" + bookId + " not found."));

        // 3. Domain Check: Reference Book cannot be checked out (instanceof test)
        if (book instanceof ReferenceBook) {
            throw new BookNotAvailableException("Reference books (e.g., '" + book.getTitle() + "') are for reading room use only!");
        }

        // 4. Verify Book availability
        if (!book.isBorrowable()) {
            throw new BookNotAvailableException("Book '" + book.getTitle() + "' is currently unavailable or out of stock.");
        }

        // 5. Verify User borrowing limit
        List<LibraryTransaction> activeTx = transactionRepository.findActiveByUserId(userId);
        if (activeTx.size() >= user.getMaxBorrowLimit()) {
            throw new MaxLimitExceededException("User " + user.getName() + " has reached maximum borrow limit of "
                    + user.getMaxBorrowLimit() + " books.");
        }

        // 6. Create Transaction
        LocalDate issueDate = LocalDate.now();
        LocalDate dueDate = issueDate.plusDays(book.getMaxBorrowDurationDays());
        LibraryTransaction tx = new LibraryTransaction(0, userId, bookId, issueDate, dueDate, TransactionType.ISSUE);

        int txId = transactionRepository.saveTransaction(tx);
        tx.setTransactionId(txId);

        // 7. Update Book Quantity & Status
        if (book instanceof PhysicalBook pb) {
            int newQty = pb.getQuantity() - 1;
            pb.setQuantity(newQty);
            bookRepository.updateBookQuantity(bookId, newQty);
        }

        recentTransactions.push(tx);
        auditLogs.add("Issued book #" + bookId + " to User #" + userId + " (Tx #" + txId + ")");
        refreshCache();

        return tx;
    }

    // --- BOOK RETURN OPERATION ---

    public synchronized double returnBook(int transactionId) throws LibraryException {
        LibraryTransaction tx = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new LibraryException("Transaction #" + transactionId + " not found."));

        if (tx.isReturned()) {
            throw new LibraryException("Transaction #" + transactionId + " has already been returned!");
        }

        Book book = bookRepository.findById(tx.getBookId())
                .orElseThrow(() -> new BookNotFoundException("Book #" + tx.getBookId() + " not found."));

        LocalDate returnDate = LocalDate.now();
        double fine = FineCalculationService.calculateFine(book, tx.getDueDate(), returnDate);

        // Update Transaction
        transactionRepository.updateReturnStatus(transactionId, returnDate, fine);

        // Update Book Stock
        if (book instanceof PhysicalBook pb) {
            int newQty = pb.getQuantity() + 1;
            pb.setQuantity(newQty);
            bookRepository.updateBookQuantity(book.getId(), newQty);
        }

        auditLogs.add("Returned book #" + book.getId() + " for Tx #" + transactionId + " with fine ₹" + fine);
        refreshCache();

        return fine;
    }

    // --- ANONYMOUS CLASS DEMONSTRATION FOR SORTING (UNIT 2) ---

    @CourseConcept(unit = 2, concept = "Anonymous Inner Class - Sorting Books")
    public List<Book> getBooksSortedByTitle() {
        List<Book> sortedList = new ArrayList<>(cachedBooks);
        // Using Anonymous Inner Class Comparator
        Collections.sort(sortedList, new Comparator<Book>() {
            @Override
            public int compare(Book b1, Book b2) {
                return b1.getTitle().compareToIgnoreCase(b2.getTitle());
            }
        });
        return sortedList;
    }

    public List<LibraryTransaction> getUserTransactions(int userId) throws DatabaseOperationException {
        return transactionRepository.findActiveByUserId(userId);
    }

    public List<LibraryTransaction> getAllTransactions() throws DatabaseOperationException {
        return transactionRepository.findAllTransactions();
    }

    public Vector<String> getAuditLogs() {
        return auditLogs;
    }
}
