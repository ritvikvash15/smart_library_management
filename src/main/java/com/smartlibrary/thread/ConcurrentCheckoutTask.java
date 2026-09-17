package com.smartlibrary.thread;

import com.smartlibrary.annotation.CourseConcept;
import com.smartlibrary.exception.LibraryException;
import com.smartlibrary.model.LibraryTransaction;
import com.smartlibrary.service.LibraryService;

/**
 * Task implementing Runnable interface to simulate concurrent user checkout.
 * Demonstrates Unit 3: Thread Creation (implements Runnable) and Execution.
 */
@CourseConcept(unit = 3, concept = "Thread Creation & Runnable Interface")
public class ConcurrentCheckoutTask implements Runnable {

    private final LibraryService libraryService;
    private final int userId;
    private final int bookId;
    private final String threadName;
    private boolean success;
    private String resultMessage;

    public ConcurrentCheckoutTask(LibraryService libraryService, int userId, int bookId, String threadName) {
        this.libraryService = libraryService;
        this.userId = userId;
        this.bookId = bookId;
        this.threadName = threadName;
        this.success = false;
    }

    @Override
    public void run() {
        System.out.println("⚡ [" + threadName + "] Attempting to issue Book #" + bookId + " for Student #" + userId + "...");
        try {
            // Artificial delay to heighten thread race condition potential
            Thread.sleep(100);

            // Calls synchronized issueBook method on shared LibraryService instance
            LibraryTransaction tx = libraryService.issueBook(userId, bookId);
            this.success = true;
            this.resultMessage = "SUCCESS: Transaction #" + tx.getTransactionId() + " created for " + threadName;
            System.out.println("✅ [" + threadName + "] Successfully issued Book #" + bookId + "! Tx #" + tx.getTransactionId());

        } catch (LibraryException e) {
            this.success = false;
            this.resultMessage = "FAILED: " + e.getMessage();
            System.out.println("❌ [" + threadName + "] Failed to issue Book #" + bookId + ": " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            this.resultMessage = "INTERRUPTED: Thread was interrupted.";
            System.err.println("⚠️ [" + threadName + "] Interrupted during execution.");
        }
    }

    public boolean isSuccess() {
        return success;
    }

    public String getResultMessage() {
        return resultMessage;
    }
}
