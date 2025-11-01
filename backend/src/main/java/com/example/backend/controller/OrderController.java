package com.example.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.backend.dto.OrderRespond;
import com.example.backend.application.OrderApplication;
import com.example.backend.dto.OrderItemProductRequest;
import com.example.backend.dto.OrderItemProductRespond;
import com.example.backend.dto.UpdateOrderStatusRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/orders/customer")
public class OrderController {

    private final OrderApplication orderApplication;

    @GetMapping("/{customerId}")
    public ResponseEntity<List<OrderRespond>> findOrdersByCustomerId(@PathVariable Integer customerId) {
        return ResponseEntity.ok(orderApplication.findOrdersByCustomerId(customerId));
    }


    @PatchMapping("/{customerId}/status/{orderId}")
    public ResponseEntity<OrderRespond> updateOrderStatus(
            @PathVariable Integer customerId,
            @PathVariable Integer orderId,
            @Valid @RequestBody UpdateOrderStatusRequest request) {
                OrderRespond respond = orderApplication.updateOrderStatus(customerId, orderId, request);
        return ResponseEntity.ok(respond);
    }

    @GetMapping("/{customerId}/get/{orderId}")
    ResponseEntity<List<OrderItemProductRespond>> getFullOrder(
        @PathVariable Integer customerId,
        @PathVariable Integer orderId
    )
    {
        List<OrderItemProductRespond> respond = orderApplication.findAllOrderItemProductByOrderId(customerId, orderId);
        return ResponseEntity.ok(respond);
    }

    @GetMapping("/{customerId}/cart")
    ResponseEntity<List<OrderItemProductRespond>> getFullCart(
        @PathVariable Integer customerId
    )
    {
        List<OrderItemProductRespond> respond = orderApplication.findAllOrderItemProductInCart(customerId);
        return ResponseEntity.ok(respond);
    }

    @PostMapping("/{customerId}/create-items")
    public ResponseEntity<OrderItemProductRespond> createOrderItemProduct(
            @PathVariable Integer customerId,
            @Valid @RequestBody OrderItemProductRequest request) {

            OrderItemProductRespond respond = orderApplication.createOrderItemProduct(customerId, request);
        return ResponseEntity.ok(respond);
    }

    @GetMapping("/{customerId}/get-items/{orderItemId}")
    public ResponseEntity<OrderItemProductRespond> getOrderItemProduct(
        @PathVariable Integer customerId,
        @PathVariable Integer orderItemId
    )
    {
        OrderItemProductRespond respond = orderApplication.findOrderItemProduct(customerId, orderItemId);
        return ResponseEntity.ok(respond);
    }

    @PatchMapping("/{customerId}/delete-items/{orderItemId}")
    public ResponseEntity<Void> deleteOrderItemProduct(
        @PathVariable Integer customerId,
        @PathVariable Integer orderItemId
    )
    {
        orderApplication.deleteOrderItem(customerId, orderItemId);  
        return ResponseEntity.noContent().build();
    }
}
