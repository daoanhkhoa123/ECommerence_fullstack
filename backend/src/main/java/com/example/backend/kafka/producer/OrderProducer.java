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
    private final KafkaTemplate<String, OrderStatusUpdateEvent> orderStatusUpdateTemplate;
    private final KafkaTemplate<String, OrderItemCreateEvent> orderItemCreateTemplate;
    private final KafkaTemplate<String, OrderItemDeleteEvent> orderItemDeleteTemplate;

    public void sendorderStatusUpdate(Integer actorId, Integer orderId, OrderStatus orderStatus)
    {
        OrderStatusUpdateEvent event = new OrderStatusUpdateEvent(actorId, orderId, orderStatus);
        orderStatusUpdateTemplate.send( KafkaTopic.ORDER_STATUS_UPDATED.getName(), event);
    }

    public void sendOrderItemCreate(Integer actorId, OrderItem orderItem) {
        VendorProduct vendorProduct = orderItem.getVendorProduct();
        Vendor vendor = vendorProduct.getVendor();
        Product product = vendorProduct.getProduct();

        OrderItemCreateEvent event = new OrderItemCreateEvent(
            actorId,
            orderItem.getId(),
            vendorProduct.getId(),
            orderItem.getQuantity(),
            orderItem.getSubTotal(),
            vendor.getShopName(),
            vendor.getPhone(),
            product.getName(),
            product.getBrand()
        );

        orderItemCreateTemplate.send(KafkaTopic.ORDER_ITEM_CREATE.getName(), event);
    }

    public void sendOrderItemDelete(Integer actorId, Integer orderItemId)
    {
        OrderItemDeleteEvent event = new OrderItemDeleteEvent(actorId, orderItemId);

        orderItemDeleteTemplate.send(KafkaTopic.ORDER_ITEM_DELETE.getName(), event);
    }
}
