package com.example.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.backend.dto.AimoduleOrderTimeDto;
import com.example.backend.dto.AimoduleOrderItemFullDto;
import com.example.backend.repository.AimoduleOrderItemRepository;
import com.example.backend.repository.AimoduleOrderRepository;

@Service
public class AimoduleOrderService {

    private final AimoduleOrderItemRepository aimoduleOrderItemRepository;
    private final AimoduleOrderRepository aimoduleOrderRepository;

    public AimoduleOrderService(AimoduleOrderItemRepository aimoduleOrderItemRepository,
        AimoduleOrderRepository orderRepository) {
        this.aimoduleOrderItemRepository = aimoduleOrderItemRepository;
        this.aimoduleOrderRepository = orderRepository;
    }

    public List<AimoduleOrderItemFullDto> findOrderItemsWithProductVendor(Integer orderId) {
        return aimoduleOrderItemRepository.findOrderItemsWithProductVendor(orderId);
    }

    public List<AimoduleOrderTimeDto> findOrderTimesByCustomer(Integer customerId) {
        return aimoduleOrderRepository.findOrderTimesByCustomerId(customerId);
    }

}
