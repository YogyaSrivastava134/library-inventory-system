package com.library.inventory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service class managing core CRUD operations for library book inventory.
 */
public class LibraryService {
    private final Map<String, Book> inventory = new LinkedHashMap<>();

    public boolean addBook(Book book) {
        if (book == null || inventory.containsKey(book.getIsbn())) {
            return false;
        }
        inventory.put(book.getIsbn(), book);
        return true;
    }

    public Collection<Book> getAllBooks() {
        return inventory.values();
    }

    public Optional<Book> findByIsbn(String isbn) {
        if (isbn == null) return Optional.empty();
        return Optional.ofNullable(inventory.get(isbn.trim()));
    }

    public List<Book> searchByTitle(String keyword) {
        List<Book> matches = new ArrayList<>();
        if (keyword == null || keyword.trim().isEmpty()) return matches;
        String lower = keyword.trim().toLowerCase();
        for (Book book : inventory.values()) {
            if (book.getTitle().toLowerCase().contains(lower)) {
                matches.add(book);
            }
        }
        return matches;
    }

    public boolean updateBook(String isbn, String newTitle, String newAuthor, int newYear, int newCopies) {
        Optional<Book> opt = findByIsbn(isbn);
        if (opt.isEmpty()) return false;

        Book book = opt.get();
        if (newTitle != null && !newTitle.trim().isEmpty()) book.setTitle(newTitle);
        if (newAuthor != null && !newAuthor.trim().isEmpty()) book.setAuthor(newAuthor);
        if (newYear > 0) book.setPublicationYear(newYear);
        if (newCopies >= 0) book.setAvailableCopies(newCopies);
        return true;
    }

    public boolean deleteBook(String isbn) {
        if (isbn == null || !inventory.containsKey(isbn.trim())) return false;
        inventory.remove(isbn.trim());
        return true;
    }

    public int getTotalBookCount() {
        return inventory.size();
    }
}
