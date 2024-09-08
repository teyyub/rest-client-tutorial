package com.client.tutorial;

import com.client.tutorial.dtos.Book;
import com.client.tutorial.services.BookRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

//@Component
public class BookFetcherRunner implements CommandLineRunner {

    @Autowired
    private BookRestClient bookRestClient;

    @Override
    public void run(String... args) {
        // Fetching books using the RestTemplate client
        List<Book> books = bookRestClient.fetchBooks();
        // Printing the list of fetched books
        books.forEach(book -> System.out.println("Fetched Book: " + book.getTitle() + " by " + book.getAuthor()));

        // Add a new book
        Book newBook = new Book(4, "Brave New World", "Aldous Huxley");
        System.out.println("\nAdding a new book: " + newBook.getTitle());
        Book addedBook = bookRestClient.addBook(newBook);
        System.out.println("Added Book: " + addedBook.getTitle() + " by " + addedBook.getAuthor());

        // Update an existing book
        Book updatedBook = new Book(4, "Brave New World Revisited", "Aldous Huxley");
        System.out.println("\nUpdating the book: " + updatedBook.getTitle());
        bookRestClient.updateBook(4, updatedBook);
        System.out.println("Updated Book to: " + updatedBook.getTitle());

        // Delete a book by ID
        System.out.println("\nDeleting book with ID 4");
        String response = bookRestClient.deleteBook(4);
        System.out.println(response);
    }
}
