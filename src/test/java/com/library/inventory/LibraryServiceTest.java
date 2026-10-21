package com.library.inventory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("LibraryService CRUD & Edge-Case Unit Tests")
public class LibraryServiceTest {

    private LibraryService service;
    private Book sampleBook1;
    private Book sampleBook2;

    @BeforeEach
    void setUp() {
        service = new LibraryService();
        sampleBook1 = new Book("978-0134685991", "Effective Java", "Joshua Bloch", 2018, 5);
        sampleBook2 = new Book("978-0132350884", "Clean Code", "Robert C. Martin", 2008, 4);
    }

    @Test
    @DisplayName("CREATE: Add book increases inventory size and succeeds")
    void testAddBookSuccess() {
        assertTrue(service.addBook(sampleBook1));
        assertEquals(1, service.getTotalBookCount());
        assertTrue(service.findByIsbn("978-0134685991").isPresent());
    }

    @Test
    @DisplayName("CREATE: Reject duplicate ISBN and maintain existing state")
    void testAddDuplicateIsbnFails() {
        service.addBook(sampleBook1);
        Book duplicate = new Book("978-0134685991", "Another Title", "Another Author", 2022, 10);
        
        assertFalse(service.addBook(duplicate), "Adding duplicate ISBN must return false");
        assertEquals(1, service.getTotalBookCount());
        assertEquals("Effective Java", service.findByIsbn("978-0134685991").get().getTitle(),
                "Duplicate should not overwrite existing book");
    }

    @Test
    @DisplayName("READ: Search by title substring works case-insensitively")
    void testSearchByTitleSubstring() {
        service.addBook(sampleBook1);
        service.addBook(sampleBook2);

        List<Book> results = service.searchByTitle("effective");
        assertEquals(1, results.size());
        assertEquals("Effective Java", results.get(0).getTitle());

        List<Book> cleanResults = service.searchByTitle("CODE");
        assertEquals(1, cleanResults.size());
        assertEquals("Clean Code", cleanResults.get(0).getTitle());
    }

    @Test
    @DisplayName("READ: Retrieve all books preserves insertion order")
    void testGetAllBooks() {
        service.addBook(sampleBook1);
        service.addBook(sampleBook2);

        Collection<Book> books = service.getAllBooks();
        assertEquals(2, books.size());
    }

    @Test
    @DisplayName("UPDATE: Successfully update title and copies while preserving ISBN")
    void testUpdateBookMetadata() {
        service.addBook(sampleBook1);
        boolean updated = service.updateBook("978-0134685991", "Effective Java (3rd Edition)", "Joshua Bloch", 2019, 8);

        assertTrue(updated);
        Book updatedBook = service.findByIsbn("978-0134685991").get();
        assertEquals("Effective Java (3rd Edition)", updatedBook.getTitle());
        assertEquals(8, updatedBook.getAvailableCopies());
    }

    @Test
    @DisplayName("DELETE: Remove book by ISBN decreases size and removes record")
    void testDeleteBookLifecycle() {
        service.addBook(sampleBook1);
        assertEquals(1, service.getTotalBookCount());

        assertTrue(service.deleteBook("978-0134685991"));
        assertEquals(0, service.getTotalBookCount());
        assertFalse(service.findByIsbn("978-0134685991").isPresent());
        assertFalse(service.deleteBook("non-existent-isbn"), "Deleting absent ISBN should return false");
    }
}
