package com.springboot.CsvProcessorApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvService {

    private static final int CHUNK_SIZE = 5;

    @Autowired
    private RestTemplate restTemplate;

    @Async
    public void processCsv(String fullPath) {
        try (BufferedReader br = Files.newBufferedReader(Paths.get(fullPath))) {
            List<String> chunk = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                chunk.add(line);
                if (chunk.size() == CHUNK_SIZE) {
                    sendTOClient(new ArrayList<>(chunk));
                    chunk.clear();
                }
            }
            if (!chunk.isEmpty()) {
                sendTOClient(chunk);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Retryable(
            value = Exception.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 4000))
    public void sendTOClient(List<String> chunk) {
        restTemplate.postForEntity(
                "http://localhost:8081/api/data/receive",
                chunk,
                String.class
        );
    }
}

