package com.library.inventory;

import java.util.Scanner;

/**
 * Reusable utility class consolidating console input parsing and validation (DRY Principle).
 * Prevents scanner buffer desynchronization and unhandled exceptions.
 */
public final class InputValidator {

    private InputValidator() {
        // Utility class: prevent instantiation
    }

    public static String readNonEmptyString(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().strip();
            if (!line.isEmpty()) {
                return line;
            }
            System.out.println("Validation Error: Input cannot be blank. Please try again.");
        }
    }

    public static int readIntegerInput(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().strip();
            try {
                int value = Integer.parseInt(line);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.printf("Validation Error: Value must be between %d and %d.%n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("Validation Error: Please enter a valid whole number.");
            }
        }
    }

    public static int parseSafeInt(String input, int fallback) {
        if (input == null || input.isBlank()) {
            return fallback;
        }
        try {
            return Integer.parseInt(input.strip());
        } catch (NumberFormatException e) {
            System.out.println("Format Warning: Invalid integer entered. Retaining default: " + fallback);
            return fallback;
        }
    }
}
