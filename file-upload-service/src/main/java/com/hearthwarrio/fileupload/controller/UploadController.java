package com.hearthwarrio.fileupload.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/upload")
public class UploadController {

    @Value("${file.upload-dir}")
    private String uploadDir;

    /**
     * Уязвимая загрузка файла: отсутствие проверки или очистки имени файла или его содержимого.
     */
    @PostMapping
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        Path uploadPath = Paths.get(uploadDir);
        if (Files.notExists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        // Saves file under original name without checks → File Upload vulnerability
        Path destination = uploadPath.resolve(filename);
        file.transferTo(destination);
        return ResponseEntity.ok("File uploaded: " + filename);
    }

    /**
     * Обслуживать загруженный файл без какой-либо очистки пути → Обход пути LFI возможен, если конечные точки раскрыты.
     */
    @GetMapping("/files/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) throws IOException {
        Path file = Paths.get(uploadDir).resolve(filename).normalize();
        Resource resource = new UrlResource(file.toUri());
        if (!resource.exists() || !resource.isReadable()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
