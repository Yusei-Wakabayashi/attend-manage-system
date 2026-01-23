package com.example.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springboot.model.Account;
import com.example.springboot.model.MonthlyRequest;

public interface MonthlyRequestRepository extends JpaRepository<MonthlyRequest, Long>
{
    MonthlyRequest findByAccountIdAndMonthRequestId(Account accountId, Long id);
    List<MonthlyRequest> findByAccountId(Account account);
    List<MonthlyRequest> findByAccountIdAndYearAndMonth(Account account, int year, int month);
    List<MonthlyRequest> findByAccountIdIn(List<Account> accounts);
}
