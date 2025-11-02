package com.example.backend.kafka.producer;

import org.springframework.stereotype.Service;

import com.example.backend.kafka.dto.AuditEvent;
import com.example.backend.kafka.enums.CRUDType;
import com.example.backend.service.JwtService;

import lombok.RequiredArgsConstructor;

// not functionable, just to call

@RequiredArgsConstructor
@Service
public class AuditProducer {
    private final JwtService jwtService;

    public AuditEvent buildAuditEvent(CRUDType evenType)
    {
        Integer accId = jwtService.getCurrentUserId();
        return new AuditEvent(evenType, accId);
    }
}
