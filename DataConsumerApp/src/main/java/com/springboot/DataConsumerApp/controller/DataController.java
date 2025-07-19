package com.springboot.DataConsumerApp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/data")
public class DataController {

    @PostMapping("/receive")
    public ResponseEntity<String> receive(@RequestBody List<String> dataChunk) {
        System.out.println("--------------------------");
        System.out.println("Received chunk of size: " + dataChunk.size());
        dataChunk.forEach(System.out::println);
        return ResponseEntity.ok("Chunk received");
    }
}

