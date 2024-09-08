package com.service.rest.controller;


import com.service.rest.dtos.Book;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final RateLimiter rateLimiter;
    private final List<Book> books = new ArrayList<>();

    @Autowired
    public BookController(RateLimiterRegistry rateLimiterRegistry) {
        this.rateLimiter = rateLimiterRegistry.rateLimiter("bookServiceRateLimiter");
        books.add(new Book(1, "The Great Gatsby", "F. Scott Fitzgerald"));
        books.add(new Book(2, "1984", "George Orwell"));
        books.add(new Book(3, "To Kill a Mockingbird", "Harper Lee"));
    }

    @GetMapping
    public ResponseEntity<?> getBooks() {
        return executeWithRateLimiter(() -> ResponseEntity.ok(books));
    }

    @PostMapping
    public Book addBook(@RequestBody Book book) {
        books.add(book);
        return book;
    }

    @PutMapping("/{id}")
    public Book updateBook(@PathVariable int id, @RequestBody Book updatedBook) {
        Optional<Book> existingBook = books.stream().filter(b -> b.getId() == id).findFirst();
        existingBook.ifPresent(book -> {
            book.setTitle(updatedBook.getTitle());
            book.setAuthor(updatedBook.getAuthor());
        });
        return existingBook.orElse(null);
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable int id) {
        boolean removed = books.removeIf(book -> book.getId() == id);
        return removed ? "Book deleted successfully." : "Book not found.";
    }

//    private <T> ResponseEntity<T> executeWithRateLimiter(Callable<ResponseEntity<T>> callable) {
//        try {
//            return RateLimiter.decorateCallable(rateLimiter, callable).call();
//        } catch (Exception e) {
//            return ResponseEntity.status(429).body(null); // Rate limit exceeded
//        }
//    }

    private <T> ResponseEntity<T> executeWithRateLimiter(Callable<ResponseEntity<T>> callable) {
        try {
            return RateLimiter.decorateCallable(rateLimiter, callable).call();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(null);
        }
    }
}


