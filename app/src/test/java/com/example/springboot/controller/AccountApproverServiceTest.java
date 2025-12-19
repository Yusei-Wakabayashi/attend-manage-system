package com.example.springboot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;

import com.example.springboot.Config;
import com.example.springboot.dto.response.ApproverResponse;
import com.example.springboot.model.Account;
import com.example.springboot.model.AccountApprover;
import com.example.springboot.model.ApprovalSetting;
import com.example.springboot.model.Department;
import com.example.springboot.model.Role;
import com.example.springboot.repository.AccountApproverRepository;
import com.example.springboot.service.AccountApproverService;
import com.example.springboot.service.AccountService;
import com.example.springboot.service.ApprovalSettingService;

@ContextConfiguration(classes = Config.class)
@ExtendWith(MockitoExtension.class)
public class AccountApproverServiceTest
{

    @InjectMocks
    @Spy
    AccountApproverService accountApproverService;  // ← 部分モックが重要！

    @Mock
    AccountService accountService;

    @Mock
    AccountApproverRepository accountApproverRepository;

    @Mock
    ApprovalSettingService approvalSettingService;

    @Test
    void updateApproverSuccess()
    {
        // 必要なデータを設定
        Account account = new Account();
        String accountUsername = "testuser";
        Long accountId = 49L;
        Role generalRole = new Role();
        Long generalRoleId = 48L;
        generalRole.setId(generalRoleId);
        account.setId(accountId);
        account.setUsername(accountUsername);
        account.setRoleId(generalRole);

        Account newAdmin = new Account();
        Long newAdminId = 43L;
        Role adminRole = new Role();
        Long adminRoleId = 40L;
        adminRole.setId(adminRoleId);
        newAdmin.setId(newAdminId);
        newAdmin.setRoleId(adminRole);

        AccountApprover approver = new AccountApprover();
        approver.setAccountId(account);

        ApprovalSetting approvalSetting = new ApprovalSetting();

        // accountServiceの動作を定義
        when(accountService.findAccountByAccountId(newAdminId)).thenReturn(newAdmin);
        when(approvalSettingService.findApprovalSettingByAccountAndApprover(any(Role.class),any(Role.class))).thenReturn(approvalSetting);

        // 同じサービス内メソッドを上書き
        doReturn(approver).when(accountApproverService).findAccountApproverByAccount(any(Account.class));
        doReturn(approver).when(accountApproverService).save(any(AccountApprover.class));

        // 実行
        int result = accountApproverService.updateApprover(account, newAdminId);

        // 想定通りか確認
        assertEquals(1, result);
    }


    @Test
    void getApproverResponseSuccess()
    {
        Account generalAccount = new Account();
        Long generalAccountId = 3L;
        String generalAccountUsername = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        Account adminAccount = new Account();
        Long adminAccountId = 4L;
        String adminAccountName = "かまどたかしろう";
        Department adminAccountDepartment = new Department();
        String adminAccountDepartmentName = "総務";
        adminAccountDepartment.setName(adminAccountDepartmentName);
        Role adminAccountRole = new Role();
        String adminAccountRoleName = "課長";
        adminAccountRole.setName(adminAccountRoleName);
        adminAccount.setId(adminAccountId);
        adminAccount.setName(adminAccountName);
        adminAccount.setDepartmentId(adminAccountDepartment);
        adminAccount.setRoleId(adminAccountRole);

        AccountApprover accountApprover = new AccountApprover();
        accountApprover.setAccountId(generalAccount);
        accountApprover.setApproverId(adminAccount);

        doReturn(accountApprover).when(accountApproverService).findAccountApproverByAccount(any(Account.class));

        ApproverResponse approverResponse = accountApproverService.getApproverResponse(generalAccount);
        assertEquals(1, approverResponse.getStatus());
    }
}
