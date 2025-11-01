package com.example.backend.application;

import org.springframework.stereotype.Service;

import com.example.backend.dto.PaymentRequest;
import com.example.backend.dto.PaymentRespond;
import com.example.backend.entity.Order;
import com.example.backend.entity.OrderItem;
import com.example.backend.entity.Payment;
import com.example.backend.enums.OrderStatus;
import com.example.backend.service.OrderItemService;
import com.example.backend.service.OrderService;
import com.example.backend.service.PaymentService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PaymentApplication {

    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final PaymentService paymentService;

    public PaymentRespond buildFromPaymet(Payment payment)
    {
        return new PaymentRespond(
                payment.getId(),
                payment.getOrder().getId(),
                payment.getOrder().getCustomer().getId(),
                payment.getPaymentMethod(),
                payment.getPaymentStatus(),
                payment.getPaidAmount(),
                payment.getTransactionRef(),
                payment.getPaidAt());
    }

    public PaymentRespond payCart(Integer customerId, PaymentRequest request)
    {
        // payment saved, decrease stock, and mark cart as paid
        Order cart = orderService.findCartByCustomerId(customerId);

        Payment payment = paymentService.paymentCart(customerId, cart, request);

        for (OrderItem orderItem : cart.getOrderItems()) {
            orderItemService.decreaseStockByOrderItem(orderItem);
        }
        orderService.updateOrderStatus(customerId, cart, OrderStatus.PAID);

        return buildFromPaymet(payment);
    }
}
