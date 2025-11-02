package com.example.backend.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.backend.entity.Customer;
import com.example.backend.entity.Vendor;
import com.example.backend.repository.CustomerRepository;
import com.example.backend.repository.VendorRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthencationService {
    private final JwtService jwtService;
    private final CustomerRepository customerRepository;
    private final VendorRepository vendorRepository;

    private Integer findCustomerIdByAccountId(Integer accountId) {
        return customerRepository.findByAccount_Id(accountId)
                .map(Customer::getId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Customer not found for accountId: " + accountId));
    }

    private Integer findVendorIdByAccountId(Integer accountId) {
        return vendorRepository.findByAccount_Id(accountId)
                .map(Vendor::getId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Vendor not found for accountId: " + accountId));
    }

    public Integer findCurrentCustomerId()
    {
        Integer accountId = jwtService.getCurrentUserId();
        return findCustomerIdByAccountId(accountId);
    }

    public Integer findCurrentVendorId()
    {
        Integer accountId = jwtService.getCurrentUserId();
        return findVendorIdByAccountId(accountId);
    }
}
