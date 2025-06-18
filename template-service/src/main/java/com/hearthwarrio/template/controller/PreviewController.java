package com.hearthwarrio.template.controller;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@RestController
public class PreviewController {
    /**
     * GET /preview
     * Возвращает простую HTML-форму для ввода шаблона FreeMarker.
     */
    @GetMapping("/preview")
    public ResponseEntity<String> getForm() {
        String html = """
            <!DOCTYPE html>
            <html><body>
              <h1>Template Preview</h1>
              <form method="post" action="/preview">
                <textarea name="template" rows="10" cols="80"></textarea><br/>
                <button type="submit">Render</button>
              </form>
            </body></html>
            """;
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(html);
    }

    /**
     * POST /preview
     * Уязвимый SSTI: напрямую обрабатывает предоставленный пользователем шаблон FreeMarker.
     */
    @PostMapping(path = "/preview", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> renderTemplate(@RequestParam("template") String tpl) {
        try {
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.DEBUG_HANDLER);
            // No sandboxing or sanitization → SSTI vulnerability
            Template template = new Template("userTpl", new StringReader(tpl), cfg);

            // Example data model; attacker can exploit via template expressions
            Map<String,Object> model = new HashMap<>();
            model.put("user", "DemoUser");

            StringWriter out = new StringWriter();
            template.process(model, out);

            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_HTML)
                    .body(out.toString());
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Template processing error: " + e.getMessage());
        }
    }
}
