package com.example.backend.kafka.config;

import java.util.Arrays;
import java.util.List;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import com.example.backend.kafka.enums.KafkaTopic;

@Configuration
public class KafkaTopicConfig {
    @Bean
    public List<NewTopic> createKafkaTopcis()
    {
        return Arrays.stream(KafkaTopic.values())
        .map(topic -> TopicBuilder.name(topic.getName())
        .partitions(1).replicas(1).build()).toList();
    }
}
