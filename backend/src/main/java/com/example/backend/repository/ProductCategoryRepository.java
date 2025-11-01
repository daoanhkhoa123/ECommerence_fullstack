package com.example.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.backend.entity.Category;
import com.example.backend.entity.ProductCategory;
import com.example.backend.entity.ProductCategoryId;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, ProductCategoryId> {

    List<ProductCategory> findByProductId(Integer productId);
    List<ProductCategory> findByCategoryId(Integer categoryId);

    @Query("SELECT pc.category FROM ProductCategory pc JOIN pc.category WHERE pc.product.id = :productId")
    List<Category> findCategoriesByProductId(@Param("productId") Integer productId);

    void deleteByProductIdAndCategoryId(Integer productId, Integer categoryId);
    void deleteByProductIdAndCategoryIdIn(Integer productId, List<Integer> categoryIds);
    void deleteByCategoryId(Integer categoryId);
    void deleteByProductId(Integer productId);

}
