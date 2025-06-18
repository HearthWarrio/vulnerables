package com.hearhwarrio.plugin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

@RestController
@RequestMapping("/plugin")
public class PluginController {

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Vulnerable RFI endpoint:
     * Downloads remote script and executes it without validation.
     */
    @GetMapping("/load")
    public ResponseEntity<String> loadPlugin(@RequestParam("url") String url) {
        try {
            // Fetch remote script content
            String code = restTemplate.getForObject(url, String.class);

            // Execute with Nashorn → RFI vulnerability
            ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
            engine.eval(code);

            return ResponseEntity.ok("Plugin loaded and executed from: " + url);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Error loading/executing plugin: " + e.getMessage());
        }
    }
}
