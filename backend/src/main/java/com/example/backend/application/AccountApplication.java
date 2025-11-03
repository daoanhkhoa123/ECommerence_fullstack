package com.example.backend.application;

import org.springframework.stereotype.Service;

import com.example.backend.config.JwtService;
import com.example.backend.dto.CustomerRequest;
import com.example.backend.dto.CustomerRespond;
import com.example.backend.dto.VendorRequest;
import com.example.backend.dto.VendorRespond;
import com.example.backend.entity.Customer;
import com.example.backend.entity.Vendor;
import com.example.backend.kafka.producer.AccountProducer;
import com.example.backend.service.AccountService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AccountApplication {
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

    // -------------------- CUSTOMER --------------------

    public CustomerRespond findCustomerById(Integer customerId) {
        Integer actorId = jwtService.getCurrentUserId();
        Customer customer = accountService.findCustomerById(customerId);

        accountProducer.sendCustomerRead(actorId, customerId);

        return buildFromCustomer(customer);
    }

    public CustomerRespond registerCustomer(CustomerRequest request) {
        Integer actorId = jwtService.getCurrentUserId();
        Customer customer = accountService.registerCustomer(request);

        accountProducer.sendCustomerCreated(actorId, customer);

        return buildFromCustomer(customer);
    }

    public CustomerRespond updateCustomer(Integer customerId, CustomerRequest request) {
        Integer actorId = jwtService.getCurrentUserId();
        Customer customer = accountService.updateCustomer(customerId, request);

        accountProducer.sendCustomerUpdated(actorId, customer);

        return buildFromCustomer(customer);
    }

    public void deleteCustomer(Integer customerId) {
        Integer actorId = jwtService.getCurrentUserId();

        accountProducer.sendCustomerDeleted(actorId, customerId);

        accountService.deleteCustomer(customerId);
    }

    // -------------------- VENDOR --------------------

    public VendorRespond findVendorById(Integer vendorId) {
        Integer actorId = jwtService.getCurrentUserId();
        Vendor vendor = accountService.findVendorById(vendorId);

        accountProducer.sendVendorRead(actorId, vendorId);

        return buildFromVendor(vendor);
    }

    public VendorRespond registerVendor(VendorRequest request) {
        Integer actorId = jwtService.getCurrentUserId();
        Vendor vendor = accountService.registerVendor(request);

        accountProducer.sendVendorCreated(actorId, vendor);

        return buildFromVendor(vendor);
    }

    public VendorRespond updateVendor(Integer vendorId, VendorRequest request) {
        Integer actorId = jwtService.getCurrentUserId();
        Vendor vendor = accountService.updateVendor(vendorId, request);

        accountProducer.sendVendorUpdated(actorId, vendor);

        return buildFromVendor(vendor);
    }

    public void deleteVendor(Integer vendorId) {
        Integer actorId = jwtService.getCurrentUserId();
        accountService.deleteVendor(vendorId);

        accountProducer.sendVendorDeleted(actorId, vendorId);

    }
}
