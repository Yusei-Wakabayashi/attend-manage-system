package com.example.springboot.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.model.Account;
import com.example.springboot.model.ApprovalSetting;
import com.example.springboot.model.Role;
import com.example.springboot.repository.AccountRepository;

@Service
public class AccountService
{
    // ここでCRUD(Create:作成,Research:検索,Update:更新,Delete:削除に関わる例外処理も行う)
    @Autowired
    private AccountRepository accountRepository;
    
    public Account getAccountByUsername(String username)
    {
        return accountRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません"));
    }

    public List<Account> getAccountByRoleId(Long roleId)
    {
        Role role = new Role();
        role.setId(roleId);
        List<Account> accounts = accountRepository.findByRoleId(role);
        if (accounts.isEmpty())
        {
            throw new RuntimeException("指定された役職が存在しません");
        }
        return accounts;
    }

    public List<Account> getAccountByApprovalSetting(List<ApprovalSetting> approvalSettings)
    {
        List<Role> roles = approvalSettings.stream()
            .map(ApprovalSetting::getApprovalId)
            .collect(Collectors.toList());
        return accountRepository.findByRoleIdIn(roles);
    }

    @Transactional
    public void resetAllTables()
    {
        accountRepository.deleteAll();
        accountRepository.resetAutoIncrement();
    }

    public void init()
    {
        
    }
}