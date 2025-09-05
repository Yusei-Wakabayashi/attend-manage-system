package com.example.springboot.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springboot.model.Account;
import com.example.springboot.model.StampRequest;

public interface StampRequestRepository extends JpaRepository<StampRequest, Long>
{
    Optional<StampRequest> findByAccountIdAndStampId(Account accountId, Long Id);
}
