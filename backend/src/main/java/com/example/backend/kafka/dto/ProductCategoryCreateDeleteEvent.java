package com.example.backend.kafka.dto;

import java.util.List;

public record ProductCategoryCreateDeleteEvent(   
    Integer actorId,
    Integer productId,
    List<Integer> categoryIds
    ) {}
