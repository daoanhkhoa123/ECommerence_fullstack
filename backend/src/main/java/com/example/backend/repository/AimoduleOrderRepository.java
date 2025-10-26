package com.example.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.backend.dto.AimoduleOrderTimeDto;
import com.example.backend.entity.Order;

@Repository
public interface AimoduleOrderRepository extends JpaRepository<Order, Integer> {

       @Query("SELECT new com.example.backend.dto.AimoduleOrderTimeDto(" +
           "o.id, o.placeAt) " +
           "FROM Order o WHERE o.customer.id = :customerId " +
           "ORDER BY o.placeAt DESC")
    List<AimoduleOrderTimeDto> findOrderTimesByCustomerId(@Param("customerId") Integer customerId);
}