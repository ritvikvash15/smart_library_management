package com.smartlibrary;

import com.smartlibrary.annotation.CourseConcept;
import com.smartlibrary.ui.ConsoleMenu;
import com.smartlibrary.util.DatabaseManager;
import com.smartlibrary.util.JpaUtil;

/**
 * Main Application Entry Point for Smart Library Management System.
 */
@CourseConcept(unit = 1, concept = "Main Application Entry Point")
public class Main {

    public static void main(String[] args) {
        printBanner();

        // Initialize Singleton Database Connection and schema
        DatabaseManager.getInstance();

        // Launch Console Menu Loop
        ConsoleMenu menu = new ConsoleMenu();
        menu.start();

        // Cleanup resources upon exit
        JpaUtil.close();
    }

    private static void printBanner() {
        System.out.println("================================================================");
        System.out.println("     SMART LIBRARY MANAGEMENT SYSTEM - CORE JAVA CLI APP       ");
        System.out.println("  Tech Stack: Java 17 | Maven | SQLite | JDBC | JPA/Hibernate   ");
        System.out.println("================================================================");
    }
}
