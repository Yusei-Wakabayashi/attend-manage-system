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
import com.example.springboot.model.Account;
// import com.example.springboot.model.Salt;
import com.example.springboot.service.AccountService;
// import com.example.springboot.service.SaltService;

@RequestMapping("/api")
@RestController
@CrossOrigin(origins = {"http://localhost:5173"})
public class PostController
{
    @Autowired
    private AccountService accountService;
    
    @PostMapping("/request")
    public String returns(@RequestBody InputPostData data)
    {
        return data.getText();
    }

    @CrossOrigin
    @PostMapping("/send/login")
    public String login(@RequestBody LoginPostData data, HttpSession session)
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
                return "session true";
            }
            else
            {
                return "tet";
            }
        }
        catch(NoSuchAlgorithmException ex1)
        {
            return "アルゴリズム名が不正";
        }
    }

    @PostMapping("/send/logout")
    public String logout(HttpServletRequest request)
    {
        SecurityContextHolder.clearContext();
        HttpSession session = request.getSession(false);
        if (session != null)
        {
            session.invalidate(); // セッション破棄
        }
        return "true logout";
    }
}