package com.example.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.example.backend.dto.OrderDto;
import com.example.backend.dto.AimoduleOrderTimeDto;
import com.example.backend.dto.OrderItemDto;
import com.example.backend.dto.AimoduleOrderItemFullDto;
import com.example.backend.service.AimoduleOrderService;
import com.example.backend.service.OrderService;

@RestController
@RequestMapping("/api/aimodule")  // 👈 base path prefix
public class AimoduleOrderController {

    private final AimoduleOrderService aimoduleOrderService;
    private final OrderService orderService;

    public AimoduleOrderController(AimoduleOrderService aimoduleOrderService,
        OrderService orderService)
    {
        this.aimoduleOrderService = aimoduleOrderService;
        this.orderService = orderService;
    }

    
    @GetMapping("/customers/{customerId}/orders/times")
    public List<AimoduleOrderTimeDto> getOrderTimesByCustomer(@PathVariable Integer customerId) {
        return aimoduleOrderService.findOrderTimesByCustomer(customerId);
    }

    @GetMapping("/orders/{orderId}/items/full")
    public List<AimoduleOrderItemFullDto> getOrderItemsWithFullInfo(@PathVariable Integer orderId) {
        return aimoduleOrderService.findOrderItemsWithProductVendor(orderId);
    }

    @GetMapping("/customers/{customerId}/orders")
    public List<OrderDto> getOrdersByCustomer(@PathVariable Integer customerId) {
        return orderService.findOrdersByCustomer(customerId);
    }

    @GetMapping("/orders/{orderId}/items")
    public List<OrderItemDto> getOrderItems(@PathVariable Integer orderId) {
        return orderService.findOrderItemsByOrder(orderId);
    }


}
