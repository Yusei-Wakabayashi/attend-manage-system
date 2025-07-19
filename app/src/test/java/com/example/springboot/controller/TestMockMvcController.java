package com.example.springboot.controller;

import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

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
import com.example.springboot.model.Account;
import com.example.springboot.model.Role;
import com.example.springboot.model.Salt;
import com.example.springboot.model.ApprovalSetting;
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
            .andExpect(content().string("session true"));
    }

    @Test
    void logout_authenticatedUser_success() throws Exception
    {
        mockMvc.perform(post("/api/send/logout")
            .with(csrf())
            .with(user("testuser")))
            .andExpect(status().isOk())
            .andExpect(content().string("true logout"));
    }

    @Test
    void getapproverlist_success() throws Exception
    {
        // whenの処理が実行された際返すaccountの作成
        Account generalAccount = new Account();
        generalAccount.setUsername("testuser");
        // 一般役職と管理役職
        Role generalRole = new Role();
        generalRole.setId(1L);
        generalAccount.setRoleId(generalRole);
        Role adminRole = new Role();
        adminRole.setId(2L);
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
        adminAccount.setUsername("adminuser");
        accounts.add(adminAccount);

        when(accountService.getAccountByUsername("testuser")).thenReturn(generalAccount);
        when(approvalSettingService.getApprovalSettings(generalAccount.getRoleId())).thenReturn(approvalSettings);
        when(accountService.getAccountByApprovalSetting(approvalSettings)).thenReturn(accounts);
        mockMvc.perform(get("/api/reach/approverlist")
            .with(csrf())
            .with(user("testuser")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(1))
            .andExpect(jsonPath("$.approverlist[0].username").value("adminuser"));
    }
}