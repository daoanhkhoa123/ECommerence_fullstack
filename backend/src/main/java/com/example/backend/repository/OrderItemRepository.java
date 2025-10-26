package com.example.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.backend.entity.OrderItem;
import com.example.backend.dto.OrderItemDto;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

    @Query("SELECT new com.example.backend.dto.OrderItemDto(" +
           "oi.id, oi.order.id, oi.vendorProduct.id, " +
           "oi.quantity, oi.price, oi.subTotal) " +
           "FROM OrderItem oi WHERE oi.order.id = :orderId")
    List<OrderItemDto> findByOrderId(@Param("orderId") Integer orderId);


}
