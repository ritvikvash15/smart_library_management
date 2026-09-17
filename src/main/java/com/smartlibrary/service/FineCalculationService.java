package com.smartlibrary.service;

import com.smartlibrary.annotation.CourseConcept;
import com.smartlibrary.model.Book;
import com.smartlibrary.util.DateUtils;

import java.time.LocalDate;

/**
 * Service for calculating overdue library fines.
 * Demonstrates Unit 1 & 2 logic and calculations.
 */
@CourseConcept(unit = 1, concept = "Methods, Operators & Business Logic")
public class FineCalculationService {

    public static double calculateFine(Book book, LocalDate dueDate, LocalDate actualReturnDate) {
        if (book == null || dueDate == null) return 0.0;
        long overdueDays = DateUtils.calculateOverdueDays(dueDate, actualReturnDate);
        if (overdueDays <= 0) return 0.0;
        return book.calculateFine((int) overdueDays);
    }
}
