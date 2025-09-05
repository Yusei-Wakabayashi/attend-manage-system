package com.example.springboot.service;

import javax.persistence.Entity;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.springboot.model.Account;
import com.example.springboot.model.StampRequest;
import com.example.springboot.repository.StampRequestRepository;

@Entity
public class StampRequestService
{
    @Autowired
    private StampRequestRepository stampRequestRepository;

    public StampRequest findByAccountIdAndStampId(Account account, Long id)
    {
        return stampRequestRepository.findByAccountIdAndStampId(account, id)
            .orElseThrow(() -> new RuntimeException("打刻漏れ申請がありません"));
    }

    public StampRequest findByAccountIdAndStampId(Long accountId, Long id)
    {
        Account account = new Account();
        account.setId(accountId);
        return stampRequestRepository.findByAccountIdAndStampId(account, id)
            .orElseThrow(() -> new RuntimeException("打刻漏れ申請がありません"));
    }
}
