package com.springboot.CsvProcessorApp.service;

import com.springboot.CsvProcessorApp.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CsvService {
    private final RestTemplate restTemplate;

    public Mono<Void> processCsv(String filePath, String fileName) {
        Path path = Paths.get(filePath, fileName);
        return Mono.fromCallable(() -> Files.newBufferedReader(path))
                .flatMapMany(reader ->
                        Flux.fromStream(reader.lines())
                                .skip(1) // skip header
                                .map(this::parseUser)
                                .buffer(5) // chunk size
                                .flatMap(chunk ->
                                        Mono.fromRunnable(() -> sendChunkWithRetry(chunk))
                                                .subscribeOn(Schedulers.boundedElastic())
                                )
                                .doFinally(sig -> {
                                    try {
                                        reader.close();
                                    } catch (IOException ignored) {
                                    }
                                })
                )
                .then().log();
    }

    private User parseUser(String line) {
        String[] cols = line.split(",");
        return new User(String.join(cols[0]), cols[1], cols[2]);
    }

    @Retryable(value = {Exception.class}, maxAttempts = 3,
            backoff = @Backoff(delay = 2000, multiplier = 2))
    public void sendChunkWithRetry(List<User> chunk) {
        restTemplate.postForEntity("http://localhost:8081/api/data/receive", chunk, Void.class);
    }
}

