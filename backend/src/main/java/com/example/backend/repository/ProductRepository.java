package com.example.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.backend.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    
    @Query("""
    SELECT pc.product 
    FROM ProductCategory pc 
    WHERE pc.category.id = :categoryId
    """)
    List<Product> findProductsByCategoryId(@Param("categoryId") Integer categoryId);

    Optional<Product> findById(Integer id);
}
