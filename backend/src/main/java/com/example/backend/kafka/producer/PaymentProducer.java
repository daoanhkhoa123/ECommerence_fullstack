package com.example.backend.kafka.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.backend.dto.PaymentRespond;
import com.example.backend.kafka.dto.PaymentCreateEvent;
import com.example.backend.kafka.enums.KafkaTopic;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PaymentProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendPaymentCreate(Integer actorId, PaymentRespond body) {
        kafkaTemplate.send(
            KafkaTopic.CART_PAY.getName(),
            new PaymentCreateEvent(
                body.paymentId(),
                body.orderId(),
                actorId,
                body.paymentMethod(),
                body.paymentStatus(),
                body.paidAmount(),
                body.transactionRef(),
                body.paidAt()
            )
        );
    }
}
