package com.example.backend.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.backend.dto.PaymentRequest;
import com.example.backend.dto.PaymentRespond;
import com.example.backend.entity.Order;
import com.example.backend.entity.OrderItem;
import com.example.backend.entity.Payment;
import com.example.backend.enums.OrderStatus;
import com.example.backend.kafka.producer.PaymentProducer;
import com.example.backend.security.JwtService;
import com.example.backend.service.OrderItemService;
import com.example.backend.service.OrderService;
import com.example.backend.service.PaymentService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PaymentApplication {
    private static final Logger log = LoggerFactory.getLogger(PaymentApplication.class);

    private final PaymentProducer paymentProducer;
    private final JwtService jwtService;
    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final PaymentService paymentService;

    private PaymentRespond buildFromPayment(Payment payment) {
        return new PaymentRespond(
            payment.getId(),
            payment.getOrder().getId(),
            payment.getOrder().getCustomer().getId(),
            payment.getPaymentMethod(),
            payment.getPaymentStatus(),
            payment.getPaidAmount(),
            payment.getTransactionRef(),
            payment.getPaidAt()
        );
    }

    public PaymentRespond payCart(PaymentRequest request) {
        Integer customerId = jwtService.getCurrentUserId();
        log.info("Retrieved current user id: {}", customerId);

        Order cart = orderService.findCartByCustomerId(customerId);
        log.info("Called orderService.findCartByCustomerId() - Found cart with id: {} containing {} items",
            cart.getId(), cart.getOrderItems().size());

        Payment payment = paymentService.paymentCart(customerId, cart, request);
        log.info("Called paymentService.paymentCart() - Created payment with id: {} and amount: {}",
            payment.getId(), payment.getPaidAmount());

        for (OrderItem orderItem : cart.getOrderItems()) {
            log.debug("Calling orderItemService.decreaseStockByOrderItem() for orderItem id: {} (vendorProductId: {})",
                orderItem.getId(), orderItem.getVendorProduct().getId());
            orderItemService.decreaseStockByOrderItem(orderItem);
            log.debug("Completed orderItemService.decreaseStockByOrderItem() for orderItem id: {}",
                orderItem.getId());
        }

        orderService.updateOrderStatus(customerId, cart, OrderStatus.PAID);
        log.info("Called orderService.updateOrderStatus() - Cart with id: {} updated to status: {}",
            cart.getId(), OrderStatus.PAID);

        PaymentRespond body = buildFromPayment(payment);
        log.info("Mapped Payment entity to PaymentRespond for payment id: {}", payment.getId());

        paymentProducer.sendPaymentCreate(customerId, body);
        log.info("Called paymentProducer.sendPaymentCreate() - Payment event sent for payment id: {}", payment.getId());

        log.info("Payment process completed successfully for customer id: {}", customerId);
        return body;
    }
}
