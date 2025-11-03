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
    private final KafkaTemplate<String, VendorProductCreateUpdateEvent> vendorProudctCreateUpdateTemplate;
    private final KafkaTemplate<String, VendorProductReadEvent> vendorProductReadTemplate;
    private final KafkaTemplate<String, VendorProductDeleteEvent> vendorProductDeleteTemplate;

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

    public void sendVendorProudctCreate(Integer actorId, VendorProduct vendorProduct)
    {
        VendorProductCreateUpdateEvent event = buildVendorProductCreateUpdate(actorId, vendorProduct);
            vendorProudctCreateUpdateTemplate.send(KafkaTopic.VENDOR_PRODUCT_CREATE.getName(), event);
    }

    public void sendVendorProudctUpdate(Integer actorId, VendorProduct vendorProduct)
    {
        VendorProductCreateUpdateEvent event = buildVendorProductCreateUpdate(actorId, vendorProduct);
            vendorProudctCreateUpdateTemplate.send(KafkaTopic.VENDOR_PRODUCT_UPDATE.getName(), event);
    }

    public void sendVendorProductRead(Integer actorId, Integer vendorId)
    {
        VendorProductReadEvent event = new VendorProductReadEvent(actorId, vendorId);
        vendorProductReadTemplate.send(KafkaTopic.VENDOR_PRODUCT_READ.getName(), event);
    }

    public void sendVendorProductDelete(Integer actorId, Integer vendorProductId)
    {
        VendorProductDeleteEvent event = new VendorProductDeleteEvent(actorId, vendorProductId);
        vendorProductDeleteTemplate.send(KafkaTopic.VENDOR_PRODUCT_DELETE.getName(), event);
    }
}
