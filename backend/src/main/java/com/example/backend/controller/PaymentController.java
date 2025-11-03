package com.example.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.application.PaymentApplication;
import com.example.backend.dto.PaymentRequest;
import com.example.backend.dto.PaymentRespond;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/payments/customer")
public class PaymentController {

    private final PaymentApplication paymentApplication;

    @PostMapping("/cart/pay")
    public ResponseEntity<PaymentRespond> payCart(
            @Valid @RequestBody PaymentRequest request) {

        PaymentRespond respond = paymentApplication.payCart(request);
        return ResponseEntity.ok(respond);
    }
}
