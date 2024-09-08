package com.client.tutorial;

import com.client.tutorial.dtos.CatImage;
import com.client.tutorial.services.CatRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CatFetcherRunner implements CommandLineRunner {

    @Autowired
    private CatRestClient catRestClient;

    @Override
    public void run(String... args) {

        System.out.println("fetchCatImagesWithHeader");
//        catRestClient.fetchCatImagesWithHeader();

        // Fetch and display cat images
        List<CatImage> catImages = catRestClient.fetchCatImages();
        catImages.forEach(catImage ->
                System.out.println(catImage)
        );

        for (CatImage catImage : catImages) {
            catRestClient.downloadAndSaveImage(catImage);
        }
    }
}