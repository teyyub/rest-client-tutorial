package com.client.tutorial.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.client.tutorial.dtos.Book;
import java.util.Arrays;
import java.util.List;

@Service
public class BookRestClient {

    private final RestTemplate restTemplate;
    private static final String BASE_URL = "http://localhost:8080/api/books";

    @Autowired
    public BookRestClient(RestTemplate restTemplate) {

        this.restTemplate = restTemplate;
    }

    public List<Book> fetchBooks() {
        // Making a GET request to the API and fetching books as an array
        Book[] booksArray = restTemplate.getForObject(BASE_URL, Book[].class);
        // Converting array to list and returning
        return Arrays.asList(booksArray);
    }

    // Add a new book (POST)
    public Book addBook(Book book) {
        return restTemplate.postForObject(BASE_URL, book, Book.class);
    }

    // Update an existing book (PUT)
    public void updateBook(int id, Book updatedBook) {
        String url = BASE_URL + "/" + id;
        restTemplate.put(url, updatedBook);
    }

    // Delete a book by id (DELETE)
    public String deleteBook(int id) {
        String url = BASE_URL + "/" + id;
        restTemplate.delete(url);
        return "Book deleted successfully.";
    }
}
