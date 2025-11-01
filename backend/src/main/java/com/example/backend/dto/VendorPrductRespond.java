package com.example.backend.dto;

import java.util.List;

public record VendorPrductRespond(
    VendorProductRequest request,
    
    List<CategoryRequestRespond> categories,
    
    Integer vendorId,
    Integer productId,
    Integer vendorProductId
) {}
