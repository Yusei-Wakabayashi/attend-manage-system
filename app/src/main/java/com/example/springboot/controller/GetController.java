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
import com.example.springboot.dto.response.AttendListResponse;
import com.example.springboot.dto.response.RequestDetilOtherTimeResponse;
import com.example.springboot.dto.response.RequestDetilOverTimeResponse;
import com.example.springboot.dto.response.RequestDetilShiftChangeResponse;
import com.example.springboot.dto.response.RequestDetilShiftResponse;
import com.example.springboot.dto.response.RequestDetilStampResponse;
import com.example.springboot.dto.response.RequestDetilVacationResponse;
import com.example.springboot.dto.ArrayResponse;
import com.example.springboot.dto.YearMonthParam;
import com.example.springboot.dto.change.LocalDateTimeToString;
import com.example.springboot.dto.input.RequestIdInput;
import com.example.springboot.dto.response.AccountInfoResponse;
import com.example.springboot.dto.response.ShiftListResponse;
import com.example.springboot.model.Account;
import com.example.springboot.model.ApprovalSetting;
import com.example.springboot.model.Attend;
import com.example.springboot.model.AttendanceExceptionRequest;
import com.example.springboot.model.Department;
import com.example.springboot.model.OverTimeRequest;
import com.example.springboot.model.Role;
import com.example.springboot.model.Shift;
import com.example.springboot.model.ShiftChangeRequest;
import com.example.springboot.model.ShiftRequest;
import com.example.springboot.model.StampRequest;
import com.example.springboot.model.VacationRequest;
import com.example.springboot.service.AccountService;
import com.example.springboot.service.ApprovalSettingService;
import com.example.springboot.service.AttendService;
import com.example.springboot.service.AttendanceExceptionRequestService;
import com.example.springboot.service.DepartmentService;
import com.example.springboot.service.OverTimeRequestService;
import com.example.springboot.service.RoleService;
import com.example.springboot.service.ShiftChangeRequestService;
import com.example.springboot.service.ShiftRequestService;
import com.example.springboot.service.ShiftService;
import com.example.springboot.service.StampRequestService;
import com.example.springboot.service.StylePlaceService;
import com.example.springboot.service.StyleService;
import com.example.springboot.service.VacationRequestService;
import com.example.springboot.util.SecurityUtil;

@RequestMapping("/api")
@RestController
@CrossOrigin(origins = {"http://localhost:5173"})
public class GetController
{
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

    @Autowired
    ShiftChangeRequestService shiftChangeRequestService;

    @Autowired
    StampRequestService stampRequestService;

    @Autowired
    AttendService attendService;

    @Autowired
    VacationRequestService vacationRequestService;

    @Autowired
    OverTimeRequestService overTimeRequestService;

