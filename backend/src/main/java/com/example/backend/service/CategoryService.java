package com.example.backend.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.backend.dto.CategoryRequestRespond;
import com.example.backend.entity.Category;
import com.example.backend.repository.CategoryRepository;
import com.example.backend.repository.ProductCategoryRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductCategoryRepository productCategoryRepository;


    public Category findCategoryById(Integer id)
    {
        return categoryRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
         "Category not found with id: " + id));        
    }

    
    public List<Category> findAllByProductId(Integer productId)
    {
        return productCategoryRepository.findCategoriesByProductId(productId);
    }


    public Category createCategory(CategoryRequestRespond request)
    {
        if (categoryRepository.existsByName(request.name()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Category already exists with name: " + request.name());

        Category category = new Category();
        category.setName(request.name());
        category.setDescription(request.description());
        
        return categoryRepository.save(category);   
    }

    public Category updateCategory(Integer categoryId, CategoryRequestRespond request) {
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        // Check if new name conflicts with existing category (excluding current category)
        if (!category.getName().equals(request.name()) && 
            categoryRepository.existsByName(request.name())) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT, 
                "Category already exists with name: " + request.name()
            );
        }

        category.setName(request.name());
        category.setDescription(request.description());
        
        return categoryRepository.save(category);
    }

    @Transactional
    public void deleteCategorById(Integer categoryId) {
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        // First delete all product category relationships
        productCategoryRepository.deleteByCategoryId(categoryId);
        
        // Then delete the category itself
        categoryRepository.delete(category);
    }    



    
}
