package com.example.springboot.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springboot.model.Account;
import com.example.springboot.model.OverTimeRequest;

public interface OverTimeRequestRepository extends JpaRepository<OverTimeRequest, Long>
{
    Optional<OverTimeRequest> findByAccountIdAndOverTimeId(Account accountId, Long Id);
}