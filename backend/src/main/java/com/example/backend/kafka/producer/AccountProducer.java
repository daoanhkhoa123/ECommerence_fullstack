package com.example.backend.kafka.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.backend.entity.Customer;
import com.example.backend.entity.Vendor;
import com.example.backend.kafka.dto.AuditEvent;
import com.example.backend.kafka.dto.CustomerEvent;
import com.example.backend.kafka.dto.VendorEvent;
import com.example.backend.kafka.enums.AccountTopic;
import com.example.backend.kafka.enums.CRUDType;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AccountProducer {
    private static final Logger logger = LoggerFactory.getLogger(AccountProducer.class);

    private final AuditProducer auditProducer;
    private final KafkaTemplate<String, CustomerEvent> customerTemplate;
    private final KafkaTemplate<String, VendorEvent> vendorTemplate;

    public CustomerEvent buildFromCustomer(CRUDType evenType, Integer customerId)
    {
        AuditEvent auditEvent = auditProducer.buildAuditEvent(evenType);
        return new CustomerEvent(auditEvent, customerId, 
        null, null, null, null, null);
    }

    public CustomerEvent buildFromCustomer(CRUDType evenType, Customer customer) {
        AuditEvent auditEvent = auditProducer.buildAuditEvent(evenType);
        return new CustomerEvent(
            auditEvent,                     
            customer.getId(),
            customer.getAccount().getEmail(),
            customer.getFullName(),
            customer.getPhone(),
            customer.getAddress(),
            customer.getBirthDate()
        );
    }

    public VendorEvent buildFromVendor(CRUDType evenType, Integer id)
    {
        AuditEvent auditEvent = auditProducer.buildAuditEvent(evenType);
        return new VendorEvent(
            auditEvent,
            id,
            null,null,null,null
        );
    }


    public VendorEvent buildFromVendor(CRUDType evenType, Vendor vendor)
    {
        AuditEvent auditEvent = auditProducer.buildAuditEvent(evenType);
        return new VendorEvent(
            auditEvent,
            vendor.getId(),
            vendor.getAccount().getEmail(),
            vendor.getShopName(),
            vendor.getDescription(),
            vendor.getPhone()
        );
    }

    public void sendCustomer(CustomerEvent event) {
        customerTemplate.send(AccountTopic.CUSTOMER.getName(), event).whenComplete((result, ex) -> {
            if (ex != null) {
                logger.error("Failed to send CustomerEvent [{}] to topic [{}]: {}",
                        event, AccountTopic.CUSTOMER.getName(), ex.getMessage(), ex);
            } else {
                logger.info("Sent CustomerEvent [{}] to topic [{}] with offset {}, partition {}, timestamp {}",
                        event, AccountTopic.CUSTOMER.getName(),
                        result.getRecordMetadata().offset(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().timestamp());
            }
        });
    }

    public void sendVendor(VendorEvent event) {
        vendorTemplate.send(AccountTopic.VENDOR.getName(), event).whenComplete((result, ex) -> {
            if (ex != null) {
                logger.error("Failed to send VendorEvent [{}] to topic [{}]: {}",
                        event, AccountTopic.VENDOR.getName(), ex.getMessage(), ex);
            } else {
                logger.info("Sent VendorEvent [{}] to topic [{}] with offset {}, partition {}, timestamp {}",
                        event, AccountTopic.VENDOR.getName(),
                        result.getRecordMetadata().offset(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().timestamp());
            }
        });
    }
}
