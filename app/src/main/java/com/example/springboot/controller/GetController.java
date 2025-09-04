package com.example.springboot.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboot.dto.AllStyleListResponse;
import com.example.springboot.dto.response.ApproverListResponse;
import com.example.springboot.dto.response.RequestDetilShiftResponse;
import com.example.springboot.dto.ArrayResponse;
import com.example.springboot.dto.YearMonthParam;
import com.example.springboot.dto.change.LocalDateTimeToString;
import com.example.springboot.dto.input.RequestIdInput;
import com.example.springboot.dto.response.AccountInfoResponse;
import com.example.springboot.dto.response.ShiftListResponse;
import com.example.springboot.model.Account;
import com.example.springboot.model.ApprovalSetting;
import com.example.springboot.model.Department;
import com.example.springboot.model.Role;
import com.example.springboot.model.Shift;
import com.example.springboot.model.ShiftRequest;
import com.example.springboot.service.AccountService;
import com.example.springboot.service.ApprovalSettingService;
import com.example.springboot.service.DepartmentService;
import com.example.springboot.service.RoleService;
import com.example.springboot.service.ShiftRequestService;
import com.example.springboot.service.ShiftService;
import com.example.springboot.service.StylePlaceService;
import com.example.springboot.service.StyleService;
import com.example.springboot.util.SecurityUtil;

@RequestMapping("/api")
@RestController
@CrossOrigin(origins = {"http://localhost:5173"})
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
    DepartmentService departmentService;

    @Autowired
    RoleService roleService;

    @Autowired
    ApprovalSettingService approvalSettingService;

    @Autowired
    StyleService styleService;

    @Autowired
    StylePlaceService stylePlaceService;

    @Autowired
    ShiftService shiftService;

    @Autowired
    ShiftRequestService shiftRequestService;

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

    @GetMapping("/reach/allstylelist")
    public ArrayResponse<AllStyleListResponse> returnAllStyleList(HttpSession session)
    {
        // ログイン済みか確認
        String username = SecurityUtil.getCurrentUsername();
        Account account = accountService.getAccountByUsername(username);
        if(account.equals(null))
        {
            return new ArrayResponse(4, new ArrayList(), "styleList");
        }
        List<AllStyleListResponse> styleListResponse = stylePlaceService.getStyleList();
        return new ArrayResponse(1, styleListResponse, "styleList");
    }

    @GetMapping("/reach/accountinfo")
    public AccountInfoResponse returnAccountInfo(HttpSession session)
    {
        AccountInfoResponse accountInfo = new AccountInfoResponse();
        String username = SecurityUtil.getCurrentUsername();
        Account account = accountService.getAccountByUsername(username);
        // 役職情報の取得
        Role role = roleService.getRoleById(account.getRoleId().getId());
        // 部署情報の取得
        Department department = departmentService.getDepartmentById(account.getDepartmentId().getId());
        // 役職idが承認者として設定されているか確認
        Boolean admin;
        List<ApprovalSetting> approvalSettings = approvalSettingService.getApprovalSettingsByApprover(role);
        if(approvalSettings.isEmpty())
        {
            // 配列が空なら承認者でない
            admin = false;
        }
        else
        {
            // それ以外は設定されているので承認者
            admin = true;
        }
        accountInfo.setStatus(1);
        accountInfo.setName(account.getName());
        accountInfo.setDepartmentName(department.getName());
        accountInfo.setRoleName(role.getName());
        accountInfo.setAdmin(admin);
        return accountInfo;
    }
    @GetMapping("/reach/shiftlist")
    public ArrayResponse returnShiftList(HttpSession session, @ModelAttribute YearMonthParam request)
    {
        String username = SecurityUtil.getCurrentUsername();
        Account account = accountService.getAccountByUsername(username);
        // accountidを基にshift_listテーブルを検索、shiftListResponseに格納
        List<ShiftListResponse> shiftListResponse = new ArrayList();
        // List<Shift> shiftList = shiftService.findByAccountId(account.getId());
        List<Shift> shiftList = shiftService.findByAccountIdAndBeginWorkBetween(account.getId(), request.getYear(), request.getMonth());
        for(Shift shift : shiftList)
        {
            shiftListResponse.add(shiftService.shiftToShiftListResponse(shift));
        }
        return new ArrayResponse(1, shiftListResponse, "shiftList");
    }

    @GetMapping("/reach/requestdetil/shift")
    public RequestDetilShiftResponse returnShiftDetil(HttpSession session, RequestIdInput request)
    {
        LocalDateTimeToString localDateTimeToString = new LocalDateTimeToString();
        int status = 0;
        // securityutilから名前を取得
        String username = SecurityUtil.getCurrentUsername();
        Account account = accountService.getAccountByUsername(username);
        // 認証情報がなければエラー
        if(account.equals(null))
        {
            RequestDetilShiftResponse requestDetilShiftResponse = new RequestDetilShiftResponse();
            status = 5;
            requestDetilShiftResponse.setStatus(status);
            return requestDetilShiftResponse;
        }
        // 名前から取得したアカウントとidで検索にかけ取得できれば返す取得できなければエラー
        ShiftRequest shiftRequest = shiftRequestService.findByAccountIdAndShiftRequestId(account, request.getRequestId());
        status = 1;
        RequestDetilShiftResponse requestDetilShiftResponse = new RequestDetilShiftResponse
        (
            status,
            localDateTimeToString.localDateTimeToString(shiftRequest.getBeginWork()),
            localDateTimeToString.localDateTimeToString(shiftRequest.getEndWork()),
            localDateTimeToString.localDateTimeToString(shiftRequest.getBeginBreak()),
            localDateTimeToString.localDateTimeToString(shiftRequest.getEndBreak()),
            shiftRequest.getRequestComment(),
            localDateTimeToString.localDateTimeToString(shiftRequest.getRequestDate()),
            shiftRequest.getRequestStatus(),
            shiftRequest.getApprover().getId().intValue(),
            shiftRequest.getApprover().getName(),
            shiftRequest.getApproverComment(),
            Objects.isNull(shiftRequest.getApprovalTime()) ? "" : localDateTimeToString.localDateTimeToString(shiftRequest.getApprovalTime())
        );
        return requestDetilShiftResponse;
    }
}