package com.client.tutorial.services;

import com.client.tutorial.dtos.CatImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

@Service
public class CatRestClient {

    private final RestTemplate restTemplate;
    private static final String BASE_URL = "https://api.thecatapi.com/v1/images/search";

    @Autowired
    public CatRestClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Fetch a list of cat images
    public List<CatImage> fetchCatImages() {
        CatImage[] catImagesArray = restTemplate.getForObject(BASE_URL, CatImage[].class);
        return Arrays.asList(catImagesArray);
    }


    public void fetchCatImagesWithHeader() {
        try {
            // Make a request to the Cat API
            ResponseEntity<String> response = restTemplate.exchange(BASE_URL, HttpMethod.GET, null, String.class);

            // Print the response body
            System.out.println("Response Body: " + response.getBody());

            // Print response headers
            HttpHeaders headers = response.getHeaders();
            System.out.println("Response Headers: " + headers);

            // Check for rate limit headers
            String rateLimit = headers.getFirst("X-RateLimit-Limit");
            String rateLimitRemaining = headers.getFirst("X-RateLimit-Remaining");
            String rateLimitReset = headers.getFirst("X-RateLimit-Reset");

            System.out.println("Rate Limit: " + rateLimit);
            System.out.println("Rate Limit Remaining: " + rateLimitRemaining);
            System.out.println("Rate Limit Reset: " + rateLimitReset);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void downloadAndSaveImage(CatImage catImage) {
        String imageUrl = catImage.getUrl();
        String fileName = "cat_" + catImage.getId() + ".jpg"; // Change extension based on image format
        try (InputStream in = new URL(imageUrl).openStream();
             FileOutputStream fos = new FileOutputStream(new File(fileName))) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) != -1) {
                fos.write(buffer, 0, length);
            }
            System.out.println("Image saved as " + fileName);
        } catch (IOException e) {
            System.err.println("Failed to download or save image: " + e.getMessage());
        }
    }
}
