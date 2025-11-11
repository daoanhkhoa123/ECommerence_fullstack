package com.example.backend.kafka.producer;

import com.example.backend.entity.ChatMessage;
import com.example.backend.kafka.dto.ChatMessageEvent;
import com.example.backend.kafka.enums.KafkaTopic;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatProducer {

    private final KafkaTemplate<String, ChatMessageEvent> kafkaTemplate;

    private ChatMessageEvent buildChatMessageEvent(ChatMessage chatMessage) {
        return new ChatMessageEvent(
            chatMessage.getAccount().getId(),
            chatMessage.getContent(),
            chatMessage.getRole(),
            chatMessage.getCreatedAt()
        );
    }

    public void sendUserMessage(ChatMessage chat) {
        ChatMessageEvent event = buildChatMessageEvent(chat);
        kafkaTemplate.send(KafkaTopic.CHAT_MESSAGE_USER.getName(), event);
    }
}
