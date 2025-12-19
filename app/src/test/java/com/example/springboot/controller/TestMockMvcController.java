package com.example.springboot.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import com.example.springboot.Config;
import com.example.springboot.dto.AllStyleListResponse;
import com.example.springboot.dto.change.LocalDateTimeToString;
import com.example.springboot.dto.change.StringToLocalDateTime;
import com.example.springboot.dto.input.LegalCheckShiftChangeInput;
import com.example.springboot.dto.input.LegalCheckShiftInput;
import com.example.springboot.dto.input.MonthlyInput;
import com.example.springboot.dto.input.OtherTimeInput;
import com.example.springboot.dto.input.OverTimeInput;
import com.example.springboot.dto.input.RequestJudgmentInput;
import com.example.springboot.dto.input.ShiftChangeInput;
import com.example.springboot.dto.input.ShiftInput;
import com.example.springboot.dto.input.StampInput;
import com.example.springboot.dto.input.UserAttendInput;
import com.example.springboot.dto.input.UserMonthWorkInfoInput;
import com.example.springboot.dto.input.UserShiftInput;
import com.example.springboot.dto.input.VacationInput;
import com.example.springboot.dto.input.WithDrowInput;
import com.example.springboot.dto.input.YearMonthInput;
import com.example.springboot.dto.response.AccountInfoResponse;
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
import com.example.springboot.dto.response.ShiftListResponse;
import com.example.springboot.dto.response.StyleResponse;
import com.example.springboot.dto.response.UserRequestListResponse;
import com.example.springboot.dto.response.VacationTypeListResponse;
import com.example.springboot.model.Account;
import com.example.springboot.model.AccountApprover;
import com.example.springboot.model.Role;
import com.example.springboot.model.Shift;
import com.example.springboot.model.ShiftChangeRequest;
import com.example.springboot.model.ShiftListOtherTime;
import com.example.springboot.model.ShiftListOverTime;
import com.example.springboot.model.ShiftListShiftRequest;
import com.example.springboot.model.ShiftListVacation;
import com.example.springboot.model.ShiftRequest;
import com.example.springboot.model.StampRequest;
import com.example.springboot.model.Style;
import com.example.springboot.model.StylePlace;
import com.example.springboot.model.Vacation;
import com.example.springboot.model.VacationRequest;
import com.example.springboot.model.VacationType;
import com.example.springboot.model.ApprovalSetting;
import com.example.springboot.model.Attend;
import com.example.springboot.model.AttendanceExceptionRequest;
import com.example.springboot.model.AttendanceExceptionType;
import com.example.springboot.model.AttendanceListSource;
import com.example.springboot.model.Department;
import com.example.springboot.model.MonthlyRequest;
import com.example.springboot.model.NewsList;
import com.example.springboot.model.OverTimeRequest;
import com.example.springboot.service.AccountApproverService;
import com.example.springboot.service.AccountService;
import com.example.springboot.service.ApprovalSettingService;
import com.example.springboot.service.AttendService;
import com.example.springboot.service.AttendanceExceptionRequestService;
import com.example.springboot.service.AttendanceExceptionTypeService;
import com.example.springboot.service.AttendanceListSourceService;
import com.example.springboot.service.DepartmentService;
import com.example.springboot.service.LegalCheckService;
import com.example.springboot.service.LegalTimeService;
import com.example.springboot.service.MonthlyRequestService;
import com.example.springboot.service.NewsListService;
import com.example.springboot.service.OverTimeRequestService;
import com.example.springboot.service.PaydHolidayService;
import com.example.springboot.service.PaydHolidayUseService;
import com.example.springboot.service.RequestService;
import com.example.springboot.service.RoleService;
import com.example.springboot.service.ShiftChangeRequestService;
import com.example.springboot.service.ShiftListOtherTimeService;
import com.example.springboot.service.ShiftListOverTimeService;
import com.example.springboot.service.ShiftListShiftRequestService;
import com.example.springboot.service.ShiftListVacationService;
import com.example.springboot.service.ShiftRequestService;
import com.example.springboot.service.ShiftService;
import com.example.springboot.service.StampRequestService;
import com.example.springboot.service.StylePlaceService;
import com.example.springboot.service.StyleService;
import com.example.springboot.service.VacationRequestService;
import com.example.springboot.service.VacationService;
import com.example.springboot.service.VacationTypeService;

@ContextConfiguration(classes = Config.class)
@WebMvcTest({PostController.class,GetController.class})
@AutoConfigureMockMvc
@Import({PostController.class,GetController.class})
public class TestMockMvcController
{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    PasswordEncoder passwordEncoder;

    @MockBean
    private AccountService accountService;

    @MockBean
    private RoleService roleService;

    @MockBean
    private DepartmentService departmentService;

    @MockBean
    private ShiftRequestService shiftRequestService;

    @MockBean
    private ApprovalSettingService approvalSettingService;

    @MockBean
    private AccountApproverService accountApproverService;

    @MockBean
    private StyleService styleService;

    @MockBean
    private StylePlaceService stylePlaceService;

    @MockBean
    private ShiftService shiftService;

    @MockBean
    private LegalTimeService legalTimeService;

    @MockBean
    private ShiftChangeRequestService shiftChangeRequestService;

    @MockBean
    private StampRequestService stampRequestService;

    @MockBean
    private AttendService attendService;

    @MockBean
    private VacationRequestService vacationRequestService;

    @MockBean
    private OverTimeRequestService overTimeRequestService;

    @MockBean
    private AttendanceExceptionRequestService attendanceExceptionRequestService;

    @MockBean
    private VacationService vacationService;

    @MockBean
    private ShiftListShiftRequestService shiftListShiftRequestService;

    @MockBean
    private MonthlyRequestService monthlyRequestService;

    @MockBean
    private VacationTypeService vacationTypeService;

    @MockBean
    private AttendanceExceptionTypeService attendanceExceptionTypeService;

    @MockBean
    private ShiftListOverTimeService shiftListOverTimeService;

    @MockBean
    private PaydHolidayService paydHolidayService;

    @MockBean
    private PaydHolidayUseService paydHolidayUseService;

    @MockBean
    private ShiftListOtherTimeService shiftListOtherTimeService;

    @MockBean
    private NewsListService newsListService;

    @MockBean
    private ShiftListVacationService shiftListVacationService;

    @MockBean
    private AttendanceListSourceService attendanceListSourceService;

    @MockBean
    private RequestService requestService;

    @MockBean
    private LegalCheckService legalCheckService;

