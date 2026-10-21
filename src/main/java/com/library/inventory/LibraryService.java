package com.library.inventory;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Refactored domain service orchestrating library business logic.
 * Enforces loose coupling by depending on the BookRepository interface (DIP).
 */
public class LibraryService {
    private final BookRepository repository;

    public LibraryService(BookRepository repository) {
        this.repository = Objects.requireNonNull(repository, "BookRepository cannot be null.");
    }

    public LibraryService() {
        this(new InMemoryBookRepository());
    }

    public boolean addBook(Book book) {
        return repository.save(book);
    }

    public Collection<Book> getAllBooks() {
        return repository.findAll();
    }

    public Optional<Book> findByIsbn(String isbn) {
        return repository.findByIsbn(isbn);
    }

    public List<Book> searchByTitle(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return List.of();
        }
        String normalized = keyword.strip().toLowerCase();
        return repository.findAll().stream()
                .filter(b -> b.getTitle().toLowerCase().contains(normalized))
                .toList();
    }

    public boolean updateBook(String isbn, String newTitle, String newAuthor, int newYear, int newCopies) {
        Optional<Book> opt = findByIsbn(isbn);
        if (opt.isEmpty()) return false;

        Book book = opt.get();
        if (newTitle != null && !newTitle.isBlank()) book.setTitle(newTitle);
        if (newAuthor != null && !newAuthor.isBlank()) book.setAuthor(newAuthor);
        if (newYear > 0) book.setPublicationYear(newYear);
        if (newCopies >= 0) book.setAvailableCopies(newCopies);
        return true;
    }

    public boolean deleteBook(String isbn) {
        return repository.deleteByIsbn(isbn);
    }

    public int getTotalBookCount() {
        return repository.count();
    }
}
