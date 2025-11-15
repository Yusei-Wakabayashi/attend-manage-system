package com.example.springboot.service;

import java.util.List;
import java.util.Objects;

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

    @Autowired
    AccountService accountService;

    @Transactional
    public int updateApprover(String username, Long newAccountId)
    {
        Account account = accountService.findAccountByUsername(username);
        if(Objects.isNull(account))
        {
            return 3;
        }
        AccountApprover accountApprover = findAccountApproverByAccount(account);
        Account newAdmin = accountService.findAccountByAccountId(newAccountId);
        if(Objects.isNull(newAdmin))
        {
            return 3;
        }
        accountApprover.setApproverId(newAdmin);
        AccountApprover resultAccountApprover = save(accountApprover);
        if(Objects.isNull(resultAccountApprover) || Objects.isNull(resultAccountApprover))
        {
            return 4;
        }
        return 1;
    }

    public AccountApprover findAccountApproverByAccount(Account account)
    {
        return accountApproverRepository.findByAccountId(account)
            .orElseThrow(() -> new RuntimeException("承認者が見つかりません"));
    }

    public List<AccountApprover> findByApproverId(Account approver)
    {
        return accountApproverRepository.findByApproverId(approver);
    }

    public AccountApprover findAccountAndApprover(Long accountId, Long approverId)
    {
        Account account = new Account();
        Account approver = new Account();
        account.setId(accountId);
        approver.setId(approverId);
        return accountApproverRepository.findByAccountIdAndApproverId(account, approver)
            .orElseThrow(() -> new RuntimeException("情報が見つかりません"));
    }

    public AccountApprover findAccountAndApprover(Account account, Long approverId)
    {
        Account approver = new Account();
        approver.setId(approverId);
        return accountApproverRepository.findByAccountIdAndApproverId(account, approver)
            .orElseThrow(() -> new RuntimeException("情報が見つかりません"));
    }

    public AccountApprover findAccountAndApprover(Long accountId, Account approver)
    {
        Account account = new Account();
        account.setId(accountId);
        return accountApproverRepository.findByAccountIdAndApproverId(account, approver)
            .orElseThrow(() -> new RuntimeException("情報が見つかりません"));
    }

    public AccountApprover findAccountAndApprover(Account account, Account approver)
    {
        return accountApproverRepository.findByAccountIdAndApproverId(account, approver)
            .orElseThrow(() -> new RuntimeException("情報が見つかりません"));
    }

    public AccountApprover save(AccountApprover accountApprover)
    {
        return accountApproverRepository.save(accountApprover);
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