    @Test
    void loginSuccess() throws Exception
    {
        Account generalAccount = new Account();
        String generalAccountUsername = "testuser";
        String generalAccountPassword = "password";
        generalAccount.setUsername(generalAccountUsername);
        generalAccount.setPassword(passwordEncoder.encode(generalAccountPassword));

        String json = String.format
        (
            """
                {
                    "id": "%s",
                    "password": "%s"
                }
            """,
            generalAccountUsername, generalAccountPassword
        );

        when(accountService.findAccountByUsername(anyString())).thenReturn(generalAccount);
        mockMvc.perform
        (
            post("/api/send/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(1)
        );
    }

    @Test
    void logoutAuthenticatedUserSuccess() throws Exception
    {
        mockMvc.perform
        (
            post("/api/send/logout")
            .with(csrf())
            .with(user("testuser")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(1)
        );
    }

    @Test
    void getapproverlistSuccess() throws Exception
    {
        // userの情報
        String generalAccountName = "testuser";
        // whenで返す情報に必要
        Long adminAccountId = 5L;
        String adminAccountName = "adminuser";
        String adminDepartmentName = "soumu";
        String adminRoleName = "katyou";

        // whenの処理が実行された際返すaccountの作成
        Account generalAccount = new Account();
        generalAccount.setUsername(generalAccountName);

        ApproverListResponse approverListResponse = new ApproverListResponse(adminAccountId, adminAccountName, adminDepartmentName, adminRoleName);
        List<ApproverListResponse> approverListResponses = new ArrayList<ApproverListResponse>();
        approverListResponses.add(approverListResponse);

        when(accountService.findCurrentAccount()).thenReturn(generalAccount);
        when(accountService.findApproverListFor(any(Account.class))).thenReturn(approverListResponses);
        mockMvc.perform
        (
            get("/api/reach/approverlist")
            .with(csrf())
            .with(user(generalAccountName))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(1))
        .andExpect(jsonPath("$.approverlist[0].id").value(adminAccountId))
        .andExpect(jsonPath("$.approverlist[0].name").value(adminAccountName))
        .andExpect(jsonPath("$.approverlist[0].departmentName").value(adminDepartmentName))
        .andExpect(jsonPath("$.approverlist[0].roleName").value(adminRoleName));
    }

    @Test
    void approversetSuccess() throws Exception
    {
        Account generalAccount = new Account();
        Long generalAccountId = 1L;
        String generalAccountName = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountName);

        int sendNewAdminAccountId = 3;
        String json = String.format
        (
            """
                {
                    "approver": "%s"
                }
            """,
            sendNewAdminAccountId
        );

        when(accountService.findCurrentAccount()).thenReturn(generalAccount);
        when(accountApproverService.updateApprover(any(Account.class), anyLong())).thenReturn(1);
        mockMvc.perform
        (
            post("/api/send/approverset")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .with(csrf())
            .with(user(generalAccountName))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(1));
    }

    @Test
    void getAllStyleListSuccess() throws Exception
    {
        // whenで返すアカウントオブジェクト
        String generalAccountName = "testuser";
        Long generalAccountId = 1L;
        Account generalAccount = new Account();
        generalAccount.setId(generalAccountId);
        generalAccount.setName(generalAccountName);
        // whenで返すスタイルリストオブジェクト
        Long workStylePlaceId = 1L;
        Long homeStylePlaceId = 2L;
        String workStylePlaceName = "work";
        String homeStylePlaceName = "home";
        List<AllStyleListResponse> styleListResponse = new ArrayList<AllStyleListResponse>();
        AllStyleListResponse styleTypeWork = new AllStyleListResponse();
        AllStyleListResponse styleTypeHome = new AllStyleListResponse();
        styleTypeWork.setId(workStylePlaceId);
        styleTypeHome.setId(homeStylePlaceId);
        styleTypeWork.setName(workStylePlaceName);
        styleTypeHome.setName(homeStylePlaceName);
        styleListResponse.add(styleTypeWork);
        styleListResponse.add(styleTypeHome);

        when(accountService.findCurrentAccount()).thenReturn(generalAccount);
        when(stylePlaceService.findStyleList()).thenReturn(styleListResponse);
        mockMvc.perform
        (
            get("/api/reach/allstylelist")
            .with(csrf())
            .with(user(generalAccountName))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(1))
        .andExpect(jsonPath("$.styleList[0].name").value(workStylePlaceName))
        .andExpect(jsonPath("$.styleList[1].name").value(homeStylePlaceName));
    }

    @Test
    void styleSetSuccess() throws Exception
    {
        // whenで返すアカウントのオブジェクト
        String generalAccountUsername = "testuser";
        Long generalAccountId = 1L;
        Account generalAccount = new Account();
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        int sendNewStyleId = 2;

        String json = String.format
        (
            """
                {
                    "style": "%s"
                }
            """,
            sendNewStyleId
        );

        when(accountService.findCurrentAccount()).thenReturn(generalAccount);
        when(styleService.updateStyle(any(Account.class), anyLong())).thenReturn(1);
        mockMvc.perform
        (
            post("/api/send/style")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .with(csrf())
            .with(user(generalAccountUsername))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(1));
    }
    
    @Test
    void accountInfoSuccess() throws Exception
    {
        Long generalAccountId = 1L;
        String generalAccountName = "testuser";
        Long generalRoleId = 34L;
        String generalRoleName = "kakarityou";
        Long generalDepartmentId = 4L;
        String generalDepartmentName = "soumu";
        Boolean generalAccountAdmin = false;

        Account account = new Account();
        Role role = new Role();
        Department department = new Department();
        role.setId(generalRoleId);
        role.setName(generalRoleName);
        department.setId(generalDepartmentId);
        department.setName(generalDepartmentName);
        account.setId(generalAccountId);
        account.setName(generalAccountName);
        account.setRoleId(role);
        account.setDepartmentId(department);

        AccountInfoResponse accountInfoResponse = new AccountInfoResponse(1, generalAccountName, generalDepartmentName, generalRoleName, generalAccountAdmin);

        when(accountService.findCurrentAccount()).thenReturn(account);
        when(accountService.getCurrentAccountInfo(any(Account.class))).thenReturn(accountInfoResponse);
        mockMvc.perform
        (
            get("/api/reach/accountinfo")
            .with(csrf())
            .with(user(generalAccountName))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(1))
        .andExpect(jsonPath("$.name").value(generalAccountName))
        .andExpect(jsonPath("$.departmentName").value(generalDepartmentName))
        .andExpect(jsonPath("$.roleName").value(generalRoleName))
        .andExpect(jsonPath("$.admin").value(generalAccountAdmin));
    }

    @Test
    void adminAccountInfoSuccess() throws Exception
    {
        Long adminAccountId = 1L;
        String adminAccountName = "testuser";
        Long adminRoleId = 34L;
        String adminRoleName = "kakarityou";
        Long adminDepartmentId = 4L;
        String adminDepartmentName = "soumu";
        Boolean adminAccountAdmin = true;

        Account account = new Account();
        Role role = new Role();
        Department department = new Department();
        role.setId(adminRoleId);
        role.setName(adminRoleName);
        department.setId(adminDepartmentId);
        department.setName(adminDepartmentName);
        account.setId(adminAccountId);
        account.setName(adminAccountName);
        account.setRoleId(role);
        account.setDepartmentId(department);

        Role generalRole = new Role();
        Long generalRoleId = 3L;
        generalRole.setId(generalRoleId);

        List<ApprovalSetting> approvalSettings = new ArrayList<ApprovalSetting>();
        ApprovalSetting approvalSetting = new ApprovalSetting();
        approvalSetting.setRoleId(generalRole);
        approvalSetting.setApprovalId(role);
        approvalSettings.add(approvalSetting);

        AccountInfoResponse accountInfoResponse = new AccountInfoResponse(1, adminAccountName, adminDepartmentName, adminRoleName, adminAccountAdmin);

        when(accountService.findCurrentAccount()).thenReturn(account);
        when(accountService.getCurrentAccountInfo(any(Account.class))).thenReturn(accountInfoResponse);
        mockMvc.perform
        (
            get("/api/reach/accountinfo")
            .with(csrf())
            .with(user(adminAccountName))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(1))
        .andExpect(jsonPath("$.name").value(adminAccountName))
        .andExpect(jsonPath("$.departmentName").value(adminDepartmentName))
        .andExpect(jsonPath("$.roleName").value(adminRoleName))
        .andExpect(jsonPath("$.admin").value(adminAccountAdmin));
    }

    @Test
    void shiftListSuccess() throws Exception
    {
        Account generalAccount = new Account();
        Long generalAccountId = 3L;
        String generalAccountName = "testuer";
        generalAccount.setId(generalAccountId);
        generalAccount.setName(generalAccountName);

        String time = "00:00:00";
        Long generalShiftId = 1L;

        String generalShiftBeginWork = "2025/06/21T08:30:00";
        String generalShiftEndWork = "2025/06/21T17:30:00";
        String generalShiftBeginBreak = "2025/06/21T11:30:00";
        String generalShiftEndBreak = "2025/06/21T12:30:30";
        Time generalShiftLateness = Time.valueOf(time);
        Time generalShiftLeaveEarly = Time.valueOf(time);
        Time generalShiftOuting = Time.valueOf(time);
        Time generalShiftOverWork = Time.valueOf(time);

        ShiftListResponse generalShiftListResponse = new ShiftListResponse
        (
            generalShiftId,
            generalShiftBeginWork,
            generalShiftEndWork,
            generalShiftBeginBreak,
            generalShiftEndBreak,
            generalShiftLateness,
            generalShiftLeaveEarly,
            generalShiftOuting,
            generalShiftOverWork
        );
        List<ShiftListResponse> generalShiftListResponses = List.of(generalShiftListResponse);

        when(accountService.findCurrentAccount()).thenReturn(generalAccount);
        when(shiftService.findByShiftListFor(any(Account.class), any(YearMonthInput.class))).thenReturn(generalShiftListResponses);
        mockMvc.perform
        (
            get("/api/reach/shiftlist")
            .param("year", "2025")
            .param("month", "6")
            .with(csrf())
            .with(user(generalAccountName))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(1))
        .andExpect(jsonPath("$.shiftList[0].id").value(generalShiftListResponse.getId()))
        .andExpect(jsonPath("$.shiftList[0].beginWork").value(generalShiftListResponse.getBeginWork()))
        .andExpect(jsonPath("$.shiftList[0].endWork").value(generalShiftListResponse.getEndWork()))
        .andExpect(jsonPath("$.shiftList[0].beginBreak").value(generalShiftListResponse.getBeginBreak()))
        .andExpect(jsonPath("$.shiftList[0].endBreak").value(generalShiftListResponse.getEndBreak()))
        .andExpect(jsonPath("$.shiftList[0].lateness").value(String.valueOf(generalShiftListResponse.getLateness())))
        .andExpect(jsonPath("$.shiftList[0].leaveEarly").value(String.valueOf(generalShiftListResponse.getLeaveEarly())))
        .andExpect(jsonPath("$.shiftList[0].outing").value(String.valueOf(generalShiftListResponse.getOuting())))
        .andExpect(jsonPath("$.shiftList[0].overWork").value(String.valueOf(generalShiftListResponse.getOverWork())));
    }

    @Test
    void shiftRequestSuccess() throws Exception
    {
        String generalBeginWork = "2026/09/02T09:00:00";
        String generalBeginBreak = "2026/09/02T12:00:00";
        String generalEndBreak = "2026/09/02T13:00:00";
        String generalEndWork = "2026/09/02T18:00:00";
        String generalRequestComment = "";
        String generalRequestDate = "2025/08/02T00:00:11";
        String json = String.format
        (
            """
                {
                    "beginWork": "%s",
                    "beginBreak": "%s",
                    "endBreak": "%s",
                    "endWork": "%s",
                    "requestComment": "%s",
                    "requestDate": "%s"
                }
            """,
        generalBeginWork, generalBeginBreak, generalEndBreak, generalEndWork, generalRequestComment, generalRequestDate
        );

        Account generalAccount = new Account();
        Long generalAccountId = 1L;
        String generalAccountName = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setName(generalAccountName);

        when(accountService.findCurrentAccount()).thenReturn(generalAccount);
        when(requestService.createShiftRequest(any(Account.class), any(ShiftInput.class))).thenReturn(1);
        mockMvc.perform
        (
            post("/api/send/shift")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .with(csrf())
            .with(user(generalAccountName))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(1));
    }

    @Test
    void shiftRequestDetailSuccess() throws Exception
    {
        Account generalAccount = new Account();
        String generalAccountUsername = "testuser";
        Long generalAccountId = 1L;
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        Account adminAccount = new Account();
        String adminAccountName = "かまどたかしろう";
        Long adminAccountId = 2L;
        adminAccount.setId(adminAccountId);
        adminAccount.setName(adminAccountName);

        LocalDateTime beginWork = LocalDateTime.parse("2025/08/08/09/30/00",DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        LocalDateTime endWork = LocalDateTime.parse("2025/08/08/12/30/00",DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        LocalDateTime beginBreak = LocalDateTime.parse("2025/08/13/09/30/00",DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        LocalDateTime endBreak = LocalDateTime.parse("2025/08/08/18/30/00",DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        String requestComment = "";
        LocalDateTime requestDate = LocalDateTime.parse("2025/07/07/00/00/00",DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        int requestStatus = 1;
        LocalDateTime approvalTime = null;
        String approverComment = null;

        RequestDetailShiftResponse requestDetailShiftResponse =
        new RequestDetailShiftResponse
        (
            1,
            beginWork.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + beginWork.format(DateTimeFormatter.ofPattern("HH:mm:ss")),
            endWork.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + endWork.format(DateTimeFormatter.ofPattern("HH:mm:ss")),
            beginBreak.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + beginBreak.format(DateTimeFormatter.ofPattern("HH:mm:ss")),
            endBreak.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + endBreak.format(DateTimeFormatter.ofPattern("HH:mm:ss")),
            false,
            requestComment,
            requestDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + requestDate.format(DateTimeFormatter.ofPattern("HH:mm:ss")),
            requestStatus,
            adminAccountId.intValue(),
            adminAccountName,
            approverComment,
            approvalTime == null ? "" : approvalTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + approvalTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
        );

        when(accountService.findCurrentAccount()).thenReturn(generalAccount);
        when(requestService.getShiftDetail(any(Account.class), anyLong())).thenReturn(requestDetailShiftResponse);
        mockMvc.perform
        (
            get("/api/reach/requestdetail/shift")
            .param("requestId","1")
            .with(csrf())
            .with(user(generalAccountUsername))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(1))
        .andExpect(jsonPath("$.beginWork").value(beginWork.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + beginWork.format(DateTimeFormatter.ofPattern("HH:mm:ss"))))
        .andExpect(jsonPath("$.endWork").value(endWork.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + endWork.format(DateTimeFormatter.ofPattern("HH:mm:ss"))))
        .andExpect(jsonPath("$.beginBreak").value(beginBreak.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + beginBreak.format(DateTimeFormatter.ofPattern("HH:mm:ss"))))
        .andExpect(jsonPath("$.endBreak").value(endBreak.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + endBreak.format(DateTimeFormatter.ofPattern("HH:mm:ss"))))
        .andExpect(jsonPath("$.requestComment").value(requestComment))
        .andExpect(jsonPath("$.requestDate").value(requestDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + requestDate.format(DateTimeFormatter.ofPattern("HH:mm:ss"))))
        .andExpect(jsonPath("$.requestStatus").value(requestStatus))
        .andExpect(jsonPath("$.approverId").value(adminAccountId.intValue()))
        .andExpect(jsonPath("$.approverName").value(adminAccountName))
        .andExpect(jsonPath("$.approverComment").value(approverComment))
        .andExpect(jsonPath("$.approvalTime").value(Objects.isNull(approvalTime) ? "" : approvalTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + approvalTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"))));
    }

    @Test
    void shiftChangeRequestDetailSuccess() throws Exception
    {
        Account generalAccount = new Account();
        String generalAccountUsername = "testuser";
        Long generalAccountId = 1L;
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        Account adminAccount = new Account();
        String adminAccountName = "かまどたかしろう";
        Long adminAccountId = 2L;
        adminAccount.setId(adminAccountId);
        adminAccount.setName(adminAccountName);

        Shift shift = new Shift();
        Long shiftId = 1L;
        shift.setShiftId(shiftId);

        LocalDateTime beginWork = LocalDateTime.parse("2025/08/08/09/30/00",DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        LocalDateTime endWork = LocalDateTime.parse("2025/08/08/12/30/00",DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        LocalDateTime beginBreak = LocalDateTime.parse("2025/08/13/09/30/00",DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        LocalDateTime endBreak = LocalDateTime.parse("2025/08/08/18/30/00",DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        String requestComment = "";
        LocalDateTime requestDate = LocalDateTime.parse("2025/07/07/00/00/00",DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        int requestStatus = 1;
        LocalDateTime approvalTime = null;
        String approverComment = null;

        RequestDetailShiftChangeResponse requestDetailShiftChangeResponse =
        new RequestDetailShiftChangeResponse
        (
            1,
            shiftId.intValue(),
            beginWork.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + beginWork.format(DateTimeFormatter.ofPattern("HH:mm:ss")),
            endWork.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + endWork.format(DateTimeFormatter.ofPattern("HH:mm:ss")),
            beginBreak.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + beginBreak.format(DateTimeFormatter.ofPattern("HH:mm:ss")),
            endBreak.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + endBreak.format(DateTimeFormatter.ofPattern("HH:mm:ss")),
            false,
            requestComment,
            requestDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + requestDate.format(DateTimeFormatter.ofPattern("HH:mm:ss")),
            requestStatus,
            adminAccountId.intValue(),
            adminAccountName,
            approverComment,
            approvalTime == null ? "" : approvalTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + approvalTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
        );

        when(accountService.findCurrentAccount()).thenReturn(generalAccount);
        when(requestService.getShiftChangeDetail(any(Account.class), anyLong())).thenReturn(requestDetailShiftChangeResponse);
        mockMvc.perform
        (
            get("/api/reach/requestdetail/changetime")
            .param("requestId","1")
            .with(csrf())
            .with(user(generalAccountUsername))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(1))
        .andExpect(jsonPath("$.shiftId").value(shiftId))
        .andExpect(jsonPath("$.beginWork").value(beginWork.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + beginWork.format(DateTimeFormatter.ofPattern("HH:mm:ss"))))
        .andExpect(jsonPath("$.endWork").value(endWork.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + endWork.format(DateTimeFormatter.ofPattern("HH:mm:ss"))))
        .andExpect(jsonPath("$.beginBreak").value(beginBreak.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + beginBreak.format(DateTimeFormatter.ofPattern("HH:mm:ss"))))
        .andExpect(jsonPath("$.endBreak").value(endBreak.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + endBreak.format(DateTimeFormatter.ofPattern("HH:mm:ss"))))
        .andExpect(jsonPath("$.requestComment").value(requestComment))
        .andExpect(jsonPath("$.requestDate").value(requestDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + requestDate.format(DateTimeFormatter.ofPattern("HH:mm:ss"))))
        .andExpect(jsonPath("$.requestStatus").value(requestStatus))
        .andExpect(jsonPath("$.approverId").value(adminAccountId))
        .andExpect(jsonPath("$.approverName").value(adminAccountName))
        .andExpect(jsonPath("$.approverComment").value(approverComment))
        .andExpect(jsonPath("$.approvalTime").value(Objects.isNull(approvalTime) ? "" : approvalTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + approvalTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"))));
    }

    @Test
    void stampRequestDetailSuccess() throws Exception
    {
        LocalDateTimeToString localDateTimeToString = new LocalDateTimeToString();
        Account generalAccount = new Account();
        String generalAccountUsername = "testuser";
        Long generalAccountId = 1L;
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        Account adminAccount = new Account();
        String adminAccountName = "かまどたかしろう";
        Long adminAccountId = 2L;
        adminAccount.setId(adminAccountId);
        adminAccount.setName(adminAccountName);

        Shift shift = new Shift();
        Long shiftId = 1L;
        shift.setShiftId(shiftId);

        Long stampId = 1L;
        LocalDateTime beginWork = LocalDateTime.parse("2025/08/08/09/30/00",DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        LocalDateTime endWork = LocalDateTime.parse("2025/08/08/12/30/00",DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        LocalDateTime beginBreak = LocalDateTime.parse("2025/08/13/09/30/00",DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        LocalDateTime endBreak = LocalDateTime.parse("2025/08/08/18/30/00",DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        String requestComment = "";
        LocalDateTime requestDate = LocalDateTime.parse("2025/07/07/00/00/00",DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        int requestStatus = 1;
        LocalDateTime approvalTime = null;
        String approverComment = null;

        RequestDetailStampResponse requestDetailStampResponse =
        new RequestDetailStampResponse
        (
            1,
            stampId.intValue(),
            beginWork.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + beginWork.format(DateTimeFormatter.ofPattern("HH:mm:ss")),
            endWork.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + endWork.format(DateTimeFormatter.ofPattern("HH:mm:ss")),
            beginBreak.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + beginBreak.format(DateTimeFormatter.ofPattern("HH:mm:ss")),
            endBreak.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + endBreak.format(DateTimeFormatter.ofPattern("HH:mm:ss")),
            requestComment,
            requestDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + requestDate.format(DateTimeFormatter.ofPattern("HH:mm:ss")),
            requestStatus,
            adminAccountId.intValue(),
            adminAccountName,
            approverComment,
            approvalTime == null ? "" : approvalTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + approvalTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
        );

        when(accountService.findCurrentAccount()).thenReturn(generalAccount);
        when(requestService.getStampDetail(any(Account.class), anyLong())).thenReturn(requestDetailStampResponse);
        mockMvc.perform
        (
            get("/api/reach/requestdetail/stamp")
            .param("requestId","1")
            .with(csrf())
            .with(user(generalAccountUsername))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(1))
        .andExpect(jsonPath("$.shiftId").value(shiftId))
        .andExpect(jsonPath("$.beginWork").value(localDateTimeToString.localDateTimeToString(beginWork)))
        .andExpect(jsonPath("$.endWork").value(localDateTimeToString.localDateTimeToString(endWork)))
        .andExpect(jsonPath("$.beginBreak").value(localDateTimeToString.localDateTimeToString(beginBreak)))
        .andExpect(jsonPath("$.endBreak").value(localDateTimeToString.localDateTimeToString(endBreak)))
        .andExpect(jsonPath("$.requestComment").value(requestComment))
        .andExpect(jsonPath("$.requestDate").value(localDateTimeToString.localDateTimeToString(requestDate)))
        .andExpect(jsonPath("$.requestStatus").value(requestStatus))
        .andExpect(jsonPath("$.approverId").value(adminAccountId))
        .andExpect(jsonPath("$.approverName").value(adminAccountName))
        .andExpect(jsonPath("$.approverComment").value(approverComment))
        .andExpect(jsonPath("$.approvalTime").value(Objects.isNull(approvalTime) ? "" : localDateTimeToString.localDateTimeToString(approvalTime)));
    }

    @Test
    void shiftChangeRequestSuccess() throws Exception
    {
        String generalBeginWork = "2026/09/02T09:00:00";
        String generalBeginBreak = "2026/09/02T12:00:00";
        String generalEndBreak = "2026/09/02T13:00:00";
        String generalEndWork = "2026/09/02T18:00:00";
        String generalRequestComment = "";
        String generalRequestDate = "2025/08/02T00:00:11";
        int generalId = 1;
        String json = String.format
        (
            """
                {
                    "beginWork": "%s",
                    "beginBreak": "%s",
                    "endBreak": "%s",
                    "endWork": "%s",
                    "requestComment": "%s",
                    "requestDate": "%s",
                    "shiftId": "%s"
                }
            """,
        generalBeginWork, generalBeginBreak, generalEndBreak, generalEndWork, generalRequestComment, generalRequestDate, generalId
        );

        Account generalAccount = new Account();
        Long generalAccountId = 1L;
        String generalAccountName = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setName(generalAccountName);

        when(accountService.findCurrentAccount()).thenReturn(generalAccount);
        when(requestService.createShiftChangeRequest(any(Account.class), any(ShiftChangeInput.class))).thenReturn(1);
        mockMvc.perform
        (
            post("/api/send/changetime")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .with(csrf())
            .with(user(generalAccountName))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(1));
    }

    @Test
    void attendListSuccess() throws Exception
    {
        Long generalAccountId = 1L;
        String generalAccountName = "testuser";
        String time = "00:00:00";
        String workTime = "08:00:00";
        String breakTime = "01:00:00";
        Long generalAttendId = 1L;
        LocalDateTime generalAttendBeginWork = LocalDateTime.parse("2025/06/21/08/30/00",DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        LocalDateTime generalAttendEndWork = LocalDateTime.parse("2025/06/21/17/30/00",DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        LocalDateTime generalAttendBeginBreak = LocalDateTime.parse("2025/06/21/11/30/00",DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        LocalDateTime generalAttendEndBreak = LocalDateTime.parse("2025/06/21/12/30/30",DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        Time generalAttendLateness = Time.valueOf(time);
        Time generalAttendLeaveEarly = Time.valueOf(time);
        Time generalAttendOuting = Time.valueOf(time);
        Time generalAttendWorkTime = Time.valueOf(workTime);
        Time generalAttendBreakTime = Time.valueOf(breakTime);
        Time generalAttendOverWork = Time.valueOf(time);
        Time generalAttendHolidayWork = Time.valueOf(time);
        Time generalAttendLateNightWork = Time.valueOf(time);
        Time generalVacationTime = Time.valueOf(time);
        Time generalAbsenceTime = Time.valueOf(time);

        Account generalAccount = new Account();
        generalAccount.setId(generalAccountId);
        generalAccount.setName(generalAccountName);

        Attend generalAttend = new Attend
        (
            generalAttendId, generalAccount, generalAttendBeginWork,
            generalAttendEndWork, generalAttendBeginBreak, generalAttendEndBreak,
            generalAttendLateness, generalAttendLeaveEarly, generalAttendOuting, generalAttendWorkTime,
            generalAttendBreakTime, generalAttendOverWork, generalAttendHolidayWork, generalAttendLateNightWork,
            generalVacationTime, generalAbsenceTime
        );

        AttendListResponse generalAttendListResponse = new AttendListResponse
        (
            generalAttendId,
            generalAttendBeginWork.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + generalAttendBeginWork.format(DateTimeFormatter.ofPattern("HH:mm:ss")),
            generalAttendEndWork.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + generalAttendEndWork.format(DateTimeFormatter.ofPattern("HH:mm:ss")),
            generalAttendBeginBreak.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + generalAttendBeginBreak.format(DateTimeFormatter.ofPattern("HH:mm:ss")),
            generalAttendEndBreak.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + generalAttendEndBreak.format(DateTimeFormatter.ofPattern("HH:mm:ss")),
            generalAttendWorkTime,
            generalAttendBreakTime,
            generalAttendLateness,
            generalAttendLeaveEarly,
            generalAttendOuting,
            generalAttendOverWork,
            generalAttendHolidayWork,
            generalAttendLateNightWork,
            generalVacationTime,
            generalAbsenceTime
        );

        List<Attend> generalAttends = new ArrayList<Attend>();
        generalAttends.add(generalAttend);

        List<AttendListResponse> generalAttendListResponses = new ArrayList<AttendListResponse>();
        generalAttendListResponses.add(generalAttendListResponse);

        when(accountService.findCurrentAccount()).thenReturn(generalAccount);
        when(attendService.findAttendListFor(any(Account.class), any(YearMonthInput.class))).thenReturn(generalAttendListResponses);
        mockMvc.perform
        (
            get("/api/reach/attendlist")
            .param("year", "2025")
            .param("month", "6")
            .with(csrf())
            .with(user(generalAccountName))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(1))
        .andExpect(jsonPath("$.attendList[0].id").value(generalAttendListResponse.getId()))
        .andExpect(jsonPath("$.attendList[0].beginWork").value(generalAttendListResponse.getBeginWork()))
        .andExpect(jsonPath("$.attendList[0].endWork").value(generalAttendListResponse.getEndWork()))
        .andExpect(jsonPath("$.attendList[0].beginBreak").value(generalAttendListResponse.getBeginBreak()))
        .andExpect(jsonPath("$.attendList[0].endBreak").value(generalAttendListResponse.getEndBreak()))
        .andExpect(jsonPath("$.attendList[0].workTime").value(String.valueOf(generalAttendListResponse.getWorkTime())))
        .andExpect(jsonPath("$.attendList[0].breakTime").value(String.valueOf(generalAttendListResponse.getBreakTime())))
        .andExpect(jsonPath("$.attendList[0].lateness").value(String.valueOf(generalAttendListResponse.getLateness())))
        .andExpect(jsonPath("$.attendList[0].leaveEarly").value(String.valueOf(generalAttendListResponse.getLeaveEarly())))
        .andExpect(jsonPath("$.attendList[0].outing").value(String.valueOf(generalAttendListResponse.getOuting())))
        .andExpect(jsonPath("$.attendList[0].overWork").value(String.valueOf(generalAttendListResponse.getOverWork())))
        .andExpect(jsonPath("$.attendList[0].holidayWork").value(String.valueOf(generalAttendListResponse.getHolidayWork())))
        .andExpect(jsonPath("$.attendList[0].lateNightWork").value(String.valueOf(generalAttendListResponse.getLateNightWork())));
    }

    @Test
    void vacationRequestDetailSuccess() throws Exception
    {
        LocalDateTimeToString localDateTimeToString = new LocalDateTimeToString();
        Account generalAccount = new Account();
        String generalAccountUsername = "testuser";
        Long generalAccountId = 1L;
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        Account adminAccount = new Account();
        String adminAccountName = "かまどたかしろう";
        Long adminAccountId = 2L;
        adminAccount.setId(adminAccountId);
        adminAccount.setName(adminAccountName);

        Shift shift = new Shift();
        Long shiftId = 1L;
        shift.setShiftId(shiftId);

        VacationType vacationType = new VacationType();
        Long vacationTypeId = 1L;
        vacationType.setVacationTypeId(vacationTypeId);

        VacationRequest vacationRequest = new VacationRequest();
        Long vacationRequestId = 1L;
        LocalDateTime beginVacation = LocalDateTime.parse("2025/08/08/09/30/00",DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        LocalDateTime endVacation = LocalDateTime.parse("2025/08/08/12/30/00",DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        String requestComment = "";
        LocalDateTime requestDate = LocalDateTime.parse("2025/07/07/00/00/00",DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        int requestStatus = 1;
        LocalDateTime approvalTime = null;
        String approverComment = null;
        vacationRequest.setVacationId(vacationRequestId);
        vacationRequest.setAccountId(generalAccount);
        vacationRequest.setVacationTypeId(vacationType);
        vacationRequest.setBeginVacation(beginVacation);
        vacationRequest.setEndVacation(endVacation);
        vacationRequest.setRequestComment(requestComment);
        vacationRequest.setRequestDate(requestDate);
        vacationRequest.setRequestStatus(requestStatus);
        vacationRequest.setApprover(adminAccount);
        vacationRequest.setApproverComment(approverComment);
        vacationRequest.setApprovalTime(approvalTime);
        vacationRequest.setShiftId(shift);

        RequestDetailVacationResponse requestDetailVacationResponse = new RequestDetailVacationResponse
        (
            1,
            vacationRequest.getShiftId().getShiftId().intValue(),
            vacationRequest.getVacationTypeId().getVacationTypeId().intValue(),
            vacationRequest.getBeginVacation().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + vacationRequest.getBeginVacation().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
            vacationRequest.getEndVacation().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + vacationRequest.getEndVacation().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
            vacationRequest.getRequestComment(),
            vacationRequest.getRequestDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + vacationRequest.getRequestDate().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
            vacationRequest.getRequestStatus(),
            vacationRequest.getApprover().getId().intValue(),
            vacationRequest.getApprover().getName(),
            vacationRequest.getApproverComment(),
            Objects.isNull(vacationRequest.getApprovalTime()) ? "" : vacationRequest.getApprovalTime().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + vacationRequest.getApprovalTime().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
        );

        when(accountService.findCurrentAccount()).thenReturn(generalAccount);
        when(requestService.getVacationDetail(any(Account.class), anyLong())).thenReturn(requestDetailVacationResponse);
        mockMvc.perform
        (
            get("/api/reach/requestdetail/vacation")
            .param("requestId","1")
            .with(csrf())
            .with(user(generalAccountUsername))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(1))
        .andExpect(jsonPath("$.shiftId").value(shiftId))
        .andExpect(jsonPath("$.vacationType").value(vacationTypeId))
        .andExpect(jsonPath("$.beginVacation").value(localDateTimeToString.localDateTimeToString(beginVacation)))
        .andExpect(jsonPath("$.endVacation").value(localDateTimeToString.localDateTimeToString(endVacation)))
        .andExpect(jsonPath("$.requestComment").value(requestComment))
        .andExpect(jsonPath("$.requestDate").value(localDateTimeToString.localDateTimeToString(requestDate)))
        .andExpect(jsonPath("$.requestStatus").value(requestStatus))
        .andExpect(jsonPath("$.approverId").value(adminAccountId))
        .andExpect(jsonPath("$.approverName").value(adminAccountName))
        .andExpect(jsonPath("$.approverComment").value(approverComment))
        .andExpect(jsonPath("$.approvalTime").value(Objects.isNull(approvalTime) ? "" : localDateTimeToString.localDateTimeToString(approvalTime)));
    }

    @Test
    void overTimeRequestDetailSuccess() throws Exception
    {
        LocalDateTimeToString localDateTimeToString = new LocalDateTimeToString();
        Account generalAccount = new Account();
        String generalAccountUsername = "testuser";
        Long generalAccountId = 1L;
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        Account adminAccount = new Account();
        String adminAccountName = "かまどたかしろう";
        Long adminAccountId = 2L;
        adminAccount.setId(adminAccountId);
        adminAccount.setName(adminAccountName);

        Shift shift = new Shift();
        Long shiftId = 1L;
        shift.setShiftId(shiftId);

        OverTimeRequest overTimeRequest = new OverTimeRequest();
        Long overTimeRequestId = 1L;
        LocalDateTime beginOverWork = LocalDateTime.parse("2025/08/08/09/30/00",DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        LocalDateTime endOverWork = LocalDateTime.parse("2025/08/08/12/30/00",DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        String requestComment = "";
        LocalDateTime requestDate = LocalDateTime.parse("2025/07/07/00/00/00",DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        int requestStatus = 1;
        LocalDateTime approvalTime = null;
        String approverComment = null;
        overTimeRequest.setOverTimeId(overTimeRequestId);
        overTimeRequest.setAccountId(generalAccount);
        overTimeRequest.setBeginWork(beginOverWork);
        overTimeRequest.setEndWork(endOverWork);
        overTimeRequest.setRequestComment(requestComment);
        overTimeRequest.setRequestDate(requestDate);
        overTimeRequest.setRequestStatus(requestStatus);
        overTimeRequest.setApprover(adminAccount);
        overTimeRequest.setApproverComment(approverComment);
        overTimeRequest.setApprovalTime(approvalTime);
        overTimeRequest.setShiftId(shift);

        RequestDetailOverTimeResponse requestDetailOverTimeResponse = new RequestDetailOverTimeResponse
        (
            1,
            overTimeRequest.getShiftId().getShiftId().intValue(),
            overTimeRequest.getBeginWork().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + overTimeRequest.getBeginWork().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
            overTimeRequest.getEndWork().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + overTimeRequest.getEndWork().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
            overTimeRequest.getRequestComment(),
            overTimeRequest.getRequestDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + overTimeRequest.getRequestDate().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
            overTimeRequest.getRequestStatus(),
            Objects.isNull(overTimeRequest.getApprover()) ? null : overTimeRequest.getApprover().getId().intValue(),
            Objects.isNull(overTimeRequest.getApprover()) ? "" : overTimeRequest.getApprover().getName(),
            overTimeRequest.getApproverComment(),
            Objects.isNull(overTimeRequest.getApprovalTime()) ? "" : overTimeRequest.getApprovalTime().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + overTimeRequest.getApprovalTime().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
        );

        when(accountService.findCurrentAccount()).thenReturn(generalAccount);
        when(requestService.getOverTimeDetail(any(Account.class),anyLong())).thenReturn(requestDetailOverTimeResponse);
        mockMvc.perform
        (
            get("/api/reach/requestdetail/overtime")
            .param("requestId","1")
            .with(csrf())
            .with(user(generalAccountUsername))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(1))
        .andExpect(jsonPath("$.shiftId").value(shiftId))
        .andExpect(jsonPath("$.beginOverWork").value(localDateTimeToString.localDateTimeToString(beginOverWork)))
        .andExpect(jsonPath("$.endOverWork").value(localDateTimeToString.localDateTimeToString(endOverWork)))
        .andExpect(jsonPath("$.requestComment").value(requestComment))
        .andExpect(jsonPath("$.requestDate").value(localDateTimeToString.localDateTimeToString(requestDate)))
        .andExpect(jsonPath("$.requestStatus").value(requestStatus))
        .andExpect(jsonPath("$.approverId").value(adminAccountId))
        .andExpect(jsonPath("$.approverName").value(adminAccountName))
        .andExpect(jsonPath("$.approverComment").value(approverComment))
        .andExpect(jsonPath("$.approvalTime").value(Objects.isNull(approvalTime) ? "" : localDateTimeToString.localDateTimeToString(approvalTime)));
    }

    @Test
    void otherTimeRequestDetailSuccess() throws Exception
    {
        LocalDateTimeToString localDateTimeToString = new LocalDateTimeToString();
        Account generalAccount = new Account();
        String generalAccountUsername = "testuser";
        Long generalAccountId = 1L;
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        Account adminAccount = new Account();
        String adminAccountName = "かまどたかしろう";
        Long adminAccountId = 2L;
        adminAccount.setId(adminAccountId);
        adminAccount.setName(adminAccountName);

        Shift shift = new Shift();
        Long shiftId = 1L;
        shift.setShiftId(shiftId);

        AttendanceExceptionType attendanceExceptionType = new AttendanceExceptionType();
        Long attendanceExceptionTypeId = 12L;
        attendanceExceptionType.setAttendanceExceptionTypeId(attendanceExceptionTypeId);

        AttendanceExceptionRequest attendanceExceptionRequest = new AttendanceExceptionRequest();
        Long attendanceExceptionRequestId = 1L;
        LocalDateTime beginTime = LocalDateTime.parse("2025/08/08/09/30/00",DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        LocalDateTime endTime = LocalDateTime.parse("2025/08/08/12/30/00",DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        String requestComment = "";
        LocalDateTime requestDate = LocalDateTime.parse("2025/07/07/00/00/00",DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        int requestStatus = 1;
        LocalDateTime approvalTime = null;
        String approverComment = null;
        attendanceExceptionRequest.setAttendanceExceptionId(attendanceExceptionRequestId);
        attendanceExceptionRequest.setAccountId(generalAccount);
        attendanceExceptionRequest.setAttendanceExceptionTypeId(attendanceExceptionType);
        attendanceExceptionRequest.setBeginTime(beginTime);
        attendanceExceptionRequest.setEndTime(endTime);
        attendanceExceptionRequest.setRequestComment(requestComment);
        attendanceExceptionRequest.setRequestDate(requestDate);
        attendanceExceptionRequest.setRequestStatus(requestStatus);
        attendanceExceptionRequest.setApprover(adminAccount);
        attendanceExceptionRequest.setApproverComment(approverComment);
        attendanceExceptionRequest.setApprovalTime(approvalTime);
        attendanceExceptionRequest.setShiftId(shift);

        RequestDetailOtherTimeResponse requestDetailOtherTimeResponse = new RequestDetailOtherTimeResponse
        (
            1,
            attendanceExceptionRequest.getShiftId().getShiftId().intValue(),
            attendanceExceptionRequest.getAttendanceExceptionTypeId().getAttendanceExceptionTypeId().intValue(),
            attendanceExceptionRequest.getBeginTime().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + attendanceExceptionRequest.getBeginTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
            attendanceExceptionRequest.getEndTime().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + attendanceExceptionRequest.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
            attendanceExceptionRequest.getRequestComment(),
            attendanceExceptionRequest.getRequestDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + attendanceExceptionRequest.getRequestDate().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
            attendanceExceptionRequest.getRequestStatus(),
            Objects.isNull(attendanceExceptionRequest.getApprover()) ? null : attendanceExceptionRequest.getApprover().getId().intValue(),
            Objects.isNull(attendanceExceptionRequest.getApprover()) ? "" : attendanceExceptionRequest.getApprover().getName(),
            attendanceExceptionRequest.getApproverComment(),
            Objects.isNull(attendanceExceptionRequest.getApprovalTime()) ? "" : attendanceExceptionRequest.getApprovalTime().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + attendanceExceptionRequest.getApprovalTime().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
        );

        when(accountService.findCurrentAccount()).thenReturn(generalAccount);
        when(requestService.getOtherTimeDetail(any(Account.class),anyLong())).thenReturn(requestDetailOtherTimeResponse);
        mockMvc.perform
        (
            get("/api/reach/requestdetail/othertime")
            .param("requestId","1")
            .with(csrf())
            .with(user(generalAccountUsername))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(1))
        .andExpect(jsonPath("$.shiftId").value(shiftId))
        .andExpect(jsonPath("$.typeId").value(attendanceExceptionTypeId))
        .andExpect(jsonPath("$.beginOtherTime").value(localDateTimeToString.localDateTimeToString(beginTime)))
        .andExpect(jsonPath("$.endOtherTime").value(localDateTimeToString.localDateTimeToString(endTime)))
        .andExpect(jsonPath("$.requestComment").value(requestComment))
        .andExpect(jsonPath("$.requestDate").value(localDateTimeToString.localDateTimeToString(requestDate)))
        .andExpect(jsonPath("$.requestStatus").value(requestStatus))
        .andExpect(jsonPath("$.approverId").value(adminAccountId))
        .andExpect(jsonPath("$.approverName").value(adminAccountName))
        .andExpect(jsonPath("$.approverComment").value(approverComment))
        .andExpect(jsonPath("$.approvalTime").value(Objects.isNull(approvalTime) ? "" : localDateTimeToString.localDateTimeToString(approvalTime)));

    }

    @Test
    void stampRequestSuccess() throws Exception
    {
        String generalBeginWork = "2025/09/02T09:00:00";
        String generalBeginBreak = "2025/09/02T12:00:00";
        String generalEndBreak = "2025/09/02T13:00:00";
        String generalEndWork = "2025/09/02T18:00:00";
        String generalRequestComment = "";
        String generalRequestDate = "2025/08/02T00:00:11";
        int generalId = 1;
        String json = String.format
        (
            """
                {
                    "beginWork": "%s",
                    "beginBreak": "%s",
                    "endBreak": "%s",
                    "endWork": "%s",
                    "requestComment": "%s",
                    "requestDate": "%s",
                    "shiftId": "%s"
                }
            """,
        generalBeginWork, generalBeginBreak, generalEndBreak, generalEndWork, generalRequestComment, generalRequestDate, generalId
        );

        Account generalAccount = new Account();
        Long generalAccountId = 1L;
        String generalAccountName = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setName(generalAccountName);

        when(accountService.findCurrentAccount()).thenReturn(generalAccount);
        when(requestService.createStampRequest(any(Account.class), any(StampInput.class))).thenReturn(1);
        mockMvc.perform
        (
            post("/api/send/stamp")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .with(csrf())
            .with(user(generalAccountName))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(1));
    }

    @Test
    void monthlyRequestDetailSuccess() throws Exception
    {
        StringToLocalDateTime stringToLocalDateTime = new StringToLocalDateTime();
        LocalDateTimeToString localDateTimeToString = new LocalDateTimeToString();
        Account generalAccount = new Account();
        Long generalAccountId = 1L;
        String generalAccountUsername = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        Account adminAccount = new Account();
        Long adminAccountId = 2L;
        String adminAccountName = "闇落ちしたたかし";
        adminAccount.setId(adminAccountId);
        adminAccount.setName(adminAccountName);

        MonthlyRequest generalMonthlyRequest = new MonthlyRequest();
        String generalWorkTime = "160:00:00";
        String generalOverTime = "01:00:00";
        String generalEarlyTime = "02:00:00";
        String generalLeavingTime = "02:00:00";
        String generalOutingTime = "02:00:00";
        String generalAbsenceTime = "02:00:00";
        String generalPaydHolidayTime = "02:00:00";
        String generalSpecialTime = "02:00:00";
        String generalHolidayWorkTime = "02:00:00";
        String generalLateNightWorkTime = "02:00:00";
        int generalYear = 2025;
        int generalMonth = 07;
        String generalRequestComment = "";
        String generalRequestDate = "2025/08/01/08/00/21";
        int generalRequestStatus = 1;
        String genearlApproverComment = "";
        String generalApprovalTime = null;
        generalMonthlyRequest.setWorkTime(generalWorkTime);
        generalMonthlyRequest.setOverTime(generalOverTime);
        generalMonthlyRequest.setEarlyTime(generalEarlyTime);
        generalMonthlyRequest.setLeavingTime(generalLeavingTime);
        generalMonthlyRequest.setOutingTime(generalOutingTime);
        generalMonthlyRequest.setAbsenceTime(generalAbsenceTime);
        generalMonthlyRequest.setPaydHolidayTime(generalPaydHolidayTime);
        generalMonthlyRequest.setSpecialTime(generalSpecialTime);
        generalMonthlyRequest.setHolidayWorkTime(generalHolidayWorkTime);
        generalMonthlyRequest.setLateNightWorkTime(generalLateNightWorkTime);
        generalMonthlyRequest.setYear(generalYear);
        generalMonthlyRequest.setMonth(generalMonth);
        generalMonthlyRequest.setRequestComment(generalRequestComment);
        generalMonthlyRequest.setRequestDate(LocalDateTime.parse(generalRequestDate, DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        generalMonthlyRequest.setRequestStatus(generalRequestStatus);
        generalMonthlyRequest.setApprover(adminAccount);
        generalMonthlyRequest.setApprovalDate(Objects.isNull(generalApprovalTime) ? null : stringToLocalDateTime.stringToLocalDateTime(generalApprovalTime));
        generalMonthlyRequest.setApproverComment(genearlApproverComment);

        RequestDetailMonthlyResponse requestDetailMonthlyResponse = new RequestDetailMonthlyResponse
        (
            1,
            generalMonthlyRequest.getWorkTime(),
            generalMonthlyRequest.getOverTime(),
            generalMonthlyRequest.getEarlyTime(),
            generalMonthlyRequest.getLeavingTime(),
            generalMonthlyRequest.getOutingTime(),
            generalMonthlyRequest.getAbsenceTime(),
            generalMonthlyRequest.getPaydHolidayTime(),
            generalMonthlyRequest.getSpecialTime(),
            generalMonthlyRequest.getHolidayWorkTime(),
            generalMonthlyRequest.getLateNightWorkTime(),
            generalMonthlyRequest.getYear(),
            generalMonthlyRequest.getMonth(),
            generalMonthlyRequest.getRequestComment(),
            generalMonthlyRequest.getRequestDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + generalMonthlyRequest.getRequestDate().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
            generalMonthlyRequest.getRequestStatus(),
            Objects.isNull(generalMonthlyRequest.getApprover()) ? null : generalMonthlyRequest.getApprover().getId().intValue(),
            Objects.isNull(generalMonthlyRequest.getApprover()) ? "" : generalMonthlyRequest.getApprover().getName(),
            generalMonthlyRequest.getApproverComment(),
            Objects.isNull(generalMonthlyRequest.getApprovalDate()) ? "" : generalMonthlyRequest.getApprovalDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + generalMonthlyRequest.getApprovalDate().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
        );

        when(accountService.findCurrentAccount()).thenReturn(generalAccount);
        when(requestService.getMonthlyDetail(any(Account.class), anyLong())).thenReturn(requestDetailMonthlyResponse);
        mockMvc.perform
        (
            get("/api/reach/requestdetail/monthly")
            .param("requestId","1")
            .with(csrf())
            .with(user(generalAccountUsername))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(1))
        .andExpect(jsonPath("$.workTime").value(generalWorkTime))
        .andExpect(jsonPath("$.overTime").value(generalOverTime))
        .andExpect(jsonPath("$.earlyTime").value(generalEarlyTime))
        .andExpect(jsonPath("$.leavingTime").value(generalLeavingTime))
        .andExpect(jsonPath("$.outingTime").value(generalOutingTime))
        .andExpect(jsonPath("$.absenceTime").value(generalAbsenceTime))
        .andExpect(jsonPath("$.paydHolidayTime").value(generalPaydHolidayTime))
        .andExpect(jsonPath("$.specialTime").value(generalSpecialTime))
        .andExpect(jsonPath("$.holidayWorkTime").value(generalHolidayWorkTime))
        .andExpect(jsonPath("$.lateNightWorkTime").value(generalLateNightWorkTime))
        .andExpect(jsonPath("$.year").value(generalYear))
        .andExpect(jsonPath("$.month").value(generalMonth))
        .andExpect(jsonPath("$.requestComment").value(generalRequestComment))
        .andExpect(jsonPath("$.requestDate").value(localDateTimeToString.localDateTimeToString(LocalDateTime.parse(generalRequestDate, DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")))))
        .andExpect(jsonPath("$.requestStatus").value(generalRequestStatus))
        .andExpect(jsonPath("$.approverId").value(adminAccountId))
        .andExpect(jsonPath("$.approverName").value(adminAccountName))
        .andExpect(jsonPath("$.approverComment").value(genearlApproverComment))
        .andExpect(jsonPath("$.approvalTime").value(Objects.isNull(generalApprovalTime) ? "" : localDateTimeToString.localDateTimeToString(LocalDateTime.parse(generalRequestDate, DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")))));
    }

    @Test
    void requestListResponseSuccess() throws Exception
    {
        Account generalAccount = new Account();
        Long generalAccountId = 1L;
        String generalAccountUsername = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        List<RequestListResponse> requestListResponses = new ArrayList<RequestListResponse>();
        RequestListResponse requestListResponse = new RequestListResponse();
        requestListResponses.add(requestListResponse);

        when(accountService.findCurrentAccount()).thenReturn(generalAccount);
        when(requestService.getRequestList(generalAccount)).thenReturn(requestListResponses);
        mockMvc.perform
        (
            get("/api/reach/requestlist")
            .with(csrf())
            .with(user(generalAccountUsername))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(1));
    }

    @Test 
    void monthWorkInfoResponseSuccess() throws Exception
    {
        StringToLocalDateTime stringToLocalDateTime = new StringToLocalDateTime();
        Account generalAccount = new Account();
        String generalAccountUsername = "testuser";
        Long generalAccountId = 3L;
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        Time generalTime = Time.valueOf("01:00:00");
        List<Attend> attends = new ArrayList<Attend>();
        Attend attend = new Attend();
        attend.setWorkTime(generalTime);
        attend.setLateness(generalTime);
        attend.setLeaveEarly(generalTime);
        attend.setOuting(generalTime);
        attend.setOverWork(generalTime);
        attend.setAbsenceTime(generalTime);
        attend.setVacationTime(generalTime);
        attend.setLateNightWork(generalTime);
        attend.setHolidayWork(generalTime);

        Attend generalAttend = new Attend();
        generalAttend.setWorkTime(generalTime);
        generalAttend.setLateness(generalTime);
        generalAttend.setLeaveEarly(generalTime);
        generalAttend.setOuting(generalTime);
        generalAttend.setOverWork(generalTime);
        generalAttend.setAbsenceTime(generalTime);
        generalAttend.setVacationTime(generalTime);
        generalAttend.setLateNightWork(generalTime);
        generalAttend.setHolidayWork(generalTime);

        attends.add(attend);
        attends.add(generalAttend);

        List<Vacation> vacations = new ArrayList<Vacation>();
        Vacation vacation = new Vacation();
        vacation.setBeginVacation(stringToLocalDateTime.stringToLocalDateTime("2025/09/08T01:00:00"));
        vacation.setEndVacation(stringToLocalDateTime.stringToLocalDateTime("2025/09/08T02:00:00"));
        vacations.add(vacation);

        MonthWorkInfoResponse monthWorkInfoResponse = new MonthWorkInfoResponse();
        monthWorkInfoResponse.setStatus(1);;

        when(accountService.findCurrentAccount()).thenReturn(generalAccount);
        when(attendService.monthWorkInfoResponse(any(Account.class), any(YearMonthInput.class))).thenReturn(monthWorkInfoResponse);
        mockMvc.perform
        (
            get("/api/reach/monthworkinfo")
            .param("year","2025")
            .param("month", "9")
            .with(csrf())
            .with(user(generalAccountUsername))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(1));
    }

    @Test
    void styleResponseSuccess() throws Exception
    {
        Account generalAccount = new Account();
        Long generalAccountId = 1L;
        String generalAccountUsername = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        Style generalStyle = new Style();
        Long generalStyleId = 2L;
        StylePlace generalStylePlace = new StylePlace();
        String generalStylePlaceName = "出勤";
        generalStylePlace.setName(generalStylePlaceName);
        generalStyle.setStyleId(generalStyleId);
        generalStyle.setStylePlaceId(generalStylePlace);

        StyleResponse styleResponse = new StyleResponse();
        styleResponse.setStatus(1);

        when(accountService.findCurrentAccount()).thenReturn(generalAccount);
        when(styleService.returnStyle(any(Account.class))).thenReturn(styleResponse);
        mockMvc.perform
        (
            get("/api/reach/style")
            .with(csrf())
            .with(user(generalAccountUsername))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(1));
    }

    @Test
    void approverResponseSuccess() throws Exception
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

        ApproverResponse approverResponse = new ApproverResponse();
        approverResponse.setStatus(1);

        when(accountService.findCurrentAccount()).thenReturn(generalAccount);
        when(accountApproverService.getApproverResponse(any(Account.class))).thenReturn(approverResponse);
        mockMvc.perform
        (
            get("/api/reach/approver")
            .with(csrf())
            .with(user(generalAccountUsername))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(1));
    }

    @Test
    void allVacationTypeListSuccess() throws Exception
    {
        Account generalAccount = new Account();
        String generalAccountUsername = "testuser";
        generalAccount.setUsername(generalAccountUsername);

        List<VacationType> vacationTypes = new ArrayList<VacationType>();
        VacationType vacationType = new VacationType();
        Long vacationTypeId = 3L;
        String vacationTypeName = "代休";
        vacationType.setVacationTypeId(vacationTypeId);
        vacationType.setVacationName(vacationTypeName);
        vacationTypes.add(vacationType);

        List<VacationTypeListResponse> vacationTypeListResponses = new ArrayList<VacationTypeListResponse>();
        VacationTypeListResponse vacationTypeListResponse = new VacationTypeListResponse();
        vacationTypeListResponses.add(vacationTypeListResponse);

        when(accountService.findCurrentAccount()).thenReturn(generalAccount);
        when(vacationTypeService.returnAllVacationTypeListResponses()).thenReturn(vacationTypeListResponses);
        mockMvc.perform
        (
            get("/api/reach/allvacationtypelist")
            .with(csrf())
            .with(user(generalAccountUsername))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(1));
    }

    @Test
    void allOtherTypeListSuccess() throws Exception
    {
        Account generalAccount = new Account();
        String generalAccountUsername = "testuser";
        generalAccount.setUsername(generalAccountUsername);

        List<AttendanceExceptionType> attendanceExceptionTypes = new ArrayList<AttendanceExceptionType>();
        AttendanceExceptionType attendanceExceptionType = new AttendanceExceptionType();
        Long attendanceExceptionTypeId = 2L;
        String attendanceExceptionTypeName = "遅刻";
        attendanceExceptionType.setAttendanceExceptionTypeId(attendanceExceptionTypeId);
        attendanceExceptionType.setAttednaceExceptionTypeName(attendanceExceptionTypeName);
        attendanceExceptionTypes.add(attendanceExceptionType);

        List<OtherTypeListResponse> otherTypeListResponses = new ArrayList<OtherTypeListResponse>();

        when(accountService.findCurrentAccount()).thenReturn(generalAccount);
        when(attendanceExceptionTypeService.returnOtherTypeListResponses()).thenReturn(otherTypeListResponses);
        mockMvc.perform
        (
            get("/api/reach/allothertypelist")
            .with(csrf())
            .with(user(generalAccountUsername))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(1));
    }

    @Test
    void vacationRequestSuccess() throws Exception
    {
        String generalBeginOverWork = "2026/11/22T10:00:00";
        String generalEndOverWork = "2026/11/22T11:00:00";
        String generalRequestComment = "";
        String generalRequestDate = "2026/08/02T00:00:11";
        int generalId = 1;
        int vacationType = 1;
        String json = String.format
        (
            """
                {
                    "beginVacation": "%s",
                    "endVacation": "%s",
                    "vacationType": "%s",
                    "requestComment": "%s",
                    "requestDate": "%s",
                    "shiftId": "%s"
                }
            """,
            generalBeginOverWork, generalEndOverWork, vacationType, generalRequestComment, generalRequestDate, generalId
        );

        Account generalAccount = new Account();
        Long generalAccountId = 1L;
        String generalAccountUsername = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        when(accountService.findCurrentAccount()).thenReturn(generalAccount);
        when(requestService.createVacationRequest(any(Account.class), any(VacationInput.class))).thenReturn(1);
        mockMvc.perform
        (
            post("/api/send/vacation")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .with(csrf())
            .with(user(generalAccountUsername))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(1));
    }

    @Test
    void paydHolidayHistorySuccess() throws Exception
    {
        Account generalAccount = new Account();
        String generalAccountUsername = "testuser";
        generalAccount.setUsername(generalAccountUsername);

        List<PaydHolidayHistoryListResponse> paydHolidayHistoryListResponses = new ArrayList<PaydHolidayHistoryListResponse>();

        when(accountService.findCurrentAccount()).thenReturn(generalAccount);
        when(paydHolidayService.returnPaydHolidayHistoryListResponses(any(Account.class))).thenReturn(paydHolidayHistoryListResponses);
        mockMvc.perform
        (
            get("/api/reach/paydholidayhistory")
            .with(csrf())
            .with(user(generalAccountUsername))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(1));
    }

    @Test
    void userAttendListSuccess() throws Exception
    {
        Long generalAccountId = 1L;
        String generalAccountName = "testuser";
        String time = "00:00:00";
        String workTime = "08:00:00";
        String breakTime = "01:00:00";
        Long generalAttendId = 1L;
        LocalDateTime generalAttendBeginWork = LocalDateTime.parse("2025/06/21/08/30/00",DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        LocalDateTime generalAttendEndWork = LocalDateTime.parse("2025/06/21/17/30/00",DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        LocalDateTime generalAttendBeginBreak = LocalDateTime.parse("2025/06/21/11/30/00",DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        LocalDateTime generalAttendEndBreak = LocalDateTime.parse("2025/06/21/12/30/30",DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        Time generalAttendLateness = Time.valueOf(time);
        Time generalAttendLeaveEarly = Time.valueOf(time);
        Time generalAttendOuting = Time.valueOf(time);
        Time generalAttendWorkTime = Time.valueOf(workTime);
        Time generalAttendBreakTime = Time.valueOf(breakTime);
        Time generalAttendOverWork = Time.valueOf(time);
        Time generalAttendHolidayWork = Time.valueOf(time);
        Time generalAttendLateNightWork = Time.valueOf(time);
        Time generalVacationTime = Time.valueOf(time);
        Time generalAbsenceTime = Time.valueOf(time);

        Account generalAccount = new Account();
        generalAccount.setId(generalAccountId);
        generalAccount.setName(generalAccountName);

        Account adminAccount = new Account();
        Long adminAccountId = 112L;
        String adminAccountUsername = "hogehoge";
        adminAccount.setId(adminAccountId);
        adminAccount.setUsername(adminAccountUsername);

        AccountApprover accountApprover = new AccountApprover();
        accountApprover.setAccountId(generalAccount);
        accountApprover.setApproverId(adminAccount);

        Attend generalAttend = new Attend
        (
            generalAttendId, generalAccount, generalAttendBeginWork,
            generalAttendEndWork, generalAttendBeginBreak, generalAttendEndBreak,
            generalAttendLateness, generalAttendLeaveEarly, generalAttendOuting, generalAttendWorkTime,
            generalAttendBreakTime, generalAttendOverWork, generalAttendHolidayWork, generalAttendLateNightWork,
            generalVacationTime, generalAbsenceTime
        );

        List<AttendListResponse> attendListResponses = new ArrayList<AttendListResponse>();

        List<Attend> generalAttends = new ArrayList<Attend>();
        generalAttends.add(generalAttend);
        when(accountService.findCurrentAccount()).thenReturn(generalAccount);
        when(accountApproverService.findAccountAndApprover(anyLong(), any(Account.class))).thenReturn(accountApprover);
        when(attendService.getAttendListResponses(any(Account.class), any(UserAttendInput.class))).thenReturn(attendListResponses);
        mockMvc.perform(
            get("/api/reach/user/attendinfo")
            .param("accountId", String.valueOf(generalAccountId))
            .param("year", "2025")
            .param("month", "6")
            .with(csrf())
            .with(user(adminAccountUsername))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(1));
    }

    @Test
    void userShiftListSuccess() throws Exception
    {
        Long generalAccountId = 1L;
        String generalAccountName = "testuser";
        String time = "00:00:00";
        Long generalShiftId = 1L;
        LocalDateTime generalShiftBeginWork = LocalDateTime.parse("2025/06/21/08/30/00",DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        LocalDateTime generalShiftEndWork = LocalDateTime.parse("2025/06/21/17/30/00",DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        LocalDateTime generalShiftBeginBreak = LocalDateTime.parse("2025/06/21/11/30/00",DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        LocalDateTime generalShiftEndBreak = LocalDateTime.parse("2025/06/21/12/30/30",DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        Time generalShiftLateness = Time.valueOf(time);
        Time generalShiftLeaveEarly = Time.valueOf(time);
        Time generalShiftOuting = Time.valueOf(time);
        Time generalShiftOverWork = Time.valueOf(time);
        Account generalAccount = new Account();
        generalAccount.setId(generalAccountId);
        generalAccount.setName(generalAccountName);
        Account adminAccount = new Account();
        Long adminAccountId = 24L;
        String adminAccoutUsername = "hogehoge";
        adminAccount.setId(adminAccountId);
        adminAccount.setUsername(adminAccoutUsername);

        AccountApprover accountApprover = new AccountApprover();
        accountApprover.setAccountId(generalAccount);
        accountApprover.setApproverId(adminAccount);
        Shift generalShift = new Shift
        (
            generalShiftId, generalAccount, generalShiftBeginWork,
            generalShiftEndWork, generalShiftBeginBreak, generalShiftEndBreak,
            generalShiftLateness, generalShiftLeaveEarly, generalShiftOuting, generalShiftOverWork
        );

        List<Shift> generalShifts = new ArrayList<Shift>();
        generalShifts.add(generalShift);

        List<ShiftListResponse> shiftListResponses = new ArrayList<ShiftListResponse>();

        when(accountService.findCurrentAccount()).thenReturn(generalAccount);
        when(accountApproverService.findAccountAndApprover(anyLong(), any(Account.class))).thenReturn(accountApprover);
        when(shiftService.returnShiftListResponses(any(Account.class), any(AccountApprover.class), any(UserShiftInput.class))).thenReturn(shiftListResponses);
        mockMvc.perform(
            get("/api/reach/user/shiftinfo")
            .param("accountId", String.valueOf(adminAccountId))
            .param("year", "2025")
            .param("month", "6")
            .with(csrf())
            .with(user(generalAccountName))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(1));
    }

    @Test
    void userMonthWorkInfoSuccess() throws Exception
    {
        Account adminAccount = new Account();
        Long adminAccountId = 3L;
        String adminAccountUsername = "testuser";
        adminAccount.setId(adminAccountId);
        adminAccount.setUsername(adminAccountUsername);

        Account generalAccount = new Account();
        Long generalAccountId = 4L;
        String generalAccountUsername = "hogehoge";
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        AccountApprover accountApprover = new AccountApprover();
        accountApprover.setAccountId(generalAccount);
        accountApprover.setApproverId(adminAccount);


        MonthWorkInfoResponse monthWorkInfoResponse = new MonthWorkInfoResponse();
        monthWorkInfoResponse.setStatus(1);

        when(accountService.findCurrentAccount()).thenReturn(adminAccount);
        when(accountApproverService.findAccountAndApprover(anyLong(), any(Account.class))).thenReturn(accountApprover);
        when(attendService.getMonthWorkInfoResponse(any(Account.class), any(AccountApprover.class), any(UserMonthWorkInfoInput.class))).thenReturn(monthWorkInfoResponse);
        mockMvc.perform
        (
            get("/api/reach/user/monthworkinfo")
            .param("accountId", "3")
            .param("year", "2025")
            .param("month", "9")
            .with(csrf())
            .with(user(adminAccountUsername))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(1));
    }

    @Test
    void userRequestListSuccess() throws Exception
    {
        Account adminAccount = new Account();
        Long adminAccountId = 5L;
        String adminAccountUsername = "testuser";
        String adminAccountName = "権平権平";
        adminAccount.setId(adminAccountId);
        adminAccount.setUsername(adminAccountUsername);
        adminAccount.setName(adminAccountName);

        Account generalAccount = new Account();
        Long generalAccountId = 7L;
        String generalAccountUsername = "hogehoge";
        String generalAccountName = "キキ";
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);
        generalAccount.setName(generalAccountName);

        Account guestAccount = new Account();
        Long guestAccountId = 99L;
        String guestAccountUsername = "nanasinogonbei";
        String guestAccountName = "ララ";
        guestAccount.setId(guestAccountId);
        guestAccount.setUsername(guestAccountUsername);
        guestAccount.setName(guestAccountName);

        when(accountService.findCurrentAccount()).thenReturn(adminAccount);
        when(requestService.getUserRequestListResponses(any(Account.class))).thenReturn(new ArrayList<UserRequestListResponse>());
        mockMvc.perform
        (
            get("/api/reach/user/requestlist")
            .with(csrf())
            .with(user(adminAccountUsername))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(1));
    }

    @Test
    void legalCheckShiftSuccess() throws Exception
    {
        String generalBeginWork = "2026/09/02T09:00:00";
        String generalBeginBreak = "2026/09/02T12:00:00";
        String generalEndBreak = "2026/09/02T13:00:00";
        String generalEndWork = "2026/09/02T18:00:00";

        Account generalAccount = new Account();
        Long generalAccountId = 1L;
        String generalAccountName = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setName(generalAccountName);

        when(accountService.findCurrentAccount()).thenReturn(generalAccount);
        when(legalCheckService.shiftLegalCheck(any(Account.class), any(LegalCheckShiftInput.class))).thenReturn(1);
        mockMvc.perform
        (
            get("/api/reach/legalcheck/shift")
            .param("beginWork", generalBeginWork)
            .param("endWork", generalEndWork)
            .param("beginBreak", generalBeginBreak)
            .param("endBreak", generalEndBreak)
            .with(csrf())
            .with(user(generalAccountName))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(1));
    }

    @Test
    void legalCheckShiftChangeSuccess() throws Exception
    {
        String generalBeginWork = "2026/09/02T09:00:00";
        String generalBeginBreak = "2026/09/02T12:00:00";
        String generalEndBreak = "2026/09/02T13:00:00";
        String generalEndWork = "2026/09/02T18:00:00";

        Account generalAccount = new Account();
        Long generalAccountId = 1L;
        String generalAccountName = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setName(generalAccountName);
    
        when(accountService.findCurrentAccount()).thenReturn(generalAccount);
        when(legalCheckService.shiftChangeLegalCheck(any(Account.class), any(LegalCheckShiftChangeInput.class))).thenReturn(1);
        mockMvc.perform
        (
            get("/api/reach/legalcheck/shiftchange")
            .param("beginWork", generalBeginWork)
            .param("endWork", generalEndWork)
            .param("beginBreak", generalBeginBreak)
            .param("endBreak", generalEndBreak)
            .with(csrf())
            .with(user(generalAccountName))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(1));
    }

    @Test
    void otherTimeRequestOutingSuccess() throws Exception
    {
        int requestShiftId = 1;
        int requestOtherType = 1;
        String requestBeginOtherTime = "2025/12/23T09:00:00";
        String requestEndOtherTime = "2025/12/23T10:00:00";
        String requestComment = "";
        String requestDate = "2025/09/02T10:00:00";
        String json = String.format
        (
            """
                {
                    "shiftId": "%s",
                    "otherType": "%s",
                    "beginOtherTime": "%s",
                    "endOtherTime": "%s",
                    "requestComment": "%s",
                    "requestDate": "%s"
                }
            """,
            requestShiftId, requestOtherType, requestBeginOtherTime, requestEndOtherTime, requestComment, requestDate
        );

        Account generalAccount = new Account();
        Long generalAccountId = 3L;
        String generalAccountUsername = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        when(accountService.findCurrentAccount()).thenReturn(generalAccount);
        when(requestService.createAttendanceExceptionRequest(any(Account.class), any(OtherTimeInput.class))).thenReturn(1);
        mockMvc.perform
        (
            post("/api/send/othertime")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .with(csrf())
            .with(user(generalAccountUsername))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(1));
    }

    @Test
    void otherTimeRequestLatenessSuccess() throws Exception
    {
        int requestShiftId = 1;
        int requestOtherType = 2;
        String requestBeginOtherTime = "2025/12/23T09:00:00";
        String requestEndOtherTime = "2025/12/23T10:00:00";
        String requestComment = "";
        String requestDate = "2025/09/02T10:00:00";
        String json = String.format
        (
            """
                {
                    "shiftId": "%s",
                    "otherType": "%s",
                    "beginOtherTime": "%s",
                    "endOtherTime": "%s",
                    "requestComment": "%s",
                    "requestDate": "%s"
                }
            """,
            requestShiftId, requestOtherType, requestBeginOtherTime, requestEndOtherTime, requestComment, requestDate
        );

        Account generalAccount = new Account();
        Long generalAccountId = 3L;
        String generalAccountUsername = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        when(accountService.findCurrentAccount()).thenReturn(generalAccount);
        when(requestService.createAttendanceExceptionRequest(any(Account.class), any(OtherTimeInput.class))).thenReturn(1);
        mockMvc.perform
        (
            post("/api/send/othertime")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .with(csrf())
            .with(user(generalAccountUsername))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(1));
    }

    @Test
    void otherTimeRequestLeaveEarlySuccess() throws Exception
    {
        int requestShiftId = 1;
        int requestOtherType = 3;
        String requestBeginOtherTime = "2025/12/23T17:00:00";
        String requestEndOtherTime = "2025/12/23T18:00:00";
        String requestComment = "";
        String requestDate = "2025/09/02T10:00:00";
        String json = String.format
            ("""
                {
                    "shiftId": "%s",
                    "otherType": "%s",
                    "beginOtherTime": "%s",
                    "endOtherTime": "%s",
                    "requestComment": "%s",
                    "requestDate": "%s"
                }
            """,
            requestShiftId, requestOtherType, requestBeginOtherTime, requestEndOtherTime, requestComment, requestDate
        );

        Account generalAccount = new Account();
        Long generalAccountId = 3L;
        String generalAccountUsername = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        when(accountService.findCurrentAccount()).thenReturn(generalAccount);
        when(requestService.createAttendanceExceptionRequest(any(Account.class), any(OtherTimeInput.class))).thenReturn(1);
        mockMvc.perform
        (
            post("/api/send/othertime")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .with(csrf())
            .with(user(generalAccountUsername))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(1));
    }

    @Test
    void overTimeRequstBeforeSuccess() throws Exception
    {
        int requestShiftId = 1;
        String requestBeginOtherTime = "2025/12/23T07:00:00";
        String requestEndOtherTime = "2025/12/23T09:00:00";
        String requestComment = "";
        String requestDate = "2025/09/02T10:00:00";
        String json = String.format
        (
            """
                {
                    "shiftId": "%s",
                    "beginOverTime": "%s",
                    "endOverTime": "%s",
                    "requestComment": "%s",
                    "requestDate": "%s"
                }
            """,
            requestShiftId, requestBeginOtherTime, requestEndOtherTime, requestComment, requestDate
        );

        Account generalAccount = new Account();
        Long generalAccountId = 3L;
        String generalAccountUsername = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        when(accountService.findCurrentAccount()).thenReturn(generalAccount);
        when(requestService.createOverTimeRequest(any(Account.class), any(OverTimeInput.class))).thenReturn(1);
        mockMvc.perform
        (
            post("/api/send/overtime")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .with(csrf())
            .with(user(generalAccountUsername))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(1));
    }

    @Test
    void overTimeRequestAfterSuccess() throws Exception
    {
        int requestShiftId = 1;
        String requestBeginOtherTime = "2025/12/23T18:00:00";
        String requestEndOtherTime = "2025/12/23T20:00:00";
        String requestComment = "";
        String requestDate = "2025/09/02T10:00:00";
        String json = String.format
        (   """
                {
                    "shiftId": "%s",
                    "beginOverTime": "%s",
                    "endOverTime": "%s",
                    "requestComment": "%s",
                    "requestDate": "%s"
                }
            """,
            requestShiftId, requestBeginOtherTime, requestEndOtherTime, requestComment, requestDate
        );

        Account generalAccount = new Account();
        Long generalAccountId = 3L;
        String generalAccountUsername = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        when(accountService.findCurrentAccount()).thenReturn(generalAccount);
        when(requestService.createOverTimeRequest(any(Account.class), any(OverTimeInput.class))).thenReturn(1);
        mockMvc.perform
        (
            post("/api/send/overtime")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .with(csrf())
            .with(user(generalAccountUsername))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(1));
    }

    @Test
    void newsListSuccess() throws Exception
    {
        Account generalAccount = new Account();
        Long generalAccountId = 3L;
        String generalAccountUserName = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUserName);

        List<NewsList> newsLists = new ArrayList<NewsList>();
        NewsList newsList = new NewsList();
        Long newsId = 35L;
        String newsDate = "2025/07/08T22:33:33";
        String newsDetil = "your stampRequest is not approved";
        newsList.setNewsId(newsId);
        newsList.setAccountId(generalAccount);
        newsList.setDate(LocalDateTime.parse(LocalDateTime.parse(newsDate,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        newsList.setNewsDetil(newsDetil);
        newsLists.add(newsList);

        List<NewsListResponse> newsListResponses = new ArrayList<NewsListResponse>();
        NewsListResponse newsListResponse = new NewsListResponse();
        newsListResponse.setDate(newsDate);
        newsListResponse.setMessageDetil(newsDetil);
        newsListResponses.add(newsListResponse);

        when(accountService.findCurrentAccount()).thenReturn(generalAccount);
        when(newsListService.returnNewsList(any(Account.class))).thenReturn(newsListResponses);
        mockMvc.perform
        (
            get("/api/reach/news")
            .with(csrf())
            .with(user(generalAccountUserName))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(1))
        .andExpect(jsonPath("$.newsList[0].date").value(newsDate))
        .andExpect(jsonPath("$.newsList[0].messageDetil").value(newsDetil));
    }

    @Test
    void monthlyRequestSuccess() throws Exception
    {
        int year = 2025;
        int month = 1;
        String requestComment = "";
        String requestDate = "2025/09/02T10:00:00";
        String json = String.format
        (
            """
                {
                    "year": "%s",
                    "month": "%s",
                    "requestComment": "%s",
                    "requestDate": "%s"
                }
            """,
            year, month, requestComment, requestDate
        );

        Account generalAccount = new Account();
        Long generalAccountId = 3L;
        String generalAccountUsername = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        when(accountService.findCurrentAccount()).thenReturn(generalAccount);
        when(requestService.createMonthlyRequest(any(Account.class), any(MonthlyInput.class))).thenReturn(1);
        mockMvc.perform
        (
            post("/api/send/monthly")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .with(csrf())
            .with(user(generalAccountUsername))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(1));
    }

    @Test
    void withdrowShiftSuccess() throws Exception
    {
        int requestId = 1;
        int requestType = 1;
        String json = String.format
        (
            """
                {
                    "requestId": "%s",
                    "requestType": "%s"
                }
            """,
            requestId, requestType
        );

        Account generalAccount = new Account();
        Long generalAccountId = 3L;
        String generalAccountUsername = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        ShiftRequest shiftRequest = new ShiftRequest();
        Long shiftRequestid = 43L;
        shiftRequest.setShiftRequestId(shiftRequestid);
        shiftRequest.setRequestStatus(1);

        when(accountService.findCurrentAccount()).thenReturn(generalAccount);
        when(requestService.withdrow(any(Account.class), any(WithDrowInput.class))).thenReturn(1);
        mockMvc.perform
        (
            post("/api/send/withdrow")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .with(csrf())
            .with(user(generalAccountUsername))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(1));
    }

    @Test
    void withdrowShiftChangeSuccess() throws Exception
    {
        int requestId = 5;
        int requestType = 2;
        String json = String.format
        (
            """
                {
                    "requestId": "%s",
                    "requestType": "%s"
                }
            """,
            requestId, requestType
        );

        Account generalAccount = new Account();
        Long generalAccountId = 3L;
        String generalAccountUsername = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        ShiftChangeRequest shiftChangeRequest = new ShiftChangeRequest();
        Long shiftChangeRequestId = 97L;
        shiftChangeRequest.setShiftChangeId(shiftChangeRequestId);
        shiftChangeRequest.setRequestStatus(1);

        when(accountService.findCurrentAccount()).thenReturn(generalAccount);
        when(requestService.withdrow(any(Account.class), any(WithDrowInput.class))).thenReturn(1);
        mockMvc.perform
        (
            post("/api/send/withdrow")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .with(csrf())
            .with(user(generalAccountUsername))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(1));
    }

    @Test
    void withdrowStampSuccess() throws Exception
    {
        int requestId = 3;
        int requestType = 3;
        String json = String.format
        (
            """
                {
                    "requestId": "%s",
                    "requestType": "%s"
                }
            """,
            requestId, requestType
        );

        Account generalAccount = new Account();
        Long generalAccountId = 3L;
        String generalAccountUsername = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        StampRequest stampRequest = new StampRequest();
        Long stampRequestId = 9L;
        stampRequest.setStampId(stampRequestId);
        stampRequest.setRequestStatus(1);

        when(accountService.findCurrentAccount()).thenReturn(generalAccount);
        when(requestService.withdrow(any(Account.class), any(WithDrowInput.class))).thenReturn(1);
        mockMvc.perform
        (
            post("/api/send/withdrow")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .with(csrf())
            .with(user(generalAccountUsername))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(1));
    }

    @Test
    void withdrowVacationSuccess() throws Exception
    {
        int requestId = 8;
        int requestType = 4;
        String json = String.format
        (
            """
                {
                    "requestId": "%s",
                    "requestType": "%s"
                }
            """,
            requestId, requestType
        );

        Account generalAccount = new Account();
        Long generalAccountId = 3L;
        String generalAccountUsername = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        VacationRequest vacationRequest = new VacationRequest();
        Long vacationRequestId = 30L;
        vacationRequest.setVacationId(vacationRequestId);
        vacationRequest.setRequestStatus(1);

        when(accountService.findCurrentAccount()).thenReturn(generalAccount);
        when(requestService.withdrow(any(Account.class), any(WithDrowInput.class))).thenReturn(1);
        mockMvc.perform
        (
            post("/api/send/withdrow")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .with(csrf())
            .with(user(generalAccountUsername))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(1));
    }

    @Test
    void withdrowOverTimeSuccess() throws Exception
    {
        int requestId = 2;
        int requestType = 5;
        String json = String.format
        (
            """
                {
                    "requestId": "%s",
                    "requestType": "%s"
                }
            """,
            requestId, requestType
        );

        Account generalAccount = new Account();
        Long generalAccountId = 3L;
        String generalAccountUsername = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        OverTimeRequest overTimeRequest = new OverTimeRequest();
        Long overTimeRequestId = 498L;
        overTimeRequest.setOverTimeId(overTimeRequestId);
        overTimeRequest.setRequestStatus(1);

        when(accountService.findCurrentAccount()).thenReturn(generalAccount);
        when(requestService.withdrow(any(Account.class), any(WithDrowInput.class))).thenReturn(1);
        mockMvc.perform
        (
            post("/api/send/withdrow")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .with(csrf())
            .with(user(generalAccountUsername))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(1));
    }

    @Test
    void withdrowOtherTimeSuccess() throws Exception
    {
        int requestId = 38;
        int requestType = 6;
        String json = String.format
        (
            """
                {
                    "requestId": "%s",
                    "requestType": "%s"
                }
            """,
            requestId, requestType
        );

        Account generalAccount = new Account();
        Long generalAccountId = 3L;
        String generalAccountUsername = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        AttendanceExceptionRequest attendanceExceptionRequest = new AttendanceExceptionRequest();
        Long attendanceExceptionRequestId = 49L;
        attendanceExceptionRequest.setAttendanceExceptionId(attendanceExceptionRequestId);
        attendanceExceptionRequest.setRequestStatus(1);

        when(accountService.findCurrentAccount()).thenReturn(generalAccount);
        when(requestService.withdrow(any(Account.class), any(WithDrowInput.class))).thenReturn(1);
        mockMvc.perform
        (
            post("/api/send/withdrow")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .with(csrf())
            .with(user(generalAccountUsername))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(1));
    }

    @Test
    void withdrowMonthlySuccess() throws Exception
    {
        int requestId = 29;
        int requestType = 7;
        String json = String.format
        (
            """
                {
                    "requestId": "%s",
                    "requestType": "%s"
                }
            """,
            requestId, requestType
        );

        Account generalAccount = new Account();
        Long generalAccountId = 3L;
        String generalAccountUsername = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        MonthlyRequest monthlyRequest = new MonthlyRequest();
        Long monthlyRequestId = 4398L;
        monthlyRequest.setMonthRequestId(monthlyRequestId);
        monthlyRequest.setRequestStatus(1);

        List<Shift> shifts = new ArrayList<Shift>();
        Shift shift = new Shift();
        shifts.add(shift);

        List<ShiftListOtherTime> shiftListOtherTimes = new ArrayList<ShiftListOtherTime>();
        ShiftListOtherTime shiftListOtherTime = new ShiftListOtherTime();
        AttendanceExceptionRequest attendanceExceptionRequest = new AttendanceExceptionRequest();
        shiftListOtherTime.setAttendanceExceptionId(attendanceExceptionRequest);
        shiftListOtherTimes.add(shiftListOtherTime);

        List<ShiftListOverTime> shiftListOverTimes = new ArrayList<ShiftListOverTime>();
        ShiftListOverTime shiftListOverTime = new ShiftListOverTime();
        OverTimeRequest overTimeRequest = new OverTimeRequest();
        shiftListOverTime.setOverTimeId(overTimeRequest);
        shiftListOverTimes.add(shiftListOverTime);

        List<ShiftListShiftRequest> shiftListShiftRequests = new ArrayList<ShiftListShiftRequest>();
        ShiftListShiftRequest shiftListShiftRequest = new ShiftListShiftRequest();
        ShiftRequest shiftRequest = new ShiftRequest();
        shiftListShiftRequest.setShiftRequestId(shiftRequest);

        ShiftListShiftRequest shiftListShiftChangeRequest = new ShiftListShiftRequest();
        ShiftChangeRequest shiftChangeRequest = new ShiftChangeRequest();
        shiftListShiftChangeRequest.setShiftRequestId(shiftRequest);
        shiftListShiftChangeRequest.setShiftChangeRequestId(shiftChangeRequest);
        shiftListShiftRequests.add(shiftListShiftRequest);
        shiftListShiftRequests.add(shiftListShiftChangeRequest);

        List<ShiftListVacation> shiftListVacations = new ArrayList<ShiftListVacation>();
        ShiftListVacation shiftListVacation = new ShiftListVacation();
        VacationRequest vacationRequest = new VacationRequest();
        shiftListVacation.setVacationId(vacationRequest);
        shiftListVacations.add(shiftListVacation);

        List<Attend> attends = new ArrayList<Attend>();
        Attend attend = new Attend();
        attends.add(attend);

        List<AttendanceListSource> attendanceListSources = new ArrayList<AttendanceListSource>();
        AttendanceListSource attendanceListSource = new AttendanceListSource();
        StampRequest stampRequest = new StampRequest();
        attendanceListSource.setStampRequestId(stampRequest);
        attendanceListSources.add(attendanceListSource);

        when(accountService.findCurrentAccount()).thenReturn(generalAccount);
        when(requestService.withdrow(any(Account.class), any(WithDrowInput.class))).thenReturn(1);
        mockMvc.perform
        (
            post("/api/send/withdrow")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .with(csrf())
            .with(user(generalAccountUsername))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(1));
    }

    @Test
    void rejectShiftSuccess() throws Exception
    {
        int requestId = 1;
        int requestType = 1;
        String requestComment = "hogehoge";
        String requestTime = "2025/12/13T00:00:00";
        String json = String.format
        (
            """
                {
                    "requestId": "%s",
                    "requestType": "%s",
                    "requestComment": "%s",
                    "requestTime": "%s"
                }
            """,
            requestId, requestType, requestComment, requestTime
        );

        Account generalAccount = new Account();
        Long generalAccountId = 3L;
        String generalAccountUsername = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        ShiftRequest shiftRequest = new ShiftRequest();
        Long shiftRequestid = 43L;
        shiftRequest.setShiftRequestId(shiftRequestid);
        shiftRequest.setRequestStatus(1);

        when(accountService.findCurrentAccount()).thenReturn(generalAccount);
        when(requestService.reject(any(Account.class), any(RequestJudgmentInput.class))).thenReturn(1);
        mockMvc.perform
        (
            post("/api/send/reject")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .with(csrf())
            .with(user(generalAccountUsername))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(1));
    }

    @Test
    void rejectShiftChangeSuccess() throws Exception
    {
        int requestId = 5;
        int requestType = 2;
        String requestComment = "hogehoge";
        String requestTime = "2025/12/13T00:00:00";
        String json = String.format
        (
            """
                {
                    "requestId": "%s",
                    "requestType": "%s",
                    "requestComment": "%s",
                    "requestTime": "%s"
                }
            """,
            requestId, requestType, requestComment, requestTime
        );

        Account generalAccount = new Account();
        Long generalAccountId = 3L;
        String generalAccountUsername = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        ShiftChangeRequest shiftChangeRequest = new ShiftChangeRequest();
        Long shiftChangeRequestId = 97L;
        shiftChangeRequest.setShiftChangeId(shiftChangeRequestId);
        shiftChangeRequest.setRequestStatus(1);

        when(accountService.findCurrentAccount()).thenReturn(generalAccount);
        when(requestService.reject(any(Account.class), any(RequestJudgmentInput.class))).thenReturn(1);
        mockMvc.perform
        (
            post("/api/send/reject")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .with(csrf())
            .with(user(generalAccountUsername))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(1));
    }

    @Test
    void rejectStampSuccess() throws Exception
    {
        int requestId = 3;
        int requestType = 3;
        String requestComment = "";
        String requestTime = "2025/12/13T00:00:00";
        String json = String.format
        (
            """
                {
                    "requestId": "%s",
                    "requestType": "%s",
                    "requestComment": "%s",
                    "requestTime": "%s"
                }
            """,
            requestId, requestType, requestComment, requestTime
        );

        Account generalAccount = new Account();
        Long generalAccountId = 3L;
        String generalAccountUsername = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        StampRequest stampRequest = new StampRequest();
        Long stampRequestId = 9L;
        stampRequest.setStampId(stampRequestId);
        stampRequest.setRequestStatus(1);

        when(accountService.findCurrentAccount()).thenReturn(generalAccount);
        when(requestService.reject(any(Account.class), any(RequestJudgmentInput.class))).thenReturn(1);
        mockMvc.perform
        (
            post("/api/send/reject")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .with(csrf())
            .with(user(generalAccountUsername))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(1));
    }

    @Test
    void rejectVacationSuccess() throws Exception
    {
        int requestId = 8;
        int requestType = 4;
        String requestComment = "hogehoge";
        String requestTime = "2025/12/13T00:00:00";
        String json = String.format
        (
            """
                {
                    "requestId": "%s",
                    "requestType": "%s",
                    "requestComment": "%s",
                    "requestTime": "%s"
                }
            """,
            requestId, requestType, requestComment, requestTime
        );

        Account generalAccount = new Account();
        Long generalAccountId = 3L;
        String generalAccountUsername = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        VacationRequest vacationRequest = new VacationRequest();
        Long vacationRequestId = 30L;
        vacationRequest.setVacationId(vacationRequestId);
        vacationRequest.setRequestStatus(1);

        when(accountService.findCurrentAccount()).thenReturn(generalAccount);
        when(requestService.reject(any(Account.class), any(RequestJudgmentInput.class))).thenReturn(1);
        mockMvc.perform
        (
            post("/api/send/reject")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .with(csrf())
            .with(user(generalAccountUsername))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(1));
    }

    @Test
    void rejectOverTimeSuccess() throws Exception
    {
        int requestId = 2;
        int requestType = 5;
        String requestComment = "hogehoge";
        String requestTime = "2025/12/13T00:00:00";
        String json = String.format
        (
            """
                {
                    "requestId": "%s",
                    "requestType": "%s",
                    "reqeustCommnet": "%s",
                    "requestTime": "%s"
                }
            """,
            requestId, requestType, requestComment, requestTime
        );

        Account generalAccount = new Account();
        Long generalAccountId = 3L;
        String generalAccountUsername = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        OverTimeRequest overTimeRequest = new OverTimeRequest();
        Long overTimeRequestId = 498L;
        overTimeRequest.setOverTimeId(overTimeRequestId);
        overTimeRequest.setRequestStatus(1);

        when(accountService.findCurrentAccount()).thenReturn(generalAccount);
        when(requestService.reject(any(Account.class), any(RequestJudgmentInput.class))).thenReturn(1);
        mockMvc.perform
        (
            post("/api/send/reject")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .with(csrf())
            .with(user(generalAccountUsername))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(1));
    }

    @Test
    void rejectOtherTimeSuccess() throws Exception
    {
        int requestId = 38;
        int requestType = 6;
        String requestComment = "hogehoge";
        String requestTime = "2025/12/13T00:00:00";
        String json = String.format
        (
            """
                {
                    "requestId": "%s",
                    "requestType": "%s",
                    "requestComment": "%s",
                    "requestTime": "%s"
                }
            """,
            requestId, requestType, requestComment, requestTime
        );

        Account generalAccount = new Account();
        Long generalAccountId = 3L;
        String generalAccountUsername = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        AttendanceExceptionRequest attendanceExceptionRequest = new AttendanceExceptionRequest();
        Long attendanceExceptionRequestId = 49L;
        attendanceExceptionRequest.setAttendanceExceptionId(attendanceExceptionRequestId);
        attendanceExceptionRequest.setRequestStatus(1);

        when(accountService.findCurrentAccount()).thenReturn(generalAccount);
        when(requestService.reject(any(Account.class), any(RequestJudgmentInput.class))).thenReturn(1);
        mockMvc.perform
        (
            post("/api/send/reject")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .with(csrf())
            .with(user(generalAccountUsername))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(1));
    }

    @Test
    void rejectMonthlySuccess() throws Exception
    {
        int requestId = 29;
        int requestType = 7;
        String reqeustComment = "hogehoge";
        String requestTime = "2025/12/13T00:00:00";
        String json = String.format
        (
            """
                {
                    "requestId": "%s",
                    "requestType": "%s",
                    "requestComment": "%s",
                    "requestTime": "%s"
                }
            """,
            requestId, requestType, reqeustComment, requestTime
        );

        Account generalAccount = new Account();
        Long generalAccountId = 3L;
        String generalAccountUsername = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        when(accountService.findCurrentAccount()).thenReturn(generalAccount);
        when(requestService.reject(any(Account.class), any(RequestJudgmentInput.class))).thenReturn(1);
        mockMvc.perform
        (
            post("/api/send/reject")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .with(csrf())
            .with(user(generalAccountUsername))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(1));
    }

    @Test
    void approvalSuccess() throws Exception
    {
        int requestId = 29;
        int requestType = 7;
        String reqeustComment = "hogehoge";
        String requestTime = "2025/12/13T00:00:00";
        String json = String.format
        (
            """
                {
                    "requestId": "%s",
                    "requestType": "%s",
                    "requestComment": "%s",
                    "requestTime": "%s"
                }
            """,
            requestId, requestType, reqeustComment, requestTime
        );

        Account generalAccount = new Account();
        Long generalAccountId = 3L;
        String generalAccountUsername = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        when(accountService.findCurrentAccount()).thenReturn(generalAccount);
        when(requestService.approval(any(Account.class), any(RequestJudgmentInput.class))).thenReturn(1);
        mockMvc.perform
        (
            post("/api/send/approval")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .with(csrf())
            .with(user(generalAccountUsername))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(1));
    }

    @Test
    void approvalCancelSuccess() throws Exception
    {
        int requestId = 29;
        int requestType = 7;
        String reqeustComment = "hogehoge";
        String requestTime = "2025/12/13T00:00:00";
        String json = String.format
        (
            """
                {
                    "requestId": "%s",
                    "requestType": "%s",
                    "requestComment": "%s",
                    "requestTime": "%s"
                }
            """,
            requestId, requestType, reqeustComment, requestTime
        );

        Account generalAccount = new Account();
        Long generalAccountId = 3L;
        String generalAccountUsername = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        when(accountService.findCurrentAccount()).thenReturn(generalAccount);
        when(requestService.approvalCancel(any(Account.class), any(RequestJudgmentInput.class))).thenReturn(1);
        mockMvc.perform
        (
            post("/api/send/approvalcancel")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .with(csrf())
            .with(user(generalAccountUsername))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(1));
    }
}