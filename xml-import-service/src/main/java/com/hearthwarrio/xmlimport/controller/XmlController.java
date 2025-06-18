package com.hearthwarrio.xmlimport.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

@RestController
@RequestMapping("/import")
public class XmlController {
    /**
     * Уязвимая конечная точка, демонстрирующая XXE.
     * Принимает произвольный XML и анализирует с включенными внешними сущностями.
     */
    @PostMapping(consumes = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> importXml(@RequestBody String xml) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(xml));
            Document doc = builder.parse(is);

            String content = doc.getDocumentElement().getTextContent();
            return ResponseEntity.ok(content);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("XML parse error: " + e.getMessage());
        }
    }
}
