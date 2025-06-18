package com.hearthwarrio.viewer.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/view")
public class ViewController {

    @Value("${viewer.base-dir}")
    private String baseDir;

    /**
     * Уязвимая конечная точка LFI:
     * Читает файл из baseDir + filename без очистки пути.
     */
    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> viewFile(@RequestParam("file") String filename) {
        try {
            Path filePath = Paths.get(baseDir).resolve(filename);
            String content = Files.readString(filePath);  // No normalization or check
            return ResponseEntity.ok("<pre>" + content + "</pre>");
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Error reading file: " + e.getMessage());
        }
    }
}
