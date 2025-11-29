package com.example.springboot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;

import com.example.springboot.Config;
import com.example.springboot.dto.response.AccountInfoResponse;
import com.example.springboot.dto.response.ApproverListResponse;
import com.example.springboot.model.Account;
import com.example.springboot.model.ApprovalSetting;
import com.example.springboot.model.Department;
import com.example.springboot.model.Role;
import com.example.springboot.repository.AccountRepository;
import com.example.springboot.service.AccountService;
import com.example.springboot.service.ApprovalSettingService;
import com.example.springboot.service.DepartmentService;
import com.example.springboot.service.RoleService;

@ContextConfiguration(classes = Config.class)
@ExtendWith(MockitoExtension.class)
public class AccountServiceTest
{
    @InjectMocks
    @Spy
    AccountService accountService;

    @Mock
    AccountRepository accountRepository;

    @Mock
    ApprovalSettingService approvalSettingService;

    @Mock
    RoleService roleService;

    @Mock
    DepartmentService departmentService;

    @AfterEach
    void tearDown()
    {
        // テストごとにクリアしておく
        SecurityContextHolder.clearContext();
    }

    @Test
    void findCurrentAccountAuthenticationFailure()
    {
        // 前提: SecurityContext に何も入っていない
        SecurityContextHolder.clearContext();
        // テストメソッド呼び出し
        Account result = accountService.findCurrentAccount();
        // 今回は何もないのでnullが返ってくるとよし
        assertEquals(result, null);
        // repository は呼ばれないはず
        verifyNoInteractions(accountRepository);
    }

    @Test
    void findCurrentAccountAuthenticationSuccess()
    {
        // 認証情報をセット
        String username = "testuser";
        UsernamePasswordAuthenticationToken auth =
            new UsernamePasswordAuthenticationToken
                (
                    username,
                    null,
                    List.of(new SimpleGrantedAuthority("ROLE_USER"))
                );
        SecurityContextHolder.getContext().setAuthentication(auth);

        // findAccountByUsername の戻り値をモックする
        Account account = new Account();
        account.setUsername(username);
        // 同じサービス内メソッドを上書き
        doReturn(account).when(accountService).findAccountByUsername(anyString());

        // テストメソッド呼び出し
        Account result = accountService.findCurrentAccount();

        // whenで返したアカウントと同一か
        assertEquals(account, result);
    }

    @Test
    void findApproverListForSuccess()
    {
        Account generalAccount = new Account();
        Role generalRole = new Role();
        Long generalRoleId = 10L;
        generalRole.setId(generalRoleId);
        generalAccount.setRoleId(generalRole);

        ApprovalSetting approvalSettingFirst = new ApprovalSetting();
        ApprovalSetting approvalSettingSecond = new ApprovalSetting();
        List<ApprovalSetting> approvalSettings = List.of(approvalSettingFirst, approvalSettingSecond);

        Account approverFirst = new Account();
        Account approverSecond = new Account();
        List<Account> approverAccounts = List.of(approverFirst, approverSecond);
        ApproverListResponse arrayListResponseFirst = new ApproverListResponse();
        ApproverListResponse arrayListResponseSecond = new ApproverListResponse();
        List<ApproverListResponse> approverListResponses = List.of(arrayListResponseFirst, arrayListResponseSecond);

        when(approvalSettingService.findApprovalSettings(any(Role.class))).thenReturn(approvalSettings);

        doReturn(approverAccounts).when(accountService).findAccountByApprovalSetting(approvalSettings);
        doReturn(approverListResponses).when(accountService).findApproverList(approverAccounts);

        // 呼び出し
        List<ApproverListResponse> result = accountService.findApproverListFor(generalAccount);

        // 戻り値がモックしたリストと同じ
        assertEquals(approverListResponses, result);
    }

    @Test
    void getCurrentAccountInfoSuccess()
    {
        Long generalAccountId = 1L;
        String generalAccountName = "testuser";
        Long generalRoleId = 34L;
        String generalRoleName = "kakarityou";
        Long generalDepartmentId = 4L;
        String generalDepartmentName = "soumu";

        Account account = new Account();
        Role role = new Role();
        Department department = new Department();
        role.setId(generalRoleId);
        role.setName(generalRoleName);
        department.setId(generalDepartmentId);
        department.setName(generalDepartmentName);
        account.setId(generalAccountId);
        account.setName(generalAccountName);
        account.setRoleId(role);
        account.setDepartmentId(department);

        List<ApprovalSetting> approvalSettings = new ArrayList<ApprovalSetting>();

        when(roleService.findRoleById(anyLong())).thenReturn(role);
        when(departmentService.findDepartmentById(anyLong())).thenReturn(department);
        when(approvalSettingService.findApprovalSettingsByApprover(any(Role.class))).thenReturn(approvalSettings);

        AccountInfoResponse result = accountService.getCurrentAccountInfo(account);

        assertEquals(1, result.getStatus());
        assertEquals(generalAccountName, result.getName());
        assertEquals(generalRoleName, result.getRoleName());
        assertEquals(generalDepartmentName, result.getDepartmentName());
        assertEquals(false, result.isAdmin());
    }

    @Test
    void adminGetCurrentAccountInfoSuccess()
    {
        Long adminAccountId = 1L;
        String adminAccountName = "testuser";
        Long adminRoleId = 34L;
        String adminRoleName = "kakarityou";
        Long adminDepartmentId = 4L;
        String adminDepartmentName = "soumu";

        Account account = new Account();
        Role role = new Role();
        Department department = new Department();
        role.setId(adminRoleId);
        role.setName(adminRoleName);
        department.setId(adminDepartmentId);
        department.setName(adminDepartmentName);
        account.setId(adminAccountId);
        account.setName(adminAccountName);
        account.setRoleId(role);
        account.setDepartmentId(department);

        List<ApprovalSetting> approvalSettings = new ArrayList<ApprovalSetting>();
        ApprovalSetting approvalSetting = new ApprovalSetting();
        approvalSettings.add(approvalSetting);

        when(roleService.findRoleById(anyLong())).thenReturn(role);
        when(departmentService.findDepartmentById(anyLong())).thenReturn(department);
        when(approvalSettingService.findApprovalSettingsByApprover(any(Role.class))).thenReturn(approvalSettings);

        AccountInfoResponse result = accountService.getCurrentAccountInfo(account);

        assertEquals(1, result.getStatus());
        assertEquals(adminAccountName, result.getName());
        assertEquals(adminRoleName, result.getRoleName());
        assertEquals(adminDepartmentName, result.getDepartmentName());
        assertEquals(true, result.isAdmin());
    }


}
