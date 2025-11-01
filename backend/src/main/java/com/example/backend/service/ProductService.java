package com.example.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.example.backend.dto.ProductVendorRequest;
import com.example.backend.dto.VendorProductRequest;
import com.example.backend.entity.Product;
import com.example.backend.repository.ProductRepository;
import com.example.backend.repository.VendorProductRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final VendorProductRepository vendorProductRepository;
    private final ProductRepository productRepository;

    public Product createProduct(VendorProductRequest request)
    {
        Product product = new Product();
        product.setName(request.name());
        product.setDescription(request.description());
        product.setBrand(request.brand());
        product.setImageUrl(request.imageUrl());
        return productRepository.save(product);
    }

    public Product findProductByVendorProductId(Integer id)
    {
        return vendorProductRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND, "Vendor product not found with ID: " + id
        )).getProduct();
    } 

    public Product updateProduct(Product product, ProductVendorRequest request)
    {
        product.setName(request.name());
        product.setDescription(request.description());
        product.setBrand(request.brand());
        product.setImageUrl(request.imageUrl());

        return productRepository.save(product);
    }

    
}
