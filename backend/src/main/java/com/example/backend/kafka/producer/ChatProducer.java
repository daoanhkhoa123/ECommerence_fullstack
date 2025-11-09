package com.example.backend.kafka.producer;

import com.example.backend.kafka.dto.ChatMessageEvent;
import com.example.backend.kafka.enums.KafkaTopic;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ChatProducer {

    private final KafkaTemplate<String, ChatMessageEvent> kafkaTemplate;

    public String produce(ChatMessageEvent event) {
        String correlationId = UUID.randomUUID().toString();
        kafkaTemplate.send(KafkaTopic.CHAT_MESSAGE.getName(), correlationId, event);
        return correlationId;
    }
}
