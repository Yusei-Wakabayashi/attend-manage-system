package com.example.springboot.controller;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import com.example.springboot.dto.response.ApproverResponse;
import com.example.springboot.dto.response.AttendListResponse;
import com.example.springboot.dto.response.MonthWorkInfoResponse;
import com.example.springboot.dto.response.NewsListResponse;
import com.example.springboot.dto.response.OtherTypeListResponse;
import com.example.springboot.dto.response.PaydHolidayHistoryListResponse;
import com.example.springboot.dto.response.RequestDetailMonthlyResponse;
import com.example.springboot.dto.response.RequestDetailOtherTimeResponse;
import com.example.springboot.dto.response.RequestDetailOverTimeResponse;
import com.example.springboot.dto.response.RequestDetailShiftChangeResponse;
import com.example.springboot.dto.response.RequestDetailShiftResponse;
import com.example.springboot.dto.response.RequestDetailStampResponse;
import com.example.springboot.dto.response.RequestDetailVacationResponse;
import com.example.springboot.dto.response.RequestListResponse;
import com.example.springboot.dto.ArrayResponse;
import com.example.springboot.dto.change.DurationToString;
import com.example.springboot.dto.change.LocalDateTimeToString;
import com.example.springboot.dto.input.RequestIdInput;
import com.example.springboot.dto.input.UserAttendInput;
import com.example.springboot.dto.input.UserMonthWorkInfoInput;
import com.example.springboot.dto.input.UserShiftInput;
import com.example.springboot.dto.input.YearInput;
import com.example.springboot.dto.input.YearMonthInput;
import com.example.springboot.dto.response.AccountInfoResponse;
import com.example.springboot.dto.response.ShiftListResponse;
import com.example.springboot.dto.response.StyleResponse;
import com.example.springboot.dto.response.UserRequestListResponse;
import com.example.springboot.dto.response.VacationTypeListResponse;
import com.example.springboot.model.Account;
import com.example.springboot.model.AccountApprover;
import com.example.springboot.model.Attend;
import com.example.springboot.model.AttendanceExceptionRequest;
import com.example.springboot.model.AttendanceExceptionType;
import com.example.springboot.model.MonthlyRequest;
import com.example.springboot.model.NewsList;
import com.example.springboot.model.OverTimeRequest;
import com.example.springboot.model.PaydHoliday;
import com.example.springboot.model.PaydHolidayUse;
import com.example.springboot.model.Shift;
import com.example.springboot.model.ShiftChangeRequest;
import com.example.springboot.model.ShiftRequest;
import com.example.springboot.model.StampRequest;
import com.example.springboot.model.Style;
import com.example.springboot.model.Vacation;
import com.example.springboot.model.VacationRequest;
import com.example.springboot.model.VacationType;
import com.example.springboot.service.AccountApproverService;
import com.example.springboot.service.AccountService;
import com.example.springboot.service.ApprovalSettingService;
import com.example.springboot.service.AttendService;
import com.example.springboot.service.AttendanceExceptionRequestService;
import com.example.springboot.service.AttendanceExceptionTypeService;
import com.example.springboot.service.DepartmentService;
import com.example.springboot.service.MonthlyRequestService;
import com.example.springboot.service.NewsListService;
import com.example.springboot.service.OverTimeRequestService;
import com.example.springboot.service.PaydHolidayService;
import com.example.springboot.service.PaydHolidayUseService;
import com.example.springboot.service.RequestService;
import com.example.springboot.service.RoleService;
import com.example.springboot.service.ShiftChangeRequestService;
import com.example.springboot.service.ShiftRequestService;
import com.example.springboot.service.ShiftService;
import com.example.springboot.service.StampRequestService;
import com.example.springboot.service.StylePlaceService;
import com.example.springboot.service.StyleService;
import com.example.springboot.service.VacationRequestService;
import com.example.springboot.service.VacationService;
import com.example.springboot.service.VacationTypeService;
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

    @Autowired
    MonthlyRequestService monthlyRequestService;

    @Autowired
    VacationService vacationService;

    @Autowired
    AccountApproverService accountApproverService;

    @Autowired
    VacationTypeService vacationTypeService;

    @Autowired
    AttendanceExceptionTypeService attendanceExceptionTypeService;

    @Autowired
    PaydHolidayService paydHolidayService;

    @Autowired
    PaydHolidayUseService paydHolidayUseService;

    @Autowired
    NewsListService newsListService;

    @Autowired
    RequestService requestService;

    @GetMapping("/reach/approverlist")
    public ArrayResponse<ApproverListResponse> returnApproverList()
    {
        // アカウントオブジェクトを取得
        Account account = accountService.findCurrentAccount();
        if(Objects.isNull(account))
        {
            // アカウントオブジェクトがなかったときからの配列を返す
            return new ArrayResponse<>(4, Collections.emptyList(), "approverlist");
        }
        List<ApproverListResponse> approverListResponses = accountService.findApproverListFor(account);
        return new ArrayResponse<>(1,approverListResponses, "approverlist");
    }

    @GetMapping("/reach/allstylelist")
    public ArrayResponse<AllStyleListResponse> returnAllStyleList()
    {
        // ログイン済みか確認
        Account account = accountService.findCurrentAccount();
        if(Objects.isNull(account))
        {
            return new ArrayResponse<>(4,Collections.emptyList(),"styleList");
        }
        List<AllStyleListResponse> styleListResponse = stylePlaceService.findStyleList();
        return new ArrayResponse<>(1, styleListResponse, "styleList");
    }

    @GetMapping("/reach/accountinfo")
    public AccountInfoResponse returnAccountInfo()
    {
        Account account = accountService.findCurrentAccount();
        if(Objects.isNull(account))
        {
            return new AccountInfoResponse(4, "", "", "", false);
        }
        AccountInfoResponse accountInfo = accountService.getCurrentAccountInfo(account);
        return accountInfo;
    }

    @GetMapping("/reach/shiftlist")
    public ArrayResponse<ShiftListResponse> returnShiftList(@ModelAttribute YearMonthInput request)
    {
        Account account = accountService.findCurrentAccount();
        if(Objects.isNull(account))
        {
            return new ArrayResponse<ShiftListResponse>(4, Collections.emptyList(),"shiftList");
        }
        // accountidを基にshift_listテーブルを検索、shiftListResponseに格納
        List<ShiftListResponse> shiftListResponse = shiftService.findByShiftListFor(account, request);
        return new ArrayResponse<ShiftListResponse>(1, shiftListResponse, "shiftList");
    }

    @GetMapping("/reach/requestdetail/shift")
    public RequestDetailShiftResponse returnShiftDetail(@ModelAttribute RequestIdInput request)
    {
        Account account = accountService.findCurrentAccount();
        // 認証情報がなければエラー
        if(Objects.isNull(account))
        {
            RequestDetailShiftResponse requestDetailShiftResponse = new RequestDetailShiftResponse();
            requestDetailShiftResponse.setStatus(5);
            return requestDetailShiftResponse;
        }
        RequestDetailShiftResponse requestDetailShiftResponse = requestService.getShiftDetail(account, request.getRequestId());
        return requestDetailShiftResponse;
    }

    @GetMapping("/reach/requestdetail/changetime")
    public RequestDetailShiftChangeResponse returnShiftChangeDetail(HttpSession session, RequestIdInput request)
    {
        Account account = accountService.findCurrentAccount();
        // 認証情報がなければエラー
        if(Objects.isNull(account))
        {
            RequestDetailShiftChangeResponse requestDetailShiftChangeResponse = new RequestDetailShiftChangeResponse();
            requestDetailShiftChangeResponse.setStatus(5);
            return requestDetailShiftChangeResponse;
        }
        RequestDetailShiftChangeResponse requestDetailShiftChangeResponse = requestService.getShiftChangeDetail(account, request.getRequestId());
        return requestDetailShiftChangeResponse;
    }

    @GetMapping("/reach/requestdetail/stamp")
    public RequestDetailStampResponse returnStampDetil(HttpSession session, RequestIdInput request)
    {
        LocalDateTimeToString localDateTimeToString = new LocalDateTimeToString();
        int status = 0;
        // securityutilから名前を取得
        String username = SecurityUtil.getCurrentUsername();
        Account account = accountService.findAccountByUsername(username);
        RequestDetailStampResponse requestDetailStampResponse = new RequestDetailStampResponse();
        // 認証情報がなければエラー
        if(Objects.isNull(account))
        {
            status = 5;
            requestDetailStampResponse.setStatus(status);
            return requestDetailStampResponse;
        }
        StampRequest stampRequest = stampRequestService.findByAccountIdAndStampId(account, request.getRequestId());
        // 名前から取得したアカウントとidで検索にかけ取得できれば返す取得できなければエラー
        if(stampRequest.equals(null))
        {
            status = 5;
            requestDetailStampResponse.setStatus(status);
            return requestDetailStampResponse;
        }
        status = 1;
        requestDetailStampResponse.setStatus(status);
        requestDetailStampResponse.setShiftId(stampRequest.getShiftId().getShiftId().intValue());
        requestDetailStampResponse.setBeginWork(localDateTimeToString.localDateTimeToString(stampRequest.getBeginWork()));
        requestDetailStampResponse.setEndWork(localDateTimeToString.localDateTimeToString(stampRequest.getEndWork()));
        requestDetailStampResponse.setBeginBreak(localDateTimeToString.localDateTimeToString(stampRequest.getBeginBreak()));
        requestDetailStampResponse.setEndBreak(localDateTimeToString.localDateTimeToString(stampRequest.getEndBreak()));
        requestDetailStampResponse.setRequestComment(stampRequest.getRequestComment());
        requestDetailStampResponse.setRequestDate(localDateTimeToString.localDateTimeToString(stampRequest.getRequestDate()));
        requestDetailStampResponse.setRequestStatus(stampRequest.getRequestStatus());
        requestDetailStampResponse.setApproverId(stampRequest.getApprover().getId().intValue());
        requestDetailStampResponse.setApproverName(stampRequest.getApprover().getName());
        requestDetailStampResponse.setApproverComment(stampRequest.getApproverComment());
        requestDetailStampResponse.setApprovalTime(Objects.isNull(stampRequest.getApprovalTime()) ? "" : localDateTimeToString.localDateTimeToString(stampRequest.getApprovalTime()));
        return requestDetailStampResponse;
    }
    @GetMapping("/reach/attendlist")
    public ArrayResponse<AttendListResponse> returnAttendList(HttpSession session, @ModelAttribute YearMonthInput request)
    {
        String username = SecurityUtil.getCurrentUsername();
        Account account = accountService.findAccountByUsername(username);
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

    @GetMapping("/reach/requestdetail/vacation")
    public RequestDetailVacationResponse returnVacationDetil(HttpSession session, RequestIdInput request)
    {
        LocalDateTimeToString localDateTimeToString = new LocalDateTimeToString();
        RequestDetailVacationResponse requestDetailVacation = new RequestDetailVacationResponse();
        // securityutilから名前を取得
        String username = SecurityUtil.getCurrentUsername();
        Account account = accountService.findAccountByUsername(username);
        int status = 0;
        if(Objects.isNull(account))
        {
            status = 4;
            requestDetailVacation.setStatus(status);
            return requestDetailVacation;
        }
        VacationRequest vacationRequest = vacationRequestService.findByAccountIdAndVacationId(account, request.getRequestId());
        if(Objects.isNull(vacationRequest))
        {
            status = 4;
            requestDetailVacation.setStatus(status);
            return requestDetailVacation;
        }
        status = 1;
        requestDetailVacation.setStatus(status);
        requestDetailVacation.setShiftId(vacationRequest.getShiftId().getShiftId().intValue());
        requestDetailVacation.setVacationType(vacationRequest.getVacationTypeId().getVacationTypeId().intValue());
        requestDetailVacation.setBeginVacation(localDateTimeToString.localDateTimeToString(vacationRequest.getBeginVacation()));
        requestDetailVacation.setEndVacation(localDateTimeToString.localDateTimeToString(vacationRequest.getEndVacation()));
        requestDetailVacation.setRequestComment(vacationRequest.getRequestComment());
        requestDetailVacation.setRequestDate(localDateTimeToString.localDateTimeToString(vacationRequest.getRequestDate()));
        requestDetailVacation.setRequestStatus(vacationRequest.getRequestStatus());
        requestDetailVacation.setApproverId(vacationRequest.getApprover().getId().intValue());
        requestDetailVacation.setApproverName(vacationRequest.getApprover().getName());
        requestDetailVacation.setApproverComment(vacationRequest.getApproverComment());
        requestDetailVacation.setApprovalTime(Objects.isNull(vacationRequest.getApprovalTime()) ? "" : localDateTimeToString.localDateTimeToString(vacationRequest.getApprovalTime()));
        return requestDetailVacation;
    }

    @GetMapping("/reach/requestdetail/overtime")
    public RequestDetailOverTimeResponse returnOverTimeDetil(HttpSession session, RequestIdInput request)
    {
        LocalDateTimeToString localDateTimeToString = new LocalDateTimeToString();
        RequestDetailOverTimeResponse requestDetailOverTimeResponse = new RequestDetailOverTimeResponse();
        // securityutilから名前を取得
        String username = SecurityUtil.getCurrentUsername();
        Account account = accountService.findAccountByUsername(username);
        int status = 0;
        if(Objects.isNull(account))
        {
            status = 4;
            requestDetailOverTimeResponse.setStatus(status);
            return requestDetailOverTimeResponse;
        }
        OverTimeRequest overTimeRequest = overTimeRequestService.findByAccountIdAndOverTimeRequestId(account, request.getRequestId());
        if(Objects.isNull(overTimeRequest))
        {
            status = 4;
            requestDetailOverTimeResponse.setStatus(status);
            return requestDetailOverTimeResponse;
        }
        status = 1;
        requestDetailOverTimeResponse.setStatus(status);
        requestDetailOverTimeResponse.setShiftId(overTimeRequest.getShiftId().getShiftId().intValue());
        requestDetailOverTimeResponse.setBeginOverWork(localDateTimeToString.localDateTimeToString(overTimeRequest.getBeginWork()));
        requestDetailOverTimeResponse.setEndOverWork(localDateTimeToString.localDateTimeToString(overTimeRequest.getEndWork()));
        requestDetailOverTimeResponse.setRequestComment(overTimeRequest.getRequestComment());
        requestDetailOverTimeResponse.setRequestDate(localDateTimeToString.localDateTimeToString(overTimeRequest.getRequestDate()));
        requestDetailOverTimeResponse.setRequestStatus(overTimeRequest.getRequestStatus());
        requestDetailOverTimeResponse.setApproverId(Objects.isNull(overTimeRequest.getApprover()) ? null : overTimeRequest.getApprover().getId().intValue());
        requestDetailOverTimeResponse.setApproverName(Objects.isNull(overTimeRequest.getApprover()) ? "" : overTimeRequest.getApprover().getName());
        requestDetailOverTimeResponse.setApproverComment(overTimeRequest.getApproverComment());
        requestDetailOverTimeResponse.setApprovalTime(Objects.isNull(overTimeRequest.getApprovalTime()) ? "" : localDateTimeToString.localDateTimeToString(overTimeRequest.getApprovalTime()));
        return requestDetailOverTimeResponse;
    }

    @GetMapping("/reach/requestdetail/othertime")
    public RequestDetailOtherTimeResponse returnOtherTimeDetil(HttpSession session, RequestIdInput request)
    {
        LocalDateTimeToString localDateTimeToString = new LocalDateTimeToString();
        RequestDetailOtherTimeResponse requestDetailOtherTimeResponse = new RequestDetailOtherTimeResponse();
        // securityutilから名前を取得
        String username = SecurityUtil.getCurrentUsername();
        Account account = accountService.findAccountByUsername(username);
        int status = 0;
        if(Objects.isNull(account))
        {
            status = 4;
            requestDetailOtherTimeResponse.setStatus(status);
            return requestDetailOtherTimeResponse;
        }
        AttendanceExceptionRequest attendanceExceptionRequest = attendanceExceptionRequestService.findByAccountIdAndAttendanceExceptionId(account, request.getRequestId());
        if(Objects.isNull(attendanceExceptionRequest))
        {
            status = 4;
            requestDetailOtherTimeResponse.setStatus(status);
            return requestDetailOtherTimeResponse;
        }
        status = 1;
        requestDetailOtherTimeResponse.setStatus(status);
        requestDetailOtherTimeResponse.setShiftId(attendanceExceptionRequest.getShiftId().getShiftId().intValue());
        requestDetailOtherTimeResponse.setTypeId(attendanceExceptionRequest.getAttendanceExceptionTypeId().getAttendanceExceptionTypeId().intValue());
        requestDetailOtherTimeResponse.setBeginOtherTime(localDateTimeToString.localDateTimeToString(attendanceExceptionRequest.getBeginTime()));
        requestDetailOtherTimeResponse.setEndOtherTime(localDateTimeToString.localDateTimeToString(attendanceExceptionRequest.getEndTime()));
        requestDetailOtherTimeResponse.setRequestComment(attendanceExceptionRequest.getRequestComment());
        requestDetailOtherTimeResponse.setRequestDate(localDateTimeToString.localDateTimeToString(attendanceExceptionRequest.getRequestDate()));
        requestDetailOtherTimeResponse.setRequestStatus(attendanceExceptionRequest.getRequestStatus());
        requestDetailOtherTimeResponse.setApproverId(Objects.isNull(attendanceExceptionRequest.getApprover()) ? null : attendanceExceptionRequest.getApprover().getId().intValue());
        requestDetailOtherTimeResponse.setApproverName(Objects.isNull(attendanceExceptionRequest.getApprover()) ? "" : attendanceExceptionRequest.getApprover().getName());
        requestDetailOtherTimeResponse.setApproverComment(attendanceExceptionRequest.getApproverComment());
        requestDetailOtherTimeResponse.setApprovalTime(Objects.isNull(attendanceExceptionRequest.getApprovalTime()) ? "" : localDateTimeToString.localDateTimeToString(attendanceExceptionRequest.getApprovalTime()));
        return requestDetailOtherTimeResponse;
    }
    // 月次申請詳細取得を作成後申請一覧取得の作成
    @GetMapping("/reach/requestdetail/monthly")
    public RequestDetailMonthlyResponse returnMonthlyResponse(HttpSession session, RequestIdInput request)
    {
        LocalDateTimeToString localDateTimeToString = new LocalDateTimeToString();
        RequestDetailMonthlyResponse requestDetailMonthlyResponse = new RequestDetailMonthlyResponse();
        // securityutilから名前を取得
        String username = SecurityUtil.getCurrentUsername();
        Account account = accountService.findAccountByUsername(username);
        int status = 0;
        if(Objects.isNull(account))
        {
            status = 4;
            requestDetailMonthlyResponse.setStatus(status);
            return requestDetailMonthlyResponse;
        }
        MonthlyRequest monthlyRequest = monthlyRequestService.findByAccountIdAndMothlyRequestId(account, request.getRequestId());
        if(Objects.isNull(monthlyRequest))
        {
            status = 4;
            requestDetailMonthlyResponse.setStatus(status);
            return requestDetailMonthlyResponse;
        }
        status = 1;
        requestDetailMonthlyResponse.setStatus(status);
        requestDetailMonthlyResponse.setWorkTime(monthlyRequest.getWorkTime());
        requestDetailMonthlyResponse.setOverTime(monthlyRequest.getOverTime());
        requestDetailMonthlyResponse.setEarlyTime(monthlyRequest.getEarlyTime());
        requestDetailMonthlyResponse.setLeavingTime(monthlyRequest.getLeavingTime());
        requestDetailMonthlyResponse.setOutingTime(monthlyRequest.getOutingTime());
        requestDetailMonthlyResponse.setAbsenceTime(monthlyRequest.getAbsenceTime());
        requestDetailMonthlyResponse.setPaydHolidayTime(monthlyRequest.getPaydHolidayTime());
        requestDetailMonthlyResponse.setSpecialTime(monthlyRequest.getSpecialTime());
        requestDetailMonthlyResponse.setHolidayWorkTime(monthlyRequest.getHolidayWorkTime());
        requestDetailMonthlyResponse.setLateNightWorkTime(monthlyRequest.getLateNightWorkTime());
        requestDetailMonthlyResponse.setYear(monthlyRequest.getYear());
        requestDetailMonthlyResponse.setMonth(monthlyRequest.getMonth());
        requestDetailMonthlyResponse.setRequestComment(monthlyRequest.getRequestComment());
        requestDetailMonthlyResponse.setRequestDate(localDateTimeToString.localDateTimeToString(monthlyRequest.getRequestDate()));
        requestDetailMonthlyResponse.setRequestStatus(monthlyRequest.getRequestStatus());
        requestDetailMonthlyResponse.setApproverId(Objects.isNull(monthlyRequest.getApprover()) ? null : monthlyRequest.getApprover().getId().intValue());
        requestDetailMonthlyResponse.setApproverName(Objects.isNull(monthlyRequest.getApprover()) ? "" : monthlyRequest.getApprover().getName());
        requestDetailMonthlyResponse.setApproverComment(monthlyRequest.getApproverComment());
        requestDetailMonthlyResponse.setApprovalTime(Objects.isNull(monthlyRequest.getApprovalDate()) ? "" : localDateTimeToString.localDateTimeToString(monthlyRequest.getApprovalDate()));
        return requestDetailMonthlyResponse;
    }

    @GetMapping("/reach/requestlist")
    public ArrayResponse<RequestListResponse> returnRequestList(HttpSession session)
    {
        LocalDateTimeToString localDateTimeToString = new LocalDateTimeToString();
        String username = SecurityUtil.getCurrentUsername();
        Account account = accountService.findAccountByUsername(username);
        int status = 0;
        List<RequestListResponse> requestListResponse = new ArrayList<RequestListResponse>();
        // そのアカウントの申請(シフト、シフト時間変更、打刻漏れ、勤怠例外、残業、休暇、月次)をそれぞれ取得し必要な情報だけを設定
        for(ShiftRequest shiftRequest : shiftRequestService.findByAccountId(account))
        {
            RequestListResponse requestListResponseShiftRequest = new RequestListResponse();
            requestListResponseShiftRequest.setId(shiftRequest.getShiftRequestId().intValue());
            requestListResponseShiftRequest.setRequestType(1);
            requestListResponseShiftRequest.setRequestDate(localDateTimeToString.localDateTimeToString(shiftRequest.getRequestDate()));
            requestListResponseShiftRequest.setRequestStatus(shiftRequest.getRequestStatus());
            requestListResponse.add(requestListResponseShiftRequest);
        }
        for(ShiftChangeRequest shiftChangeRequest : shiftChangeRequestService.findByAccountId(account))
        {
            RequestListResponse requestListResponseShiftChangeRequest = new RequestListResponse();
            requestListResponseShiftChangeRequest.setId(shiftChangeRequest.getShiftChangeId().intValue());
            requestListResponseShiftChangeRequest.setRequestType(2);
            requestListResponseShiftChangeRequest.setRequestDate(localDateTimeToString.localDateTimeToString(shiftChangeRequest.getRequestDate()));
            requestListResponseShiftChangeRequest.setRequestStatus(shiftChangeRequest.getRequestStatus());
            requestListResponse.add(requestListResponseShiftChangeRequest);
        }
        for(StampRequest stampRequest : stampRequestService.findByAccountId(account))
        {
            RequestListResponse requestListResponseStampRequest = new RequestListResponse();
            requestListResponseStampRequest.setId(stampRequest.getStampId().intValue());
            requestListResponseStampRequest.setRequestType(2);
            requestListResponseStampRequest.setRequestDate(localDateTimeToString.localDateTimeToString(stampRequest.getRequestDate()));
            requestListResponseStampRequest.setRequestStatus(stampRequest.getRequestStatus());
            requestListResponse.add(requestListResponseStampRequest);
        }
        for(AttendanceExceptionRequest attendanceExceptionRequest : attendanceExceptionRequestService.findByAccountId(account))
        {
            RequestListResponse requestListResponseAttendanceExceptionRequest = new RequestListResponse();
            requestListResponseAttendanceExceptionRequest.setId(attendanceExceptionRequest.getAttendanceExceptionId().intValue());
            requestListResponseAttendanceExceptionRequest.setRequestType(2);
            requestListResponseAttendanceExceptionRequest.setRequestDate(localDateTimeToString.localDateTimeToString(attendanceExceptionRequest.getRequestDate()));
            requestListResponseAttendanceExceptionRequest.setRequestStatus(attendanceExceptionRequest.getRequestStatus());
            requestListResponse.add(requestListResponseAttendanceExceptionRequest);
        }
        for(OverTimeRequest overTimeRequest : overTimeRequestService.findByAccountId(account))
        {
            RequestListResponse requestListResponseOverTimeRequest = new RequestListResponse();
            requestListResponseOverTimeRequest.setId(overTimeRequest.getOverTimeId().intValue());
            requestListResponseOverTimeRequest.setRequestType(2);
            requestListResponseOverTimeRequest.setRequestDate(localDateTimeToString.localDateTimeToString(overTimeRequest.getRequestDate()));
            requestListResponseOverTimeRequest.setRequestStatus(overTimeRequest.getRequestStatus());
            requestListResponse.add(requestListResponseOverTimeRequest);
        }
        for(VacationRequest vacationRequest : vacationRequestService.findByAccountId(account))
        {
            RequestListResponse requestListResponseVacationRequest = new RequestListResponse();
            requestListResponseVacationRequest.setId(vacationRequest.getVacationId().intValue());
            requestListResponseVacationRequest.setRequestType(2);
            requestListResponseVacationRequest.setRequestDate(localDateTimeToString.localDateTimeToString(vacationRequest.getRequestDate()));
            requestListResponseVacationRequest.setRequestStatus(vacationRequest.getRequestStatus());
            requestListResponse.add(requestListResponseVacationRequest);
        }
        for(MonthlyRequest monthlyRequest : monthlyRequestService.findByAccountId(account))
        {
            RequestListResponse requestListResponseMonthRequest = new RequestListResponse();
            requestListResponseMonthRequest.setId(monthlyRequest.getMonthRequestId().intValue());
            requestListResponseMonthRequest.setRequestType(2);
            requestListResponseMonthRequest.setRequestDate(localDateTimeToString.localDateTimeToString(monthlyRequest.getRequestDate()));
            requestListResponseMonthRequest.setRequestStatus(monthlyRequest.getRequestStatus());
            requestListResponse.add(requestListResponseMonthRequest);
        }
        // 申請日時を基にソート
        requestListResponse.sort(Comparator.comparing(RequestListResponse::getRequestDate));
        status = 1;
        return new ArrayResponse<RequestListResponse>(status, requestListResponse, "requestList");
    }
    @GetMapping("/reach/monthworkinfo")
    public MonthWorkInfoResponse returnMonthWorkInfo(HttpSession session, YearMonthInput request)
    {
        DurationToString durationToString = new DurationToString();
        String username = SecurityUtil.getCurrentUsername();
        Account account = accountService.findAccountByUsername(username);
        MonthWorkInfoResponse monthWorkInfoResponse = new MonthWorkInfoResponse();
        int status = 0;
        if(Objects.isNull(account))
        {
            status = 4;
            monthWorkInfoResponse.setStatus(status);
            return monthWorkInfoResponse;
        }
        Duration monthWorkTime = Duration.ZERO;
        Duration monthLateness = Duration.ZERO;
        Duration monthLeaveEarly = Duration.ZERO;
        Duration monthOuting = Duration.ZERO;
        Duration monthOverWork = Duration.ZERO;
        Duration monthAbsenceTime = Duration.ZERO;
        Duration monthSpecialTime = Duration.ZERO;
        Duration monthLateNightWorkTime = Duration.ZERO;
        Duration monthHolidayWorkTime = Duration.ZERO;
        Duration monthPaydHolidayTime = Duration.ZERO;

        // 休暇の分け以外は取得できる
        List<Attend> attends = attendService.findByAccountIdAndBeginWorkBetween(account, request.getYear(), request.getMonth());
        for(Attend attend : attends)
        {
            monthWorkTime = monthWorkTime.plus(Duration.between(LocalTime.MIDNIGHT, attend.getWorkTime().toLocalTime()));
            monthLateness = monthLateness.plus(Duration.between(LocalTime.MIDNIGHT, attend.getLateness().toLocalTime()));
            monthLeaveEarly = monthLeaveEarly.plus(Duration.between(LocalTime.MIDNIGHT, attend.getLeaveEarly().toLocalTime()));
            monthOuting = monthOuting.plus(Duration.between(LocalTime.MIDNIGHT, attend.getOuting().toLocalTime()));
            monthOverWork = monthOverWork.plus(Duration.between(LocalTime.MIDNIGHT, attend.getOverWork().toLocalTime()));
            monthAbsenceTime = monthAbsenceTime.plus(Duration.between(LocalTime.MIDNIGHT, attend.getAbsenceTime().toLocalTime()));
            monthSpecialTime = monthSpecialTime.plus(Duration.between(LocalTime.MIDNIGHT, attend.getVacationTime().toLocalTime()));
            monthLateNightWorkTime = monthLateNightWorkTime.plus(Duration.between(LocalTime.MIDNIGHT, attend.getLateNightWork().toLocalTime()));
            monthHolidayWorkTime = monthHolidayWorkTime.plus(Duration.between(LocalTime.MIDNIGHT, attend.getHolidayWork().toLocalTime()));
        }
        // 休暇はvacationlistからその月の有給の時間を取得、休暇時間から減算
        List<Vacation> vacations = vacationService.findByAccountIdAndBeginVacationBetweenMonthAndPaydHoliday(account, request.getYear(), request.getMonth());
        for(Vacation vacation : vacations)
        {
            monthPaydHolidayTime = monthPaydHolidayTime.plus(Duration.between(vacation.getBeginVacation(), vacation.getEndVacation()));
        }
        monthSpecialTime.minus(monthPaydHolidayTime);
        status = 1;
        monthWorkInfoResponse.setStatus(status);
        monthWorkInfoResponse.setWorkTime(durationToString.durationToString(monthWorkTime));
        monthWorkInfoResponse.setLateness(durationToString.durationToString(monthLateness));
        monthWorkInfoResponse.setLeaveEarly(durationToString.durationToString(monthLeaveEarly));
        monthWorkInfoResponse.setOuting(durationToString.durationToString(monthOuting));
        monthWorkInfoResponse.setOverWork(durationToString.durationToString(monthOverWork));
        monthWorkInfoResponse.setAbsenceTime(durationToString.durationToString(monthAbsenceTime));
        monthWorkInfoResponse.setSpecialTime(durationToString.durationToString(monthSpecialTime));
        monthWorkInfoResponse.setLateNightWorkTime(durationToString.durationToString(monthLateNightWorkTime));
        monthWorkInfoResponse.setPaydHolidayTime(durationToString.durationToString(monthPaydHolidayTime));
        return monthWorkInfoResponse;
    }

    @GetMapping("/reach/style")
    public StyleResponse returnStyle(HttpSession session)
    {
        String username = SecurityUtil.getCurrentUsername();
        Account account = accountService.findAccountByUsername(username);
        int status = 0;

        Style style = styleService.findStyleByAccountId(account);
        StyleResponse response = new StyleResponse();
        status = 1;
        response.setStatus(status);
        response.setStyleId(style.getStyleId().intValue());
        response.setStyleName(style.getStylePlaceId().getName());
        return response;
    }

    @GetMapping("/reach/approver")
    public ApproverResponse returnApprover(HttpSession session)
    {
        String username = SecurityUtil.getCurrentUsername();
        Account account = accountService.findAccountByUsername(username);
        int status = 0;
        Account adminAccount = accountApproverService.findAccountApproverByAccount(account).getApproverId();
        ApproverResponse approverResponse = new ApproverResponse();
        status = 1;
        approverResponse.setStatus(status);
        approverResponse.setApproverId(adminAccount.getId().intValue());
        approverResponse.setApproverName(adminAccount.getName());
        approverResponse.setApproverDepartment(adminAccount.getDepartmentId().getName());
        approverResponse.setApproverRole(adminAccount.getRoleId().getName());
        return approverResponse;
    }

    @GetMapping("/reach/allvacationtypelist")
    public ArrayResponse<VacationTypeListResponse> returnAllVacationType(HttpSession session)
    {
        String username = SecurityUtil.getCurrentUsername();
        Account account = accountService.findAccountByUsername(username);
        if(Objects.isNull(account))
        {
        }

        int status = 0;
        List<VacationTypeListResponse> vacationTypeListResponses = new ArrayList<VacationTypeListResponse>();
        List<VacationType> vacationTypes = vacationTypeService.findAll();
        for(VacationType vacationType : vacationTypes)
        {
            VacationTypeListResponse vacationTypeListResponse = new VacationTypeListResponse();
            vacationTypeListResponse.setVacationTypeId(vacationType.getVacationTypeId().intValue());
            vacationTypeListResponse.setVacationTypeName(vacationType.getVacationName());
            vacationTypeListResponses.add(vacationTypeListResponse);
        }
        status = 1;
        return new ArrayResponse<VacationTypeListResponse>(status, vacationTypeListResponses, "vacationTypes");
    }

    @GetMapping("/reach/allothertypelist")
    public ArrayResponse<OtherTypeListResponse> returnAllOtherTypes(HttpSession session)
    {
        String username = SecurityUtil.getCurrentUsername();
        Account account = accountService.findAccountByUsername(username);
        if(Objects.isNull(account))
        {

        }
        int status = 0;
        List<OtherTypeListResponse> otherTypeListResponses = new ArrayList<OtherTypeListResponse>();
        List<AttendanceExceptionType> attendanceExceptionTypes = attendanceExceptionTypeService.findAll();
        for(AttendanceExceptionType attendanceExceptionType : attendanceExceptionTypes)
        {
            OtherTypeListResponse otherTypeListResponse = new OtherTypeListResponse();
            otherTypeListResponse.setOtherTypeId(attendanceExceptionType.getAttendanceExceptionTypeId().intValue());
            otherTypeListResponse.setOtherTypeName(attendanceExceptionType.getAttednaceExceptionTypeName());
            otherTypeListResponses.add(otherTypeListResponse);
        }
        status = 1;
        return new ArrayResponse<OtherTypeListResponse>(status, otherTypeListResponses, "otherTypes");
    }

    @GetMapping("/reach/paydholidayhistory")
    public ArrayResponse<PaydHolidayHistoryListResponse> returnPaydHolidayHistory(HttpSession session, YearInput request)
    {
        LocalDateTimeToString localDateTimeToString = new LocalDateTimeToString();
        String username = SecurityUtil.getCurrentUsername();
        Account account = accountService.findAccountByUsername(username);
        ArrayResponse<PaydHolidayHistoryListResponse> response = new ArrayResponse<>();
        int status = 0;

        if(Objects.isNull(account))
        {
            status = 1;
            response.setStatus(status);
            return response;
        }

        // 有給の取得
        // 付与の取得
        List<PaydHoliday> paydHolidays = paydHolidayService.findByAccountId(account);
        // 消化の取得
        List<PaydHolidayUse> paydHolidayUses = paydHolidayUseService.findByAccountId(account);
        List<PaydHolidayHistoryListResponse> paydHolidayHistoryListResponses = new ArrayList<PaydHolidayHistoryListResponse>();
        for(PaydHoliday paydHoliday : paydHolidays)
        {
            PaydHolidayHistoryListResponse paydHolidayHistoryListResponse = new PaydHolidayHistoryListResponse();
            paydHolidayHistoryListResponse.setType("付与");
            paydHolidayHistoryListResponse.setDate(localDateTimeToString.localDateTimeToString(paydHoliday.getGrant()));
            paydHolidayHistoryListResponse.setTime(paydHoliday.getTime());
            paydHolidayHistoryListResponses.add(paydHolidayHistoryListResponse);
        }
        for(PaydHolidayUse paydHolidayUse : paydHolidayUses)
        {
            PaydHolidayHistoryListResponse paydHolidayHistoryListResponseUse = new PaydHolidayHistoryListResponse();
            paydHolidayHistoryListResponseUse.setType("消化");
            paydHolidayHistoryListResponseUse.setDate(localDateTimeToString.localDateTimeToString(paydHolidayUse.getUseDate()));
            paydHolidayHistoryListResponseUse.setTime(paydHolidayUse.getTime().toString());
            paydHolidayHistoryListResponses.add(paydHolidayHistoryListResponseUse);
        }
        status = 1;
        response.setStatus(status);
        response.setList(paydHolidayHistoryListResponses);
        response.setKey("holidaylist");
        return response;
    }

    @GetMapping("/reach/user/attendinfo")
    public ArrayResponse<AttendListResponse> returnUserAttendList(HttpSession session, UserAttendInput request)
    {
        String username = SecurityUtil.getCurrentUsername();
        Account adminAccount = accountService.findAccountByUsername(username);

        // リクエストを送ってきているのは承認者なので引数に注意しながらメソッドに渡す
        AccountApprover accountApprover = accountApproverService.findAccountAndApprover(request.getAccountId(), adminAccount);
        // 情報が取得できていなければ利用者の承認者として設定されていないことになる
        if(Objects.isNull(accountApprover))
        {
            throw new NullPointerException("正しく情報が渡されていません");
        }

        // 以下
        int status = 1;
        List<AttendListResponse> attendListResponses = new ArrayList<AttendListResponse>();
        List<Attend> attends = attendService.findByAccountIdAndBeginWorkBetween(request.getAccountId(), request.getYear(), request.getMonth());
        for(Attend attend : attends)
        {
            attendListResponses.add(attendService.attendToAttendListResponse(attend));
        }
        return new ArrayResponse<AttendListResponse>(status, attendListResponses, "attendList");
    }

    @GetMapping("/reach/user/shiftinfo")
    public ArrayResponse<ShiftListResponse> returnUserShiftList(HttpSession session, UserShiftInput request)
    {
        String username = SecurityUtil.getCurrentUsername();
        Account adminAccount = accountService.findAccountByUsername(username);
        // リクエストを送ってきているのは承認者なので引数に注意しながらメソッドに渡す
        AccountApprover accountApprover = accountApproverService.findAccountAndApprover(request.getAccountId(), adminAccount);
        // 情報が取得できていなければ利用者の承認者として設定されていないことになる
        if(Objects.isNull(accountApprover))
        {
            throw new NullPointerException("正しく情報が渡されていません");
        }

        int status = 1;
        List<ShiftListResponse> shiftListResponses = new ArrayList<ShiftListResponse>();
        List<Shift> shifts = shiftService.findByAccountIdAndBeginWorkBetween(request.getAccountId(), request.getYear(), request.getMonth());
        for(Shift shift : shifts)
        {
            shiftListResponses.add(shiftService.shiftToShiftListResponse(shift));
        }
        return new ArrayResponse<ShiftListResponse>(status, shiftListResponses, "shiftList");
    }
    
    @GetMapping("/reach/user/monthworkinfo")
    public MonthWorkInfoResponse returnUserMonthWorkInfo(HttpSession session, UserMonthWorkInfoInput request)
    {
        DurationToString durationToString = new DurationToString();
        String username = SecurityUtil.getCurrentUsername();
        Account adminAccount = accountService.findAccountByUsername(username);
        if(Objects.isNull(adminAccount))
        {
            throw new NullPointerException("アカウントの情報が正しくありません");
        }
        // 管理者のアカウントと利用者のアカウントの引数の順番に注意
        AccountApprover accountApprover = accountApproverService.findAccountAndApprover(request.getAccountId(), adminAccount);
        if(Objects.isNull(accountApprover))
        {
            throw new NullPointerException("正しく情報が渡されていません");
        }
        Duration monthWorkTime = Duration.ZERO;
        Duration monthLateness = Duration.ZERO;
        Duration monthLeaveEarly = Duration.ZERO;
        Duration monthOuting = Duration.ZERO;
        Duration monthOverWork = Duration.ZERO;
        Duration monthAbsenceTime = Duration.ZERO;
        Duration monthSpecialTime = Duration.ZERO;
        Duration monthLateNightWorkTime = Duration.ZERO;
        Duration monthHolidayWorkTime = Duration.ZERO;
        Duration monthPaydHolidayTime = Duration.ZERO;

        // 休暇の分け以外は取得できる
        List<Attend> attends = attendService.findByAccountIdAndBeginWorkBetween(accountApprover.getAccountId(), request.getYear(), request.getMonth());
        for(Attend attend : attends)
        {
            monthWorkTime = monthWorkTime.plus(Duration.between(LocalTime.MIDNIGHT, attend.getWorkTime().toLocalTime()));
            monthLateness = monthLateness.plus(Duration.between(LocalTime.MIDNIGHT, attend.getLateness().toLocalTime()));
            monthLeaveEarly = monthLeaveEarly.plus(Duration.between(LocalTime.MIDNIGHT, attend.getLeaveEarly().toLocalTime()));
            monthOuting = monthOuting.plus(Duration.between(LocalTime.MIDNIGHT, attend.getOuting().toLocalTime()));
            monthOverWork = monthOverWork.plus(Duration.between(LocalTime.MIDNIGHT, attend.getOverWork().toLocalTime()));
            monthAbsenceTime = monthAbsenceTime.plus(Duration.between(LocalTime.MIDNIGHT, attend.getAbsenceTime().toLocalTime()));
            monthSpecialTime = monthSpecialTime.plus(Duration.between(LocalTime.MIDNIGHT, attend.getVacationTime().toLocalTime()));
            monthLateNightWorkTime = monthLateNightWorkTime.plus(Duration.between(LocalTime.MIDNIGHT, attend.getLateNightWork().toLocalTime()));
            monthHolidayWorkTime = monthHolidayWorkTime.plus(Duration.between(LocalTime.MIDNIGHT, attend.getHolidayWork().toLocalTime()));
        }
        // 休暇はvacationlistからその月の有給の時間を取得、休暇時間から減算
        List<Vacation> vacations = vacationService.findByAccountIdAndBeginVacationBetweenMonthAndPaydHoliday(accountApprover.getAccountId(), request.getYear(), request.getMonth());
        for(Vacation vacation : vacations)
        {
            monthPaydHolidayTime = monthPaydHolidayTime.plus(Duration.between(vacation.getBeginVacation(), vacation.getEndVacation()));
        }

        monthSpecialTime.minus(monthPaydHolidayTime);
        MonthWorkInfoResponse monthWorkInfoResponse = new MonthWorkInfoResponse();
        int status = 1;
        monthWorkInfoResponse.setStatus(status);
        monthWorkInfoResponse.setWorkTime(durationToString.durationToString(monthWorkTime));
        monthWorkInfoResponse.setLateness(durationToString.durationToString(monthLateness));
        monthWorkInfoResponse.setLeaveEarly(durationToString.durationToString(monthLeaveEarly));
        monthWorkInfoResponse.setOuting(durationToString.durationToString(monthOuting));
        monthWorkInfoResponse.setOverWork(durationToString.durationToString(monthOverWork));
        monthWorkInfoResponse.setAbsenceTime(durationToString.durationToString(monthAbsenceTime));
        monthWorkInfoResponse.setSpecialTime(durationToString.durationToString(monthSpecialTime));
        monthWorkInfoResponse.setLateNightWorkTime(durationToString.durationToString(monthLateNightWorkTime));
        monthWorkInfoResponse.setPaydHolidayTime(durationToString.durationToString(monthPaydHolidayTime));
        return monthWorkInfoResponse;
    }

    @GetMapping("/reach/user/requestlist")
    public ArrayResponse<UserRequestListResponse> returnUserRequestList(HttpSession session)
    {
        LocalDateTimeToString localDateTimeToString = new LocalDateTimeToString();
        String username = SecurityUtil.getCurrentUsername();
        Account adminAccount = accountService.findAccountByUsername(username);
        List<AccountApprover> accountApprovers = accountApproverService.findByApproverId(adminAccount);
        List<Account> accounts = new ArrayList<Account>();
        for(AccountApprover accountApprover : accountApprovers)
        {
            accounts.add(accountApprover.getAccountId());
        }

        List<UserRequestListResponse> userRequestListResponses = new ArrayList<UserRequestListResponse>();
        // それぞれの申請ごとに取得
        for(ShiftRequest shiftRequest : shiftRequestService.findByAccountIdIn(accounts))
        {
            UserRequestListResponse userRequestListResponseShiftRequest = new UserRequestListResponse();
            userRequestListResponseShiftRequest.setId(shiftRequest.getShiftRequestId().intValue());
            userRequestListResponseShiftRequest.setAccountId(shiftRequest.getAccountId().getId().intValue());
            userRequestListResponseShiftRequest.setAccountName(shiftRequest.getAccountId().getName());
            userRequestListResponseShiftRequest.setType(1);
            userRequestListResponseShiftRequest.setRequestDate(localDateTimeToString.localDateTimeToString(shiftRequest.getRequestDate()));
            userRequestListResponseShiftRequest.setRequestStatus(shiftRequest.getRequestStatus());
            userRequestListResponses.add(userRequestListResponseShiftRequest);
        }
        for(ShiftChangeRequest shiftChangeRequest : shiftChangeRequestService.findByAccountIdIn(accounts))
        {
            UserRequestListResponse userRequestListResponseShiftChangeRequest = new UserRequestListResponse();
            userRequestListResponseShiftChangeRequest.setId(shiftChangeRequest.getShiftChangeId().intValue());
            userRequestListResponseShiftChangeRequest.setAccountId(shiftChangeRequest.getAccountId().getId().intValue());
            userRequestListResponseShiftChangeRequest.setAccountName(shiftChangeRequest.getAccountId().getName());
            userRequestListResponseShiftChangeRequest.setType(2);
            userRequestListResponseShiftChangeRequest.setRequestDate(localDateTimeToString.localDateTimeToString(shiftChangeRequest.getRequestDate()));
            userRequestListResponseShiftChangeRequest.setRequestStatus(shiftChangeRequest.getRequestStatus());
            userRequestListResponses.add(userRequestListResponseShiftChangeRequest);
        }
        for(StampRequest stampRequest : stampRequestService.findByAccountIdIn(accounts))
        {
            UserRequestListResponse userRequestListResponseStampRequest = new UserRequestListResponse();
            userRequestListResponseStampRequest.setId(stampRequest.getStampId().intValue());
            userRequestListResponseStampRequest.setAccountId(stampRequest.getAccountId().getId().intValue());
            userRequestListResponseStampRequest.setAccountName(stampRequest.getAccountId().getName());
            userRequestListResponseStampRequest.setType(3);
            userRequestListResponseStampRequest.setRequestDate(localDateTimeToString.localDateTimeToString(stampRequest.getRequestDate()));
            userRequestListResponseStampRequest.setRequestStatus(stampRequest.getRequestStatus());
            userRequestListResponses.add(userRequestListResponseStampRequest);
        }
        for(VacationRequest vacationRequest : vacationRequestService.findByAccountIdIn(accounts))
        {
            UserRequestListResponse userRequestListResponseVacationRequest = new UserRequestListResponse();
            userRequestListResponseVacationRequest.setId(vacationRequest.getVacationId().intValue());
            userRequestListResponseVacationRequest.setAccountId(vacationRequest.getAccountId().getId().intValue());
            userRequestListResponseVacationRequest.setAccountName(vacationRequest.getAccountId().getName());
            userRequestListResponseVacationRequest.setType(4);
            userRequestListResponseVacationRequest.setRequestDate(localDateTimeToString.localDateTimeToString(vacationRequest.getRequestDate()));
            userRequestListResponseVacationRequest.setRequestStatus(vacationRequest.getRequestStatus());
            userRequestListResponses.add(userRequestListResponseVacationRequest);
        }
        for(OverTimeRequest overTimeRequest : overTimeRequestService.findByAccountIdIn(accounts))
        {
            UserRequestListResponse userRequestListResponseOverTimeRequest = new UserRequestListResponse();
            userRequestListResponseOverTimeRequest.setId(overTimeRequest.getOverTimeId().intValue());
            userRequestListResponseOverTimeRequest.setAccountId(overTimeRequest.getAccountId().getId().intValue());
            userRequestListResponseOverTimeRequest.setAccountName(overTimeRequest.getAccountId().getName());
            userRequestListResponseOverTimeRequest.setType(5);
            userRequestListResponseOverTimeRequest.setRequestDate(localDateTimeToString.localDateTimeToString(overTimeRequest.getRequestDate()));
            userRequestListResponseOverTimeRequest.setRequestStatus(overTimeRequest.getRequestStatus());
            userRequestListResponses.add(userRequestListResponseOverTimeRequest);
        }
        for(AttendanceExceptionRequest attendanceExceptionRequest : attendanceExceptionRequestService.findByAccountIdIn(accounts))
        {
            UserRequestListResponse userRequestListResponseAttendanceExceptionRequest = new UserRequestListResponse();
            userRequestListResponseAttendanceExceptionRequest.setId(attendanceExceptionRequest.getAttendanceExceptionId().intValue());
            userRequestListResponseAttendanceExceptionRequest.setAccountId(attendanceExceptionRequest.getAccountId().getId().intValue());
            userRequestListResponseAttendanceExceptionRequest.setAccountName(attendanceExceptionRequest.getAccountId().getName());
            userRequestListResponseAttendanceExceptionRequest.setType(6);
            userRequestListResponseAttendanceExceptionRequest.setRequestDate(localDateTimeToString.localDateTimeToString(attendanceExceptionRequest.getRequestDate()));
            userRequestListResponseAttendanceExceptionRequest.setRequestStatus(attendanceExceptionRequest.getRequestStatus());
            userRequestListResponses.add(userRequestListResponseAttendanceExceptionRequest);
        }
        for(MonthlyRequest monthlyRequest : monthlyRequestService.findByAccountIdIn(accounts))
        {
            UserRequestListResponse userRequestListResponseMonthlyRequest = new UserRequestListResponse();
            userRequestListResponseMonthlyRequest.setId(monthlyRequest.getMonthRequestId().intValue());
            userRequestListResponseMonthlyRequest.setAccountId(monthlyRequest.getAccountId().getId().intValue());
            userRequestListResponseMonthlyRequest.setAccountName(monthlyRequest.getAccountId().getName());
            userRequestListResponseMonthlyRequest.setType(7);
            userRequestListResponseMonthlyRequest.setRequestDate(localDateTimeToString.localDateTimeToString(monthlyRequest.getRequestDate()));
            userRequestListResponseMonthlyRequest.setRequestStatus(monthlyRequest.getRequestStatus());
            userRequestListResponses.add(userRequestListResponseMonthlyRequest);
        }

        userRequestListResponses.sort(Comparator.comparing(UserRequestListResponse::getRequestDate));

        int status = 1;
        return new ArrayResponse<UserRequestListResponse>(status, userRequestListResponses, "requestList");
    }

    @GetMapping("/reach/news")
    public ArrayResponse<NewsListResponse> returnNewsList(HttpSession session)
    {
        String username = SecurityUtil.getCurrentUsername();
        Account account = accountService.findAccountByUsername(username);
        if(Objects.isNull(account))
        {
            throw new RuntimeException("アカウントが存在しません");
        }
        List<NewsList> newsLists = newsListService.findByAccountId(account);
        List<NewsListResponse> newsListResponses = new ArrayList<NewsListResponse>();
        for(NewsList newsList : newsLists)
        {
            NewsListResponse newsListResponse = newsListService.newsListToNewsListResponse(newsList);
            newsListResponses.add(newsListResponse);
        }
        return new ArrayResponse<NewsListResponse>(1, newsListResponses, "newsList");
    }
}