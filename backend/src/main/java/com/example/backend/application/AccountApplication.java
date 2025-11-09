package com.example.backend.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.backend.dto.CustomerRequest;
import com.example.backend.dto.CustomerRespond;
import com.example.backend.dto.VendorRequest;
import com.example.backend.dto.VendorRespond;
import com.example.backend.entity.Customer;
import com.example.backend.entity.Vendor;
import com.example.backend.kafka.producer.AccountProducer;
import com.example.backend.security.JwtService;
import com.example.backend.service.AccountService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AccountApplication {

    private static final Logger log = LoggerFactory.getLogger(AccountApplication.class);

    private final AccountProducer accountProducer;
    private final AccountService accountService;
    private final JwtService jwtService;

    private CustomerRespond buildFromCustomer(Customer customer) {
        return new CustomerRespond(
            customer.getId(),
            customer.getAccount().getEmail(),
            customer.getFullName(),
            customer.getPhone(),
            customer.getAddress(),
            customer.getBirthDate()
        );
    }

    private VendorRespond buildFromVendor(Vendor vendor) {
        return new VendorRespond(
            vendor.getId(),
            vendor.getAccount().getEmail(),
            vendor.getShopName(),
            vendor.getDescription(),
            vendor.getPhone()
        );
    }

    // ----------------- CUSTOMER -----------------
    public CustomerRespond findCustomerById(Integer customerId) {
        Integer actorId = jwtService.getCurrentUserId();
        log.info("Fetching customer id={} by actor={}", customerId, actorId);

        Customer customer = accountService.findCustomerById(customerId);
        log.info("Called accountService.findCustomerById() - Found id={}, email={}", customer.getId(), customer.getAccount().getEmail());

        accountProducer.sendCustomerRead(actorId, customerId);
        log.info("Called accountProducer.sendCustomerRead() - Sent event for id={} by actor={}", customerId, actorId);

        return buildFromCustomer(customer);
    }

    public CustomerRespond registerCustomer(CustomerRequest request) {
        log.info("Registering new customer {}", request.fullName());

        Customer customer = accountService.registerCustomer(request);
        log.info("Created customer id={}, email={}", customer.getId(), customer.getAccount().getEmail());

        // For events, you can use null or 0 to indicate “anonymous actor”
        accountProducer.sendCustomerCreated(customer.getAccount().getId(), customer);
        log.info("Sent event for customer id={}", customer.getId());

        return buildFromCustomer(customer);
    }


    public CustomerRespond updateCustomer(Integer customerId, CustomerRequest request) {
        Integer actorId = jwtService.getCurrentUserId();
        if (!actorId.equals(customerId)) {
            log.warn("Unauthorized update attempt on customer id={} by actor={}", customerId, actorId);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to access this resource");
        }

        log.info("Updating customer id={} by actor={}", customerId, actorId);

        Customer customer = accountService.updateCustomer(customerId, request);
        log.info("Called accountService.updateCustomer() - Updated id={}, email={}", customer.getId(), customer.getAccount().getEmail());

        accountProducer.sendCustomerUpdated(actorId, customer);
        log.info("Called accountProducer.sendCustomerUpdated() - Sent event for id={} by actor={}", customer.getId(), actorId);

        return buildFromCustomer(customer);
    }

    public void deleteCustomer(Integer customerId) {
        Integer actorId = jwtService.getCurrentUserId();
        if (!actorId.equals(customerId)) {
            log.warn("Unauthorized delete attempt on customer id={} by actor={}", customerId, actorId);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to access this resource");
        }

        log.info("Deleting customer id={} by actor={}", customerId, actorId);

        accountProducer.sendCustomerDeleted(actorId, customerId);
        log.info("Called accountProducer.sendCustomerDeleted() - Sent event for id={} by actor={}", customerId, actorId);

        accountService.deleteCustomer(customerId);
        log.info("Called accountService.deleteCustomer() - Deleted customer id={}", customerId);
    }

    // ----------------- VENDOR -----------------
    public VendorRespond findVendorById(Integer vendorId) {
        Integer actorId = jwtService.getCurrentUserId();
        log.info("Fetching vendor id={} by actor={}", vendorId, actorId);

        Vendor vendor = accountService.findVendorById(vendorId);
        log.info("Called accountService.findVendorById() - Found id={}, email={}", vendor.getId(), vendor.getAccount().getEmail());

        accountProducer.sendVendorRead(actorId, vendorId);
        log.info("Called accountProducer.sendVendorRead() - Sent event for id={} by actor={}", vendorId, actorId);

        return buildFromVendor(vendor);
    }

    public VendorRespond registerVendor(VendorRequest request) {
        log.info("Registering new vendor {}", request.shopName());

        Vendor vendor = accountService.registerVendor(request);
        log.info("Created vendor id={}, email={}", vendor.getId(), vendor.getAccount().getEmail());

        accountProducer.sendVendorCreated(vendor.getAccount().getId(), vendor);
        log.info("Sent event for vendor id={}", vendor.getId());

        return buildFromVendor(vendor);
    }

    public VendorRespond updateVendor(Integer vendorId, VendorRequest request) {
        Integer actorId = jwtService.getCurrentUserId();
        if (!actorId.equals(vendorId)) {
            log.warn("Unauthorized update attempt on vendor id={} by actor={}", vendorId, actorId);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to access this resource");
        }

        log.info("Updating vendor id={} by actor={}", vendorId, actorId);

        Vendor vendor = accountService.updateVendor(vendorId, request);
        log.info("Called accountService.updateVendor() - Updated id={}, email={}", vendor.getId(), vendor.getAccount().getEmail());

        accountProducer.sendVendorUpdated(actorId, vendor);
        log.info("Called accountProducer.sendVendorUpdated() - Sent event for id={} by actor={}", vendor.getId(), actorId);

        return buildFromVendor(vendor);
    }

    public void deleteVendor(Integer vendorId) {
        Integer actorId = jwtService.getCurrentUserId();
        if (!actorId.equals(vendorId)) {
            log.warn("Unauthorized delete attempt on vendor id={} by actor={}", vendorId, actorId);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to access this resource");
        }

        log.info("Deleting vendor id={} by actor={}", vendorId, actorId);

        accountService.deleteVendor(vendorId);
        log.info("Called accountService.deleteVendor() - Deleted vendor id={}", vendorId);

        accountProducer.sendVendorDeleted(actorId, vendorId);
        log.info("Called accountProducer.sendVendorDeleted() - Sent event for id={} by actor={}", vendorId, actorId);
    }
}
