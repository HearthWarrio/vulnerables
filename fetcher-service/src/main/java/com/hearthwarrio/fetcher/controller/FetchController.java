package com.hearthwarrio.fetcher.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/fetch")
public class FetchController {
    private final RestTemplate restTemplate = new RestTemplate();

    /**
     *
     * Уязвимая конечная точка SSRF:
     * Принимает любой URL и возвращает извлеченный контент без ограничений.
     */
    @GetMapping
    public ResponseEntity<String> fetchUrl(@RequestParam("url") String url) {
        try {
            // No validation of 'url' → SSRF vulnerability
            String body = restTemplate.getForObject(url, String.class);
            return ResponseEntity.ok(body);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Error fetching URL: " + e.getMessage());
        }
    }
}
