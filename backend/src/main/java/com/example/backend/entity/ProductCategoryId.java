package com.example.backend.entity;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductCategoryId implements Serializable{
    private Integer product;
    private Integer category;
}
