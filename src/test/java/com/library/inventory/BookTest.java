package com.library.inventory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Book Entity Unit Tests")
public class BookTest {

    @Test
    @DisplayName("Should successfully instantiate Book with valid parameters")
    void testValidBookCreation() {
        Book book = new Book("978-0134685991", "Effective Java", "Joshua Bloch", 2018, 5);
        assertNotNull(book);
        assertEquals("978-0134685991", book.getIsbn());
        assertEquals("Effective Java", book.getTitle());
        assertEquals("Joshua Bloch", book.getAuthor());
        assertEquals(2018, book.getPublicationYear());
        assertEquals(5, book.getAvailableCopies());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when ISBN is null or whitespace")
    void testInvalidIsbnThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Book(null, "Title", "Author", 2020, 1));
        assertThrows(IllegalArgumentException.class, () -> new Book("   ", "Title", "Author", 2020, 1));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when available copies are negative")
    void testNegativeCopiesThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> 
                new Book("978-0134685991", "Title", "Author", 2020, -1));
    }
}
