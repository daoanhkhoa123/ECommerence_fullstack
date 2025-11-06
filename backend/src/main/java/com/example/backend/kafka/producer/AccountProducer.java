package com.example.backend.kafka.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.backend.entity.Customer;
import com.example.backend.entity.Vendor;
import com.example.backend.kafka.dto.*;
import com.example.backend.kafka.enums.KafkaTopic;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AccountProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    // -------------------- CUSTOMER --------------------

    private CustomerCreateUpdateEvent buildCustomerCreateUpdate(Integer actorId, Customer customer) {
        return new CustomerCreateUpdateEvent(
            actorId,
            customer.getId(),
            customer.getAccount().getEmail(),
            customer.getFullName(),
            customer.getPhone(),
            customer.getAddress(),
            customer.getBirthDate()
        );
    }

    private CustomerReadDeleteEvent buildCustomerReadDelete(Integer actorId, Integer customerId) {
        return new CustomerReadDeleteEvent(actorId, customerId);
    }

    public void sendCustomerCreated(Integer actorId, Customer customer) {
        kafkaTemplate.send(KafkaTopic.CUSTOMER_CREATE.getName(), buildCustomerCreateUpdate(actorId, customer));
    }

    public void sendCustomerUpdated(Integer actorId, Customer customer) {
        kafkaTemplate.send(KafkaTopic.CUSTOMER_UPDATE.getName(), buildCustomerCreateUpdate(actorId, customer));
    }

    public void sendCustomerRead(Integer actorId, Integer customerId) {
        kafkaTemplate.send(KafkaTopic.CUSTOMER_READ.getName(), buildCustomerReadDelete(actorId, customerId));
    }

    public void sendCustomerDeleted(Integer actorId, Integer customerId) {
        kafkaTemplate.send(KafkaTopic.CUSTOMER_DELETE.getName(), buildCustomerReadDelete(actorId, customerId));
    }

    // -------------------- VENDOR --------------------

    private VendorCreateUpdateEvent buildVendorCreateUpdate(Integer actorId, Vendor vendor) {
        return new VendorCreateUpdateEvent(
            actorId,
            vendor.getId(),
            vendor.getAccount().getEmail(),
            vendor.getShopName(),
            vendor.getDescription(),
            vendor.getPhone()
        );
    }

    private VendorReadDeleteEvent buildVendorReadDelete(Integer actorId, Integer vendorId) {
        return new VendorReadDeleteEvent(actorId, vendorId);
    }

    public void sendVendorCreated(Integer actorId, Vendor vendor) {
        kafkaTemplate.send(KafkaTopic.VENDOR_CREATE.getName(), buildVendorCreateUpdate(actorId, vendor));
    }

    public void sendVendorUpdated(Integer actorId, Vendor vendor) {
        kafkaTemplate.send(KafkaTopic.VENDOR_UPDATE.getName(), buildVendorCreateUpdate(actorId, vendor));
    }

    public void sendVendorRead(Integer actorId, Integer vendorId) {
        kafkaTemplate.send(KafkaTopic.VENDOR_READ.getName(), buildVendorReadDelete(actorId, vendorId));
    }

    public void sendVendorDeleted(Integer actorId, Integer vendorId) {
        kafkaTemplate.send(KafkaTopic.VENDOR_DELETE.getName(), buildVendorReadDelete(actorId, vendorId));
    }
}
