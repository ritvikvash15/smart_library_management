package com.smartlibrary.thread;

import com.smartlibrary.model.Book;
import com.smartlibrary.model.BookCategory;
import com.smartlibrary.model.PhysicalBook;
import com.smartlibrary.service.LibraryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Unit Test for Multithreading Synchronization")
public class SynchronizationTest {

    @Test
    @DisplayName("Test Synchronization Prevents Double Checkout")
    public void testConcurrentCheckoutSynchronization() throws Exception {
        LibraryService service = new LibraryService();

        // Create 1 copy of test physical book
        Book book = new PhysicalBook(0, "SYNC-101", "Sync Book Test", "Author S", BookCategory.COMPUTER_SCIENCE, "R1", 1);
        int bookId = service.addBook(book);

        ConcurrentCheckoutTask task1 = new ConcurrentCheckoutTask(service, 1, bookId, "T1");
        ConcurrentCheckoutTask task2 = new ConcurrentCheckoutTask(service, 2, bookId, "T2");

        Thread t1 = new Thread(task1);
        Thread t2 = new Thread(task2);

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        // Exactly one thread must succeed and one must fail!
        assertTrue((task1.isSuccess() && !task2.isSuccess()) || (!task1.isSuccess() && task2.isSuccess()),
                "Synchronization failed! Both threads either succeeded or failed together.");
    }
}
