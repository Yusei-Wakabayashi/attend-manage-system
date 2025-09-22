package com.example.springboot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.model.Account;
import com.example.springboot.model.OverTimeRequest;
import com.example.springboot.repository.OverTimeRequestRepository;


@Service
public class OverTimeRequestService
{
    @Autowired
    private OverTimeRequestRepository overTimeRequestRepository;
    
    public OverTimeRequest findByAccountIdAndOverTimeRequestId(Long accountId, Long id)
    {
        Account account = new Account();
        account.setId(accountId);
        return overTimeRequestRepository.findByAccountIdAndOverTimeId(account, id)
            .orElseThrow(() -> new RuntimeException("残業申請が見つかりません"));
    }

    public OverTimeRequest findByAccountIdAndOverTimeRequestId(Account account, Long id)
    {
        return overTimeRequestRepository.findByAccountIdAndOverTimeId(account, id)
            .orElseThrow(() -> new RuntimeException("残業申請が見つかりません"));
    }

    public List<OverTimeRequest> findByAccountId(Long accountId)
    {
        Account account = new Account();
        account.setId(accountId);
        return overTimeRequestRepository.findByAccountId(account);
    }

    public List<OverTimeRequest> findByAccountId(Account account)
    {
        return overTimeRequestRepository.findByAccountId(account);
    }

    public String save(OverTimeRequest overTimeRequest)
    {
        overTimeRequestRepository.save(overTimeRequest);
        return "ok";
    }
}