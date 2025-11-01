package com.example.backend.service;

import java.time.LocalDateTime;
import java.util.List;
import com.example.backend.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.backend.dto.OrderRespond;
import com.example.backend.dto.PaymentRequest;
import com.example.backend.dto.PaymentRespond;
import com.example.backend.dto.UpdateOrderStatusRequest;
import com.example.backend.entity.Order;
import com.example.backend.entity.OrderItem;
import com.example.backend.entity.Payment;
import com.example.backend.entity.VendorProduct;
import com.example.backend.enums.OrderStatus;
import com.example.backend.repository.OrderItemRepository;
import com.example.backend.repository.OrderRepository;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderItemRepository orderItemRepository;
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

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

    public Order findCartByCustomerId(Integer customerId)
    {
        return orderRepository.findFirstByCustomerIdAndOrderStatus(customerId, OrderStatus.PENDING)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "No pending cart found for customer with id: " + customerId));
    }

    public PaymentRespond payCart(Integer customerId, PaymentRequest request)
    {
        Order cart = findCartByCustomerId(customerId);
        
        if (cart.getTotalAmount().compareTo(request.paidAmount()) > 0)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "Insufficient payment amount");
            
        for (OrderItem orderItem : orderItemRepository.findByOrder(cart)) {
                VendorProduct vp = orderItem.getVendorProduct();
                if (vp.getStock() <= 0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                String.format("Vendor %s has run out of %s with id %d", 
                vp.getVendor().getShopName(), vp.getProduct().getName(), vp.getId()));

                vp.setStock(vp.getStock()-1);
                orderItem.setVendorProduct(vp);
                orderItemRepository.save(orderItem);
        }

        cart.setOrderStatus(OrderStatus.PAID);
        orderRepository.save(cart);

        Payment payment = new Payment();
        payment.setOrder(cart);
        payment.setPaymentMethod(request.paymentMethod());
        payment.setPaymentStatus(OrderStatus.PAID);
        payment.setTransactionRef(request.transactionRef());
        payment.setPaidAmount(request.paidAmount());
        payment.setPaidAt(LocalDateTime.now());
        paymentRepository.save(payment);

        return new PaymentRespond(payment.getId(), cart.getId(), customerId,
        payment.getPaymentMethod(), payment.getPaymentStatus(), 
        payment.getPaidAmount(), payment.getTransactionRef(), payment.getPaidAt()
        );
    }
        
}
