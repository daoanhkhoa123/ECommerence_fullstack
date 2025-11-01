package com.example.backend.dto;

import java.util.List;

public record ProductCategoryPatchRequest(
    List<Integer> addCategoryIds,
    List<Integer> remCategoryIds
) {}
