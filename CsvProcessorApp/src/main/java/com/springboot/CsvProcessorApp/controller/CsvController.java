package com.springboot.CsvProcessorApp.controller;

import com.springboot.CsvProcessorApp.model.CsvRequest;
import com.springboot.CsvProcessorApp.service.CsvService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.nio.file.Paths;

@RestController
@RequestMapping("/file/csv")
@RequiredArgsConstructor
public class CsvController {
    private final CsvService csvService;

    @PostMapping("/upload")
    public Mono<String> process(@RequestBody CsvRequest req) {
        return csvService.processCsv(req.getFilePath(), req.getFileName())
                .then(Mono.just("Processing initiated"));
    }
}


