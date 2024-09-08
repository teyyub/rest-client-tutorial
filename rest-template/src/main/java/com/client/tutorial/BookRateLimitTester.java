package com.client.tutorial;

import com.client.tutorial.dtos.Book;
import com.client.tutorial.dtos.CatImage;
import com.client.tutorial.services.BookRestClient;
import com.client.tutorial.services.CatRestClient;
import io.github.resilience4j.ratelimiter.RateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static io.github.resilience4j.ratelimiter.RateLimiter.decorateCheckedSupplier;
import static io.github.resilience4j.ratelimiter.RateLimiter.waitForPermission;

//@Component
public class BookRateLimitTester implements CommandLineRunner {

    @Autowired
    private BookRestClient bookRestClient;

    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    @Autowired
    private RateLimiter rateLimiter;


    void test(){
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

        Runnable task = () -> {
            try {
                Supplier<List<Book>> decoratedSupplier = decorateCheckedSupplier(rateLimiter, bookRestClient::fetchBooks).unchecked();
                List<Book> books = decoratedSupplier.get();
                System.out.println("Request made at: " + System.currentTimeMillis() + " with " + books.size() + " books.");

            } catch (Exception e) {
                System.out.println("Error making request: " + e.getMessage());
            }
        };

        // Schedule to run the task every 100 milliseconds (10 times per second)
        executorService.scheduleAtFixedRate(task, 0, 200, TimeUnit.MILLISECONDS);
    }

    @Override
    public void run(String... args) {
         test();
//        int requestCount = 0;
//        boolean rateLimitExceeded = false;
//
//        while (!rateLimitExceeded) {
//            try {
//                // Make a request to the Cat API
//                List<Book> books = bookRestClient.fetchBooks();
//
//
//                requestCount++;
//                System.out.println("Request count: " + requestCount);
//
//                // Pause between requests to avoid too rapid firing
//                Thread.sleep(1000); // 1 second delay
//
//            } catch (HttpClientErrorException.TooManyRequests e) {
//                System.out.println("Rate limit exceeded. Status code: " + e.getStatusCode());
//                rateLimitExceeded = true;
//            } catch (Exception e) {
//                e.printStackTrace();
//                break;
//            }
//        }
    }
}
