package com.example.springboot.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import com.example.springboot.dto.AllStyleListResponse;
import com.example.springboot.dto.ApproverListResponse;
import com.example.springboot.model.Account;
import com.example.springboot.model.AccountApprover;
import com.example.springboot.model.Role;
import com.example.springboot.model.Salt;
import com.example.springboot.model.Style;
import com.example.springboot.model.StylePlace;
import com.example.springboot.repository.AccountRepository;
import com.example.springboot.model.ApprovalSetting;
import com.example.springboot.service.AccountApproverService;
import com.example.springboot.service.AccountService;
import com.example.springboot.service.ApprovalSettingService;
import com.example.springboot.service.StylePlaceService;
import com.example.springboot.service.StyleService;

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
    private AccountRepository accountRepository;

    @MockBean
    private StyleService styleService;

    @MockBean
    private StylePlaceService stylePlaceService;

    @Test
    void loginSuccess() throws Exception
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
    void logoutAuthenticatedUserSuccess() throws Exception
    {
        mockMvc.perform(post("/api/send/logout")
            .with(csrf())
            .with(user("testuser")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(1));
    }

    @Test
    void getapproverlistSuccess() throws Exception
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
    void approversetSuccess() throws Exception
    {
        Long generalAccountId = 1L;
        String generalAccountName = "testuser";
        Long adminAccountId = 2L;
        String adminAccountName = "adminuser";
        Long newAdminAccountId = 3L;
        String newAdminAccountName = "newadmin";
        Account generalAccount = new Account();
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountName);
        Account adminAccount = new Account();
        adminAccount.setId(adminAccountId);
        adminAccount.setUsername(adminAccountName);
        Account newAdminAccount = new Account();
        newAdminAccount.setId(newAdminAccountId);
        newAdminAccount.setUsername(newAdminAccountName);
        AccountApprover accountApprover = new AccountApprover();
        accountApprover.setAccountId(generalAccount);
        accountApprover.setApproverId(adminAccount);
        AccountApprover newAccountApprover = new AccountApprover();
        newAccountApprover.setAccountId(generalAccount);
        newAccountApprover.setApproverId(newAdminAccount);
        when(accountService.getAccountByUsername(generalAccountName)).thenReturn(generalAccount);
        when(accountApproverService.getAccountApproverByAccount(generalAccount)).thenReturn(accountApprover);
        when(accountService.getAccountByAccountId(newAdminAccountId)).thenReturn(newAdminAccount);
        when(accountApproverService.save(any())).thenReturn("ok");
        mockMvc.perform(
            post("/api/send/approverset")
            .contentType(MediaType.APPLICATION_JSON)
            .content
            ("""
                {
                    "approver": "3"
                }
            """)
            .with(csrf())
            .with(user(generalAccountName))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(1));
    }
    @Test
    void getAllStyleListSuccess() throws Exception
    {
        // whenで返すアカウントオブジェクト
        String generalAccountName = "testuser";
        Long generalAccountId = 1L;
        Account generalAccount = new Account();
        generalAccount.setId(generalAccountId);
        generalAccount.setName(generalAccountName);
        // whenで返すスタイルリストオブジェクト
        Long workStylePlaceId = 1L;
        Long homeStylePlaceId = 2L;
        String workStylePlaceName = "work";
        String homeStylePlaceName = "home";
        List<AllStyleListResponse> styleListResponse = new ArrayList();
        AllStyleListResponse styleTypeWork = new AllStyleListResponse();
        AllStyleListResponse styleTypeHome = new AllStyleListResponse();
        styleTypeWork.setId(workStylePlaceId);
        styleTypeHome.setId(homeStylePlaceId);
        styleTypeWork.setName(workStylePlaceName);
        styleTypeHome.setName(homeStylePlaceName);

        styleListResponse.add(styleTypeWork);
        styleListResponse.add(styleTypeHome);

        when(accountService.getAccountByUsername(generalAccountName)).thenReturn(generalAccount);
        when(stylePlaceService.getStyleList()).thenReturn(styleListResponse);
        mockMvc.perform(
            get("/api/reach/allstylelist")
            .with(csrf())
            .with(user(generalAccountName))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(1))
        .andExpect(jsonPath("$.styleList[0].name").value(workStylePlaceName))
        .andExpect(jsonPath("$.styleList[1].name").value(homeStylePlaceName));

    }
    @Test
    void styleSetSuccess() throws Exception
    {
        // whenで返すアカウントのオブジェクト
        String generalAccountUsername = "testuser";
        Long generalAccountId = 1L;
        Account generalAccount = new Account();
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);
        // whenで返すスタイルのオブジェクト
        Long generalStyleId = 1L;
        Long generalStylePlaceId = 1L;
        Style style = new Style();
        StylePlace stylePlace = new StylePlace();
        stylePlace.setId(generalStylePlaceId);
        style.setStyleId(generalStyleId);
        style.setAccountId(generalAccount);
        style.setStylePlaceId(stylePlace);
        // whenで返すスタイルプレイスのオブジェクト
        Long generalNewStylePlaceId = 2L;
        StylePlace newStylePlace = new StylePlace();
        newStylePlace.setId(generalNewStylePlaceId);
        // whenでセーブする際の条件に使う
        Style newStyle = new Style();
        newStyle.setStyleId(generalStyleId);
        newStyle.setAccountId(generalAccount);
        newStyle.setStylePlaceId(newStylePlace);
        // whenでセーブした際に返す文字列
        String responseText = "ok";

        when(accountService.getAccountByUsername(generalAccountUsername)).thenReturn(generalAccount);
        when(styleService.getStyleByAccountId(generalAccount.getId())).thenReturn(style);
        when(stylePlaceService.getStylePlaceById(generalNewStylePlaceId)).thenReturn(newStylePlace);
        when(styleService.save(any())).thenReturn(responseText);
        mockMvc.perform(
            post("/api/send/style")
            .contentType(MediaType.APPLICATION_JSON)
            .content
            ("""
                {
                    "style": "2"
                }
            """)
            .with(csrf())
            .with(user("testuser"))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(1));
    }
}