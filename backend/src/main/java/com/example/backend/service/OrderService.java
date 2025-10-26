package com.example.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.backend.dto.OrderDto;
import com.example.backend.dto.OrderItemDto;
import com.example.backend.repository.OrderItemRepository;
import com.example.backend.repository.OrderRepository;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderService(OrderRepository orderRepository,
                        OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public List<OrderDto> findOrdersByCustomer(Integer customerId) {
        return orderRepository.findOrdersByCustomerId(customerId);
    }

    public List<OrderItemDto> findOrderItemsByOrder(Integer orderId) {
        return orderItemRepository.findByOrderId(orderId);
    }
}
