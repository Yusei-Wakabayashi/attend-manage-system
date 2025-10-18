package com.example.springboot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;

import com.example.springboot.Config;
import com.example.springboot.dto.AllStyleListResponse;
import com.example.springboot.dto.response.ApproverListResponse;
import com.example.springboot.dto.response.ShiftListResponse;
import com.example.springboot.model.Account;
import com.example.springboot.model.ApprovalSetting;
import com.example.springboot.model.Department;
import com.example.springboot.model.Role;
import com.example.springboot.model.Shift;
import com.example.springboot.model.StylePlace;
import com.example.springboot.repository.AccountRepository;
import com.example.springboot.repository.ShiftRepository;
import com.example.springboot.repository.StylePlaceRepository;
import com.example.springboot.service.AccountService;
import com.example.springboot.service.ShiftService;
import com.example.springboot.service.StylePlaceService;

@ContextConfiguration(classes = Config.class)
@AutoConfigureMockMvc
// 使用するserviceを追記
@Import({AccountService.class, StylePlaceService.class, ShiftService.class})
@SpringBootTest
public class TestJpaController
{
    @Autowired
    private AccountService accountService;

    @Autowired
    private StylePlaceService stylePlaceService;

    @Autowired
    private ShiftService shiftService;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private StylePlaceRepository stylePlaceRepository;

    @MockBean
    private ShiftRepository shiftRepository;
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
        List<ApproverListResponse> approverListResponses = accountService.getApproverList(accounts);
        assertNotNull(approverListResponses);
        assertEquals(adminAccountId, approverListResponses.get(0).getId());
        assertEquals(adminAccountName, approverListResponses.get(0).getName());
        assertEquals(adminDepartmentName, approverListResponses.get(0).getDepartmentName());
        assertEquals(adminRoleName, approverListResponses.get(0).getRoleName());
    }
    @Test
    void accountServiceGetAccountByApprovalSettingSuccess()
    {
        // serviceに渡すオブジェクトの作成
        Long generalRoleId = 1L;
        Long adminRoleId = 2L;
        Long adminAccountId = 5L;
        String adminName = "adminuser";
        Role generalRole = new Role();
        generalRole.setId(generalRoleId);
        Role adminRole = new Role();
        adminRole.setId(adminRoleId);
        ApprovalSetting testApprovalSetting = new ApprovalSetting();
        testApprovalSetting.setRoleId(generalRole);
        testApprovalSetting.setApprovalId(adminRole);
        List<ApprovalSetting> approvalSettings = new ArrayList<ApprovalSetting>();
        approvalSettings.add(testApprovalSetting);
        // whenで返すaccountsオブジェクト
        List<Account> accounts = new ArrayList<Account>();
        Account account = new Account();
        account.setId(adminAccountId);
        account.setName(adminName);
        account.setRoleId(adminRole);
        accounts.add(account);
        // whenの条件を指定するため作成
        List<Role> roles = new ArrayList<Role>();
        for(ApprovalSetting approvalSetting: approvalSettings)
        {
            roles.add(approvalSetting.getApprovalId());
        }
        when(accountRepository.findByRoleIdIn(roles)).thenReturn(accounts);
        // 以下はテストしたいserviceのメソッドを実行している
        List<Account> responseAccounts = accountService.getAccountByApprovalSetting(approvalSettings);
        assertNotNull(responseAccounts);
        assertEquals(adminName, responseAccounts.get(0).getName());
    }
    @Test
    void stylePlaceServiceGetStyleList()
    {
        // whenで返オブジェクトを作成成
        Long workStyleId = 1L;
        Long homeStyleId = 2L;
        String workStyleName = "work";
        String homeStyleName = "home";
        List<StylePlace> styles = new ArrayList<StylePlace>();
        StylePlace workStyle = new StylePlace();
        workStyle.setId(workStyleId);
        workStyle.setName(workStyleName);
        StylePlace homeStyle = new StylePlace();
        homeStyle.setId(homeStyleId);
        homeStyle.setName(homeStyleName);
        styles.add(workStyle);
        styles.add(homeStyle);

        when(stylePlaceRepository.findAll()).thenReturn(styles);
        List<AllStyleListResponse> response = stylePlaceService.getStyleList();
        assertNotNull(response);
        assertEquals(workStyleName, response.get(0).getName());
        assertEquals(homeStyleName, response.get(1).getName());
    }

    @Test
    void shiftToShiftListResponseSuccess()
    {
        Long generalAccountId = 1L;
        Account generalAccount = new Account();
        generalAccount.setId(generalAccountId);
        Long generalShiftId = 1L;
        String time = "00:00:00";
        String beginWork = "2025/06/21/08/30/00";
        String endWork = "2025/06/21/17/30/00";
        String beginBreak = "2025/06/21/11/30/00";
        String endBreak = "2025/06/21/12/30/30";
        LocalDateTime generalShiftBeginWork = LocalDateTime.parse(beginWork,DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        LocalDateTime generalShiftEndWork = LocalDateTime.parse(endWork,DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        LocalDateTime generalShiftBeginBreak = LocalDateTime.parse(beginBreak,DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        LocalDateTime generalShiftEndBreak = LocalDateTime.parse(endBreak,DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        Time generalShiftLateness = Time.valueOf(time);
        Time generalShiftLeaveEarly = Time.valueOf(time);
        Time generalShiftOuting = Time.valueOf(time);
        Time generalShiftOverWork = Time.valueOf(time);
        Shift generalShift = new Shift
        (
            generalShiftId, generalAccount, generalShiftBeginWork,
            generalShiftEndWork, generalShiftBeginBreak, generalShiftEndBreak,
            generalShiftLateness, generalShiftLeaveEarly, generalShiftOuting, generalShiftOverWork
        );
        ShiftListResponse shiftListResponse = shiftService.shiftToShiftListResponse(generalShift);
        assertNotNull(shiftListResponse);
    }
}