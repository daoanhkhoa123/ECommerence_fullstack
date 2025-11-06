package com.example.backend.kafka.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.backend.entity.Product;
import com.example.backend.entity.VendorProduct;
import com.example.backend.kafka.dto.VendorProductCreateUpdateEvent;
import com.example.backend.kafka.dto.VendorProductDeleteEvent;
import com.example.backend.kafka.dto.VendorProductReadEvent;
import com.example.backend.kafka.enums.KafkaTopic;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProductProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private VendorProductCreateUpdateEvent buildVendorProductCreateUpdate(Integer actorId, VendorProduct vendorProduct) {
        Product product = vendorProduct.getProduct();
        return new VendorProductCreateUpdateEvent(
            actorId,
            product.getName(),
            product.getDescription(),
            product.getBrand(),
            vendorProduct.getPrice(),
            vendorProduct.getStock(),
            vendorProduct.getSku(),
            vendorProduct.getIsFeatured()
        );
    }

    public void sendVendorProductCreate(Integer actorId, VendorProduct vendorProduct) {
        kafkaTemplate.send(
            KafkaTopic.VENDOR_PRODUCT_CREATE.getName(),
            buildVendorProductCreateUpdate(actorId, vendorProduct)
        );
    }

    public void sendVendorProductUpdate(Integer actorId, VendorProduct vendorProduct) {
        kafkaTemplate.send(
            KafkaTopic.VENDOR_PRODUCT_UPDATE.getName(),
            buildVendorProductCreateUpdate(actorId, vendorProduct)
        );
    }

    public void sendVendorProductRead(Integer actorId, Integer vendorId) {
        kafkaTemplate.send(
            KafkaTopic.VENDOR_PRODUCT_READ.getName(),
            new VendorProductReadEvent(actorId, vendorId)
        );
    }

    public void sendVendorProductDelete(Integer actorId, Integer vendorProductId) {
        kafkaTemplate.send(
            KafkaTopic.VENDOR_PRODUCT_DELETE.getName(),
            new VendorProductDeleteEvent(actorId, vendorProductId)
        );
    }
}
