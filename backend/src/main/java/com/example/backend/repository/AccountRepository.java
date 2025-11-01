package com.example.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    boolean existsByEmail(String email);
}
