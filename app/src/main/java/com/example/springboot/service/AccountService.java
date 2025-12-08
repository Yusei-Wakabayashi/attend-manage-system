package com.example.springboot.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.dto.response.AccountInfoResponse;
import com.example.springboot.dto.response.ApproverListResponse;
import com.example.springboot.model.Account;
import com.example.springboot.model.ApprovalSetting;
import com.example.springboot.model.Department;
import com.example.springboot.model.Role;
import com.example.springboot.repository.AccountRepository;
import com.example.springboot.util.SecurityUtil;

@Service
public class AccountService
{
    private final AccountRepository accountRepository;

    private final ApprovalSettingService approvalSettingService;

    private final RoleService roleService;

    private final DepartmentService departmentService;

    @Autowired
    public AccountService
    (
        AccountRepository accountRepository,
        ApprovalSettingService approvalSettingService,
        RoleService roleService,
        DepartmentService departmentService
    )
    {
        this.accountRepository = accountRepository;
        this.approvalSettingService = approvalSettingService;
        this.roleService = roleService;
        this.departmentService = departmentService;
    }

    // ユーザー名とアカウントの取得
    public Account findCurrentAccount()
    {
        String username = SecurityUtil.getCurrentUsername();
        if (username == null)
        {
            return null;
        }
        Account account = findAccountByUsername(username);

        return account;
    }

    // アカウントから承認者一覧を取得、レスポンスの形に合わせて返す
    public List<ApproverListResponse> findApproverListFor(Account account)
    {
        List<ApprovalSetting> approvalSettings = approvalSettingService.findApprovalSettings(account.getRoleId());
        List<Account> accounts = findAccountByApprovalSetting(approvalSettings);
        return findApproverList(accounts);
    }
    
    // accountInfoを返す
    public AccountInfoResponse getCurrentAccountInfo(Account account)
    {
        AccountInfoResponse accountInfo = new AccountInfoResponse();
        Role role = roleService.findRoleById(account.getRoleId().getId());
        Department department = departmentService.findDepartmentById(account.getDepartmentId().getId());

        List<ApprovalSetting> approvalSettings = approvalSettingService.findApprovalSettingsByApprover(role);
        // 承認者として設定されていなければ空のためfalse、空でなければ承認者のためtrue
        boolean admin = approvalSettings.isEmpty() ? false : true;

        accountInfo.setStatus(1);
        accountInfo.setName(account.getName());
        accountInfo.setDepartmentName(department.getName());
        accountInfo.setRoleName(role.getName());
        accountInfo.setAdmin(admin);
        return accountInfo;
    }

    public Account findAccountByAccountId(Long accountId)
    {
        return accountRepository.findById(accountId)
            .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません"));
    }
    
    public Account findAccountByUsername(String username)
    {
        return accountRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません"));
    }

    public List<Account> findAccountByRoleId(Long roleId)
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

    public List<Account> findAccountByApprovalSetting(List<ApprovalSetting> approvalSettings)
    {
        List<Role> roles = new ArrayList<Role>();
        for(ApprovalSetting approvalSetting: approvalSettings)
        {
            roles.add(approvalSetting.getApprovalId());
        }
        List<Account> accounts = accountRepository.findByRoleIdIn(roles);
        return accounts;
    }
    public List<ApproverListResponse> findApproverList(List<Account> accounts)
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