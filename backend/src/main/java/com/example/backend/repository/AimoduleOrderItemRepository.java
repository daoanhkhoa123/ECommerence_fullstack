package com.example.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.backend.entity.OrderItem;
import com.example.backend.dto.AimoduleOrderItemFullDto;

@Repository
public interface AimoduleOrderItemRepository extends JpaRepository<OrderItem, Integer> {

    @Query("""
        SELECT new com.example.backend.dto.AimoduleOrderItemFullDto(
            oi.id, oi.quantity, oi.price, oi.subTotal,
            p.id, p.name, p.description, p.brand,
            (SELECT c.name FROM ProductCategory pc JOIN pc.category c WHERE pc.product = p),
            vp.price, v.id, v.shopName, v.description
        )
        FROM OrderItem oi
        JOIN oi.vendorProduct vp
        JOIN vp.product p
        JOIN vp.vendor v
        WHERE oi.order.id = :orderId

    """)
    List<AimoduleOrderItemFullDto> findOrderItemsWithProductVendor(@Param("orderId") Integer orderId);
}
