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
import com.example.springboot.model.Account;
import com.example.springboot.model.AccountApprover;
import com.example.springboot.repository.AccountApproverRepository;
import com.example.springboot.service.AccountApproverService;
import com.example.springboot.service.AccountService;

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

    @Test
    void updateApproverSuccess()
    {
        // 必要なデータを設定
        Account account = new Account();
        String accountUsername = "testuser";
        Long accountId = 49L;
        account.setId(accountId);
        account.setUsername(accountUsername);

        Account newAdmin = new Account();
        Long newAdminId = 43L;
        newAdmin.setId(newAdminId);

        AccountApprover approver = new AccountApprover();
        approver.setAccountId(account);

        // accountServiceの動作を定義
        when(accountService.findAccountByAccountId(newAdminId)).thenReturn(newAdmin);

        // 同じサービス内メソッドを上書き
        doReturn(approver).when(accountApproverService).findAccountApproverByAccount(any(Account.class));
        doReturn(approver).when(accountApproverService).save(any(AccountApprover.class));

        // 実行
        int result = accountApproverService.updateApprover(account, newAdminId);

        // 想定通りか確認
        assertEquals(1, result);
    }
}
