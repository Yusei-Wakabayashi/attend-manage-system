package com.example.springboot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verifyNoInteractions;

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
import com.example.springboot.model.Account;
import com.example.springboot.repository.AccountRepository;
import com.example.springboot.service.AccountService;

@ContextConfiguration(classes = Config.class)
@ExtendWith(MockitoExtension.class)
public class AccountServiceTest
{
    @InjectMocks
    @Spy
    AccountService accountService;

    @Mock
    AccountRepository accountRepository;

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
        System.out.println(account);
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
        
    }
}
