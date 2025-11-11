package com.example.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.backend.application.ChatMessageApplication;
import com.example.backend.dto.ChatMessageRequest;
import com.example.backend.dto.ChatMessageRespond;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatMessageApplication chatApplication;

    @PostMapping
    public ResponseEntity<ChatMessageRespond> receiveMessage(@Valid @RequestBody ChatMessageRequest request) {
        ChatMessageRespond respond = chatApplication.sendUserMessage(request);
        return ResponseEntity.ok(respond);
    }

    @GetMapping
    public ResponseEntity<List<ChatMessageRespond>> findAll() {
        List<ChatMessageRespond> responds  = chatApplication.findAllMessage();
        return ResponseEntity.ok(responds);
    }
        
}   
