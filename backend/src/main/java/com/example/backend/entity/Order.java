package com.example.backend.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.backend.enums.OrderStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "orders")
@Data
@EqualsAndHashCode(callSuper = false)
public class Order extends BaseEntity{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Integer id;

    @OneToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "order_status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.PENDING;

    @Column(name = "total_amount", nullable = false, precision=12, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "shipping_address", nullable = false)
    private String shippingAddress;

    @Column(name = "placed_at")
    private LocalDateTime orderTime = LocalDateTime.now();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @PrePersist
    @PreUpdate
    private void calculateTotal()
    {
        if (orderItems!=null) {
            totalAmount=orderItems.stream().map(OrderItem::getSubTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add, null);
        } else
        {
            totalAmount=BigDecimal.ZERO;
        }
        super.onCreate();
        super.onUpdate();
    }
}
