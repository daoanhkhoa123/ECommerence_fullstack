package com.example.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.backend.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    
    @Query("""
    SELECT pc.category
    FROM ProductCategory pc
    WHERE pc.product.id = :productId
    """)
    List<Category> findCategoriesByProductId(@Param("productId") Integer productId);

    @Query("""
    SELECT c 
    FROM Category c 
    JOIN ProductCategory pc ON pc.category = c 
    WHERE pc.product.id = :productId
    """)
    List<Category> findByProducts_Id(@Param("productId") Integer productId);

    Optional<Category> findById(Integer id);
    List<Category> findByIdIn(List<Integer> ids);
    Optional<Category> findByNameIgnoreCase(String name);

    boolean existsByName(String name);
}
