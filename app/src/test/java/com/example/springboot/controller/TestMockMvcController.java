package com.example.springboot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
// import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.springboot.Config;
import com.example.springboot.dto.ApproverListResponse;
import com.example.springboot.model.Account;
import com.example.springboot.model.AccountApprover;
import com.example.springboot.model.Role;
import com.example.springboot.model.Salt;
import com.example.springboot.repository.AccountRepository;
import com.example.springboot.model.ApprovalSetting;
import com.example.springboot.model.Department;
import com.example.springboot.service.AccountApproverService;
import com.example.springboot.service.AccountService;
import com.example.springboot.service.ApprovalSettingService;

@ContextConfiguration(classes = Config.class)
@WebMvcTest({PostController.class,GetController.class})
@AutoConfigureMockMvc
@Import({PostController.class,GetController.class})
public class TestMockMvcController
{
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @MockBean
    private ApprovalSettingService approvalSettingService;

    @MockBean
    private AccountApproverService accountApproverService;

    @MockBean
    AccountRepository accountRepository;

    @Test
    void login_success() throws Exception
    {
        Salt salt = new Salt();
        salt.setText("somesalt");

        Account account = new Account();
        account.setUsername("testuser");
        account.setSaltId(salt);
        byte[] hashed = MessageDigest.getInstance("SHA-256").digest("passwordsomesalt".getBytes());
        account.setPassword(hashed);

        when(accountService.getAccountByUsername("testuser")).thenReturn(account);
        mockMvc.perform(
            post("/api/send/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content
            ("""
                {
                    "username": "testuser",
                    "password": "password"
                }
            """)
            .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(1));
    }

    @Test
    void logout_authenticatedUser_success() throws Exception
    {
        mockMvc.perform(post("/api/send/logout")
            .with(csrf())
            .with(user("testuser")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(1));
    }

    @Test
    void getapproverlist_success() throws Exception
    {
        Long generalRoleId = 1L;
        String generalAccountName = "testuser";
        Long adminRoleId = 2L;
        Long adminAccountId = 5L;
        String adminAccountName = "adminuser";
        String adminDepartmentName = "soumu";
        String adminRoleName = "katyou";

        // whenの処理が実行された際返すaccountの作成
        Account generalAccount = new Account();
        generalAccount.setUsername(generalAccountName);
        // 一般役職と管理役職
        Role generalRole = new Role();
        generalRole.setId(generalRoleId);
        generalAccount.setRoleId(generalRole);
        Role adminRole = new Role();
        adminRole.setId(adminRoleId);
        // whenの処理が実行された際返すapprovalSettingの作成
        List<ApprovalSetting> approvalSettings = new ArrayList<ApprovalSetting>();
        ApprovalSetting approvalSetting = new ApprovalSetting();
        approvalSetting.setRoleId(generalRole);
        approvalSetting.setApprovalId(adminRole);
        approvalSettings.add(approvalSetting);
        // whenの処理が実行された際返すaccountsの作成
        List<Account> accounts = new ArrayList<Account>();
        Account adminAccount = new Account();
        adminAccount.setRoleId(adminRole);
        adminAccount.setUsername(adminAccountName);
        accounts.add(adminAccount);
        ApproverListResponse approverListResponse = new ApproverListResponse(adminAccountId, adminAccountName, adminDepartmentName, adminRoleName);
        List<ApproverListResponse> approverListResponses = new ArrayList();
        approverListResponses.add(approverListResponse);

        when(accountService.getAccountByUsername(generalAccountName)).thenReturn(generalAccount);
        when(approvalSettingService.getApprovalSettings(generalAccount.getRoleId())).thenReturn(approvalSettings);
        when(accountService.getAccountByApprovalSetting(approvalSettings)).thenReturn(accounts);
        when(accountService.getApproverList(accounts)).thenReturn(approverListResponses);
        mockMvc.perform(get("/api/reach/approverlist")
            .with(csrf())
            .with(user("testuser")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(1))
            .andExpect(jsonPath("$.approverlist[0].id").value(adminAccountId))
            .andExpect(jsonPath("$.approverlist[0].name").value(adminAccountName))
            .andExpect(jsonPath("$.approverlist[0].departmentName").value(adminDepartmentName))
            .andExpect(jsonPath("$.approverlist[0].roleName").value(adminRoleName));
    }

    @Test
    void approverset_success() throws Exception
    {
        Account generalAccount = new Account();
        generalAccount.setId(1L);
        generalAccount.setUsername("testuser");
        Account adminAccount = new Account();
        adminAccount.setId(2L);
        generalAccount.setUsername("adminuser");
        AccountApprover accountApprover = new AccountApprover();
        accountApprover.setAccountId(generalAccount);
        accountApprover.setApproverId(adminAccount);
        when(accountService.getAccountByUsername("testuser")).thenReturn(generalAccount);
        when(accountApproverService.getAccountApproverByAccount(generalAccount)).thenReturn(accountApprover);
        when(accountService.getAccountByAccountId(2L)).thenReturn(adminAccount);
        when(accountApproverService.save(accountApprover)).thenReturn("ok");
        mockMvc.perform(
            post("/api/send/approverset")
            .contentType(MediaType.APPLICATION_JSON)
            .content
            ("""
                {
                    "approver": "2"
                }
            """)
            .with(csrf())
            .with(user("testuser")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(1));
    }

    @Test
    void accountServiceGetApproverListSuccess()
    {
        Long adminAccountId = 5L;
        String adminAccountName = "adminuser";
        Long adminDepartmentId = 2L;
        String adminDepartmentName = "soumu";
        Long adminRoleId = 2L;
        String adminRoleName = "katyou";
        Department department = new Department();
        department.setId(adminDepartmentId);
        department.setName(adminDepartmentName);
        Role role = new Role();
        role.setId(adminRoleId);
        role.setName(adminRoleName);
        Account account = new Account();
        account.setId(adminAccountId);
        account.setName(adminAccountName);
        account.setDepartmentId(department);
        account.setRoleId(role);
        
        List<Account> accounts = new ArrayList<>();
        accounts.add(account);
        when(accountService.getApproverList(anyList())).thenCallRealMethod();
        List<ApproverListResponse> approverListResponses = accountService.getApproverList(accounts);
        assertNotNull(approverListResponses);
        assertEquals(adminAccountId, approverListResponses.get(0).getId());
        assertEquals(adminAccountName, approverListResponses.get(0).getName());
        assertEquals(adminDepartmentName, approverListResponses.get(0).getDepartmentName());
        assertEquals(adminRoleName, approverListResponses.get(0).getRoleName());
    }
    // @Test
    // void accountServiceGetAccountByApprovalSetting()
    // {
    //     // serviceに渡すオブジェクトの作成
    //     Long generalRoleId = 1L;
    //     Long adminRoleId = 2L;
    //     Long adminAccountId = 5L;
    //     String adminName = "adminuser";
    //     Role generalRole = new Role();
    //     generalRole.setId(generalRoleId);
    //     Role adminRole = new Role();
    //     adminRole.setId(adminRoleId);
    //     ApprovalSetting approvalSetting = new ApprovalSetting();
    //     approvalSetting.setRoleId(generalRole);
    //     approvalSetting.setApprovalId(adminRole);
    //     List<ApprovalSetting> approvalSettings = new ArrayList();
    //     approvalSettings.add(approvalSetting);
    //     // whenで返すaccountsオブジェクト
    //     List<Account> accounts = new ArrayList();
    //     Account account = new Account();
    //     account.setId(adminAccountId);
    //     account.setName(adminName);
    //     account.setRoleId(adminRole);

    //     List<Role> roles = approvalSettings.stream()
    //         .map(ApprovalSetting::getApprovalId)
    //         .collect(Collectors.toList());
    //     when(accountService.getAccountByApprovalSetting(approvalSettings)).thenCallRealMethod();
    //     when(accountRepository.findByRoleIdIn(roles)).thenReturn(accounts);
    //     List<Account> responseAccounts = accountService.getAccountByApprovalSetting(approvalSettings);
    //     System.out.println(responseAccounts);
    //     assertNotNull(responseAccounts);
    //     assertEquals(adminName, responseAccounts.get(0).getName());
    // }
}