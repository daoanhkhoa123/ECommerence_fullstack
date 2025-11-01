package com.example.backend.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.backend.entity.Category;
import com.example.backend.entity.Product;
import com.example.backend.entity.ProductCategory;
import com.example.backend.repository.CategoryRepository;
import com.example.backend.repository.ProductCategoryRepository;
import com.example.backend.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProductCategoryService {
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public List<ProductCategory> addProductCategory(Integer productId, List<Integer> categoryIds)
    {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Product not found: " + productId
            ));

        List<ProductCategory> pcs = categoryIds.stream().map(
            catId -> {
                Category cat = categoryRepository.findById(catId)
                    .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Category not found: " + catId));

                ProductCategory pc = new ProductCategory();
                pc.setCategory(cat);
                pc.setProduct(product);
                return pc;
            }).toList();

        return productCategoryRepository.saveAll(pcs);
    }

    public void removeProductCategory(Integer productId, List<Integer> categoryIds)
    {
        if (categoryIds.isEmpty())
            return;
        productCategoryRepository.deleteByProductIdAndCategoryIdIn(productId, categoryIds);
    }
}
