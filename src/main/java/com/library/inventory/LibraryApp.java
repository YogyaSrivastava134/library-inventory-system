package com.library.inventory;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Main presentation controller providing an interactive CLI experience.
 * Leverages InputValidator for robust, exception-immune user interaction.
 */
public class LibraryApp {
    private static final Scanner scanner = new Scanner(System.in);
    private static final LibraryService libraryService = new LibraryService(new InMemoryBookRepository());

    public static void main(String[] args) {
        preloadSampleData();
        boolean running = true;
        System.out.println("=====================================================");
        System.out.println("   REFACTORED LIBRARY BOOK INVENTORY SYSTEM (v2.0)  ");
        System.out.println("=====================================================");

        while (running) {
            displayMenu();
            int choice = InputValidator.readIntegerInput(scanner, "Enter your choice (1-6): ", 1, 6);
            switch (choice) {
                case 1 -> handleAddBook();
                case 2 -> handleListAllBooks();
                case 3 -> handleSearchBooks();
                case 4 -> handleUpdateBook();
                case 5 -> handleDeleteBook();
                case 6 -> {
                    System.out.println("\nThank you for using the Refactored Library System. Goodbye!");
                    running = false;
                }
            }
        }
    }

    private static void displayMenu() {
        System.out.println("\n------------- MAIN MENU -------------");
        System.out.println("1. Add New Book (Create)");
        System.out.println("2. List All Books (Read)");
        System.out.println("3. Search Book by ISBN or Title");
        System.out.println("4. Update Book Information (Update)");
        System.out.println("5. Delete Book (Delete)");
        System.out.println("6. Exit");
        System.out.println("-------------------------------------");
    }

    private static void handleAddBook() {
        System.out.println("\n--- [CREATE] Add New Book ---");
        String isbn = InputValidator.readNonEmptyString(scanner, "Enter ISBN (e.g., 978-0134685991): ");
        if (libraryService.findByIsbn(isbn).isPresent()) {
            System.out.println("Error: A book with ISBN '" + isbn + "' already exists.");
            return;
        }
        String title = InputValidator.readNonEmptyString(scanner, "Enter Book Title: ");
        String author = InputValidator.readNonEmptyString(scanner, "Enter Author Name: ");
        int year = InputValidator.readIntegerInput(scanner, "Enter Publication Year (1450-2026): ", 1450, 2026);
        int copies = InputValidator.readIntegerInput(scanner, "Enter Number of Available Copies: ", 0, 10000);

        if (libraryService.addBook(new Book(isbn, title, author, year, copies))) {
            System.out.println("Success: Book registered in inventory successfully!");
        } else {
            System.out.println("Error: Failed to register book.");
        }
    }

    private static void handleListAllBooks() {
        System.out.println("\n--- [READ] Current Library Inventory ---");
        Collection<Book> books = libraryService.getAllBooks();
        if (books.isEmpty()) {
            System.out.println("The inventory is currently empty.");
            return;
        }
        renderTable(books);
        System.out.println("Total Distinct Titles: " + libraryService.getTotalBookCount());
    }

    private static void handleSearchBooks() {
        System.out.println("\n--- [READ] Search Book ---");
        System.out.println("1. Search by ISBN\n2. Search by Title Substring");
        int option = InputValidator.readIntegerInput(scanner, "Select search option (1-2): ", 1, 2);

        if (option == 1) {
            String isbn = InputValidator.readNonEmptyString(scanner, "Enter exact ISBN: ");
            Optional<Book> result = libraryService.findByIsbn(isbn);
            if (result.isPresent()) renderTable(List.of(result.get()));
            else System.out.println("No book found with ISBN: " + isbn);
        } else {
            String keyword = InputValidator.readNonEmptyString(scanner, "Enter Title keyword: ");
            List<Book> results = libraryService.searchByTitle(keyword);
            if (results.isEmpty()) System.out.println("No books matching keyword '" + keyword + "' found.");
            else renderTable(results);
        }
    }

    private static void handleUpdateBook() {
        System.out.println("\n--- [UPDATE] Modify Book Information ---");
        String isbn = InputValidator.readNonEmptyString(scanner, "Enter ISBN of book to update: ");
        Optional<Book> optionalBook = libraryService.findByIsbn(isbn);

        if (optionalBook.isEmpty()) {
            System.out.println("Error: Book with ISBN '" + isbn + "' does not exist.");
            return;
        }

        Book existing = optionalBook.get();
        System.out.println("\nCurrent Details:");
        renderTable(List.of(existing));

        System.out.println("\n(Press ENTER to leave a field unchanged)");
        System.out.print("Enter New Title [" + existing.getTitle() + "]: ");
        String newTitle = scanner.nextLine().strip();

        System.out.print("Enter New Author [" + existing.getAuthor() + "]: ");
        String newAuthor = scanner.nextLine().strip();

        System.out.print("Enter New Publication Year [" + existing.getPublicationYear() + "]: ");
        String yearInput = scanner.nextLine().strip();
        int newYear = yearInput.isEmpty() ? -1 : InputValidator.parseSafeInt(yearInput, existing.getPublicationYear());

        System.out.print("Enter New Available Copies [" + existing.getAvailableCopies() + "]: ");
        String copiesInput = scanner.nextLine().strip();
        int newCopies = copiesInput.isEmpty() ? -1 : InputValidator.parseSafeInt(copiesInput, existing.getAvailableCopies());

        if (libraryService.updateBook(isbn, newTitle, newAuthor, newYear, newCopies)) {
            System.out.println("Success: Book information updated successfully!");
        } else {
            System.out.println("Error: Update failed.");
        }
    }

    private static void handleDeleteBook() {
        System.out.println("\n--- [DELETE] Remove Book ---");
        String isbn = InputValidator.readNonEmptyString(scanner, "Enter ISBN of book to delete: ");
        Optional<Book> optionalBook = libraryService.findByIsbn(isbn);

        if (optionalBook.isEmpty()) {
            System.out.println("Error: Book with ISBN '" + isbn + "' not found.");
            return;
        }

        renderTable(List.of(optionalBook.get()));
        System.out.print("Are you sure you want to delete this book? (Y/N): ");
        if (scanner.nextLine().strip().equalsIgnoreCase("Y")) {
            if (libraryService.deleteBook(isbn)) System.out.println("Success: Book removed from inventory.");
            else System.out.println("Error: Failed to delete book.");
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    private static void renderTable(Collection<Book> books) {
        String format = "| %-14s | %-32s | %-22s | %-4s | %-6s |%n";
        String line = "+----------------+----------------------------------+------------------------+------+--------+";
        System.out.println(line);
        System.out.printf(format, "ISBN", "Title", "Author", "Year", "Copies");
        System.out.println(line);
        for (Book b : books) {
            String title = b.getTitle().length() > 32 ? b.getTitle().substring(0, 29) + "..." : b.getTitle();
            String author = b.getAuthor().length() > 22 ? b.getAuthor().substring(0, 19) + "..." : b.getAuthor();
            System.out.printf(format, b.getIsbn(), title, author, b.getPublicationYear(), b.getAvailableCopies());
        }
        System.out.println(line);
    }

    private static void preloadSampleData() {
        libraryService.addBook(new Book("978-0134685991", "Effective Java", "Joshua Bloch", 2018, 5));
        libraryService.addBook(new Book("978-0596009205", "Head First Design Patterns", "Eric Freeman", 2004, 3));
        libraryService.addBook(new Book("978-0132350884", "Clean Code", "Robert C. Martin", 2008, 4));
    }
}
