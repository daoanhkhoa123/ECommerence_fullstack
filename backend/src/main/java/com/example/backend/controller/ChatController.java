package com.example.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.backend.application.ChatApplication;
import com.example.backend.dto.ChatMessageDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatApplication chatApplication;

    @PostMapping
    public ResponseEntity<ChatMessageDTO> receiveMessage(@Valid @RequestBody ChatMessageDTO message) {
        ChatMessageDTO respond = chatApplication.sendMessage(message);
        return ResponseEntity.ok(respond);
    }
}
