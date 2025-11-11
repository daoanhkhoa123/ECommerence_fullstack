package com.example.backend.application;

import com.example.backend.dto.ChatMessageRequest;
import com.example.backend.dto.ChatMessageRespond;
import com.example.backend.entity.ChatMessage;
import com.example.backend.kafka.dto.ChatMessageEvent;
import com.example.backend.kafka.producer.ChatProducer;
import com.example.backend.security.JwtService;
import com.example.backend.service.ChatMessageService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMessageApplication {

    private final ChatMessageService chatMessageService;
    private final ChatProducer chatProducer;
    private final JwtService jwtService;
    private static final Logger log = LoggerFactory.getLogger(ChatMessageApplication.class);

    private ChatMessageRespond buildRespond(ChatMessage chatMessage) {
        return new ChatMessageRespond(
            chatMessage.getContent(),
            chatMessage.getRole(),
            chatMessage.getCreatedAt()
        );
    }

    public ChatMessageRespond sendUserMessage(ChatMessageRequest request) {
        Integer userId = jwtService.getCurrentUserId();

        // Save user message
        ChatMessage chatMessage = chatMessageService.saveUserMessage(request, userId);
        log.info(
            "User message saved | messageId={} | accountId={} | role={} | message='{}' | timestamp={}",
            chatMessage.getId(),
            chatMessage.getAccount().getId(),
            chatMessage.getRole(),
            chatMessage.getContent(),
            chatMessage.getCreatedAt()
        );

        // Send to Kafka
        chatProducer.sendUserMessage(chatMessage);
        log.info(
            "User message sent to Kafka | messageId={} | topic={}",
            chatMessage.getId(),
            "chat.message.user.v1"
        );

        return buildRespond(chatMessage);
    }

    public void recieveSystemMesageFromEvent(ChatMessageEvent event) {
        ChatMessage chatMessage = chatMessageService.saveSystemMessage(event);
        log.info(
            "System message received and saved | messageId={} | accountId={} | role={} | message='{}' | timestamp={}",
            chatMessage.getId(),
            chatMessage.getAccount().getId(),
            chatMessage.getRole(),
            chatMessage.getContent(),
            chatMessage.getCreatedAt()
        );
    }

    public List<ChatMessageRespond> findAllMessage()
    {
        Integer userId = jwtService.getCurrentUserId();
        return chatMessageService.findAllByAccountId(userId).stream().map(
            this::buildRespond).toList();
    }
}
