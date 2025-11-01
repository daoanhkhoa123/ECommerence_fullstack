package com.example.backend.kafka.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import com.example.backend.kafka.dto.CustomerEvent;
import com.example.backend.kafka.dto.VendorEvent;
import com.example.backend.kafka.enums.AccountTopic;

public class AccountProducer {
    private static final Logger logger = LoggerFactory.getLogger(AccountProducer.class);
    private final KafkaTemplate<String, CustomerEvent> customerTemplate;
    private final KafkaTemplate<String, VendorEvent> vendorTemplate;

    public AccountProducer(KafkaTemplate<String, CustomerEvent> customerTemplate,
        KafkaTemplate<String, VendorEvent> vendorTemplate)
    {
        this.customerTemplate = customerTemplate;
        this.vendorTemplate = vendorTemplate;
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
