package com.smartlibrary.util;

import com.smartlibrary.annotation.CourseConcept;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Utility class for Date calculation and formatting.
 * Demonstrates: Java 8+ Date Time API, Static Methods.
 */
@CourseConcept(unit = 1, concept = "Date Processing & Static Utilities")
public class DateUtils {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static String formatDate(LocalDate date) {
        if (date == null) return "N/A";
        return date.format(FORMATTER);
    }

    public static LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty() || dateStr.equals("N/A")) return null;
        return LocalDate.parse(dateStr, FORMATTER);
    }

    public static long calculateOverdueDays(LocalDate dueDate, LocalDate returnDate) {
        LocalDate actualReturn = returnDate != null ? returnDate : LocalDate.now();
        if (actualReturn.isAfter(dueDate)) {
            return ChronoUnit.DAYS.between(dueDate, actualReturn);
        }
        return 0;
    }
}
