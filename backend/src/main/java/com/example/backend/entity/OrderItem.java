package com.example.backend.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "orders_items")
@Data
@EqualsAndHashCode(callSuper = false)
public class OrderItem extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable=false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "vendor_product_id", nullable = false)
    private VendorProduct vendorProduct;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "subtotal" ,nullable = false, precision = 12, scale = 2, insertable = false, updatable = false)
    private BigDecimal subTotal;

    @PrePersist 
    @PreUpdate
    protected void calculateSubTotal() {
        BigDecimal price = vendorProduct.getPrice();
        subTotal = price.multiply(BigDecimal.valueOf(quantity));
    }
}
