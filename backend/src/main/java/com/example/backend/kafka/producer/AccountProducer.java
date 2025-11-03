package com.example.backend.kafka.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.backend.entity.Customer;
import com.example.backend.entity.Vendor;
import com.example.backend.kafka.dto.CustomerCreateUpdateEvent;
import com.example.backend.kafka.dto.CustomerReadDeleteEvent;
import com.example.backend.kafka.dto.VendorCreateUpdateEvent;
import com.example.backend.kafka.dto.VendorReadDeleteEvent;
import com.example.backend.kafka.enums.KafkaTopic;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AccountProducer {

    private final KafkaTemplate<String, CustomerCreateUpdateEvent> customerCreateUpdateTemplate;
    private final KafkaTemplate<String, CustomerReadDeleteEvent> customerReadDeleteTemplate;
    private final KafkaTemplate<String, VendorCreateUpdateEvent> vendorCreateUpdateTemplate;
    private final KafkaTemplate<String, VendorReadDeleteEvent> vendorReadDeleteTemplate;

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
        CustomerCreateUpdateEvent event = buildCustomerCreateUpdate(actorId, customer);
        customerCreateUpdateTemplate.send(KafkaTopic.CUSTOMER_CREATE.getName(), event);
    }

    public void sendCustomerUpdated(Integer actorId, Customer customer) {
        CustomerCreateUpdateEvent event = buildCustomerCreateUpdate(actorId, customer);
        customerCreateUpdateTemplate.send(KafkaTopic.CUSTOMER_UPDATE.getName(), event);
    }

    public void sendCustomerRead(Integer actorId, Integer customerId) {
        CustomerReadDeleteEvent event = buildCustomerReadDelete(actorId, customerId);
        customerReadDeleteTemplate.send(KafkaTopic.CUSTOMER_READ.getName(), event);
    }

    public void sendCustomerDeleted(Integer actorId, Integer customerId) {
        CustomerReadDeleteEvent event = buildCustomerReadDelete(actorId, customerId);
        customerReadDeleteTemplate.send(KafkaTopic.CUSTOMER_DELETE.getName(), event);
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
        VendorCreateUpdateEvent event = buildVendorCreateUpdate(actorId, vendor);
        vendorCreateUpdateTemplate.send(KafkaTopic.VENDOR_CREATE.getName(), event);
    }

    public void sendVendorUpdated(Integer actorId, Vendor vendor) {
        VendorCreateUpdateEvent event = buildVendorCreateUpdate(actorId, vendor);
        vendorCreateUpdateTemplate.send(KafkaTopic.VENDOR_UPDATE.getName(), event);
    }

    public void sendVendorRead(Integer actorId, Integer vendorId) {
        VendorReadDeleteEvent event = buildVendorReadDelete(actorId, vendorId);
        vendorReadDeleteTemplate.send(KafkaTopic.VENDOR_READ.getName(), event);
    }

    public void sendVendorDeleted(Integer actorId, Integer vendorId) {
        VendorReadDeleteEvent event = buildVendorReadDelete(actorId, vendorId);
        vendorReadDeleteTemplate.send(KafkaTopic.VENDOR_DELETE.getName(), event);
    }
}
