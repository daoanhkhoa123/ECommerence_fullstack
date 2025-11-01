package com.example.backend.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.backend.dto.OrderRespond;
import com.example.backend.dto.UpdateOrderStatusRequest;
import com.example.backend.entity.Order;
import com.example.backend.repository.OrderRepository;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<OrderRespond> findOrderByCustomerId(Integer customerId) {
        return orderRepository.findByCustomerId(customerId).stream()
                .map(order -> new OrderRespond(
                        order.getId(),
                        order.getCustomer().getId(),
                        order.getOrderStatus(),
                        order.getTotalAmount(),
                        order.getShippingAddress(),
                        order.getOrderTime()
                ))
                .toList();
    }

        public OrderRespond updateOrderStatus(Integer orderId, UpdateOrderStatusRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Order not found with ID: " + orderId
                ));

        order.setOrderStatus(request.status());
        orderRepository.save(order);

        return new OrderRespond(
                order.getId(),
                order.getCustomer().getId(),
                order.getOrderStatus(),
                order.getTotalAmount(),
                order.getShippingAddress(),
                order.getOrderTime()
        );
    }
        
}
