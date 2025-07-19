package com.springboot.CsvProcessorApp.controller;

import com.springboot.CsvProcessorApp.service.CsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Paths;

@RestController
@RequestMapping("/file/csv")
public class CsvController {

    @Autowired
    private CsvService csvService;

    @PostMapping("/upload")
    public ResponseEntity<String> upload(
            @RequestParam String filePath,
            @RequestParam String fileName) {

        String fullPath = Paths.get(filePath, fileName).toString();
        csvService.processCsv(fullPath);
        return ResponseEntity.accepted().body("Upload accepted. File is being processed.");
    }
}

