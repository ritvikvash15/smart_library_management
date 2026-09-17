package com.smartlibrary.thread;

import com.smartlibrary.annotation.CourseConcept;
import com.smartlibrary.model.Book;
import com.smartlibrary.model.BookCategory;
import com.smartlibrary.model.PhysicalBook;
import com.smartlibrary.service.LibraryService;

import java.util.List;

/**
 * Manager for demonstrating Multithreading, Synchronization, and Thread Safety.
 * Demonstrates Unit 3: Thread Lifecycle, Start(), Join(), Synchronization, Critical Section.
 */
@CourseConcept(unit = 3, concept = "Multithreading & Synchronization Demonstration")
public class MultithreadingDemoManager {

    private final LibraryService libraryService;

    public MultithreadingDemoManager(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    public void runConcurrencyDemo() {
        System.out.println("\n====================================================");
        System.out.println("   CONCURRENT TRANSACTION DEMO (MULTITHREADING)     ");
        System.out.println("====================================================");
        System.out.println("Scenario: Creating a high-demand single-copy book and spawning");
        System.out.println("2 concurrent threads (Student-1 & Student-2) trying to check");
        System.out.println("it out at the exact same millisecond.\n");

        try {
            // 1. Create a single-copy test physical book
            Book limitedBook = new PhysicalBook(0, "978-0000000001", "Limited Edition Java Handbook", "James Gosling", BookCategory.COMPUTER_SCIENCE, "Special Rack 01", 1);
            int bookId = libraryService.addBook(limitedBook);

            System.out.println("📚 Created Test Book: ID #" + bookId + " | Title: '" + limitedBook.getTitle() + "' | Quantity: 1");

            // 2. Instantiate tasks for 2 threads targeting the same book
            ConcurrentCheckoutTask task1 = new ConcurrentCheckoutTask(libraryService, 1, bookId, "Thread-Student-1");
            ConcurrentCheckoutTask task2 = new ConcurrentCheckoutTask(libraryService, 2, bookId, "Thread-Student-2");

            Thread thread1 = new Thread(task1, "Student-Thread-1");
            Thread thread2 = new Thread(task2, "Student-Thread-2");

            System.out.println("\n🧵 Initial Thread States:");
            System.out.println("   Thread 1 State: " + thread1.getState() + " (NEW)");
            System.out.println("   Thread 2 State: " + thread2.getState() + " (NEW)");

            System.out.println("\n🚀 Starting Concurrent Threads simultaneously...");
            thread1.start();
            thread2.start();

            // Demonstrate join() to wait for both threads to finish
            thread1.join();
            thread2.join();

            System.out.println("\n🏁 Final Thread States after completion:");
            System.out.println("   Thread 1 State: " + thread1.getState() + " (TERMINATED)");
            System.out.println("   Thread 2 State: " + thread2.getState() + " (TERMINATED)");

            System.out.println("\n====================================================");
            System.out.println("                DEMONSTRATION RESULT                ");
            System.out.println("====================================================");
            System.out.println("Thread 1 Result: " + task1.getResultMessage());
            System.out.println("Thread 2 Result: " + task2.getResultMessage());

            // Verify final book stock from repository
            List<Book> books = libraryService.getAllBooks();
            for (Book b : books) {
                if (b.getId() == bookId && b instanceof PhysicalBook pb) {
                    System.out.println("Remaining Quantity in Inventory: " + pb.getQuantity() + " copy (Status: " + pb.getStatus() + ")");
                }
            }

            System.out.println("\n💡 Technical Concurrency Summary:");
            System.out.println("   Without 'synchronized' method in LibraryService, both threads");
            System.out.println("   would read quantity=1 simultaneously and both create issue records,");
            System.out.println("   causing a Race Condition (quantity becomes -1).");
            System.out.println("   With Java synchronization, Thread-1 enters the critical section first,");
            System.out.println("   decrements quantity to 0. When Thread-2 enters, it catches");
            System.out.println("   BookNotAvailableException safely!");
            System.out.println("====================================================\n");

        } catch (Exception e) {
            System.err.println("Error running Multithreading Demo: " + e.getMessage());
        }
    }
}
