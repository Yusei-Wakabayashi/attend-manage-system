package com.example.springboot.controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboot.dto.InputPostData;
import com.example.springboot.dto.LoginPostData;
import com.example.springboot.dto.Response;
import com.example.springboot.dto.IdData;
import com.example.springboot.model.Account;
import com.example.springboot.model.AccountApprover;
import com.example.springboot.service.AccountApproverService;
// import com.example.springboot.model.Salt;
import com.example.springboot.service.AccountService;
// import com.example.springboot.service.SaltService;
import com.example.springboot.util.SecurityUtil;

@RequestMapping("/api")
@RestController
@CrossOrigin(origins = {"http://localhost:5173"})
public class PostController
{
    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountApproverService accountApproverService;
    
    @PostMapping("/request")
    public String returns(@RequestBody InputPostData data)
    {
        return data.getText();
    }

    @CrossOrigin
    @PostMapping("/send/login")
    public Response login(@RequestBody LoginPostData data, HttpSession session)
    {
        try{
            MessageDigest md = MessageDigest.getInstance("SHA256");
            String username = data.getUsername();
            String password = data.getPassword();
            Account account = accountService.getAccountByUsername(username);
            byte[] perfectPassword = md.digest((password + account.getSaltId().getText()).getBytes());
            if (Arrays.equals(perfectPassword, account.getPassword()))
            {
                // 保存する認証情報を作成
                UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(account.getUsername(), null); // 認証が完了しているためpasswordなどの情報は保存していない
                // 認証情報を保存
                SecurityContextHolder.getContext().setAuthentication(authToken);
                return new Response(1);
            }
            else
            {
                return new Response(6);
            }
        }
        catch(NoSuchAlgorithmException ex1)
        {
            return new Response(4);
        }
    }

    @PostMapping("/send/logout")
    public Response logout(HttpServletRequest request)
    {
        SecurityContextHolder.clearContext();
        HttpSession session = request.getSession(false);
        if (session != null)
        {
            session.invalidate(); // セッション破棄
        }
        return new Response(1);
    }

    @PostMapping("/send/approverset")
    public Response approverset(@RequestBody IdData idData, HttpSession session)
    {
        String username = SecurityUtil.getCurrentUsername();
        Account account = accountService.getAccountByUsername(username);
        AccountApprover accountApprover = accountApproverService.getAccountApproverByAccount(account);
        Account newAccount = accountService.getAccountByAccountId(idData.getId());
        accountApprover.setApproverId(newAccount);
        String response = accountApproverService.save(accountApprover);
        if (response.equals("ok"))
        {
            return new Response(1);
        }
        return new Response(4);
    }
}