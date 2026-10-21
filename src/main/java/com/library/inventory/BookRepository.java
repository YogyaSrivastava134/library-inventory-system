package com.library.inventory;

import java.util.Collection;
import java.util.Optional;

/**
 * Repository interface defining persistence contracts for Book entities.
 * Decouples domain services from physical data storage engines (Repository Pattern).
 */
public interface BookRepository {
    boolean save(Book book);
    Optional<Book> findByIsbn(String isbn);
    Collection<Book> findAll();
    boolean deleteByIsbn(String isbn);
    int count();
}
