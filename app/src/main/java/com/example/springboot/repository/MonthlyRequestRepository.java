package com.example.springboot.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springboot.model.Account;
import com.example.springboot.model.MonthlyRequest;

public interface MonthlyRequestRepository extends JpaRepository<MonthlyRequest, Long>
{
    Optional<MonthlyRequest> findByAccountIdAndMonthRequestId(Account accountId, Long id);
    List<MonthlyRequest> findByAccountId(Account account);
}
