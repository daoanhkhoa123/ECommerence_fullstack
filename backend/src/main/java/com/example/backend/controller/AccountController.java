package com.example.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.backend.application.AccountApplication;
import com.example.backend.dto.CustomerRequest;
import com.example.backend.dto.CustomerRespond;
import com.example.backend.dto.VendorRequest;
import com.example.backend.dto.VendorRespond;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {
    private final AccountApplication accountApplication;

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<CustomerRespond> getCustomer(@PathVariable Integer customerId)
    {
        CustomerRespond respond = accountApplication.findCustomerById(customerId);
        return ResponseEntity.ok(respond);
    }

    @PostMapping("/cusomter/register")
    public ResponseEntity<CustomerRespond> registerCustomer(
        @Valid @RequestBody CustomerRequest request) {
            CustomerRespond respond = accountApplication.registerCustomer(request);
            return ResponseEntity.ok(respond);
    }


    @PatchMapping("/customer/update/{customerId}")
    public ResponseEntity<CustomerRespond> updateCustomer(
        @PathVariable Integer customerId,
        @Valid @RequestBody CustomerRequest request) {

            CustomerRespond respond = accountApplication.updateCustomer(customerId, request);
            return ResponseEntity.ok(respond);
    }

    
    @DeleteMapping("/customers/{customerId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Integer customerId) {
        accountApplication.deleteCustomer(customerId);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/vendor/{vendorId}")
    public ResponseEntity<VendorRespond> getVendor(@PathVariable Integer vendorId)
    {
        VendorRespond respond = accountApplication.findVendorById(vendorId);
        return ResponseEntity.ok(respond);
    }

    @PostMapping("/vendor/register")
    public ResponseEntity<VendorRespond> registerVendor(
        @Valid @RequestBody VendorRequest request) {
            VendorRespond respond = accountApplication.registerVendor(request);
            return ResponseEntity.ok(respond);
    }


    @PatchMapping("/vendor/update/{vendorId}")
    public ResponseEntity<VendorRespond> updateVendor(
        @PathVariable Integer vendorId,
        @Valid @RequestBody VendorRequest request) {
            
            VendorRespond respond = accountApplication.updateVendor(vendorId, request);
            return ResponseEntity.ok(respond);
    }


    @DeleteMapping("/vendors/{vendorId}")
    public ResponseEntity<Void> deleteVendor(@PathVariable Integer vendorId) {
        accountApplication.deleteVendor(vendorId);
        return ResponseEntity.noContent().build();
    }

}