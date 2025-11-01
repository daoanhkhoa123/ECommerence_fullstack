package com.example.backend.dto;

import java.util.List;

public record ProductCategoryPatchRespond(
    Integer productId,
    List<CategoryRequestRespond> categoryDtos
) {}
