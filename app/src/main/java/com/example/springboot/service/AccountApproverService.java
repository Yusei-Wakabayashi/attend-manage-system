package com.example.springboot.service;

import java.util.List;

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
    public AccountApprover getAccountApproverByAccount(Account account)
    {
        return accountApproverRepository.findByAccountId(account)
            .orElseThrow(() -> new RuntimeException("承認者が見つかりません"));
    }

    public List<AccountApprover> findByApproverId(Account approver)
    {
        return accountApproverRepository.findByApproverId(approver);
    }

    public AccountApprover getAccountAndApprover(Long accountId, Long approverId)
    {
        Account account = new Account();
        Account approver = new Account();
        account.setId(accountId);
        approver.setId(approverId);
        return accountApproverRepository.findByAccountIdAndApproverId(account, approver)
            .orElseThrow(() -> new RuntimeException("情報が見つかりません"));
    }

    public AccountApprover getAccountAndApprover(Account account, Long approverId)
    {
        Account approver = new Account();
        approver.setId(approverId);
        return accountApproverRepository.findByAccountIdAndApproverId(account, approver)
            .orElseThrow(() -> new RuntimeException("情報が見つかりません"));
    }

    public AccountApprover getAccountAndApprover(Long accountId, Account approver)
    {
        Account account = new Account();
        account.setId(accountId);
        return accountApproverRepository.findByAccountIdAndApproverId(account, approver)
            .orElseThrow(() -> new RuntimeException("情報が見つかりません"));
    }

    public AccountApprover getAccountAndApprover(Account account, Account approver)
    {
        return accountApproverRepository.findByAccountIdAndApproverId(account, approver)
            .orElseThrow(() -> new RuntimeException("情報が見つかりません"));
    }

    public String save(AccountApprover accountApprover)
    {
        accountApproverRepository.save(accountApprover);
        return "ok";
    }
    @Transactional
    public void resetAllTables()
    {
        accountApproverRepository.deleteAll();
        accountApproverRepository.resetAutoIncrement();
    }

    public void init()
    {
        
    }
}
