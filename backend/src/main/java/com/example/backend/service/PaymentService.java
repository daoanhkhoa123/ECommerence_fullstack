package com.example.backend.service;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.backend.dto.PaymentRequest;
import com.example.backend.entity.Order;
import com.example.backend.entity.Payment;
import com.example.backend.enums.OrderStatus;
import com.example.backend.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public Payment paymentCart(Integer customerId, Order cart, PaymentRequest request) {
        if (cart.getTotalAmount().compareTo(request.paidAmount()) > 0)
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Insufficient payment amount");

        Payment payment = new Payment();
        payment.setOrder(cart);
        payment.setPaymentMethod(request.paymentMethod());
        payment.setPaymentStatus(OrderStatus.PAID);
        payment.setTransactionRef(request.transactionRef());
        payment.setPaidAmount(request.paidAmount());
        payment.setPaidAt(LocalDateTime.now());
        paymentRepository.save(payment);

        return payment;
    }
}
