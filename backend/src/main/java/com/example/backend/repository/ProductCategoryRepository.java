package com.example.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.backend.entity.ProductCategory;
import com.example.backend.entity.ProductCategoryId;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, ProductCategoryId> {

    List<ProductCategory> findByProductId(Integer productId);
    List<ProductCategory> findByCategoryId(Integer categoryId);
    void deleteByProductIdAndCategoryId(Integer productId, Integer categoryId);
    void deleteByCategoryId(Integer categoryId);
    void deleteByProductId(Integer productId);

}
