package com.client.tutorial;

import com.client.tutorial.dtos.CatImage;
import com.client.tutorial.services.CatRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

//@Component
public class RateLimitTester implements CommandLineRunner {

    @Autowired
    private CatRestClient catRestClient;

    @Autowired
    private RestTemplate restTemplate;

    private static final String BASE_URL = "https://api.thecatapi.com/v1/images/search";

    @Override
    public void run(String... args) {
        int requestCount = 0;
        boolean rateLimitExceeded = false;

        while (!rateLimitExceeded) {
            try {
                // Make a request to the Cat API
                CatImage[] catImagesArray = restTemplate.getForObject(BASE_URL, CatImage[].class);
                List<CatImage> catImages = Arrays.asList(catImagesArray);

                // Print out the result for demonstration
                System.out.println("Fetched " + catImages.size() + " cat images.");

                requestCount++;
                System.out.println("Request count: " + requestCount);

                // Pause between requests to avoid too rapid firing
                Thread.sleep(1000); // 1 second delay

            } catch (HttpClientErrorException.TooManyRequests e) {
                System.out.println("Rate limit exceeded. Status code: " + e.getStatusCode());
                rateLimitExceeded = true;
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
