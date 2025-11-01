package com.example.backend.service;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.backend.dto.UpdateOrderStatusRequest;
import com.example.backend.entity.Order;
import com.example.backend.enums.OrderStatus;
import com.example.backend.repository.OrderRepository;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public List<Order> findAllByCustomerId(Integer customerId) {
        return orderRepository.findByCustomer_Id(customerId);
    }

    public Order updateOrderStatus(Integer customerId,
                                Integer orderId,
                                UpdateOrderStatusRequest request) {

        // Load the order or throw 404
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                String.format("Order with id %d not found", orderId)
            ));

        // Check ownership
        if (!order.getCustomer().getId().equals(customerId)) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                String.format("User id %d does not have access to order id %d",
                            customerId, order.getId())
            );
        }

        order.setOrderStatus(request.status());
        return orderRepository.save(order);
    }

    public Order updateOrderStatus(Integer customerId, 
        Order order, OrderStatus status) {

        if (!order.getCustomer().getId().equals(customerId))
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                String.format("User id %d does not have access to order id %d",
                            customerId, order.getId())
            );

        order.setOrderStatus(status);
        return orderRepository.save(order);
    }


    public Order findCartByCustomerId(Integer customerId) {
        return orderRepository.findFirstByCustomerIdAndOrderStatus(customerId, OrderStatus.PENDING)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Note: We should be able to find a default one. No pending cart found for customer with id: " + customerId
        ));
    }

}
