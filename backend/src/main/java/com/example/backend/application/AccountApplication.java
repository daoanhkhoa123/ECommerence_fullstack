package com.example.backend.application;

import org.springframework.stereotype.Service;

import com.example.backend.dto.CustomerRequest;
import com.example.backend.dto.CustomerRespond;
import com.example.backend.dto.VendorRequest;
import com.example.backend.dto.VendorRespond;
import com.example.backend.entity.Customer;
import com.example.backend.entity.Vendor;
import com.example.backend.kafka.dto.CustomerEvent;
import com.example.backend.kafka.dto.VendorEvent;
import com.example.backend.kafka.enums.CRUDType;
import com.example.backend.kafka.producer.AccountProducer;
import com.example.backend.service.AccountService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AccountApplication {
    private final AccountProducer accountProducer;
    private final AccountService accountService;

    private CustomerRespond buildFromCustomer(Customer cusomter)
    {
        return new CustomerRespond(cusomter.getId(), cusomter.getAccount().getEmail(), 
        cusomter.getFullName(), cusomter.getPhone(), 
        cusomter.getAddress(), cusomter.getBirthDate());
    }

    private VendorRespond buildFromVendor(Vendor vendor)
    {
        return new VendorRespond(vendor.getId(), vendor.getAccount().getEmail(), 
        vendor.getShopName(), vendor.getDescription(), vendor.getPhone());
    }

    public CustomerRespond findCustomerById(Integer customerId)
    {
        Customer customer = accountService.findCustomerById(customerId);
        
        CustomerEvent customerEvent = accountProducer.buildFromCustomer(CRUDType.READ, customerId);
        accountProducer.sendCustomer(customerEvent);

        return buildFromCustomer(customer);
    }

    public VendorRespond findVendorById(Integer vendorId)
    {
        Vendor vendor = accountService.findVendorById(vendorId);

        VendorEvent vendorEvent = accountProducer.buildFromVendor(CRUDType.READ, vendorId);
        accountProducer.sendVendor(vendorEvent);

        return buildFromVendor(vendor);
    }

    public CustomerRespond registerCustomer(CustomerRequest request)
    {
        Customer customer = accountService.registerCustomer(request);

        CustomerEvent customerEvent = accountProducer.buildFromCustomer(CRUDType.CREATE, customer);
        accountProducer.sendCustomer(customerEvent);

        return buildFromCustomer(customer);
    }

    public VendorRespond registerVendor(VendorRequest request)
    {
        Vendor vendor = accountService.registerVendor(request);

        VendorEvent vendorEvent = accountProducer.buildFromVendor(CRUDType.CREATE, vendor);
        accountProducer.sendVendor(vendorEvent); 

        return buildFromVendor(vendor);
    }

    public CustomerRespond updateCustomer(Integer customerId, CustomerRequest request)
    {
        Customer customer = accountService.updateCustomer(customerId, request);

        CustomerEvent customerEvent = accountProducer.buildFromCustomer(CRUDType.UPDATE, customer);
        accountProducer.sendCustomer(customerEvent);    

        return buildFromCustomer(customer);
    }

    public VendorRespond updateVendor(Integer vendorid, VendorRequest request)
    {
        Vendor vendor = accountService.updateVendor(vendorid, request);

        VendorEvent vendorEvent = accountProducer.buildFromVendor(CRUDType.UPDATE, vendor);
        accountProducer.sendVendor(vendorEvent);

        return buildFromVendor(vendor);
    }

    public void deleteCustomer(Integer customerId)
    {
        CustomerEvent customerEvent = accountProducer.buildFromCustomer(CRUDType.DELETE, customerId);
        accountProducer.sendCustomer(customerEvent);

        accountService.deleteCustomer(customerId);
    }

    public void deleteVendor(Integer vendorId)
    {
        VendorEvent vendorEvent = accountProducer.buildFromVendor(CRUDType.DELETE, vendorId);
        accountProducer.sendVendor(vendorEvent);

        accountService.deleteVendor(vendorId);
    }
}
