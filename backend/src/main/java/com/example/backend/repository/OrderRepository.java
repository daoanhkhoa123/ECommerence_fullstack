package com.example.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.backend.dto.OrderDto;
import com.example.backend.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query("SELECT new com.example.backend.dto.OrderDto(" +
           "o.id, o.customer.id, o.orderStatus, " +
           "o.totalAmount, o.shippingAddress, o.placeAt) " +
           "FROM Order o WHERE o.customer.id = :customerId")
    List<OrderDto> findOrdersByCustomerId(@Param("customerId") Integer customerId);
}
