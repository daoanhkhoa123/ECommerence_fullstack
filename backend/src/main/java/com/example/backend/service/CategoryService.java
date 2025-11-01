package com.example.backend.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.backend.dto.CategoryRequestRespond;
import com.example.backend.dto.ProductCategoryPatchRequest;
import com.example.backend.dto.ProductCategoryPatchRespond;
import com.example.backend.entity.Category;
import com.example.backend.entity.Product;
import com.example.backend.entity.ProductCategory;
import com.example.backend.repository.CategoryRepository;
import com.example.backend.repository.ProductCategoryRepository;
import com.example.backend.repository.ProductRepository;

import jakarta.transaction.Transactional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductRepository productRepository;

    public CategoryService(CategoryRepository categoryRepository,
        ProductCategoryRepository productCategoryRepository,
        ProductRepository productRepository)
    {
        this.categoryRepository = categoryRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.productRepository = productRepository;
    }

    public CategoryRequestRespond findCategoryById(Integer id)
    {
        Category category = categoryRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
         "Category not found with id: " + id));
        
        return new CategoryRequestRespond(category.getName(), 
        category.getDescription(), category.getId());
    }

    public CategoryRequestRespond createCategory(CategoryRequestRespond request)
    {
        if (categoryRepository.existsByName(request.name()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Category already exists with name: " + request.name());

        Category category = new Category();
        category.setName(request.name());
        category.setDescription(request.description());
        categoryRepository.save(category);   

        return new CategoryRequestRespond(category.getName(), category.getDescription(),
         category.getId());
    }

    public List<ProductCategory> addProductCategory(Integer productId, List<Integer> categoryIds)
    {
        Product product = productRepository.findById(productId).get();

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

    private void removeProductCategory(Integer productId, List<Integer> categoryIds)
    {
        if (categoryIds.isEmpty())
            return;

        for (Integer id : categoryIds) {
            productCategoryRepository.deleteByProductIdAndCategoryId(productId, id);    
        }
    }

    public List<CategoryRequestRespond> findAllByProduct(Integer productId)
    {
        return productCategoryRepository.findByProductId(productId)
        .stream().map(pc->{
            Category cat = pc.getCategory();
            return new CategoryRequestRespond(cat.getName(), cat.getDescription(), cat.getId());
        }).toList();
    }

    @Transactional
    public ProductCategoryPatchRespond patchProductCategory(Integer productId, ProductCategoryPatchRequest request)
    {
        if ((request.addCategoryIds() == null || request.addCategoryIds().isEmpty()) &&
            (request.remCategoryIds() == null || request.remCategoryIds().isEmpty())) 
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "At least one of addCategoryIds or remCategoryIds must be provided"
            );

        // Check intersection
        if (request.addCategoryIds() != null && request.remCategoryIds() != null &&
            request.addCategoryIds().stream().anyMatch(request.remCategoryIds()::contains)) 
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "addCategoryIds and remCategoryIds must not overlap"
            );

        // Check product existence
        if (!productRepository.existsById(productId)) 
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Product not found: " + productId
            );

        addProductCategory(productId, request.addCategoryIds());
        removeProductCategory(productId, request.remCategoryIds());
        
        List<CategoryRequestRespond> catdots = findAllByProduct(productId);
        return new ProductCategoryPatchRespond(productId, catdots);
    }

    public CategoryRequestRespond updateCategory(Integer categoryId, CategoryRequestRespond request) {
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
        category = categoryRepository.save(category);

        return new CategoryRequestRespond(
            category.getName(),
            category.getDescription(),
            category.getId()
        );
    }

    @Transactional
    public void deleteCategory(Integer categoryId) {
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        // First delete all product category relationships
        productCategoryRepository.deleteByCategoryId(categoryId);
        
        // Then delete the category itself
        categoryRepository.delete(category);
    }    
}
