package com.example.springboot.controller;

import java.util.Collections;
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
import com.example.springboot.dto.response.Response;
import com.example.springboot.dto.ArrayResponse;
import com.example.springboot.dto.input.LegalCheckShiftChangeInput;
import com.example.springboot.dto.input.LegalCheckShiftInput;
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
import com.example.springboot.service.AccountApproverService;
import com.example.springboot.service.AccountService;
import com.example.springboot.service.ApprovalSettingService;
import com.example.springboot.service.AttendService;
import com.example.springboot.service.AttendanceExceptionRequestService;
import com.example.springboot.service.AttendanceExceptionTypeService;
import com.example.springboot.service.DepartmentService;
import com.example.springboot.service.LegalCheckService;
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

    @Autowired
    LegalCheckService legalCheckService;

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
        Account account = accountService.findCurrentAccount();
        // 認証情報がなければエラー
        if(Objects.isNull(account))
        {
            RequestDetailStampResponse requestDetailStampResponse = new RequestDetailStampResponse();
            requestDetailStampResponse.setStatus(4);
            return requestDetailStampResponse;
        }
        RequestDetailStampResponse requestDetailStampResponse = requestService.getStampDetail(account, request.getRequestId());
        return requestDetailStampResponse;
    }

    @GetMapping("/reach/attendlist")
    public ArrayResponse<AttendListResponse> returnAttendList(HttpSession session, @ModelAttribute YearMonthInput request)
    {
        Account account = accountService.findCurrentAccount();
        if(Objects.isNull(account))
        {
            return new ArrayResponse<AttendListResponse>(4, Collections.emptyList(), "attendList");
        }
        List<AttendListResponse> attendListResponse = attendService.findAttendListFor(account, request);
        return new ArrayResponse<AttendListResponse>(1, attendListResponse, "attendList");
    }

    @GetMapping("/reach/requestdetail/vacation")
    public RequestDetailVacationResponse returnVacationDetil(HttpSession session, RequestIdInput request)
    {
        Account account = accountService.findCurrentAccount();
        if(Objects.isNull(account))
        {
            RequestDetailVacationResponse requestDetailVacation = new RequestDetailVacationResponse();
            requestDetailVacation.setStatus(4);
            return requestDetailVacation;
        }
        RequestDetailVacationResponse requestDetailVacationResponse = requestService.getVacationDetail(account, request.getRequestId());
        return requestDetailVacationResponse;
    }

    @GetMapping("/reach/requestdetail/overtime")
    public RequestDetailOverTimeResponse returnOverTimeDetil(HttpSession session, RequestIdInput request)
    {
        Account account = accountService.findCurrentAccount();
        if(Objects.isNull(account))
        {
            RequestDetailOverTimeResponse requestDetailOverTimeResponse = new RequestDetailOverTimeResponse();
            requestDetailOverTimeResponse.setStatus(4);
            return requestDetailOverTimeResponse;
        }
        RequestDetailOverTimeResponse resultRequestDetailOverTimeResponse = requestService.getOverTimeDetail(account, request.getRequestId());
        return resultRequestDetailOverTimeResponse;
    }

    @GetMapping("/reach/requestdetail/othertime")
    public RequestDetailOtherTimeResponse returnOtherTimeDetil(HttpSession session, RequestIdInput request)
    {
        Account account = accountService.findCurrentAccount();
        if(Objects.isNull(account))
        {
            RequestDetailOtherTimeResponse requestDetailOtherTimeResponse = new RequestDetailOtherTimeResponse();
            requestDetailOtherTimeResponse.setStatus(4);
            return requestDetailOtherTimeResponse;
        }
        RequestDetailOtherTimeResponse resultRequestDetailOtherTimeResponse = requestService.getOtherTimeDetail(account, request.getRequestId());
        return resultRequestDetailOtherTimeResponse;
    }
    // 月次申請詳細取得を作成後申請一覧取得の作成
    @GetMapping("/reach/requestdetail/monthly")
    public RequestDetailMonthlyResponse returnMonthlyResponse(HttpSession session, RequestIdInput request)
    {
        Account account = accountService.findCurrentAccount();
        if(Objects.isNull(account))
        {
            RequestDetailMonthlyResponse requestDetailMonthlyResponse = new RequestDetailMonthlyResponse();
            requestDetailMonthlyResponse.setStatus(4);
            return requestDetailMonthlyResponse;
        }
        RequestDetailMonthlyResponse requestDetailMonthlyResponse = requestService.getMonthlyDetail(account, request.getRequestId());
        return requestDetailMonthlyResponse;
    }

    @GetMapping("/reach/requestlist")
    public ArrayResponse<RequestListResponse> returnRequestList(HttpSession session)
    {
        Account account = accountService.findCurrentAccount();
        if(Objects.isNull(account))
        {
            return new ArrayResponse<RequestListResponse>(3, Collections.emptyList(), "requestList");
        }
        List<RequestListResponse> requestListResponse = requestService.getRequestList(account);
        return new ArrayResponse<RequestListResponse>(1, requestListResponse, "requestList");
    }

    @GetMapping("/reach/monthworkinfo")
    public MonthWorkInfoResponse returnMonthWorkInfo(YearMonthInput request)
    {
        Account account = accountService.findCurrentAccount();
        if(Objects.isNull(account))
        {
            MonthWorkInfoResponse monthWorkInfoResponse = new MonthWorkInfoResponse();
            monthWorkInfoResponse.setStatus(3);
            return monthWorkInfoResponse;
        }
        return attendService.monthWorkInfoResponse(account, request);
    }

    @GetMapping("/reach/style")
    public StyleResponse returnStyle(HttpSession session)
    {
        Account account = accountService.findCurrentAccount();
        if(Objects.isNull(account))
        {
            StyleResponse response = new StyleResponse();
            response.setStatus(3);
            return response;
        }
        return styleService.returnStyle(account);
    }

    @GetMapping("/reach/approver")
    public ApproverResponse returnApprover()
    {
        Account account = accountService.findCurrentAccount();
        if(Objects.isNull(account))
        {
            ApproverResponse approverResponse = new ApproverResponse();
            approverResponse.setStatus(3);
            return approverResponse;
        }
        ApproverResponse approverResponse = accountApproverService.getApproverResponse(account);
        return approverResponse;
    }

    @GetMapping("/reach/allvacationtypelist")
    public ArrayResponse<VacationTypeListResponse> returnAllVacationType(HttpSession session)
    {
        Account account = accountService.findCurrentAccount();
        if(Objects.isNull(account))
        {
            return new ArrayResponse<VacationTypeListResponse>(3, Collections.emptyList(), "vacationTypes");
        }
        List<VacationTypeListResponse> vacationTypeListResponses = vacationTypeService.returnAllVacationTypeListResponses();
        return new ArrayResponse<VacationTypeListResponse>(1, vacationTypeListResponses, "vacationTypes");
    }

    @GetMapping("/reach/allothertypelist")
    public ArrayResponse<OtherTypeListResponse> returnAllOtherTypes(HttpSession session)
    {
        Account account = accountService.findCurrentAccount();
        if(Objects.isNull(account))
        {
            return new ArrayResponse<OtherTypeListResponse>(3, Collections.emptyList(), "otherTypes");
        }
        List<OtherTypeListResponse> otherTypeListResponses = attendanceExceptionTypeService.returnOtherTypeListResponses();
        return new ArrayResponse<OtherTypeListResponse>(1, otherTypeListResponses, "otherTypes");
    }

    @GetMapping("/reach/paydholidayhistory")
    public ArrayResponse<PaydHolidayHistoryListResponse> returnPaydHolidayHistory(HttpSession session, YearInput request)
    {
        Account account = accountService.findCurrentAccount();
        if(Objects.isNull(account))
        {
            return new ArrayResponse<PaydHolidayHistoryListResponse>(3, Collections.emptyList(), "holidaylist");
        }
        List<PaydHolidayHistoryListResponse> paydHolidayHistoryListResponses = paydHolidayService.returnPaydHolidayHistoryListResponses(account);
        return new ArrayResponse<PaydHolidayHistoryListResponse>(1, paydHolidayHistoryListResponses, "holidaylist");
    }

    @GetMapping("/reach/user/attendinfo")
    public ArrayResponse<AttendListResponse> returnUserAttendList(HttpSession session, UserAttendInput request)
    {
        Account adminAccount = accountService.findCurrentAccount();
        if(Objects.isNull(adminAccount))
        {
            return new ArrayResponse<AttendListResponse>(3, Collections.emptyList(), "attendList");
        }

        // リクエストを送ってきているのは承認者なので引数に注意しながらメソッドに渡す
        AccountApprover accountApprover = accountApproverService.findAccountAndApprover(request.getAccountId(), adminAccount);
        // 情報が取得できていなければ利用者の承認者として設定されていないことになる
        if(Objects.isNull(accountApprover))
        {
            return new ArrayResponse<AttendListResponse>(3, Collections.emptyList(), "attendList");
        }
        List<AttendListResponse> attendListResponses = attendService.getAttendListResponses(adminAccount, request);
        return new ArrayResponse<AttendListResponse>(1, attendListResponses, "attendList");
    }

    @GetMapping("/reach/user/shiftinfo")
    public ArrayResponse<ShiftListResponse> returnUserShiftList(HttpSession session, UserShiftInput request)
    {
        Account adminAccount = accountService.findCurrentAccount();
        if(Objects.isNull(adminAccount))
        {
            return new ArrayResponse<ShiftListResponse>(1, Collections.emptyList(), "shiftList");
        }
        // リクエストを送ってきているのは承認者なので引数に注意しながらメソッドに渡す
        AccountApprover accountApprover = accountApproverService.findAccountAndApprover(request.getAccountId(), adminAccount);
        // 情報が取得できていなければ利用者の承認者として設定されていないことになる
        if(Objects.isNull(accountApprover))
        {
            return new ArrayResponse<ShiftListResponse>(3, Collections.emptyList(), "shiftList");
        }

        List<ShiftListResponse> shiftListResponses = shiftService.returnShiftListResponses(adminAccount, accountApprover, request);
        return new ArrayResponse<ShiftListResponse>(1, shiftListResponses, "shiftList");
    }
    
    @GetMapping("/reach/user/monthworkinfo")
    public MonthWorkInfoResponse returnUserMonthWorkInfo(HttpSession session, UserMonthWorkInfoInput request)
    {
        Account adminAccount = accountService.findCurrentAccount();
        if(Objects.isNull(adminAccount))
        {
            MonthWorkInfoResponse monthWorkInfoResponse = new MonthWorkInfoResponse();
            monthWorkInfoResponse.setStatus(3);
            return monthWorkInfoResponse;
        }
        // 管理者のアカウントと利用者のアカウントの引数の順番に注意
        AccountApprover accountApprover = accountApproverService.findAccountAndApprover(request.getAccountId(), adminAccount);
        if(Objects.isNull(accountApprover))
        {
            MonthWorkInfoResponse monthWorkInfoResponse = new MonthWorkInfoResponse();
            monthWorkInfoResponse.setStatus(3);
            return monthWorkInfoResponse;
        }
        MonthWorkInfoResponse monthWorkInfoResponse = attendService.getMonthWorkInfoResponse(adminAccount, accountApprover, request);
        return monthWorkInfoResponse;
    }

    @GetMapping("/reach/user/requestlist")
    public ArrayResponse<UserRequestListResponse> returnUserRequestList(HttpSession session)
    {
        Account adminAccount = accountService.findCurrentAccount();
        if(Objects.isNull(adminAccount))
        {
            return new ArrayResponse<UserRequestListResponse>(1, Collections.emptyList(), "requestList");
        }
        List<UserRequestListResponse> userRequestListResponses = requestService.getUserRequestListResponses(adminAccount);
        return new ArrayResponse<UserRequestListResponse>(1, userRequestListResponses, "requestList");
    }

    @GetMapping("/reach/news")
    public ArrayResponse<NewsListResponse> returnNewsList(HttpSession session)
    {
        Account account = accountService.findCurrentAccount();
        if(Objects.isNull(account))
        {
            return new ArrayResponse<NewsListResponse>(3, Collections.emptyList(), "newsList");
        }
        List<NewsListResponse> newsListResponses = newsListService.returnNewsList(account);
        return new ArrayResponse<NewsListResponse>(1, newsListResponses, "newsList");
    }

    @GetMapping("/reach/legalcheck/shift")
    public Response shiftLegalCheck(LegalCheckShiftInput legalCheckShiftInput)
    {
        Account account = accountService.findCurrentAccount();
        if(Objects.isNull(account))
        {
            return new Response(3);
        }
        int result = legalCheckService.shiftLegalCheck(account, legalCheckShiftInput);
        return new Response(result);
    }

    @GetMapping("/reach/legalcheck/shiftchange")
    public Response shiftChangeResponse(LegalCheckShiftChangeInput legalCheckShiftChangeInput)
    {
        Account account = accountService.findCurrentAccount();
        if(Objects.isNull(account))
        {
            return new Response(3);
        }
        int result = legalCheckService.shiftChangeLegalCheck(account, legalCheckShiftChangeInput);
        return new Response(result);
    }

}