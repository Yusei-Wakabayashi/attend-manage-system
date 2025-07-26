package com.example.springboot.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.model.Account;
import com.example.springboot.model.AccountApprover;
import com.example.springboot.repository.AccountApproverRepository;

@Service
public class AccountApproverService 
{
    @Autowired
    AccountApproverRepository accountApproverRepository;
    @Transactional
    public AccountApprover getAccountApproverByAccount(Account account)
    {
        return accountApproverRepository.findByAccountId(account)
            .orElseThrow(() -> new RuntimeException("承認者が見つかりません"));
    }

    public String save(AccountApprover accountApprover)
    {
        accountApproverRepository.save(accountApprover);
        return "ok";
    }
    public void resetAllTables()
    {
        accountApproverRepository.deleteAll();
        accountApproverRepository.resetAutoIncrement();
    }

    public void init()
    {
        
    }
}