    @Autowired
    AttendanceExceptionRequestService attendanceExceptionRequestService;

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
        List<AllStyleListResponse> styleListResponse = new ArrayList<AllStyleListResponse>();
        ArrayResponse<AllStyleListResponse> arrayResponse = new ArrayResponse<AllStyleListResponse>();
        if(account.equals(null))
        {
            arrayResponse.setStatus(4);
            arrayResponse.setList(styleListResponse);
            arrayResponse.setKey("styleList");
            return arrayResponse;
        }
        styleListResponse = stylePlaceService.getStyleList();
        arrayResponse.setStatus(1);
        arrayResponse.setList(styleListResponse);
        arrayResponse.setKey("styleList");
        return arrayResponse;
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
    public ArrayResponse<ShiftListResponse> returnShiftList(HttpSession session, @ModelAttribute YearMonthParam request)
    {
        String username = SecurityUtil.getCurrentUsername();
        Account account = accountService.getAccountByUsername(username);
        // accountidを基にshift_listテーブルを検索、shiftListResponseに格納
        List<ShiftListResponse> shiftListResponse = new ArrayList<ShiftListResponse>();
        ArrayResponse<ShiftListResponse> arrayResponse = new ArrayResponse<ShiftListResponse>();
        // List<Shift> shiftList = shiftService.findByAccountId(account.getId());
        List<Shift> shiftList = shiftService.findByAccountIdAndBeginWorkBetween(account.getId(), request.getYear(), request.getMonth());
        for(Shift shift : shiftList)
        {
            shiftListResponse.add(shiftService.shiftToShiftListResponse(shift));
        }
        arrayResponse.setStatus(1);
        arrayResponse.setList(shiftListResponse);
        arrayResponse.setKey("shiftList");
        return arrayResponse;
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
    @GetMapping("/reach/requestdetil/changetime")
    public RequestDetilShiftChangeResponse returnShiftChangeDetil(HttpSession session, RequestIdInput request)
    {
        LocalDateTimeToString localDateTimeToString = new LocalDateTimeToString();
        int status = 0;
        // securityutilから名前を取得
        String username = SecurityUtil.getCurrentUsername();
        Account account = accountService.getAccountByUsername(username);
        RequestDetilShiftChangeResponse requestDetilShiftChangeResponse = new RequestDetilShiftChangeResponse();
        // 認証情報がなければエラー
        if(account.equals(null))
        {
            status = 5;
            requestDetilShiftChangeResponse.setStatus(status);
            return requestDetilShiftChangeResponse;
        }
        // 名前から取得したアカウントとidで検索にかけ取得できれば返す取得できなければエラー
        ShiftChangeRequest shiftChangeRequest = shiftChangeRequestService.findByAccountIdAndShiftChangeRequestId(account, request.getRequestId());
        if(shiftChangeRequest.equals(null))
        {
            status = 3;
            return requestDetilShiftChangeResponse;
        }
        status = 1;
        requestDetilShiftChangeResponse.setStatus(status);
        requestDetilShiftChangeResponse.setShiftId(shiftChangeRequest.getShiftId().getShiftId().intValue());
        requestDetilShiftChangeResponse.setBeginWork(localDateTimeToString.localDateTimeToString(shiftChangeRequest.getBeginWork()));
        requestDetilShiftChangeResponse.setEndWork(localDateTimeToString.localDateTimeToString(shiftChangeRequest.getEndWork()));
        requestDetilShiftChangeResponse.setBeginBreak(localDateTimeToString.localDateTimeToString(shiftChangeRequest.getBeginBreak()));
        requestDetilShiftChangeResponse.setEndBreak(localDateTimeToString.localDateTimeToString(shiftChangeRequest.getEndBreak()));
        requestDetilShiftChangeResponse.setRequestComment(shiftChangeRequest.getRequestComment());
        requestDetilShiftChangeResponse.setRequestDate(localDateTimeToString.localDateTimeToString(shiftChangeRequest.getRequestDate()));
        requestDetilShiftChangeResponse.setRequestStatus(shiftChangeRequest.getRequestStatus());
        requestDetilShiftChangeResponse.setApproverId(shiftChangeRequest.getApprover().getId().intValue());
        requestDetilShiftChangeResponse.setApproverName(shiftChangeRequest.getApprover().getName());
        requestDetilShiftChangeResponse.setApproverComment(shiftChangeRequest.getApproverComment());
        requestDetilShiftChangeResponse.setApprovalTime(Objects.isNull(shiftChangeRequest.getApprovalTime()) ? "" : localDateTimeToString.localDateTimeToString(shiftChangeRequest.getApprovalTime()));

        return requestDetilShiftChangeResponse;
    }

    @GetMapping("/reach/requestdetil/stamp")
    public RequestDetilStampResponse returnStampDetil(HttpSession session, RequestIdInput request)
    {
        LocalDateTimeToString localDateTimeToString = new LocalDateTimeToString();
        int status = 0;
        // securityutilから名前を取得
        String username = SecurityUtil.getCurrentUsername();
        Account account = accountService.getAccountByUsername(username);
        RequestDetilStampResponse requestDetilStampResponse = new RequestDetilStampResponse();
        // 認証情報がなければエラー
        if(Objects.isNull(account))
        {
            status = 5;
            requestDetilStampResponse.setStatus(status);
            return requestDetilStampResponse;
        }
        StampRequest stampRequest = stampRequestService.findByAccountIdAndStampId(account, request.getRequestId());
        // 名前から取得したアカウントとidで検索にかけ取得できれば返す取得できなければエラー
        if(stampRequest.equals(null))
        {
            status = 5;
            requestDetilStampResponse.setStatus(status);
            return requestDetilStampResponse;
        }
        status = 1;
        requestDetilStampResponse.setStatus(status);
        requestDetilStampResponse.setShiftId(stampRequest.getShiftId().getShiftId().intValue());
        requestDetilStampResponse.setBeginWork(localDateTimeToString.localDateTimeToString(stampRequest.getBeginWork()));
        requestDetilStampResponse.setEndWork(localDateTimeToString.localDateTimeToString(stampRequest.getEndWork()));
        requestDetilStampResponse.setBeginBreak(localDateTimeToString.localDateTimeToString(stampRequest.getBeginBreak()));
        requestDetilStampResponse.setEndBreak(localDateTimeToString.localDateTimeToString(stampRequest.getEndBreak()));
        requestDetilStampResponse.setRequestComment(stampRequest.getRequestComment());
        requestDetilStampResponse.setRequestDate(localDateTimeToString.localDateTimeToString(stampRequest.getRequestDate()));
        requestDetilStampResponse.setRequestStatus(stampRequest.getRequestStatus());
        requestDetilStampResponse.setApproverId(stampRequest.getApprover().getId().intValue());
        requestDetilStampResponse.setApproverName(stampRequest.getApprover().getName());
        requestDetilStampResponse.setApproverComment(stampRequest.getApproverComment());
        requestDetilStampResponse.setApprovalTime(Objects.isNull(stampRequest.getApprovalTime()) ? "" : localDateTimeToString.localDateTimeToString(stampRequest.getApprovalTime()));
        return requestDetilStampResponse;
    }
    @GetMapping("/reach/attendlist")
    public ArrayResponse<AttendListResponse> returnAttendList(HttpSession session, @ModelAttribute YearMonthParam request)
    {
        String username = SecurityUtil.getCurrentUsername();
        Account account = accountService.getAccountByUsername(username);
        List<AttendListResponse> attendListResponse = new ArrayList<AttendListResponse>();
        List<Attend> attendList = attendService.findByAccountIdAndBeginWorkBetween(account.getId(), request.getYear(), request.getMonth());
        ArrayResponse<AttendListResponse> arrayResponse = new ArrayResponse<AttendListResponse>();
        for(Attend attend : attendList)
        {
            attendListResponse.add(attendService.attendToAttendListResponse(attend));
        }
        arrayResponse.setStatus(1);
        arrayResponse.setList(attendListResponse);
        arrayResponse.setKey("attendList");
        return arrayResponse;
    }

    @GetMapping("/reach/requestdetil/vacation")
    public RequestDetilVacationResponse returnVacationDetil(HttpSession session, RequestIdInput request)
    {
        LocalDateTimeToString localDateTimeToString = new LocalDateTimeToString();
        RequestDetilVacationResponse requestDetilVacation = new RequestDetilVacationResponse();
        // securityutilから名前を取得
        String username = SecurityUtil.getCurrentUsername();
        Account account = accountService.getAccountByUsername(username);
        int status = 0;
        if(Objects.isNull(account))
        {
            status = 4;
            requestDetilVacation.setStatus(status);
            return requestDetilVacation;
        }
        VacationRequest vacationRequest = vacationRequestService.findByAccountIdAndVacationId(account, request.getRequestId());
        if(Objects.isNull(vacationRequest))
        {
            status = 4;
            requestDetilVacation.setStatus(status);
            return requestDetilVacation;
        }
        status = 1;
        requestDetilVacation.setStatus(status);
        requestDetilVacation.setShiftId(vacationRequest.getShiftId().getShiftId().intValue());
        requestDetilVacation.setVacationType(vacationRequest.getVacationTypeId().getVacationTypeId().intValue());
        requestDetilVacation.setBeginVacation(localDateTimeToString.localDateTimeToString(vacationRequest.getBeginVacation()));
        requestDetilVacation.setEndVacation(localDateTimeToString.localDateTimeToString(vacationRequest.getEndVacation()));
        requestDetilVacation.setRequestComment(vacationRequest.getRequestComment());
        requestDetilVacation.setRequestDate(localDateTimeToString.localDateTimeToString(vacationRequest.getRequestDate()));
        requestDetilVacation.setRequestStatus(vacationRequest.getRequestStatus());
        requestDetilVacation.setApproverId(vacationRequest.getApprover().getId().intValue());
        requestDetilVacation.setApproverName(vacationRequest.getApprover().getName());
        requestDetilVacation.setApproverComment(vacationRequest.getApproverComment());
        requestDetilVacation.setApprovalTime(Objects.isNull(vacationRequest.getApprovalTime()) ? "" : localDateTimeToString.localDateTimeToString(vacationRequest.getApprovalTime()));
        return requestDetilVacation;
    }

    @GetMapping("/reach/requestdetil/overtime")
    public RequestDetilOverTimeResponse returnOverTimeDetil(HttpSession session, RequestIdInput request)
    {
        LocalDateTimeToString localDateTimeToString = new LocalDateTimeToString();
        RequestDetilOverTimeResponse requestDetilOverTimeResponse = new RequestDetilOverTimeResponse();
        // securityutilから名前を取得
        String username = SecurityUtil.getCurrentUsername();
        Account account = accountService.getAccountByUsername(username);
        int status = 0;
        if(Objects.isNull(account))
        {
            status = 4;
            requestDetilOverTimeResponse.setStatus(status);
            return requestDetilOverTimeResponse;
        }
        OverTimeRequest overTimeRequest = overTimeRequestService.findByAccountIdAndOverTimeRequestId(account, request.getRequestId());
        if(Objects.isNull(overTimeRequest))
        {
            status = 4;
            requestDetilOverTimeResponse.setStatus(status);
            return requestDetilOverTimeResponse;
        }
        status = 1;
        requestDetilOverTimeResponse.setStatus(status);
        requestDetilOverTimeResponse.setShiftId(overTimeRequest.getShiftId().getShiftId().intValue());
        requestDetilOverTimeResponse.setBeginOverWork(localDateTimeToString.localDateTimeToString(overTimeRequest.getBeginWork()));
        requestDetilOverTimeResponse.setEndOverWork(localDateTimeToString.localDateTimeToString(overTimeRequest.getEndWork()));
        requestDetilOverTimeResponse.setRequestComment(overTimeRequest.getRequestComment());
        requestDetilOverTimeResponse.setRequestDate(localDateTimeToString.localDateTimeToString(overTimeRequest.getRequestDate()));
        requestDetilOverTimeResponse.setRequestStatus(overTimeRequest.getRequestStatus());
        requestDetilOverTimeResponse.setApproverId(Objects.isNull(overTimeRequest.getApprover()) ? null : overTimeRequest.getApprover().getId().intValue());
        requestDetilOverTimeResponse.setApproverName(Objects.isNull(overTimeRequest.getApprover()) ? "" : overTimeRequest.getApprover().getName());
        requestDetilOverTimeResponse.setApproverComment(overTimeRequest.getApproverComment());
        requestDetilOverTimeResponse.setApprovalTime(Objects.isNull(overTimeRequest.getApprovalTime()) ? "" : localDateTimeToString.localDateTimeToString(overTimeRequest.getApprovalTime()));
        return requestDetilOverTimeResponse;
    }
    @GetMapping("/reach/requestdetil/othertime")
    public RequestDetilOtherTimeResponse returnOtherTimeDetil(HttpSession session, RequestIdInput request)
    {
        LocalDateTimeToString localDateTimeToString = new LocalDateTimeToString();
        RequestDetilOtherTimeResponse requestDetilOtherTimeResponse = new RequestDetilOtherTimeResponse();
        // securityutilから名前を取得
        String username = SecurityUtil.getCurrentUsername();
        Account account = accountService.getAccountByUsername(username);
        int status = 0;
        if(Objects.isNull(account))
        {
            status = 4;
            requestDetilOtherTimeResponse.setStatus(status);
            return requestDetilOtherTimeResponse;
        }
        AttendanceExceptionRequest attendanceExceptionRequest = attendanceExceptionRequestService.findByAccountIdAndAttendanceExceptionId(account, request.getRequestId());
        if(Objects.isNull(attendanceExceptionRequest))
        {
            status = 4;
            requestDetilOtherTimeResponse.setStatus(status);
            return requestDetilOtherTimeResponse;
        }
        status = 1;
        requestDetilOtherTimeResponse.setStatus(status);
        requestDetilOtherTimeResponse.setShiftId(attendanceExceptionRequest.getShiftId().getShiftId().intValue());
        requestDetilOtherTimeResponse.setTypeId(attendanceExceptionRequest.getAttendanceExceptionTypeId().getAttedanceExceptionTypeId().intValue());
        requestDetilOtherTimeResponse.setBeginOtherTime(localDateTimeToString.localDateTimeToString(attendanceExceptionRequest.getBeginTime()));
        requestDetilOtherTimeResponse.setEndOtherTime(localDateTimeToString.localDateTimeToString(attendanceExceptionRequest.getEndTime()));
        requestDetilOtherTimeResponse.setRequestComment(attendanceExceptionRequest.getRequestComment());
        requestDetilOtherTimeResponse.setRequestDate(localDateTimeToString.localDateTimeToString(attendanceExceptionRequest.getRequestDate()));
        requestDetilOtherTimeResponse.setRequestStatus(attendanceExceptionRequest.getRequestStatus());
        requestDetilOtherTimeResponse.setApproverId(Objects.isNull(attendanceExceptionRequest.getApprover()) ? null : attendanceExceptionRequest.getApprover().getId().intValue());
        requestDetilOtherTimeResponse.setApproverName(Objects.isNull(attendanceExceptionRequest.getApprover()) ? "" : attendanceExceptionRequest.getApprover().getName());
        requestDetilOtherTimeResponse.setApproverComment(attendanceExceptionRequest.getApproverComment());
        requestDetilOtherTimeResponse.setApprovalTime(Objects.isNull(attendanceExceptionRequest.getApprovalTime()) ? "" : localDateTimeToString.localDateTimeToString(attendanceExceptionRequest.getApprovalTime()));
        return requestDetilOtherTimeResponse;
    }
}