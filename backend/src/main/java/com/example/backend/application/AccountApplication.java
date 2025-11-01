package com.example.backend.application;

import org.springframework.stereotype.Service;

import com.example.backend.dto.CustomerRequest;
import com.example.backend.dto.CustomerRespond;
import com.example.backend.dto.VendorRequest;
import com.example.backend.dto.VendorRespond;
import com.example.backend.entity.Customer;
import com.example.backend.entity.Vendor;
import com.example.backend.service.AccountService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AccountApplication {
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
        return buildFromCustomer(customer);
    }

    public VendorRespond findVendorById(Integer vendorId)
    {
        Vendor vendor = accountService.findVendorById(vendorId);
        return buildFromVendor(vendor);
    }

    public CustomerRespond registerCustomer(CustomerRequest request)
    {
        Customer customer = accountService.registerCustomer(request);
        return buildFromCustomer(customer);
    }

    public VendorRespond registerVendor(VendorRequest request)
    {
        Vendor vendor = accountService.registerVendor(request);
        return buildFromVendor(vendor);
    }

    public CustomerRespond updateCustomer(Integer customerId, CustomerRequest request)
    {
        Customer customer = accountService.updateCustomer(customerId, request);
        return buildFromCustomer(customer);
    }

    public VendorRespond updateVendor(Integer vendorid, VendorRequest request)
    {
        Vendor vendor = accountService.updateVendor(vendorid, request);
        return buildFromVendor(vendor);
    }

    public void deleteCustomer(Integer customerId)
    {
        accountService.deleteCustomer(customerId);
    }

    public void deleteVendor(Integer vendorId)
    {
        accountService.deleteVendor(vendorId);
    }
}
