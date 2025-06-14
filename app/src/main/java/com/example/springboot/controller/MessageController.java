package com.example.springboot.controller;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@SpringBootApplication
@RequestMapping
public class MessageController {
    @CrossOrigin(origins = {"http://localhost:5173"})
    @GetMapping("/api/message")
    public String getMessage() {
        return "こんにちは、Vueから来ましたね！";
    }
}
