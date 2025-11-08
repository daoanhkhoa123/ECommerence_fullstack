package com.example.backend.kafka.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.backend.entity.OrderItem;
import com.example.backend.entity.Product;
import com.example.backend.entity.Vendor;
import com.example.backend.entity.VendorProduct;
import com.example.backend.enums.OrderStatus;
import com.example.backend.kafka.dto.OrderItemCreateEvent;
import com.example.backend.kafka.dto.OrderItemDeleteEvent;
import com.example.backend.kafka.dto.OrderStatusUpdateEvent;
import com.example.backend.kafka.enums.KafkaTopic;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrderProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendOrderStatusUpdate(Integer actorId, Integer orderId, OrderStatus orderStatus) {
        kafkaTemplate.send(
            KafkaTopic.ORDER_STATUS_UPDATED.getName(),
            new OrderStatusUpdateEvent(actorId, orderId, orderStatus)
        );
    }

    public void sendOrderItemCreate(Integer actorId, OrderItem orderItem) {
        VendorProduct vendorProduct = orderItem.getVendorProduct();
        Vendor vendor = vendorProduct.getVendor();
        Product product = vendorProduct.getProduct();

        kafkaTemplate.send(
            KafkaTopic.ORDER_ITEM_CREATE.getName(),
            new OrderItemCreateEvent(
                actorId,
                orderItem.getOrder().getId(),
                orderItem.getId(),
                vendorProduct.getId(),
                orderItem.getQuantity(),
                orderItem.getSubTotal(),
                vendor.getShopName(),
                vendor.getPhone(),
                product.getName(),
                product.getBrand()
            )
        );
    }

    public void sendOrderItemDelete(Integer actorId, Integer orderItemId) {
        kafkaTemplate.send(
            KafkaTopic.ORDER_ITEM_DELETE.getName(),
            new OrderItemDeleteEvent(actorId, orderItemId)
        );
    }
}
