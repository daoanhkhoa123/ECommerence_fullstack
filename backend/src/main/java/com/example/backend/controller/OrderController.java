package com.example.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.backend.dto.OrderRespond;
import com.example.backend.dto.OrderItemProductRequest;
import com.example.backend.dto.OrderItemProductRespond;
import com.example.backend.dto.UpdateOrderStatusRequest;
import com.example.backend.service.OrderItemService;
import com.example.backend.service.OrderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderItemService orderItemService;
    private final OrderService orderService;

    public OrderController(
        OrderService orderService, 
        OrderItemService orderItemService) {
        this.orderService = orderService;
        this.orderItemService = orderItemService;
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderRespond>> findOrdersByCustomerId(@PathVariable Integer customerId) {
        return ResponseEntity.ok(orderService.findOrderByCustomerId(customerId));
    }


    @PatchMapping("/status/{orderId}")
    public ResponseEntity<OrderRespond> updateOrderStatus(
            @PathVariable Integer orderId,
            @Valid @RequestBody UpdateOrderStatusRequest request) {
                OrderRespond respond = orderService.updateOrderStatus(orderId, request);
        return ResponseEntity.ok(respond);
    }

    @GetMapping("/customer/{customerId}/get/{orderId}")
    ResponseEntity<List<OrderItemProductRespond>> getFullOrder(
        @PathVariable Integer customerId,
        @PathVariable Integer orderId
    )
    {
        List<OrderItemProductRespond> respond = orderItemService.findAllOrderItemProductByOrderId(customerId, orderId);
        return ResponseEntity.ok(respond);
    }

    @GetMapping("/customer/{customerId}/cart")
    ResponseEntity<List<OrderItemProductRespond>> getFullCart(
        @PathVariable Integer customerId,
        @PathVariable Integer orderId
    )
    {
        List<OrderItemProductRespond> respond = orderItemService.findAllOrderItemProductByOrderId(customerId, orderId);
        return ResponseEntity.ok(respond);
    }

    @PostMapping("/customer/{customerId}/create-items")
    public ResponseEntity<OrderItemProductRespond> createOrderItemProduct(
            @PathVariable Integer customerId,
            @Valid @RequestBody OrderItemProductRequest request) {

            OrderItemProductRespond respond = orderItemService.createOrderItemProduct(customerId, request);
        return ResponseEntity.ok(respond);
    }

    @GetMapping("/customer/{customerId}/get-items/{orderItemId}")
    public ResponseEntity<OrderItemProductRespond> getOrderItemProduct(
        @PathVariable Integer customerId,
        @PathVariable Integer orderItemId
    )
    {
        OrderItemProductRespond respond = orderItemService.findOrderItemProduct(customerId, orderItemId);
        return ResponseEntity.ok(respond);
    }

    @PatchMapping("/customer/{customerId}/delete-items/{orderItemId}")
    public ResponseEntity<Void> deleteOrderItemProduct(
        @PathVariable Integer customerId,
        @PathVariable Integer orderItemId
    )
    {
        orderItemService.deleteOrderItem(customerId, orderItemId);  
        return ResponseEntity.noContent().build();
    }
}
