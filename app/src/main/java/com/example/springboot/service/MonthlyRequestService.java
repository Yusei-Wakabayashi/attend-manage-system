package com.example.springboot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.model.Account;
import com.example.springboot.model.MonthlyRequest;
import com.example.springboot.repository.MonthlyRequestRepository;

@Service
public class MonthlyRequestService
{
    @Autowired
    private MonthlyRequestRepository monthlyRequestRepository;
    public MonthlyRequest findByAccountIdAndMothlyRequestId(Long accountId, Long id)
    {
        Account account = new Account();
        account.setId(accountId);
        return monthlyRequestRepository.findByAccountIdAndMonthRequestId(account, id)
            .orElseThrow(() -> new RuntimeException(""));
    }

    public MonthlyRequest findByAccountIdAndMothlyRequestId(Account account, Long id)
    {
        return monthlyRequestRepository.findByAccountIdAndMonthRequestId(account, id)
            .orElseThrow(() -> new RuntimeException(""));
    }

    public List<MonthlyRequest> findByAccountId(Long accountId)
    {
        Account account = new Account();
        account.setId(accountId);
        return monthlyRequestRepository.findByAccountId(account);
    }

    public List<MonthlyRequest> findByAccountIdAndYearAndMonth(Long accountId, int year, int month)
    {
        Account account = new Account();
        account.setId(accountId);
        List<MonthlyRequest> monthlyRequests = monthlyRequestRepository.findByAccountIdAndYearAndMonth(account, year, month);
        return monthlyRequests;
    }

    public List<MonthlyRequest> findByAccountIdAndYearAndMonth(Account account, int year, int month)
    {
        List<MonthlyRequest> monthlyRequests = monthlyRequestRepository.findByAccountIdAndYearAndMonth(account, year, month);
        return monthlyRequests;
    }

    public List<MonthlyRequest> findByAccountId(Account account)
    {
        return monthlyRequestRepository.findByAccountId(account);
    }

    public List<MonthlyRequest> findAll()
    {
        return monthlyRequestRepository.findAll();
    }
}
