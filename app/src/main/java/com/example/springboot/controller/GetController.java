package com.example.springboot.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboot.dto.ApproverListResponse;
import com.example.springboot.dto.ArrayResponse;
import com.example.springboot.model.Account;
import com.example.springboot.model.ApprovalSetting;
import com.example.springboot.service.AccountService;
import com.example.springboot.service.ApprovalSettingService;
import com.example.springboot.util.SecurityUtil;

@RequestMapping("/api")
@RestController
public class GetController
{
    // @GetMapping("/reach/sl")
    // public ArrayResponse<Shift> returnShiftList(HttpSession session)
    // {
    //     String userId = "Id";

    // }
    @Autowired
    AccountService accountService;

    @Autowired
    ApprovalSettingService approvalSettingService;

    @GetMapping("/reach/approverlist")
    public ArrayResponse<ApproverListResponse> returnApproverList(HttpSession session)
    {
        // 認証情報にあるリクエストを送ってきたユーザー名を取得
        String username = SecurityUtil.getCurrentUsername();
        // ユーザー名からアカウントオブジェクトを取得
        Account account = accountService.getAccountByUsername(username);
        // 承認者として適切な役職の取得
        List<ApprovalSetting> approvalSettings = approvalSettingService.getApprovalSettings(account.getRoleId());
        // 役職を基に承認者の取得
        List<Account> accounts = accountService.getAccountByApprovalSetting(approvalSettings);
        List<ApproverListResponse> approverListResponses = accountService.getApproverList(accounts);
        return new ArrayResponse<>(1,approverListResponses, "approverlist");
    }
}