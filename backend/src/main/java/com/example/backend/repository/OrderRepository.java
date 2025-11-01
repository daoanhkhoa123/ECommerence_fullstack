package com.example.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend.entity.Order;
import com.example.backend.enums.OrderStatus;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findByCustomerId(Integer customerId);
    Optional<Order> findFirstByCustomerIdAndOrderStatus(Integer customerId, OrderStatus orderStatus);
}
