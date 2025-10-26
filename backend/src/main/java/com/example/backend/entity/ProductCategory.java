package com.example.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "products_categories", uniqueConstraints = 
@UniqueConstraint(columnNames = {"product_id", "category_id"}))
@IdClass(ProductCategoryId.class)
@Data
@EqualsAndHashCode(callSuper = false)
public class ProductCategory extends BaseEntity{

    @Id
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;


    @Id
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
