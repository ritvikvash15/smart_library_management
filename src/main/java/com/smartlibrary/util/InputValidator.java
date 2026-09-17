package com.smartlibrary.util;

import com.smartlibrary.annotation.CourseConcept;
import com.smartlibrary.exception.InvalidInputException;

import java.util.Scanner;

/**
 * Utility class for robust command line input reading and validation.
 * Demonstrates: Exception Handling, Static Methods, Input Validation, Regular Expressions.
 */
@CourseConcept(unit = 3, concept = "Input Validation & Exception Prevention")
public class InputValidator {

    public static int readInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid input! Please enter a valid integer numeric value.");
            }
        }
    }

    public static double readDouble(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid input! Please enter a valid decimal number.");
            }
        }
    }

    public static String readNonEmptyString(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("❌ Input cannot be empty! Please try again.");
        }
    }

    public static String readEmail(Scanner scanner, String prompt) throws InvalidInputException {
        System.out.print(prompt);
        String email = scanner.nextLine().trim();
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new InvalidInputException("Invalid email format: '" + email + "'");
        }
        return email;
    }

    public static String readPhone(Scanner scanner, String prompt) throws InvalidInputException {
        System.out.print(prompt);
        String phone = scanner.nextLine().trim();
        if (!phone.matches("^[0-9]{10}$")) {
            throw new InvalidInputException("Phone number must be exactly 10 digits.");
        }
        return phone;
    }
}
