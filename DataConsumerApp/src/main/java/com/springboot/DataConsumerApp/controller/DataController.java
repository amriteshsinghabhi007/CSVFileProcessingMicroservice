package com.springboot.DataConsumerApp.controller;

import com.springboot.DataConsumerApp.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/data")
public class DataController {

    @PostMapping("/receive")
    public ResponseEntity<Void> receive(@RequestBody List<User> users) {
        System.out.println("--------------------------");
        System.out.println("Received chunk of size: " + users.size());
        users.forEach(System.out::println);
        return ResponseEntity.ok().build();
    }
}

