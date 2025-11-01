package com.example.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.backend.dto.CustomerRequest;
import com.example.backend.dto.CustomerRespond;
import com.example.backend.dto.VendorRequest;
import com.example.backend.dto.VendorRespond;
import com.example.backend.service.AccountService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<CustomerRespond> getCustomer(@PathVariable Integer customerId)
    {
        CustomerRespond respond = accountService.getCustomer(customerId);
        return ResponseEntity.ok(respond);
    }

    @PostMapping("/cusomter/register")
    public ResponseEntity<CustomerRespond> registerCustomer(
        @Valid @RequestBody CustomerRequest request) {
            CustomerRespond respond = accountService.registerCustomer(request);
            return ResponseEntity.ok(respond);
    }


    @PatchMapping("/customer/update/{customerId}")
    public ResponseEntity<CustomerRespond> updateCustomer(
        @PathVariable Integer customerId,
        @Valid @RequestBody CustomerRequest request) {

            CustomerRespond respond = accountService.updateCustomer(customerId, request);
            return ResponseEntity.ok(respond);
    }

    
    @DeleteMapping("/customers/{customerId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Integer customerId) {
        accountService.deleteCustomer(customerId);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/vendor/{vendorId}")
    public ResponseEntity<VendorRespond> getVendor(@PathVariable Integer vendorId)
    {
        VendorRespond respond = accountService.getVendor(vendorId);
        return ResponseEntity.ok(respond);
    }

    @PostMapping("/vendor/register")
    public ResponseEntity<VendorRespond> registerVendor(
        @Valid @RequestBody VendorRequest request) {
            VendorRespond respond = accountService.registerVendor(request);
            return ResponseEntity.ok(respond);
    }


    @PatchMapping("/vendor/update/{vendorId}")
    public ResponseEntity<VendorRespond> updateVendor(
        @PathVariable Integer vendorId,
        @Valid @RequestBody VendorRequest request) {
            
            VendorRespond respond = accountService.updateVendor(vendorId, request);
            return ResponseEntity.ok(respond);
    }


    @DeleteMapping("/vendors/{vendorId}")
    public ResponseEntity<Void> deleteVendor(@PathVariable Integer vendorId) {
        accountService.deleteVendor(vendorId);
        return ResponseEntity.noContent().build();
    }

}