package com.example.backend.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.backend.dto.OrderItemProductRequest;
import com.example.backend.entity.Order;
import com.example.backend.entity.OrderItem;
import com.example.backend.entity.VendorProduct;
import com.example.backend.enums.OrderStatus;
import com.example.backend.repository.CustomerRepository;
import com.example.backend.repository.OrderItemRepository;
import com.example.backend.repository.OrderRepository;
import com.example.backend.repository.VendorProductRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrderItemService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final VendorProductRepository vendorProductRepository;
    private final CustomerRepository customerRepository;

    public OrderItem createOrderItemProduct(Integer customerId, OrderItemProductRequest request) {
        // Ensure the customer exists
        customerRepository.findById(customerId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Customer not found with ID: " + customerId
                ));

        // Find or ensure the customer has a pending order
        Order order = orderRepository
                .findFirstByCustomerIdAndOrderStatus(customerId, OrderStatus.PENDING)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Customer must have one pending order before adding items."
                ));

        // Find vendor product
        VendorProduct vendorProduct = vendorProductRepository.findById(request.vendorProductId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Vendor product not found with ID: " + request.vendorProductId()
                ));

        // Create new order item
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setVendorProduct(vendorProduct);
        orderItem.setQuantity(request.quantity());
        orderItemRepository.save(orderItem);

        // Update order total
        order.getOrderItems().add(orderItem);
        order.setTotalAmount(order.getTotalAmount().add(orderItem.getSubTotal()));
        orderRepository.save(order);

        return orderItem;
    }

    public OrderItem findOrderItemProduct(Integer customerId, Integer orderItemId) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "OrderItem not found with id: " + orderItemId
                ));

        if (!orderItem.getOrder().getCustomer().getId().equals(customerId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Customer id " + customerId + " does not have access to OrderItem id: " + orderItemId
            );
        }

        return orderItem;
    }

    public List<OrderItem> findAllOrderItemProductByOrderId(Integer customerId, Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Order not found"
                ));

        if (!order.getCustomer().getId().equals(customerId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Order does not belong to this customer"
            );
        }

        return order.getOrderItems();
    }

    @Transactional
    public VendorProduct decreaseStockByOrderItem(OrderItem orderItem)
    {
        VendorProduct vp = orderItem.getVendorProduct();
        if (vp.getStock() <= 0)
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    String.format(
                            "Vendor %s has run out of %s with id %d",
                            vp.getVendor().getShopName(),
                            vp.getProduct().getName(),
                            vp.getId()
                    ));
                    
        vp.setStock(vp.getStock() - orderItem.getQuantity());
        vendorProductRepository.save(vp);

        return vp;
    }

    @Transactional
    public void deleteOrderItem(Integer customerId, Integer orderItemId) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "OrderItem not found with id: " + orderItemId
                ));

        if (!orderItem.getOrder().getCustomer().getId().equals(customerId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Customer id " + customerId + " does not have access to OrderItem id: " + orderItemId
            );
        }

        orderItemRepository.deleteById(orderItemId);
    }
}
