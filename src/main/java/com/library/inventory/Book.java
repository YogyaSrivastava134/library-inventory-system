package com.library.inventory;

/**
 * Domain entity representing a Book in the library inventory.
 */
public class Book {
    private final String isbn;
    private String title;
    private String author;
    private int publicationYear;
    private int availableCopies;

    public Book(String isbn, String title, String author, int publicationYear, int availableCopies) {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN cannot be null or empty.");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty.");
        }
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Author cannot be null or empty.");
        }
        if (availableCopies < 0) {
            throw new IllegalArgumentException("Available copies cannot be negative.");
        }

        this.isbn = isbn.trim();
        this.title = title.trim();
        this.author = author.trim();
        this.publicationYear = publicationYear;
        this.availableCopies = availableCopies;
    }

    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getPublicationYear() { return publicationYear; }
    public int getAvailableCopies() { return availableCopies; }

    public void setTitle(String title) {
        if (title != null && !title.trim().isEmpty()) {
            this.title = title.trim();
        }
    }

    public void setAuthor(String author) {
        if (author != null && !author.trim().isEmpty()) {
            this.author = author.trim();
        }
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public void setAvailableCopies(int availableCopies) {
        if (availableCopies >= 0) {
            this.availableCopies = availableCopies;
        }
    }

    @Override
    public String toString() {
        return String.format("ISBN: %-15s | Title: %-30s | Author: %-20s | Year: %4d | Copies: %d",
                isbn, title, author, publicationYear, availableCopies);
    }
}
