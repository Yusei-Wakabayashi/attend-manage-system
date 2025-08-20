package com.example.springboot.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.dto.ApproverListResponse;
import com.example.springboot.model.Account;
import com.example.springboot.model.ApprovalSetting;
import com.example.springboot.model.Role;
import com.example.springboot.repository.AccountRepository;

@Service
public class AccountService
{
    @Autowired
    private AccountRepository accountRepository;

    public Account getAccountByAccountId(Long accountId)
    {
        return accountRepository.findById(accountId)
            .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません"));
    }
    
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
        List<Role> roles = new ArrayList();
        for(ApprovalSetting approvalSetting: approvalSettings)
        {
            roles.add(approvalSetting.getApprovalId());
        }
        List<Account> accounts = accountRepository.findByRoleIdIn(roles);
        return accounts;
    }
    public List<ApproverListResponse> getApproverList(List<Account> accounts)
    {
        List<ApproverListResponse> responseList = new ArrayList<>();

        for (Account account : accounts)
        {
            // nullでなければ名前を取得
            String roleName = account.getRoleId() != null ? account.getRoleId().getName() : null;
            String departmentName = account.getDepartmentId() != null ? account.getDepartmentId().getName() : null;

            ApproverListResponse response = new ApproverListResponse
            (
                account.getId(),
                account.getName(),
                departmentName,
                roleName
            );

            responseList.add(response);
        }

        return responseList;
    }

    public String save(Account account)
    {
        accountRepository.save(account);
        return "ok";
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