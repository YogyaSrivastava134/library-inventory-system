package com.library.inventory;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Thread-safe in-memory repository implementation using ConcurrentHashMap.
 * Guarantees lock-free read operations and deterministic O(1) mutations.
 */
public class InMemoryBookRepository implements BookRepository {
    private final Map<String, Book> storage = new ConcurrentHashMap<>();

    @Override
    public boolean save(Book book) {
        if (book == null || storage.containsKey(book.getIsbn())) {
            return false;
        }
        storage.put(book.getIsbn(), book);
        return true;
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        if (isbn == null) return Optional.empty();
        return Optional.ofNullable(storage.get(isbn.strip()));
    }

    @Override
    public Collection<Book> findAll() {
        return Collections.unmodifiableCollection(storage.values());
    }

    @Override
    public boolean deleteByIsbn(String isbn) {
        if (isbn == null) return false;
        return storage.remove(isbn.strip()) != null;
    }

    @Override
    public int count() {
        return storage.size();
    }
}
