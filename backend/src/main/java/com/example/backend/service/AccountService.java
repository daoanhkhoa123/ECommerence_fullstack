package com.example.backend.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.example.backend.dto.AccountRegisterRequest;
import com.example.backend.dto.CustomerRequest;
import com.example.backend.dto.VendorRequest;
import com.example.backend.entity.Account;
import com.example.backend.entity.Customer;
import com.example.backend.entity.Vendor;
import com.example.backend.enums.AccountRole;
import com.example.backend.repository.AccountRepository;
import com.example.backend.repository.CustomerRepository;
import com.example.backend.repository.VendorRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final VendorRepository vendorRepository;
    private final PasswordEncoder passwordEncoder;
    
    private Account setByRequest(Account account, AccountRegisterRequest request)
    {
        account.setEmail(request.email());
        account.setPasswordHash(passwordEncoder.encode(request.password()));
        return account;
    }

    private Customer setByRequest(Customer customer, CustomerRequest request)
    {
        Account account = setByRequest(customer.getAccount(), request.accountRequest());
        account.setRole(AccountRole.CUSTOMER);
        
        customer.setAccount(account);
        customer.setFullName(request.fullName());
        customer.setPhone(request.phone());
        customer.setAddress(request.address());
        customer.setBirthDate(request.birthDate());
        return customer;
    }

    private Vendor setByRequest(Vendor vendor, VendorRequest request)
    {
        Account account = setByRequest(vendor.getAccount(), request.accountRequest());
        account.setRole(AccountRole.VENDOR);

        vendor.setAccount(account);
        vendor.setShopName(request.shopName());
        vendor.setDescription(request.description());
        vendor.setPhone(request.phone());
        return vendor;
    }

    public Customer findCustomerById(Integer id)
    {
        return customerRepository.findById(id).orElseThrow(() -> 
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found with id: " + id));
    }

    public Vendor findVendorById(Integer id)
    {
        return vendorRepository.findById(id).orElseThrow(() -> 
        new ResponseStatusException(HttpStatus.NOT_FOUND, "Vendor not found with id: "+id));
    }

    public Vendor registerVendor(VendorRequest request)
    {
        Vendor vendor = setByRequest(new Vendor(), request);
        return vendorRepository.save(vendor);
    }

    public Customer registerCustomer(CustomerRequest request)
    {
        Customer customer = setByRequest(new Customer(), request);
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(Integer customerId, CustomerRequest request)
    {
        Customer customer = customerRepository.findById(customerId)
         .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));
        customer = setByRequest(customer, request);
        
        return customerRepository.save(customer);        
    }

    public Vendor updateVendor(Integer vendorId, VendorRequest request)
    {
        Vendor vendor = vendorRepository.findById(vendorId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vendor not found"));
        vendor = setByRequest(vendor, request);
        
        return vendorRepository.save(vendor);
    }

    public void deleteCustomer(Integer customerId) {
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));

        Account account = customer.getAccount();

        customerRepository.delete(customer);

        if (account != null) {
            accountRepository.delete(account);
        }
    }

    public void deleteVendor(Integer vendorId) {
        Vendor vendor = vendorRepository.findById(vendorId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vendor not found"));

        Account account = vendor.getAccount();

        vendorRepository.delete(vendor);

        if (account != null) {
            accountRepository.delete(account);
        }
    }

}
