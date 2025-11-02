package com.example.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.backend.application.AuthencationApplication;
import com.example.backend.dto.AuthencationRequest;
import com.example.backend.dto.AuthencationRespond;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
public class AuthencationController {

    private final AuthencationApplication authencationApplication;

    @PostMapping("/login")
    public ResponseEntity<AuthencationRespond> login(@Valid @RequestBody AuthencationRequest request)
    {
        AuthencationRespond respond = authencationApplication.login(request);
        return ResponseEntity.ok(respond);
    }
}
