package com.example.backend.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.backend.entity.Account;
import com.example.backend.repository.AccountRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) 
    throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found: "+username));

        return User.builder().username(account.getEmail())
        .password(account.getPasswordHash())
        .roles(account.getRole().name()).build();
    }

}
