package com.example.springboot.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
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
import com.example.springboot.dto.response.ApproverListResponse;
import com.example.springboot.dto.response.AttendListResponse;
import com.example.springboot.dto.response.NewsListResponse;
import com.example.springboot.dto.response.ShiftListResponse;
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
import com.example.springboot.model.LegalTime;
import com.example.springboot.model.MonthlyRequest;
import com.example.springboot.model.NewsList;
import com.example.springboot.model.OverTimeRequest;
import com.example.springboot.model.PaydHoliday;
import com.example.springboot.model.PaydHolidayUse;
import com.example.springboot.service.AccountApproverService;
import com.example.springboot.service.AccountService;
import com.example.springboot.service.ApprovalSettingService;
import com.example.springboot.service.AttendService;
import com.example.springboot.service.AttendanceExceptionRequestService;
import com.example.springboot.service.AttendanceExceptionTypeService;
import com.example.springboot.service.AttendanceListSourceService;
import com.example.springboot.service.DepartmentService;
import com.example.springboot.service.LegalTimeService;
import com.example.springboot.service.MonthlyRequestService;
import com.example.springboot.service.NewsListService;
import com.example.springboot.service.OverTimeRequestService;
import com.example.springboot.service.PaydHolidayService;
import com.example.springboot.service.PaydHolidayUseService;
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

        List<ApprovalSetting> approvalSettings = new ArrayList<ApprovalSetting>();

        when(accountService.findCurrentAccount()).thenReturn(account);
        when(roleService.findRoleById(any())).thenReturn(role);
        when(departmentService.findDepartmentById(any())).thenReturn(department);
        when(approvalSettingService.findApprovalSettingsByApprover(any())).thenReturn(approvalSettings);
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

        when(accountService.findCurrentAccount()).thenReturn(account);
        when(roleService.findRoleById(any())).thenReturn(role);
        when(departmentService.findDepartmentById(any())).thenReturn(department);
        when(approvalSettingService.findApprovalSettingsByApprover(any())).thenReturn(approvalSettings);
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
        LocalDateTimeToString localDateTimeToString = new LocalDateTimeToString();
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
        Shift generalShift = new Shift
        (
            generalShiftId, generalAccount, generalShiftBeginWork,
            generalShiftEndWork, generalShiftBeginBreak, generalShiftEndBreak,
            generalShiftLateness, generalShiftLeaveEarly, generalShiftOuting, generalShiftOverWork
        );
        ShiftListResponse generalShiftListResponse = new ShiftListResponse
        (
            generalShiftId,
            localDateTimeToString.localDateTimeToString(generalShiftBeginWork),
            localDateTimeToString.localDateTimeToString(generalShiftEndWork),
            localDateTimeToString.localDateTimeToString(generalShiftBeginBreak),
            localDateTimeToString.localDateTimeToString(generalShiftEndBreak),
            generalShiftLateness,
            generalShiftLeaveEarly,
            generalShiftOuting,
            generalShiftOverWork
        );
        List<Shift> generalShifts = new ArrayList<Shift>();
        generalShifts.add(generalShift);

        when(accountService.findAccountByUsername(anyString())).thenReturn(generalAccount);
        // when(shiftService.findByAccountId(anyLong())).thenReturn(generalShifts);
        when(shiftService.findByAccountIdAndBeginWorkBetween(anyLong(), anyInt(), anyInt())).thenReturn(generalShifts);
        when(shiftService.shiftToShiftListResponse(any(Shift.class))).thenReturn(generalShiftListResponse);
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
        StringToLocalDateTime stringToLocalDateTime = new StringToLocalDateTime();
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

        LegalTime legalTime = new LegalTime();
        Long legalTimeId = 1L;
        LocalDateTime legalTimeBegin = stringToLocalDateTime.stringToLocalDateTime("2025/08/08T21:00:00");
        String legalTimeScheduleWorkTime = "08:00:00";
        String legalTimeWeeklyWorkTime = "40:00:00";
        String legalTimeMonthlyOverWork = "45:00:00";
        String legalTimeYearOverWork = "360:00:00";
        String legalTimeMaxOverWorkTime = "100:00:00";
        String legalTimeMonthlyOverWorkAverage = "80:00:00";
        String legalTimeLateNightWorkTimeBegin = "22:00:00";
        String legalTimeLateNightWorkTimeEnd = "05:00:00";
        String legalTimeScheduleBreakTime = "01:00:00";
        int legalTimeWeeklyHoliday = 1;

        legalTime.setLegalTimeId(legalTimeId);
        legalTime.setBegin(legalTimeBegin);
        legalTime.setScheduleWorkTime(legalTimeScheduleWorkTime);
        legalTime.setWeeklyWorkTime(legalTimeWeeklyWorkTime);
        legalTime.setMonthlyOverWorkTime(legalTimeMonthlyOverWork);
        legalTime.setYearOverWorkTime(legalTimeYearOverWork);
        legalTime.setMaxOverWorkTime(legalTimeMaxOverWorkTime);
        legalTime.setMonthlyOverWorkAverage(legalTimeMonthlyOverWorkAverage);
        legalTime.setLateNightWorkBegin(legalTimeLateNightWorkTimeBegin);
        legalTime.setLateNightWorkEnd(legalTimeLateNightWorkTimeEnd);
        legalTime.setScheduleBreakTime(legalTimeScheduleBreakTime);
        legalTime.setWeeklyHoliday(legalTimeWeeklyHoliday);

        Account generalAccount = new Account();
        Long generalAccountId = 1L;
        String generalAccountName = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setName(generalAccountName);

        List<Shift> shifts = new ArrayList<Shift>();
        List<ShiftRequest> shiftRequests = new ArrayList<ShiftRequest>();

        ShiftRequest shiftRequest = new ShiftRequest();
        Long shiftRequestId = 2L;
        shiftRequest.setShiftRequestId(shiftRequestId);

        when(accountService.findAccountByUsername(anyString())).thenReturn(generalAccount);
        when(shiftService.findByAccountIdAndDayBeginWorkBetween(any(Account.class), any(LocalDateTime.class))).thenReturn(shifts);
        when(shiftRequestService.findAccountIdAndBeginWorkBetweenDay(any(Account.class), any(LocalDateTime.class))).thenReturn(shiftRequests);
        when(shiftRequestService.save(any(ShiftRequest.class))).thenReturn(shiftRequest);
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
    void shiftRequestDetilSuccess() throws Exception
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

        ShiftRequest shiftRequest = new ShiftRequest();
        Long shiftRequestId = 1L;
        LocalDateTime beginWork = LocalDateTime.parse("2025/08/08/09/30/00",DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        LocalDateTime endWork = LocalDateTime.parse("2025/08/08/12/30/00",DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        LocalDateTime beginBreak = LocalDateTime.parse("2025/08/13/09/30/00",DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        LocalDateTime endBreak = LocalDateTime.parse("2025/08/08/18/30/00",DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        String requestComment = "";
        LocalDateTime requestDate = LocalDateTime.parse("2025/07/07/00/00/00",DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        int requestStatus = 1;
        LocalDateTime approvalTime = null;
        String approverComment = null;
        shiftRequest.setShiftRequestId(shiftRequestId);
        shiftRequest.setAccountId(generalAccount);
        shiftRequest.setBeginWork(beginWork);
        shiftRequest.setEndWork(endWork);
        shiftRequest.setBeginBreak(beginBreak);
        shiftRequest.setEndBreak(endBreak);
        shiftRequest.setRequestComment(requestComment);
        shiftRequest.setRequestDate(requestDate);
        shiftRequest.setRequestStatus(requestStatus);
        shiftRequest.setApprover(adminAccount);
        shiftRequest.setApproverComment(approverComment);
        shiftRequest.setApprovalTime(approvalTime);

        when(accountService.findAccountByUsername(anyString())).thenReturn(generalAccount);
        when(shiftRequestService.findByAccountIdAndShiftRequestId(any(Account.class),anyLong())).thenReturn(shiftRequest);
        mockMvc.perform
        (
            get("/api/reach/requestdetil/shift")
            .param("requestId","1")
            .with(csrf())
            .with(user(generalAccountUsername))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(1))
        .andExpect(jsonPath("$.beginWork").value(localDateTimeToString.localDateTimeToString(beginWork)))
        .andExpect(jsonPath("$.endWork").value(localDateTimeToString.localDateTimeToString(endWork)))
        .andExpect(jsonPath("$.beginBreak").value(localDateTimeToString.localDateTimeToString(beginBreak)))
        .andExpect(jsonPath("$.endBreak").value(localDateTimeToString.localDateTimeToString(endBreak)))
        .andExpect(jsonPath("$.requestComment").value(requestComment))
        .andExpect(jsonPath("$.requestDate").value(localDateTimeToString.localDateTimeToString(requestDate)))
        .andExpect(jsonPath("$.requestStatus").value(requestStatus))
        .andExpect(jsonPath("$.approverId").value(adminAccountId.intValue()))
        .andExpect(jsonPath("$.approverName").value(adminAccountName))
        .andExpect(jsonPath("$.approverComment").value(approverComment))
        .andExpect(jsonPath("$.approvalTime").value(Objects.isNull(approvalTime) ? "" : localDateTimeToString.localDateTimeToString(approvalTime)));
    }

    @Test
    void shiftChangeRequestDetilSuccess() throws Exception
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

        ShiftChangeRequest shiftChangeRequest = new ShiftChangeRequest();
        Long shiftChangeRequestId = 1L;
        LocalDateTime beginWork = LocalDateTime.parse("2025/08/08/09/30/00",DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        LocalDateTime endWork = LocalDateTime.parse("2025/08/08/12/30/00",DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        LocalDateTime beginBreak = LocalDateTime.parse("2025/08/13/09/30/00",DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        LocalDateTime endBreak = LocalDateTime.parse("2025/08/08/18/30/00",DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        String requestComment = "";
        LocalDateTime requestDate = LocalDateTime.parse("2025/07/07/00/00/00",DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        int requestStatus = 1;
        LocalDateTime approvalTime = null;
        String approverComment = null;
        shiftChangeRequest.setShiftChangeId(shiftChangeRequestId);
        shiftChangeRequest.setAccountId(generalAccount);
        shiftChangeRequest.setBeginWork(beginWork);
        shiftChangeRequest.setEndWork(endWork);
        shiftChangeRequest.setBeginBreak(beginBreak);
        shiftChangeRequest.setEndBreak(endBreak);
        shiftChangeRequest.setRequestComment(requestComment);
        shiftChangeRequest.setRequestDate(requestDate);
        shiftChangeRequest.setRequestStatus(requestStatus);
        shiftChangeRequest.setApprover(adminAccount);
        shiftChangeRequest.setApproverComment(approverComment);
        shiftChangeRequest.setApprovalTime(approvalTime);
        shiftChangeRequest.setShiftId(shift);

        when(accountService.findAccountByUsername(anyString())).thenReturn(generalAccount);
        when(shiftChangeRequestService.findByAccountIdAndShiftChangeRequestId(any(Account.class),anyLong())).thenReturn(shiftChangeRequest);
        mockMvc.perform
        (
            get("/api/reach/requestdetil/changetime")
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
    void stampRequestDetilSuccess() throws Exception
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

        StampRequest stampRequest = new StampRequest();
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
        stampRequest.setStampId(stampId);
        stampRequest.setAccountId(generalAccount);
        stampRequest.setBeginWork(beginWork);
        stampRequest.setEndWork(endWork);
        stampRequest.setBeginBreak(beginBreak);
        stampRequest.setEndBreak(endBreak);
        stampRequest.setRequestComment(requestComment);
        stampRequest.setRequestDate(requestDate);
        stampRequest.setRequestStatus(requestStatus);
        stampRequest.setApprover(adminAccount);
        stampRequest.setApproverComment(approverComment);
        stampRequest.setApprovalTime(approvalTime);
        stampRequest.setShiftId(shift);

        when(accountService.findAccountByUsername(anyString())).thenReturn(generalAccount);
        when(stampRequestService.findByAccountIdAndStampId(any(Account.class),anyLong())).thenReturn(stampRequest);
        mockMvc.perform
        (
            get("/api/reach/requestdetil/stamp")
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
        StringToLocalDateTime stringToLocalDateTime = new StringToLocalDateTime();
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

        LegalTime legalTime = new LegalTime();
        Long legalTimeId = 1L;
        LocalDateTime legalTimeBegin = LocalDateTime.parse(LocalDateTime.parse("2025/08/08T21:00:00",DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        String legalTimeScheduleWorkTime = "08:00:00";
        String legalTimeWeeklyWorkTime = "40:00:00";
        String legalTimeMonthlyOverWork = "45:00:00";
        String legalTimeYearOverWork = "360:00:00";
        String legalTimeMaxOverWorkTime = "100:00:00";
        String legalTimeMonthlyOverWorkAverage = "80:00:00";
        String legalTimeLateNightWorkTimeBegin = "22:00:00";
        String legalTimeLateNightWorkTimeEnd = "05:00:00";
        String legalTimeScheduleBreakTime = "01:00:00";
        int legalTimeWeeklyHoliday = 1;

        legalTime.setLegalTimeId(legalTimeId);
        legalTime.setBegin(legalTimeBegin);
        legalTime.setScheduleWorkTime(legalTimeScheduleWorkTime);
        legalTime.setWeeklyWorkTime(legalTimeWeeklyWorkTime);
        legalTime.setMonthlyOverWorkTime(legalTimeMonthlyOverWork);
        legalTime.setYearOverWorkTime(legalTimeYearOverWork);
        legalTime.setMaxOverWorkTime(legalTimeMaxOverWorkTime);
        legalTime.setMonthlyOverWorkAverage(legalTimeMonthlyOverWorkAverage);
        legalTime.setLateNightWorkBegin(legalTimeLateNightWorkTimeBegin);
        legalTime.setLateNightWorkEnd(legalTimeLateNightWorkTimeEnd);
        legalTime.setScheduleBreakTime(legalTimeScheduleBreakTime);
        legalTime.setWeeklyHoliday(legalTimeWeeklyHoliday);

        Account generalAccount = new Account();
        Long generalAccountId = 1L;
        String generalAccountName = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setName(generalAccountName);

        Shift shift = new Shift();
        Long shiftId = 1L;
        shift.setShiftId(shiftId);
        shift.setBeginWork(stringToLocalDateTime.stringToLocalDateTime(generalBeginWork));

        LocalDateTime generalBeginWorkTimeShift = stringToLocalDateTime.stringToLocalDateTime("2025/09/17T09:00:00");
        LocalDateTime generalEndWorkTimeShift = stringToLocalDateTime.stringToLocalDateTime("2025/09/17T18:00:00");
        LocalDateTime generalBeginBreakTimeShift = stringToLocalDateTime.stringToLocalDateTime("2025/09/17T12:00:00");
        LocalDateTime generalEndBreakTimeShift = stringToLocalDateTime.stringToLocalDateTime("2025/09/17T13:00:00");

        List<ShiftChangeRequest> generalShiftChangeRequests = new ArrayList<ShiftChangeRequest>();
        ShiftChangeRequest shiftChangeRequest = new ShiftChangeRequest();
        Long shiftChangeRequestId = 35L;
        shiftChangeRequest.setShiftChangeId(shiftChangeRequestId);
        shiftChangeRequest.setShiftId(shift);
        shiftChangeRequest.setBeginWork(generalBeginWorkTimeShift);
        shiftChangeRequest.setEndWork(generalEndWorkTimeShift);
        shiftChangeRequest.setBeginBreak(generalBeginBreakTimeShift);
        shiftChangeRequest.setEndBreak(generalEndBreakTimeShift);

        when(accountService.findAccountByUsername(anyString())).thenReturn(generalAccount);
        when(shiftService.findByAccountIdAndShiftId(any(Account.class), anyLong())).thenReturn(shift);
        when(shiftChangeRequestService.findByAccountIdAndShiftIdAndRequestStatusWait(any(Account.class), anyLong())).thenReturn(generalShiftChangeRequests);
        when(shiftChangeRequestService.save(any(ShiftChangeRequest.class))).thenReturn(shiftChangeRequest);
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
        LocalDateTimeToString localDateTimeToString = new LocalDateTimeToString();
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
            localDateTimeToString.localDateTimeToString(generalAttendBeginWork),
            localDateTimeToString.localDateTimeToString(generalAttendEndWork),
            localDateTimeToString.localDateTimeToString(generalAttendBeginBreak),
            localDateTimeToString.localDateTimeToString(generalAttendEndBreak),
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
        when(accountService.findAccountByUsername(anyString())).thenReturn(generalAccount);
        when(attendService.findByAccountIdAndBeginWorkBetween(anyLong(), anyInt(), anyInt())).thenReturn(generalAttends);
        when(attendService.attendToAttendListResponse(any(Attend.class))).thenReturn(generalAttendListResponse);
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
    void vacationRequestDetilSuccess() throws Exception
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

        when(accountService.findAccountByUsername(anyString())).thenReturn(generalAccount);
        when(vacationRequestService.findByAccountIdAndVacationId(any(Account.class),anyLong())).thenReturn(vacationRequest);
        mockMvc.perform
        (
            get("/api/reach/requestdetil/vacation")
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
    void overTimeRequestDetilSuccess() throws Exception
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

        when(accountService.findAccountByUsername(anyString())).thenReturn(generalAccount);
        when(overTimeRequestService.findByAccountIdAndOverTimeRequestId(any(Account.class),anyLong())).thenReturn(overTimeRequest);
        mockMvc.perform
        (
            get("/api/reach/requestdetil/overtime")
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
    void otherTimeRequestDetilSuccess() throws Exception
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

        when(accountService.findAccountByUsername(anyString())).thenReturn(generalAccount);
        when(attendanceExceptionRequestService.findByAccountIdAndAttendanceExceptionId(any(Account.class),anyLong())).thenReturn(attendanceExceptionRequest);
        mockMvc.perform
        (
            get("/api/reach/requestdetil/othertime")
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
        StringToLocalDateTime stringToLocalDateTime = new StringToLocalDateTime();
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

        Shift generalShift = new Shift();
        generalShift.setBeginWork(stringToLocalDateTime.stringToLocalDateTime(generalBeginWork));
        generalShift.setEndWork(stringToLocalDateTime.stringToLocalDateTime(generalEndWork));
        generalShift.setBeginBreak(stringToLocalDateTime.stringToLocalDateTime(generalBeginBreak));
        generalShift.setEndBreak(stringToLocalDateTime.stringToLocalDateTime(generalEndBreak));

        List<StampRequest> stampRequests = new ArrayList<StampRequest>();

        StampRequest stampRequest = new StampRequest();
        Long stampRequestId = 3L;
        stampRequest.setStampId(stampRequestId);

        when(accountService.findAccountByUsername(anyString())).thenReturn(generalAccount);
        when(shiftService.findByAccountIdAndShiftId(any(Account.class), anyLong())).thenReturn(generalShift);
        when(stampRequestService.findByShiftIdAndRequestStatusWait(any(Shift.class))).thenReturn(stampRequests);
        when(stampRequestService.save(any(StampRequest.class))).thenReturn(stampRequest);
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
    void monthlyRequestDetilSuccess() throws Exception
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

        when(accountService.findAccountByUsername(anyString())).thenReturn(generalAccount);
        when(monthlyRequestService.findByAccountIdAndMothlyRequestId(any(Account.class), anyLong())).thenReturn(generalMonthlyRequest);
        mockMvc.perform
        (
            get("/api/reach/requestdetil/monthly")
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
        StringToLocalDateTime stringToLocalDateTime = new StringToLocalDateTime();
        LocalDateTimeToString localDateTimeToString = new LocalDateTimeToString();
        Account generalAccount = new Account();
        Long generalAccountId = 1L;
        String generalAccountUsername = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        Long generalRequestId = 3L;
        int generalRequestStatus = 1;
        String generalLocalDateTimeShift = "2025/06/07T21:33:44";
        String generalLocalDateTimeShiftChange = "2025/06/07T21:33:45";
        String generalLocalDateTimeStamp = "2025/06/07T21:33:46";
        String generalLocalDateTimeAttendanceException = "2025/06/07T21:33:47";
        String generalLocalDateTimeOverTime = "2025/06/07T21:33:48";
        String generalLocalDateTimeVacation = "2025/06/07T21:33:49";
        String generalLocalDateTimeMonthly = "2025/06/07T21:33:50";

        List<ShiftRequest> shiftRequests = new ArrayList<ShiftRequest>();
        ShiftRequest generalShiftRequest = new ShiftRequest();
        generalShiftRequest.setShiftRequestId(generalRequestId);
        generalShiftRequest.setRequestDate(stringToLocalDateTime.stringToLocalDateTime(generalLocalDateTimeShift));
        generalShiftRequest.setRequestStatus(generalRequestStatus);
        shiftRequests.add(generalShiftRequest);

        List<ShiftChangeRequest> shiftChangeRequests = new ArrayList<ShiftChangeRequest>();
        ShiftChangeRequest generalShiftChangeRequest = new ShiftChangeRequest();
        generalShiftChangeRequest.setShiftChangeId(generalRequestId);
        generalShiftChangeRequest.setRequestDate(stringToLocalDateTime.stringToLocalDateTime(generalLocalDateTimeShiftChange));
        generalShiftChangeRequest.setRequestStatus(generalRequestStatus);
        shiftChangeRequests.add(generalShiftChangeRequest);

        List<StampRequest> stampRequests = new ArrayList<StampRequest>();
        StampRequest generalStampRequest = new StampRequest();
        generalStampRequest.setStampId(generalRequestId);
        generalStampRequest.setRequestDate(stringToLocalDateTime.stringToLocalDateTime(generalLocalDateTimeStamp));
        generalStampRequest.setRequestStatus(generalRequestStatus);
        stampRequests.add(generalStampRequest);
    
        List<AttendanceExceptionRequest> attendanceExceptionRequests = new ArrayList<AttendanceExceptionRequest>();
        AttendanceExceptionRequest generalAttendanceExceptionRequest = new AttendanceExceptionRequest();
        generalAttendanceExceptionRequest.setAttendanceExceptionId(generalRequestId);
        generalAttendanceExceptionRequest.setRequestDate(stringToLocalDateTime.stringToLocalDateTime(generalLocalDateTimeAttendanceException));
        generalAttendanceExceptionRequest.setRequestStatus(generalRequestStatus);
        attendanceExceptionRequests.add(generalAttendanceExceptionRequest);

        List<VacationRequest> vacationRequests = new ArrayList<VacationRequest>();
        VacationRequest generalVacationRequest = new VacationRequest();
        generalVacationRequest.setVacationId(generalRequestId);
        generalVacationRequest.setRequestDate(stringToLocalDateTime.stringToLocalDateTime(generalLocalDateTimeVacation));
        generalVacationRequest.setRequestStatus(generalRequestStatus);
        vacationRequests.add(generalVacationRequest);

        List<OverTimeRequest> overTimeRequests = new ArrayList<OverTimeRequest>();
        OverTimeRequest generalOverTimeRequest = new OverTimeRequest();
        generalOverTimeRequest.setOverTimeId(generalRequestId);
        generalOverTimeRequest.setRequestDate(stringToLocalDateTime.stringToLocalDateTime(generalLocalDateTimeOverTime));
        generalOverTimeRequest.setRequestStatus(generalRequestStatus);
        vacationRequests.add(generalVacationRequest);

        List<MonthlyRequest> monthlyRequests = new ArrayList<MonthlyRequest>();
        MonthlyRequest generalMonthlyRequest = new MonthlyRequest();
        generalMonthlyRequest.setMonthRequestId(generalRequestId);
        generalMonthlyRequest.setRequestDate(stringToLocalDateTime.stringToLocalDateTime(generalLocalDateTimeMonthly));
        generalMonthlyRequest.setRequestStatus(generalRequestStatus);
        monthlyRequests.add(generalMonthlyRequest);

        when(accountService.findAccountByUsername(anyString())).thenReturn(generalAccount);
        when(shiftRequestService.findByAccountId(any(Account.class))).thenReturn(shiftRequests);
        when(shiftChangeRequestService.findByAccountId(any(Account.class))).thenReturn(shiftChangeRequests);
        when(stampRequestService.findByAccountId(any(Account.class))).thenReturn(stampRequests);
        when(attendanceExceptionRequestService.findByAccountId(any(Account.class))).thenReturn(attendanceExceptionRequests);
        when(vacationRequestService.findByAccountId(any(Account.class))).thenReturn(vacationRequests);
        when(overTimeRequestService.findByAccountId(any(Account.class))).thenReturn(overTimeRequests);
        when(monthlyRequestService.findByAccountId(any(Account.class))).thenReturn(monthlyRequests);
        mockMvc.perform
        (
            get("/api/reach/requestlist")
            .with(csrf())
            .with(user(generalAccountUsername))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.requestList[0].requestDate").value(localDateTimeToString.localDateTimeToString(stringToLocalDateTime.stringToLocalDateTime(generalLocalDateTimeShift))))
        .andExpect(jsonPath("$.requestList[1].requestDate").value(localDateTimeToString.localDateTimeToString(stringToLocalDateTime.stringToLocalDateTime(generalLocalDateTimeShiftChange))));
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

        when(accountService.findAccountByUsername(anyString())).thenReturn(generalAccount);
        when(attendService.findByAccountIdAndBeginWorkBetween(any(Account.class), anyInt(),anyInt())).thenReturn(attends);
        when(vacationService.findByAccountIdAndBeginVacationBetweenMonthAndPaydHoliday(any(Account.class), anyInt(), anyInt())).thenReturn(vacations);
        mockMvc.perform
        (
            get("/api/reach/monthworkinfo")
            .param("year","2025")
            .param("month", "9")
            .with(csrf())
            .with(user(generalAccountUsername))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(1))
        .andExpect(jsonPath("$.workTime").value("002:00:00"));
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

        when(accountService.findAccountByUsername(anyString())).thenReturn(generalAccount);
        when(styleService.findStyleByAccountId(any(Account.class))).thenReturn(generalStyle);
        mockMvc.perform
        (
            get("/api/reach/style")
            .with(csrf())
            .with(user(generalAccountUsername))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(1))
        .andExpect(jsonPath("$.styleId").value(generalStyleId.intValue()))
        .andExpect(jsonPath("$.styleName").value(generalStylePlaceName));
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

        when(accountService.findAccountByUsername(anyString())).thenReturn(generalAccount);
        when(accountApproverService.findAccountApproverByAccount(any(Account.class))).thenReturn(accountApprover);
        mockMvc.perform
        (
            get("/api/reach/approver")
            .with(csrf())
            .with(user(generalAccountUsername))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(1))
        .andExpect(jsonPath("$.approverId").value(adminAccountId.intValue()))
        .andExpect(jsonPath("$.approverName").value(adminAccountName))
        .andExpect(jsonPath("$.approverDepartment").value(adminAccountDepartmentName))
        .andExpect(jsonPath("$.approverRole").value(adminAccountRoleName));
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

        when(accountService.findAccountByUsername(anyString())).thenReturn(generalAccount);
        when(vacationTypeService.findAll()).thenReturn(vacationTypes);
        mockMvc.perform
        (
            get("/api/reach/allvacationtypelist")
            .with(csrf())
            .with(user(generalAccountUsername))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(1))
        .andExpect(jsonPath("$.vacationTypes[0].vacationTypeId").value(vacationTypeId.intValue()))
        .andExpect(jsonPath("$.vacationTypes[0].vacationTypeName").value(vacationTypeName));
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

        when(accountService.findAccountByUsername(anyString())).thenReturn(generalAccount);
        when(attendanceExceptionTypeService.findAll()).thenReturn(attendanceExceptionTypes);
        mockMvc.perform
        (
            get("/api/reach/allothertypelist")
            .with(csrf())
            .with(user(generalAccountUsername))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(1))
        .andExpect(jsonPath("$.otherTypes[0].otherTypeId").value(attendanceExceptionTypeId.intValue()))
        .andExpect(jsonPath("$.otherTypes[0].otherTypeName").value(attendanceExceptionTypeName));
    }

    @Test
    void vacationRequestSuccess() throws Exception
    {
        StringToLocalDateTime stringToLocalDateTime = new StringToLocalDateTime();
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

        Shift generalShift = new Shift();
        Long generalShiftId = 3L;
        String generalBeginWork = "2026/11/22T09:00:00";
        String generalEndWork = "2026/11/22T18:00:00";
        String generalBeginBreak = "2026/11/22T12:00:00";
        String generalEndBreak = "2026/11/22T13:00:00";
        generalShift.setShiftId(generalShiftId);
        generalShift.setBeginWork(stringToLocalDateTime.stringToLocalDateTime(generalBeginWork));
        generalShift.setEndWork(stringToLocalDateTime.stringToLocalDateTime(generalEndWork));
        generalShift.setBeginBreak(stringToLocalDateTime.stringToLocalDateTime(generalBeginBreak));
        generalShift.setEndBreak(stringToLocalDateTime.stringToLocalDateTime(generalEndBreak));

        List<VacationRequest> vacationRequests = new ArrayList<VacationRequest>();
        VacationRequest generalVacationRequest = new VacationRequest();
        Long generalVacationRequestId = 5L;
        String generalVacationRequestBegin = "2026/11/22T09:00:00";
        String generalVacationRequestEnd = "2026/11/22T10:00:00";
        generalVacationRequest.setVacationId(generalVacationRequestId);
        generalVacationRequest.setAccountId(generalAccount);
        generalVacationRequest.setShiftId(generalShift);
        generalVacationRequest.setBeginVacation(stringToLocalDateTime.stringToLocalDateTime(generalVacationRequestBegin));
        generalVacationRequest.setEndVacation(stringToLocalDateTime.stringToLocalDateTime(generalVacationRequestEnd));
        vacationRequests.add(generalVacationRequest);

        List<ShiftListOverTime> shiftListOverTimes = new ArrayList<ShiftListOverTime>();
        ShiftListOverTime generalShiftListOverTime = new ShiftListOverTime();
        OverTimeRequest generalOverTimeRequest = new OverTimeRequest();
        String generalOverTimeRequestBegin = "2026/11/22T14:00:00";
        String generalOverTimeRequestEnd = "2026/11/22T15:00:00";
        generalOverTimeRequest.setBeginWork(stringToLocalDateTime.stringToLocalDateTime(generalOverTimeRequestBegin));
        generalOverTimeRequest.setEndWork(stringToLocalDateTime.stringToLocalDateTime(generalOverTimeRequestEnd));
        generalShiftListOverTime.setOverTimeId(generalOverTimeRequest);
        shiftListOverTimes.add(generalShiftListOverTime);

        List<OverTimeRequest> overTimeRequests = new ArrayList<OverTimeRequest>();
        OverTimeRequest adminOverTimeRequest = new OverTimeRequest();
        String adminOverTimeRequestBegin = "2026/11/22T18:00:00";
        String adminOverTimeRequestEnd = "2026/11/22T20:00:00";
        adminOverTimeRequest.setAccountId(generalAccount);
        adminOverTimeRequest.setBeginWork(stringToLocalDateTime.stringToLocalDateTime(adminOverTimeRequestBegin));
        adminOverTimeRequest.setEndWork(stringToLocalDateTime.stringToLocalDateTime(adminOverTimeRequestEnd));
        overTimeRequests.add(adminOverTimeRequest);

        List<PaydHoliday> paydHolidays = new ArrayList<PaydHoliday>();
        PaydHoliday generalPaydHoliday = new PaydHoliday();
        String generalPaydHolidayTime = "08:00:00";
        generalPaydHoliday.setTime(generalPaydHolidayTime);
        paydHolidays.add(generalPaydHoliday);

        List<VacationRequest> paydHolidayVacationRequests = new ArrayList<VacationRequest>();
        VacationRequest paydHolidayVacationRequest = new VacationRequest();
        VacationType generalVacationType = new VacationType();
        Long generalVacationTypeId = 1L;
        generalVacationType.setVacationTypeId(generalVacationTypeId);
        String paydHolidayVacationRequestBegin = "2026/12/31T00:00:00";
        String paydHolidayVacationRequestEnd = "2026/12/31T07:00:00";
        paydHolidayVacationRequest.setVacationTypeId(generalVacationType);
        paydHolidayVacationRequest.setBeginVacation(stringToLocalDateTime.stringToLocalDateTime(paydHolidayVacationRequestBegin));
        paydHolidayVacationRequest.setEndVacation(stringToLocalDateTime.stringToLocalDateTime(paydHolidayVacationRequestEnd));
        paydHolidayVacationRequests.add(paydHolidayVacationRequest);

        VacationRequest vacationRequest = new VacationRequest();
        Long vacationRequestId = 3L;
        vacationRequest.setVacationId(vacationRequestId);

        when(accountService.findAccountByUsername(anyString())).thenReturn(generalAccount);
        when(shiftService.findByAccountIdAndShiftId(any(Account.class), anyLong())).thenReturn(generalShift);
        when(vacationRequestService.findByAccountIdAndShiftId(any(Account.class), any(Shift.class))).thenReturn(vacationRequests);
        when(shiftListOverTimeService.findByShiftId(any(Shift.class))).thenReturn(shiftListOverTimes);
        when(overTimeRequestService.findByAccountIdAndShiftIdAndRequestStatusWait(any(Account.class), any(Shift.class))).thenReturn(overTimeRequests);
        when(paydHolidayService.findByAccountIdAndLimitAfter(any(Account.class))).thenReturn(paydHolidays);
        when(vacationRequestService.findByAccountIdAndRequestStatusWaitAndVacationTypePaydHoiday(any(Account.class))).thenReturn(paydHolidayVacationRequests);
        when(vacationTypeService.findById(anyLong())).thenReturn(generalVacationType);
        when(vacationRequestService.save(any(VacationRequest.class))).thenReturn(vacationRequest);
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
        StringToLocalDateTime stringToLocalDateTime = new StringToLocalDateTime();
        LocalDateTimeToString localDateTimeToString = new LocalDateTimeToString();

        Account generalAccount = new Account();
        String generalAccountUsername = "testuser";
        generalAccount.setUsername(generalAccountUsername);

        List<PaydHoliday> paydHolidays = new ArrayList<PaydHoliday>();
        PaydHoliday generalPaydHoliday = new PaydHoliday();
        String generalPaydHolidayGrant = "2025/04/01T09:00:00";
        String generalPaydHolidayTime = "160:00:00";
        generalPaydHoliday.setGrant(stringToLocalDateTime.stringToLocalDateTime(generalPaydHolidayGrant));
        generalPaydHoliday.setTime(generalPaydHolidayTime);
        paydHolidays.add(generalPaydHoliday);

        List<PaydHolidayUse> paydHolidayUses = new ArrayList<PaydHolidayUse>();
        PaydHolidayUse generalPaydHolidayUse = new PaydHolidayUse();
        String generalPaydHolidayUseDate = "2025/11/02T10:00:00";
        String generalPaydHolidayUseTime = "08:00:00";
        generalPaydHolidayUse.setUseDate(stringToLocalDateTime.stringToLocalDateTime(generalPaydHolidayUseDate));
        generalPaydHolidayUse.setTime(Time.valueOf(generalPaydHolidayUseTime));
        paydHolidayUses.add(generalPaydHolidayUse);

        when(accountService.findAccountByUsername(anyString())).thenReturn(generalAccount);
        when(paydHolidayService.findByAccountId(any(Account.class))).thenReturn(paydHolidays);
        when(paydHolidayUseService.findByAccountId(any(Account.class))).thenReturn(paydHolidayUses);
        mockMvc.perform
        (
            get("/api/reach/paydholidayhistory")
            .with(csrf())
            .with(user(generalAccountUsername))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(1))
        .andExpect(jsonPath("$.holidaylist[0].type").value("付与"))
        .andExpect(jsonPath("$.holidaylist[0].date").value(localDateTimeToString.localDateTimeToString(stringToLocalDateTime.stringToLocalDateTime(generalPaydHolidayGrant))))
        .andExpect(jsonPath("$.holidaylist[0].time").value(generalPaydHolidayTime))
        .andExpect(jsonPath("$.holidaylist[1].type").value("消化"))
        .andExpect(jsonPath("$.holidaylist[1].date").value(localDateTimeToString.localDateTimeToString(stringToLocalDateTime.stringToLocalDateTime(generalPaydHolidayUseDate))))
        .andExpect(jsonPath("$.holidaylist[1].time").value(generalPaydHolidayUseTime));
    }

    @Test
    void userAttendListSuccess() throws Exception
    {
        LocalDateTimeToString localDateTimeToString = new LocalDateTimeToString();
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

        AttendListResponse generalAttendListResponse = new AttendListResponse
        (
            generalAttendId,
            localDateTimeToString.localDateTimeToString(generalAttendBeginWork),
            localDateTimeToString.localDateTimeToString(generalAttendEndWork),
            localDateTimeToString.localDateTimeToString(generalAttendBeginBreak),
            localDateTimeToString.localDateTimeToString(generalAttendEndBreak),
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
        when(accountService.findAccountByUsername(anyString())).thenReturn(generalAccount);
        when(accountApproverService.findAccountAndApprover(anyLong(), any(Account.class))).thenReturn(accountApprover);
        when(attendService.findByAccountIdAndBeginWorkBetween(anyLong(), anyInt(), anyInt())).thenReturn(generalAttends);
        when(attendService.attendToAttendListResponse(any(Attend.class))).thenReturn(generalAttendListResponse);
        mockMvc.perform(
            get("/api/reach/user/attendinfo")
            .param("accountId", String.valueOf(generalAccountId))
            .param("year", "2025")
            .param("month", "6")
            .with(csrf())
            .with(user(adminAccountUsername))
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
    void userShiftListSuccess() throws Exception
    {
        LocalDateTimeToString localDateTimeToString = new LocalDateTimeToString();
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
        ShiftListResponse generalShiftListResponse = new ShiftListResponse
        (
            generalShiftId,
            localDateTimeToString.localDateTimeToString(generalShiftBeginWork),
            localDateTimeToString.localDateTimeToString(generalShiftEndWork),
            localDateTimeToString.localDateTimeToString(generalShiftBeginBreak),
            localDateTimeToString.localDateTimeToString(generalShiftEndBreak),
            generalShiftLateness,
            generalShiftLeaveEarly,
            generalShiftOuting,
            generalShiftOverWork
        );
        List<Shift> generalShifts = new ArrayList<Shift>();
        generalShifts.add(generalShift);

        when(accountService.findAccountByUsername(anyString())).thenReturn(generalAccount);
        when(accountApproverService.findAccountAndApprover(anyLong(), any(Account.class))).thenReturn(accountApprover);
        when(shiftService.findByAccountIdAndBeginWorkBetween(anyLong(), anyInt(), anyInt())).thenReturn(generalShifts);
        when(shiftService.shiftToShiftListResponse(any(Shift.class))).thenReturn(generalShiftListResponse);
        mockMvc.perform(
            get("/api/reach/user/shiftinfo")
            .param("accountId", String.valueOf(adminAccountId))
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
    void userMonthWorkInfoSuccess() throws Exception
    {
        StringToLocalDateTime stringToLocalDateTime = new StringToLocalDateTime();
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

        when(accountService.findAccountByUsername(anyString())).thenReturn(adminAccount);
        when(accountApproverService.findAccountAndApprover(anyLong(), any(Account.class))).thenReturn(accountApprover);
        when(attendService.findByAccountIdAndBeginWorkBetween(any(Account.class), anyInt(), anyInt())).thenReturn(attends);
        when(vacationService.findByAccountIdAndBeginVacationBetweenMonthAndPaydHoliday(any(Account.class), anyInt(), anyInt())).thenReturn(vacations);
        mockMvc.perform
        (
            get("/api/reach/user/monthworkinfo")
            .param("accountId", "3")
            .param("year", "2025")
            .param("month", "9")
            .with(csrf())
            .with(user(adminAccountUsername))
        )
        .andExpect(status().isOk());
    }

    @Test
    void userRequestListSuccess() throws Exception
    {
        // LocalDateTimeToString localDateTimeToString = new LocalDateTimeToString();
        StringToLocalDateTime stringToLocalDateTime = new StringToLocalDateTime();
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

        AccountApprover generalAccountApprover = new AccountApprover();
        generalAccountApprover.setAccountId(generalAccount);
        generalAccountApprover.setApproverId(adminAccount);

        AccountApprover guestAccountApprover = new AccountApprover();
        guestAccountApprover.setAccountId(guestAccount);
        guestAccountApprover.setApproverId(adminAccount);

        List<AccountApprover> accountApprovers = new ArrayList<AccountApprover>();
        accountApprovers.add(generalAccountApprover);
        accountApprovers.add(guestAccountApprover);

        List<ShiftRequest> shiftRequests = new ArrayList<ShiftRequest>();
        ShiftRequest shiftRequest = new ShiftRequest();
        Long shiftRequestId = 43L;
        String shiftRequestRequestDate = "2025/10/01T20:11:00";
        int shiftRequestRequestStatus = 1;
        shiftRequest.setShiftRequestId(shiftRequestId);
        shiftRequest.setAccountId(generalAccount);
        shiftRequest.setRequestDate(stringToLocalDateTime.stringToLocalDateTime(shiftRequestRequestDate));
        shiftRequest.setRequestStatus(shiftRequestRequestStatus);
        shiftRequests.add(shiftRequest);

        List<ShiftChangeRequest> shiftChangeRequests = new ArrayList<ShiftChangeRequest>();
        ShiftChangeRequest shiftChangeRequest = new ShiftChangeRequest();
        Long shiftChangeRequestId = 44L;
        String shiftChangeRequestRequestDate = "2025/09/20T03:33:33";
        int shiftChangeRequestRequestStatus = 1;
        shiftChangeRequest.setShiftChangeId(shiftChangeRequestId);
        shiftChangeRequest.setAccountId(generalAccount);
        shiftChangeRequest.setRequestDate(stringToLocalDateTime.stringToLocalDateTime(shiftChangeRequestRequestDate));
        shiftChangeRequest.setRequestStatus(shiftChangeRequestRequestStatus);
        shiftChangeRequests.add(shiftChangeRequest);

        List<StampRequest> stampRequests = new ArrayList<StampRequest>();
        StampRequest stampRequest = new StampRequest();
        Long stampRequestId = 45L;
        String stampRequestRequestDate = "2025/10/10T10:10:10";
        int stampRequestRequestStatus = 1;
        stampRequest.setStampId(stampRequestId);
        stampRequest.setAccountId(guestAccount);
        stampRequest.setRequestDate(stringToLocalDateTime.stringToLocalDateTime(stampRequestRequestDate));
        stampRequest.setRequestStatus(stampRequestRequestStatus);
        stampRequests.add(stampRequest);

        List<VacationRequest> vacationRequests = new ArrayList<VacationRequest>();
        VacationRequest vacationRequest = new VacationRequest();
        Long vacationRequestId = 46L;
        String vacationRequestRequestDate = "2025/01/23T12:31:23";
        int vacationRequestRequestStatus = 1;
        vacationRequest.setVacationId(vacationRequestId);
        vacationRequest.setAccountId(guestAccount);
        vacationRequest.setRequestDate(stringToLocalDateTime.stringToLocalDateTime(vacationRequestRequestDate));
        vacationRequest.setRequestStatus(vacationRequestRequestStatus);
        vacationRequests.add(vacationRequest);

        List<OverTimeRequest> overTimeRequests = new ArrayList<OverTimeRequest>();
        OverTimeRequest overTimeRequest = new OverTimeRequest();
        Long overTimeRequestId = 47L;
        String overTimeRequestRequestDate = "2025/02/25T20:00:25";
        int overTimeRequestRequestStatus = 1;
        overTimeRequest.setOverTimeId(overTimeRequestId);
        overTimeRequest.setAccountId(guestAccount);
        overTimeRequest.setRequestDate(stringToLocalDateTime.stringToLocalDateTime(overTimeRequestRequestDate));
        overTimeRequest.setRequestStatus(overTimeRequestRequestStatus);
        overTimeRequests.add(overTimeRequest);

        List<AttendanceExceptionRequest> attendanceExceptionRequets = new ArrayList<AttendanceExceptionRequest>();
        AttendanceExceptionRequest attendanceExceptionRequest = new AttendanceExceptionRequest();
        Long attendanceExceptionRequestId = 48L;
        String attendanceExceptionRequestRequestDate = "2025/10/09T19:33:33";
        int attendanceExceptionRequestRequestStatus = 1;
        attendanceExceptionRequest.setAttendanceExceptionId(attendanceExceptionRequestId);
        attendanceExceptionRequest.setAccountId(guestAccount);
        attendanceExceptionRequest.setRequestDate(stringToLocalDateTime.stringToLocalDateTime(attendanceExceptionRequestRequestDate));
        attendanceExceptionRequest.setRequestStatus(attendanceExceptionRequestRequestStatus);
        attendanceExceptionRequets.add(attendanceExceptionRequest);

        List<MonthlyRequest> monthlyRequests = new ArrayList<MonthlyRequest>();
        MonthlyRequest monthlyRequest = new MonthlyRequest();
        Long monthlyRequestId = 49L;
        String monthlyRequestRequestDate = "2025/10/11T14:25:34";
        int monthlyRequestRequestStatus = 1;
        monthlyRequest.setMonthRequestId(monthlyRequestId);
        monthlyRequest.setAccountId(guestAccount);
        monthlyRequest.setRequestDate(stringToLocalDateTime.stringToLocalDateTime(monthlyRequestRequestDate));
        monthlyRequest.setRequestStatus(monthlyRequestRequestStatus);
        monthlyRequests.add(monthlyRequest);

        when(accountService.findAccountByUsername(anyString())).thenReturn(adminAccount);
        when(accountApproverService.findByApproverId(any(Account.class))).thenReturn(accountApprovers);
        when(shiftRequestService.findByAccountIdIn(anyList())).thenReturn(shiftRequests);
        when(shiftChangeRequestService.findByAccountIdIn(anyList())).thenReturn(shiftChangeRequests);
        when(stampRequestService.findByAccountIdIn(anyList())).thenReturn(stampRequests);
        when(vacationRequestService.findByAccountIdIn(anyList())).thenReturn(vacationRequests);
        when(overTimeRequestService.findByAccountIdIn(anyList())).thenReturn(overTimeRequests);
        when(attendanceExceptionRequestService.findByAccountIdIn(anyList())).thenReturn(attendanceExceptionRequets);
        when(monthlyRequestService.findByAccountIdIn(anyList())).thenReturn(monthlyRequests);
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
        StringToLocalDateTime stringToLocalDateTime = new StringToLocalDateTime();
        String generalBeginWork = "2026/09/02T09:00:00";
        String generalBeginBreak = "2026/09/02T12:00:00";
        String generalEndBreak = "2026/09/02T13:00:00";
        String generalEndWork = "2026/09/02T18:00:00";
        String json = String.format
        ("""
            {
                "beginWork": "%s",
                "beginBreak": "%s",
                "endBreak": "%s",
                "endWork": "%s"
            }
        """,
        generalBeginWork, generalBeginBreak, generalEndBreak, generalEndWork
        );

        LegalTime legalTime = new LegalTime();
        Long legalTimeId = 1L;
        LocalDateTime legalTimeBegin = stringToLocalDateTime.stringToLocalDateTime("2025/08/08T21:00:00");
        String legalTimeScheduleWorkTime = "08:00:00";
        String legalTimeWeeklyWorkTime = "40:00:00";
        String legalTimeMonthlyOverWork = "45:00:00";
        String legalTimeYearOverWork = "360:00:00";
        String legalTimeMaxOverWorkTime = "100:00:00";
        String legalTimeMonthlyOverWorkAverage = "80:00:00";
        String legalTimeLateNightWorkTimeBegin = "22:00:00";
        String legalTimeLateNightWorkTimeEnd = "05:00:00";
        String legalTimeScheduleBreakTime = "01:00:00";
        int legalTimeWeeklyHoliday = 1;

        legalTime.setLegalTimeId(legalTimeId);
        legalTime.setBegin(legalTimeBegin);
        legalTime.setScheduleWorkTime(legalTimeScheduleWorkTime);
        legalTime.setWeeklyWorkTime(legalTimeWeeklyWorkTime);
        legalTime.setMonthlyOverWorkTime(legalTimeMonthlyOverWork);
        legalTime.setYearOverWorkTime(legalTimeYearOverWork);
        legalTime.setMaxOverWorkTime(legalTimeMaxOverWorkTime);
        legalTime.setMonthlyOverWorkAverage(legalTimeMonthlyOverWorkAverage);
        legalTime.setLateNightWorkBegin(legalTimeLateNightWorkTimeBegin);
        legalTime.setLateNightWorkEnd(legalTimeLateNightWorkTimeEnd);
        legalTime.setScheduleBreakTime(legalTimeScheduleBreakTime);
        legalTime.setWeeklyHoliday(legalTimeWeeklyHoliday);

        Account generalAccount = new Account();
        Long generalAccountId = 1L;
        String generalAccountName = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setName(generalAccountName);

        Long shiftId = 20L;
        LocalDateTime BeginWorkTimeShift = stringToLocalDateTime.stringToLocalDateTime("2025/09/18T09:00:00");
        LocalDateTime EndWorkTimeShift = stringToLocalDateTime.stringToLocalDateTime("2025/09/18T18:00:00");
        LocalDateTime BeginBreakTimeShift = stringToLocalDateTime.stringToLocalDateTime("2025/09/18T12:00:00");
        LocalDateTime EndBreakTimeShift = stringToLocalDateTime.stringToLocalDateTime("2025/09/18T13:00:00");
    
        Long generalShiftId = 22L;
        LocalDateTime generalBeginWorkTimeShift = stringToLocalDateTime.stringToLocalDateTime("2025/09/17T09:00:00");
        LocalDateTime generalEndWorkTimeShift = stringToLocalDateTime.stringToLocalDateTime("2025/09/17T18:00:00");
        LocalDateTime generalBeginBreakTimeShift = stringToLocalDateTime.stringToLocalDateTime("2025/09/17T12:00:00");
        LocalDateTime generalEndBreakTimeShift = stringToLocalDateTime.stringToLocalDateTime("2025/09/17T13:00:00");

        List<Shift> generalShifts = new ArrayList<Shift>();
        Shift shift = new Shift();
        shift.setShiftId(shiftId);
        shift.setBeginWork(BeginWorkTimeShift);
        shift.setEndWork(EndWorkTimeShift);
        shift.setBeginBreak(BeginBreakTimeShift);
        shift.setEndBreak(EndBreakTimeShift);
        generalShifts.add(shift);

        Shift generalShift = new Shift();
        generalShift.setShiftId(generalShiftId);
        generalShift.setBeginWork(generalBeginWorkTimeShift);
        generalShift.setEndWork(generalEndWorkTimeShift);
        generalShift.setBeginBreak(generalBeginBreakTimeShift);
        generalShift.setEndBreak(generalEndBreakTimeShift);
        generalShifts.add(generalShift);

        ShiftRequest shiftRequest = new ShiftRequest();
        shiftRequest.setBeginWork(BeginWorkTimeShift);
        shiftRequest.setEndWork(EndWorkTimeShift);
        shiftRequest.setBeginBreak(BeginBreakTimeShift);
        shiftRequest.setEndBreak(EndBreakTimeShift);

        ShiftChangeRequest shiftChangeRequest = new ShiftChangeRequest();
        shiftChangeRequest.setShiftId(shift);
        shiftChangeRequest.setBeginWork(generalBeginWorkTimeShift);
        shiftChangeRequest.setEndWork(generalEndWorkTimeShift);
        shiftChangeRequest.setBeginBreak(generalBeginBreakTimeShift);
        shiftChangeRequest.setEndBreak(generalEndBreakTimeShift);

        List<ShiftListShiftRequest> shiftListShiftRequests = new ArrayList<ShiftListShiftRequest>();
        ShiftListShiftRequest shiftListShiftRequest = new ShiftListShiftRequest();
        shiftListShiftRequest.setShiftRequestId(shiftRequest);
        ShiftListShiftRequest generaShiftListShiftRequest = new ShiftListShiftRequest();
        generaShiftListShiftRequest.setShiftChangeRequestId(shiftChangeRequest);
        shiftListShiftRequests.add(shiftListShiftRequest);
        shiftListShiftRequests.add(generaShiftListShiftRequest);

        List<Vacation> vacations = new ArrayList<Vacation>();

        when(accountService.findAccountByUsername(anyString())).thenReturn(generalAccount);
        when(legalTimeService.findFirstByOrderByBeginDesc()).thenReturn(legalTime);
        when(shiftService.findByAccountIdAndBeginWorkBetweenWeek(any(Account.class), any(LocalDateTime.class))).thenReturn(generalShifts);
        when(shiftListShiftRequestService.findByShiftIdIn(anyList())).thenReturn(shiftListShiftRequests);
        when(vacationService.findByAccountIdAndBeginVacationBetweenWeek(any(Account.class), any(LocalDateTime.class))).thenReturn(vacations);
        mockMvc.perform
        (
            post("/api/send/legalcheck/shift")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .with(csrf())
            .with(user(generalAccountName))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(1));
    }

    @Test
    void legalCheckShiftChangeSuccess() throws Exception
    {
        StringToLocalDateTime stringToLocalDateTime = new StringToLocalDateTime();
        String generalBeginWork = "2026/09/02T09:00:00";
        String generalBeginBreak = "2026/09/02T12:00:00";
        String generalEndBreak = "2026/09/02T13:00:00";
        String generalEndWork = "2026/09/02T18:00:00";
        int generalId = 1;
        String json = String.format
        ("""
            {
                "beginWork": "%s",
                "beginBreak": "%s",
                "endBreak": "%s",
                "endWork": "%s",
                "shiftId": "%s"
            }
        """,
        generalBeginWork, generalBeginBreak, generalEndBreak, generalEndWork, generalId
        );

        LegalTime legalTime = new LegalTime();
        Long legalTimeId = 1L;
        LocalDateTime legalTimeBegin = LocalDateTime.parse(LocalDateTime.parse("2025/08/08T21:00:00",DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        String legalTimeScheduleWorkTime = "08:00:00";
        String legalTimeWeeklyWorkTime = "40:00:00";
        String legalTimeMonthlyOverWork = "45:00:00";
        String legalTimeYearOverWork = "360:00:00";
        String legalTimeMaxOverWorkTime = "100:00:00";
        String legalTimeMonthlyOverWorkAverage = "80:00:00";
        String legalTimeLateNightWorkTimeBegin = "22:00:00";
        String legalTimeLateNightWorkTimeEnd = "05:00:00";
        String legalTimeScheduleBreakTime = "01:00:00";
        int legalTimeWeeklyHoliday = 1;

        legalTime.setLegalTimeId(legalTimeId);
        legalTime.setBegin(legalTimeBegin);
        legalTime.setScheduleWorkTime(legalTimeScheduleWorkTime);
        legalTime.setWeeklyWorkTime(legalTimeWeeklyWorkTime);
        legalTime.setMonthlyOverWorkTime(legalTimeMonthlyOverWork);
        legalTime.setYearOverWorkTime(legalTimeYearOverWork);
        legalTime.setMaxOverWorkTime(legalTimeMaxOverWorkTime);
        legalTime.setMonthlyOverWorkAverage(legalTimeMonthlyOverWorkAverage);
        legalTime.setLateNightWorkBegin(legalTimeLateNightWorkTimeBegin);
        legalTime.setLateNightWorkEnd(legalTimeLateNightWorkTimeEnd);
        legalTime.setScheduleBreakTime(legalTimeScheduleBreakTime);
        legalTime.setWeeklyHoliday(legalTimeWeeklyHoliday);

        Account generalAccount = new Account();
        Long generalAccountId = 1L;
        String generalAccountName = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setName(generalAccountName);

        Shift shift = new Shift();
        Long shiftId = 1L;
        shift.setShiftId(shiftId);
        shift.setBeginWork(stringToLocalDateTime.stringToLocalDateTime(generalBeginWork));

        Long generalShiftId = 22L;
        LocalDateTime generalBeginWorkTimeShift = stringToLocalDateTime.stringToLocalDateTime("2025/09/17T09:00:00");
        LocalDateTime generalEndWorkTimeShift = stringToLocalDateTime.stringToLocalDateTime("2025/09/17T18:00:00");
        LocalDateTime generalBeginBreakTimeShift = stringToLocalDateTime.stringToLocalDateTime("2025/09/17T12:00:00");
        LocalDateTime generalEndBreakTimeShift = stringToLocalDateTime.stringToLocalDateTime("2025/09/17T13:00:00");

        Long adminShiftId = 20L;
        LocalDateTime adminBeginWorkTimeShift = stringToLocalDateTime.stringToLocalDateTime("2025/09/18T09:00:00");
        LocalDateTime adminEndWorkTimeShift = stringToLocalDateTime.stringToLocalDateTime("2025/09/18T18:00:00");
        LocalDateTime adminBeginBreakTimeShift = stringToLocalDateTime.stringToLocalDateTime("2025/09/18T12:00:00");
        LocalDateTime adminEndBreakTimeShift = stringToLocalDateTime.stringToLocalDateTime("2025/09/18T13:00:00");

        List<Shift> generalShifts = new ArrayList<Shift>();
        Shift adminShift = new Shift();
        adminShift.setShiftId(adminShiftId);
        adminShift.setBeginWork(adminBeginWorkTimeShift);
        adminShift.setEndWork(adminEndWorkTimeShift);
        adminShift.setBeginBreak(adminBeginBreakTimeShift);
        adminShift.setEndBreak(adminEndBreakTimeShift);
        Shift generalShift = new Shift();
        generalShift.setShiftId(generalShiftId);
        generalShift.setBeginWork(generalBeginWorkTimeShift);
        generalShift.setEndWork(generalEndWorkTimeShift);
        generalShift.setBeginBreak(generalBeginBreakTimeShift);
        generalShift.setEndBreak(generalEndBreakTimeShift);
        generalShifts.add(adminShift);
        generalShifts.add(generalShift);

        ShiftRequest shiftRequest = new ShiftRequest();
        shiftRequest.setBeginWork(adminBeginWorkTimeShift);
        shiftRequest.setEndWork(adminEndWorkTimeShift);
        shiftRequest.setBeginBreak(adminBeginBreakTimeShift);
        shiftRequest.setEndBreak(adminEndBreakTimeShift);

        ShiftChangeRequest shiftChangeRequest = new ShiftChangeRequest();
        shiftChangeRequest.setShiftId(shift);
        shiftChangeRequest.setBeginWork(generalBeginWorkTimeShift);
        shiftChangeRequest.setEndWork(generalEndWorkTimeShift);
        shiftChangeRequest.setBeginBreak(generalBeginBreakTimeShift);
        shiftChangeRequest.setEndBreak(generalEndBreakTimeShift);

        List<ShiftListShiftRequest> shiftListShiftRequests = new ArrayList<ShiftListShiftRequest>();
        ShiftListShiftRequest shiftListShiftRequest = new ShiftListShiftRequest();
        shiftListShiftRequest.setShiftRequestId(shiftRequest);
        ShiftListShiftRequest generalShiftListShiftRequest = new ShiftListShiftRequest();
        generalShiftListShiftRequest.setShiftChangeRequestId(shiftChangeRequest);
        shiftListShiftRequests.add(shiftListShiftRequest);
        shiftListShiftRequests.add(generalShiftListShiftRequest);

        List<Vacation> vacations = new ArrayList<Vacation>();

        when(accountService.findAccountByUsername(anyString())).thenReturn(generalAccount);
        when(shiftService.findByAccountIdAndShiftId(any(Account.class), anyLong())).thenReturn(shift);
        when(legalTimeService.findFirstByOrderByBeginDesc()).thenReturn(legalTime);
        when(shiftService.findByAccountIdAndBeginWorkBetweenWeek(any(Account.class), any(LocalDateTime.class))).thenReturn(generalShifts);
        when(shiftListShiftRequestService.findByShiftIdIn(anyList())).thenReturn(shiftListShiftRequests);
        when(vacationService.findByAccountIdAndBeginVacationBetweenWeek(any(Account.class), any(LocalDateTime.class))).thenReturn(vacations);
        mockMvc.perform
        (
            post("/api/send/legalcheck/shiftchange")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
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
        StringToLocalDateTime stringToLocalDateTime = new StringToLocalDateTime();
        Account generalAccount = new Account();
        Long generalAccountId = 3L;
        String generalAccountUsername = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        Shift generalShift = new Shift();
        Long generalShiftId = 5L;
        String generalShiftBeginWork = "2025/12/23T09:00:00";
        String generalShiftEndWork = "2025/12/23T18:00:00";
        generalShift.setShiftId(generalShiftId);
        generalShift.setBeginWork(stringToLocalDateTime.stringToLocalDateTime(generalShiftBeginWork));
        generalShift.setEndWork(stringToLocalDateTime.stringToLocalDateTime(generalShiftEndWork));

        List<ShiftListOverTime> shiftListOverTimes = new ArrayList<ShiftListOverTime>();
        ShiftListOverTime shiftListOverTime = new ShiftListOverTime();
        shiftListOverTimes.add(shiftListOverTime);

        ShiftListShiftRequest shiftListShiftRequest = new ShiftListShiftRequest();
        ShiftRequest shiftRequest = new ShiftRequest();
        Long shiftRequestId = 51L;
        String shiftRequestBegin = "2025/12/23T09:00:00";
        String shiftRequestEnd = "2025/12/23T18:00:00";
        shiftRequest.setShiftRequestId(shiftRequestId);
        shiftRequest.setBeginWork(stringToLocalDateTime.stringToLocalDateTime(shiftRequestBegin));
        shiftRequest.setEndWork(stringToLocalDateTime.stringToLocalDateTime(shiftRequestEnd));
        shiftListShiftRequest.setShiftRequestId(shiftRequest);

        List<AttendanceExceptionRequest> attendanceExceptionRequests = new ArrayList<AttendanceExceptionRequest>();
        AttendanceExceptionRequest attendanceExceptionRequest = new AttendanceExceptionRequest();
        Long attendanceExceptionRequestId = 34L;
        attendanceExceptionRequest.setAttendanceExceptionId(attendanceExceptionRequestId);

        when(accountService.findAccountByUsername(anyString())).thenReturn(generalAccount);
        when(shiftService.findByAccountIdAndShiftId(any(Account.class), anyLong())).thenReturn(generalShift);
        when(shiftListOverTimeService.findByShiftId(any(Shift.class))).thenReturn(shiftListOverTimes);
        when(shiftListShiftRequestService.findByShiftId(any(Shift.class))).thenReturn(shiftListShiftRequest);
        when(attendanceExceptionRequestService.findByAccountIdAndShiftIdAndOutingAndBeginTimeBetweenOrEndTimeBetweenAndRequestStatusWaitOrRequestStatusApproved(any(Account.class), any(Shift.class), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(attendanceExceptionRequests);
        when(attendanceExceptionRequestService.save(any(AttendanceExceptionRequest.class))).thenReturn(attendanceExceptionRequest);
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
        StringToLocalDateTime stringToLocalDateTime = new StringToLocalDateTime();
        Account generalAccount = new Account();
        Long generalAccountId = 3L;
        String generalAccountUsername = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        Shift generalShift = new Shift();
        Long generalShiftId = 5L;
        String generalShiftBeginWork = "2025/12/23T09:00:00";
        String generalShiftEndWork = "2025/12/23T18:00:00";
        generalShift.setShiftId(generalShiftId);
        generalShift.setBeginWork(stringToLocalDateTime.stringToLocalDateTime(generalShiftBeginWork));
        generalShift.setEndWork(stringToLocalDateTime.stringToLocalDateTime(generalShiftEndWork));

        List<ShiftListOverTime> shiftListOverTimes = new ArrayList<ShiftListOverTime>();
        ShiftListOverTime shiftListOverTime = new ShiftListOverTime();
        Long shiftListOverTimeId = 3L;
        shiftListOverTime.setShiftListOverTimeId(shiftListOverTimeId);
        shiftListOverTimes.add(shiftListOverTime);

        OverTimeRequest overTimeRequest = new OverTimeRequest();
        String shiftListOverTimeBeginOverTime = "2025/12/23T18:00:00";
        String shiftListOverTimeEndOverTime = "2025/12/23T19:00:00";
        overTimeRequest.setBeginWork(stringToLocalDateTime.stringToLocalDateTime(shiftListOverTimeBeginOverTime));
        overTimeRequest.setEndWork(stringToLocalDateTime.stringToLocalDateTime(shiftListOverTimeEndOverTime));

        ShiftListShiftRequest shiftListShiftRequest = new ShiftListShiftRequest();
        ShiftChangeRequest shiftChangeRequest = new ShiftChangeRequest();
        Long shiftChangeRequestId = 51L;
        String shiftChangeRequestBegin = "2025/12/23T09:00:00";
        String shiftChangeRequestEnd = "2025/12/23T18:00:00";
        shiftChangeRequest.setShiftChangeId(shiftChangeRequestId);
        shiftChangeRequest.setBeginWork(stringToLocalDateTime.stringToLocalDateTime(shiftChangeRequestBegin));
        shiftChangeRequest.setEndWork(stringToLocalDateTime.stringToLocalDateTime(shiftChangeRequestEnd));
        shiftListShiftRequest.setShiftChangeRequestId(shiftChangeRequest);

        AttendanceExceptionRequest attendanceExceptionRequest = new AttendanceExceptionRequest();
        Long attendanceExceptionRequestId = 34L;
        attendanceExceptionRequest.setAttendanceExceptionId(attendanceExceptionRequestId);

        when(accountService.findAccountByUsername(anyString())).thenReturn(generalAccount);
        when(shiftService.findByAccountIdAndShiftId(any(Account.class), anyLong())).thenReturn(generalShift);
        when(shiftListOverTimeService.findByShiftId(any(Shift.class))).thenReturn(shiftListOverTimes);
        when(shiftListShiftRequestService.findByShiftId(any(Shift.class))).thenReturn(shiftListShiftRequest);
        when(attendanceExceptionRequestService.save(any(AttendanceExceptionRequest.class))).thenReturn(attendanceExceptionRequest);
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
        int requestOtherType = 1;
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
        StringToLocalDateTime stringToLocalDateTime = new StringToLocalDateTime();
        Account generalAccount = new Account();
        Long generalAccountId = 3L;
        String generalAccountUsername = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        Shift generalShift = new Shift();
        Long generalShiftId = 5L;
        String generalShiftBeginWork = "2025/12/23T09:00:00";
        String generalShiftEndWork = "2025/12/23T18:00:00";
        generalShift.setShiftId(generalShiftId);
        generalShift.setBeginWork(stringToLocalDateTime.stringToLocalDateTime(generalShiftBeginWork));
        generalShift.setEndWork(stringToLocalDateTime.stringToLocalDateTime(generalShiftEndWork));

        List<ShiftListOverTime> shiftListOverTimes = new ArrayList<ShiftListOverTime>();
        ShiftListOverTime shiftListOverTime = new ShiftListOverTime();
        Long shiftListOverTimeId = 3L;
        shiftListOverTime.setShiftListOverTimeId(shiftListOverTimeId);
        shiftListOverTimes.add(shiftListOverTime);

        OverTimeRequest overTimeRequest = new OverTimeRequest();
        String shiftListOverTimeBeginOverTime = "2025/12/23T08:00:00";
        String shiftListOverTimeEndOverTime = "2025/12/23T09:00:00";
        overTimeRequest.setBeginWork(stringToLocalDateTime.stringToLocalDateTime(shiftListOverTimeBeginOverTime));
        overTimeRequest.setEndWork(stringToLocalDateTime.stringToLocalDateTime(shiftListOverTimeEndOverTime));

        ShiftListShiftRequest shiftListShiftRequest = new ShiftListShiftRequest();
        ShiftChangeRequest shiftChangeRequest = new ShiftChangeRequest();
        Long shiftChangeRequestId = 51L;
        String shiftChangeRequestBegin = "2025/12/23T09:00:00";
        String shiftChangeRequestEnd = "2025/12/23T18:00:00";
        shiftChangeRequest.setShiftChangeId(shiftChangeRequestId);
        shiftChangeRequest.setBeginWork(stringToLocalDateTime.stringToLocalDateTime(shiftChangeRequestBegin));
        shiftChangeRequest.setEndWork(stringToLocalDateTime.stringToLocalDateTime(shiftChangeRequestEnd));
        shiftListShiftRequest.setShiftChangeRequestId(shiftChangeRequest);

        AttendanceExceptionRequest attendanceExceptionRequest = new AttendanceExceptionRequest();
        Long attendanceExceptionRequestId = 43L;
        attendanceExceptionRequest.setAttendanceExceptionId(attendanceExceptionRequestId);

        when(accountService.findAccountByUsername(anyString())).thenReturn(generalAccount);
        when(shiftService.findByAccountIdAndShiftId(any(Account.class), anyLong())).thenReturn(generalShift);
        when(shiftListOverTimeService.findByShiftId(any(Shift.class))).thenReturn(shiftListOverTimes);
        when(shiftListShiftRequestService.findByShiftId(any(Shift.class))).thenReturn(shiftListShiftRequest);
        when(attendanceExceptionRequestService.save(any(AttendanceExceptionRequest.class))).thenReturn(attendanceExceptionRequest);
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

        StringToLocalDateTime stringToLocalDateTime = new StringToLocalDateTime();
        Account generalAccount = new Account();
        Long generalAccountId = 3L;
        String generalAccountUsername = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        Shift shift = new Shift();
        Long shiftId = 35L;
        String shiftBegin = "2025/12/23T09:00:00";
        String shiftEnd = "2025/12/23T18:00:00";
        shift.setShiftId(shiftId);
        shift.setBeginWork(stringToLocalDateTime.stringToLocalDateTime(shiftBegin));
        shift.setEndWork(stringToLocalDateTime.stringToLocalDateTime(shiftEnd));

        List<OverTimeRequest> overTimeRequests = new ArrayList<OverTimeRequest>();

        List<Shift> shifts = new ArrayList<Shift>();

        List<ShiftListOtherTime> shiftListOtherTimes = new ArrayList<ShiftListOtherTime>();

        List<OverTimeRequest> overTimeRequestMonth = new ArrayList<OverTimeRequest>();
        // 45時間ちょうどでエラーを起こさないかチェックするため10時間の残業を用意する
        OverTimeRequest firstOverTimeRequest = new OverTimeRequest();
        String firstOverTimeRequestBegin = "2025/12/10T00:00:00";
        String firstOverTimeRequestEnd = "2025/12/10T10:00:00";
        firstOverTimeRequest.setBeginWork(stringToLocalDateTime.stringToLocalDateTime(firstOverTimeRequestBegin));
        firstOverTimeRequest.setEndWork(stringToLocalDateTime.stringToLocalDateTime(firstOverTimeRequestEnd));

        OverTimeRequest secondOverTimeRequest = new OverTimeRequest();
        String secondOverTimeRequestBegin = "2025/12/11T00:00:00";
        String secondOverTimeRequestEnd = "2025/12/11T10:00:00";
        secondOverTimeRequest.setBeginWork(stringToLocalDateTime.stringToLocalDateTime(secondOverTimeRequestBegin));
        secondOverTimeRequest.setEndWork(stringToLocalDateTime.stringToLocalDateTime(secondOverTimeRequestEnd));

        OverTimeRequest thirdOverTimeRequest = new OverTimeRequest();
        String thirdOverTimeRequestBegin = "2025/12/12T00:00:00";
        String thirdOverTimeRequestEnd = "2025/12/12T10:00:00";
        thirdOverTimeRequest.setBeginWork(stringToLocalDateTime.stringToLocalDateTime(thirdOverTimeRequestBegin));
        thirdOverTimeRequest.setEndWork(stringToLocalDateTime.stringToLocalDateTime(thirdOverTimeRequestEnd));

        OverTimeRequest fourthOverTimeRequest = new OverTimeRequest();
        String fourthOverTimeRequestBegin = "2025/12/13T00:00:00";
        String fourthOverTimeRequestEnd = "2025/12/13T10:00:00";
        fourthOverTimeRequest.setBeginWork(stringToLocalDateTime.stringToLocalDateTime(fourthOverTimeRequestBegin));
        fourthOverTimeRequest.setEndWork(stringToLocalDateTime.stringToLocalDateTime(fourthOverTimeRequestEnd));

        OverTimeRequest fifthOverTimeRequest = new OverTimeRequest();
        String fifthOverTimeRequestBegin = "2025/12/14T06:00:00";
        String fifthOverTimeRequestEnd = "2025/12/14T09:00:00";
        fifthOverTimeRequest.setBeginWork(stringToLocalDateTime.stringToLocalDateTime(fifthOverTimeRequestBegin));
        fifthOverTimeRequest.setEndWork(stringToLocalDateTime.stringToLocalDateTime(fifthOverTimeRequestEnd));

        overTimeRequestMonth.add(firstOverTimeRequest);
        overTimeRequestMonth.add(secondOverTimeRequest);
        overTimeRequestMonth.add(thirdOverTimeRequest);
        overTimeRequestMonth.add(fourthOverTimeRequest);
        overTimeRequestMonth.add(fifthOverTimeRequest);

        OverTimeRequest newOverTimeRequest = new OverTimeRequest();
        Long newOverTimeRequestId = 43L;
        newOverTimeRequest.setOverTimeId(newOverTimeRequestId);

        LegalTime legalTime = new LegalTime();
        String monthlyOverTime = "45:00:00";
        legalTime.setMonthlyOverWorkTime(monthlyOverTime);
        
        when(accountService.findAccountByUsername(anyString())).thenReturn(generalAccount);
        when(shiftService.findByAccountIdAndShiftId(any(Account.class), anyLong())).thenReturn(shift);
        when(overTimeRequestService.findByAccounIdAndRequestStatusWaitOrApprovedAndBeginWorkOrEndWorkBetween(any(Account.class), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(overTimeRequests);
        when(shiftService.shiftOverLapping(any(Account.class), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(shifts);
        when(shiftListOtherTimeService.findByShiftId(any(Shift.class))).thenReturn(shiftListOtherTimes);
        when(overTimeRequestService.findByAccountIdAndRequestStatusWaitOrApprovedAndBeginWorkBetweenMonth(any(Account.class), anyInt(), anyInt())).thenReturn(overTimeRequestMonth);
        when(legalTimeService.findFirstByOrderByBeginDesc()).thenReturn(legalTime);
        when(overTimeRequestService.save(any(OverTimeRequest.class))).thenReturn(newOverTimeRequest);
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

        StringToLocalDateTime stringToLocalDateTime = new StringToLocalDateTime();
        Account generalAccount = new Account();
        Long generalAccountId = 3L;
        String generalAccountUsername = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        Shift shift = new Shift();
        Long shiftId = 35L;
        String shiftBegin = "2025/12/23T09:00:00";
        String shiftEnd = "2025/12/23T18:00:00";
        shift.setShiftId(shiftId);
        shift.setBeginWork(stringToLocalDateTime.stringToLocalDateTime(shiftBegin));
        shift.setEndWork(stringToLocalDateTime.stringToLocalDateTime(shiftEnd));

        List<OverTimeRequest> overTimeRequests = new ArrayList<OverTimeRequest>();

        List<Shift> shifts = new ArrayList<Shift>();

        List<ShiftListOtherTime> shiftListOtherTimes = new ArrayList<ShiftListOtherTime>();

        List<OverTimeRequest> overTimeRequestMonth = new ArrayList<OverTimeRequest>();
        // 45時間ちょうどでエラーを起こさないかチェックするため10時間の残業を用意する
        OverTimeRequest firstOverTimeRequest = new OverTimeRequest();
        String firstOverTimeRequestBegin = "2025/12/10T00:00:00";
        String firstOverTimeRequestEnd = "2025/12/10T10:00:00";
        firstOverTimeRequest.setBeginWork(stringToLocalDateTime.stringToLocalDateTime(firstOverTimeRequestBegin));
        firstOverTimeRequest.setEndWork(stringToLocalDateTime.stringToLocalDateTime(firstOverTimeRequestEnd));

        OverTimeRequest secondOverTimeRequest = new OverTimeRequest();
        String secondOverTimeRequestBegin = "2025/12/11T00:00:00";
        String secondOverTimeRequestEnd = "2025/12/11T10:00:00";
        secondOverTimeRequest.setBeginWork(stringToLocalDateTime.stringToLocalDateTime(secondOverTimeRequestBegin));
        secondOverTimeRequest.setEndWork(stringToLocalDateTime.stringToLocalDateTime(secondOverTimeRequestEnd));

        OverTimeRequest thirdOverTimeRequest = new OverTimeRequest();
        String thirdOverTimeRequestBegin = "2025/12/12T00:00:00";
        String thirdOverTimeRequestEnd = "2025/12/12T10:00:00";
        thirdOverTimeRequest.setBeginWork(stringToLocalDateTime.stringToLocalDateTime(thirdOverTimeRequestBegin));
        thirdOverTimeRequest.setEndWork(stringToLocalDateTime.stringToLocalDateTime(thirdOverTimeRequestEnd));

        OverTimeRequest fourthOverTimeRequest = new OverTimeRequest();
        String fourthOverTimeRequestBegin = "2025/12/13T00:00:00";
        String fourthOverTimeRequestEnd = "2025/12/13T10:00:00";
        fourthOverTimeRequest.setBeginWork(stringToLocalDateTime.stringToLocalDateTime(fourthOverTimeRequestBegin));
        fourthOverTimeRequest.setEndWork(stringToLocalDateTime.stringToLocalDateTime(fourthOverTimeRequestEnd));

        OverTimeRequest fifthOverTimeRequest = new OverTimeRequest();
        String fifthOverTimeRequestBegin = "2025/12/14T06:00:00";
        String fifthOverTimeRequestEnd = "2025/12/14T09:00:00";
        fifthOverTimeRequest.setBeginWork(stringToLocalDateTime.stringToLocalDateTime(fifthOverTimeRequestBegin));
        fifthOverTimeRequest.setEndWork(stringToLocalDateTime.stringToLocalDateTime(fifthOverTimeRequestEnd));

        overTimeRequestMonth.add(firstOverTimeRequest);
        overTimeRequestMonth.add(secondOverTimeRequest);
        overTimeRequestMonth.add(thirdOverTimeRequest);
        overTimeRequestMonth.add(fourthOverTimeRequest);
        overTimeRequestMonth.add(fifthOverTimeRequest);

        LegalTime legalTime = new LegalTime();
        String monthlyOverTime = "45:00:00";
        legalTime.setMonthlyOverWorkTime(monthlyOverTime);

        OverTimeRequest newOverTimeRequest = new OverTimeRequest();
        Long newOverTimeRequestId = 34L;
        newOverTimeRequest.setOverTimeId(newOverTimeRequestId);
        
        when(accountService.findAccountByUsername(anyString())).thenReturn(generalAccount);
        when(shiftService.findByAccountIdAndShiftId(any(Account.class), anyLong())).thenReturn(shift);
        when(overTimeRequestService.findByAccounIdAndRequestStatusWaitOrApprovedAndBeginWorkOrEndWorkBetween(any(Account.class), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(overTimeRequests);
        when(shiftService.shiftOverLapping(any(Account.class), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(shifts);
        when(shiftListOtherTimeService.findByShiftId(any(Shift.class))).thenReturn(shiftListOtherTimes);
        when(overTimeRequestService.findByAccountIdAndRequestStatusWaitOrApprovedAndBeginWorkBetweenMonth(any(Account.class), anyInt(), anyInt())).thenReturn(overTimeRequestMonth);
        when(legalTimeService.findFirstByOrderByBeginDesc()).thenReturn(legalTime);
        when(overTimeRequestService.save(any(OverTimeRequest.class))).thenReturn(newOverTimeRequest);
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
        StringToLocalDateTime stringToLocalDateTime = new StringToLocalDateTime();
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
        newsList.setDate(stringToLocalDateTime.stringToLocalDateTime(newsDate));
        newsList.setNewsDetil(newsDetil);
        newsLists.add(newsList);

        NewsListResponse newsListResponse = new NewsListResponse();
        newsListResponse.setDate(newsDate);
        newsListResponse.setMessageDetil(newsDetil);

        when(accountService.findAccountByUsername(anyString())).thenReturn(generalAccount);
        when(newsListService.findByAccountId(any(Account.class))).thenReturn(newsLists);
        when(newsListService.newsListToNewsListResponse(any(NewsList.class))).thenReturn(newsListResponse);
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

        List<NewsList> newsLists = new ArrayList<NewsList>();

        List<ShiftRequest> shiftRequests = new ArrayList<ShiftRequest>();

        List<ShiftChangeRequest> shiftChangeRequests = new ArrayList<ShiftChangeRequest>();

        List<StampRequest> stampRequests = new ArrayList<StampRequest>();

        List<AttendanceExceptionRequest> attendanceExceptionRequests = new ArrayList<AttendanceExceptionRequest>();

        List<OverTimeRequest> overTimeRequests = new ArrayList<OverTimeRequest>();

        List<VacationRequest> vacationRequests = new ArrayList<VacationRequest>();

        List<Shift> shifts = new ArrayList<Shift>();
        Shift shift = new Shift();
        shifts.add(shift);

        List<ShiftListShiftRequest> shiftListShiftRequests = new ArrayList<ShiftListShiftRequest>();

        ShiftListShiftRequest shiftListShiftRequest = new ShiftListShiftRequest();
        ShiftRequest shiftRequest = new ShiftRequest();
        shiftListShiftRequest.setShiftRequestId(shiftRequest);

        ShiftListShiftRequest generalShiftListShiftRequest = new ShiftListShiftRequest();
        ShiftChangeRequest shiftChangeRequest = new ShiftChangeRequest();
        generalShiftListShiftRequest.setShiftChangeRequestId(shiftChangeRequest);

        shiftListShiftRequests.add(shiftListShiftRequest);
        shiftListShiftRequests.add(generalShiftListShiftRequest);

        List<Attend> attends = new ArrayList<Attend>();
        Attend attend = new Attend();
        String attendWorkTime = "02:00:00";
        String generalAttendTime = "00:00:00";
        Long attendId = 3L;
        attend.setAttendanceId(attendId);
        attend.setWorkTime(Time.valueOf(attendWorkTime));
        attend.setOverWork(Time.valueOf(generalAttendTime));
        attend.setLateNightWork(Time.valueOf(generalAttendTime));
        attend.setLateness(Time.valueOf(generalAttendTime));
        attend.setLeaveEarly(Time.valueOf(generalAttendTime));
        attend.setOuting(Time.valueOf(generalAttendTime));
        attend.setAbsenceTime(Time.valueOf(generalAttendTime));
        attend.setHolidayWork(Time.valueOf(generalAttendTime));
        attend.setVacationTime(Time.valueOf(generalAttendTime));
        attends.add(attend);

        List<AttendanceListSource> attendanceListSources = new ArrayList<AttendanceListSource>();
        AttendanceListSource attendanceListSource = new AttendanceListSource();
        StampRequest stampRequest = new StampRequest();
        attendanceListSource.setStampRequestId(stampRequest);
        attendanceListSources.add(attendanceListSource);

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

        List<ShiftListVacation> shiftListVacations = new ArrayList<ShiftListVacation>();
        ShiftListVacation shiftListVacation = new ShiftListVacation();
        VacationRequest vacationRequest = new VacationRequest();
        shiftListVacation.setVacationId(vacationRequest);
        shiftListVacations.add(shiftListVacation);

        List<Vacation> vacations = new ArrayList<Vacation>();

        MonthlyRequest monthlyRequest = new MonthlyRequest();
        Long monthlyRequestId = 34L;
        monthlyRequest.setMonthRequestId(monthlyRequestId);

        when(accountService.findAccountByUsername(anyString())).thenReturn(generalAccount);
        when(newsListService.findByAccountIdAndDateBetweenMonthly(any(Account.class), anyInt(), anyInt())).thenReturn(newsLists);
        when(shiftRequestService.findByAccountIdAndBeginWorkBetweenAndRequestStatusWait(any(Account.class), anyInt(), anyInt())).thenReturn(shiftRequests);
        when(shiftChangeRequestService.findByAccountIdAndBeginWorkBetweenAndRequestStatusWait(any(Account.class), anyInt(), anyInt())).thenReturn(shiftChangeRequests);
        when(stampRequestService.findByAccountIdAndBeginWorkBetweenAndRequestStatusWait(any(Account.class), anyInt(), anyInt())).thenReturn(stampRequests);
        when(attendanceExceptionRequestService.findByAccountIdAndBeginTimeBetweenAndRequestStatusWait(any(Account.class), anyInt(), anyInt())).thenReturn(attendanceExceptionRequests);
        when(overTimeRequestService.findByAccountIdAndBeginWorkAndRequstStatusWait(any(Account.class), anyInt(), anyInt())).thenReturn(overTimeRequests);
        when(vacationRequestService.findByAccountIdAndBeginVacationBetweenAndRequestStatusWait(any(Account.class), anyInt(), anyInt())).thenReturn(vacationRequests);
        when(shiftService.findByAccountIdAndBeginWorkBetween(any(Account.class), anyInt(), anyInt())).thenReturn(shifts);
        when(shiftListOtherTimeService.findByShiftIdIn(anyList())).thenReturn(shiftListOtherTimes);
        when(shiftListOverTimeService.findByShiftIdIn(anyList())).thenReturn(shiftListOverTimes);
        when(shiftListShiftRequestService.findByShiftIdIn(anyList())).thenReturn(shiftListShiftRequests);
        when(shiftListVacationService.findByShiftIdIn(anyList())).thenReturn(shiftListVacations);
        when(attendService.findByAccountIdAndBeginWorkBetween(any(Account.class), anyInt(), anyInt())).thenReturn(attends);
        when(attendanceListSourceService.findByAttendIdIn(anyList())).thenReturn(attendanceListSources);
        when(vacationService.findByAccountIdAndBeginVacationBetweenMonthAndPaydHoliday(any(Account.class), anyInt(), anyInt())).thenReturn(vacations);
        when(shiftRequestService.save(any(ShiftRequest.class))).thenReturn(shiftRequest);
        when(shiftChangeRequestService.save(any(ShiftChangeRequest.class))).thenReturn(shiftChangeRequest);
        when(stampRequestService.save(any(StampRequest.class))).thenReturn(stampRequest);
        when(vacationRequestService.save(any(VacationRequest.class))).thenReturn(vacationRequest);
        when(attendanceExceptionRequestService.save(any(AttendanceExceptionRequest.class))).thenReturn(attendanceExceptionRequest);
        when(overTimeRequestService.save(any(OverTimeRequest.class))).thenReturn(overTimeRequest);
        when(monthlyRequestService.save(any(MonthlyRequest.class))).thenReturn(monthlyRequest);
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

        when(accountService.findAccountByUsername(generalAccountUsername)).thenReturn(generalAccount);
        when(shiftRequestService.findByAccountIdAndShiftRequestId(any(Account.class), anyLong())).thenReturn(shiftRequest);
        when(shiftRequestService.save(any(ShiftRequest.class))).thenReturn(shiftRequest);
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
    void withdrowShiftChaneSuccess() throws Exception
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

        when(accountService.findAccountByUsername(generalAccountUsername)).thenReturn(generalAccount);
        when(shiftChangeRequestService.findByAccountIdAndShiftChangeRequestId(any(Account.class), anyLong())).thenReturn(shiftChangeRequest);
        when(shiftChangeRequestService.save(any(ShiftChangeRequest.class))).thenReturn(shiftChangeRequest);
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

        when(accountService.findAccountByUsername(generalAccountUsername)).thenReturn(generalAccount);
        when(stampRequestService.findByAccountIdAndStampId(any(Account.class), anyLong())).thenReturn(stampRequest);
        when(stampRequestService.save(any(StampRequest.class))).thenReturn(stampRequest);
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

        when(accountService.findAccountByUsername(generalAccountUsername)).thenReturn(generalAccount);
        when(vacationRequestService.findByAccountIdAndVacationId(any(Account.class), anyLong())).thenReturn(vacationRequest);
        when(vacationRequestService.save(any(VacationRequest.class))).thenReturn(vacationRequest);
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

        when(accountService.findAccountByUsername(generalAccountUsername)).thenReturn(generalAccount);
        when(overTimeRequestService.findByAccountIdAndOverTimeRequestId(any(Account.class), anyLong())).thenReturn(overTimeRequest);
        when(overTimeRequestService.save(any(OverTimeRequest.class))).thenReturn(overTimeRequest);
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

        when(accountService.findAccountByUsername(anyString())).thenReturn(generalAccount);
        when(attendanceExceptionRequestService.findByAccountIdAndAttendanceExceptionId(any(Account.class), anyLong())).thenReturn(attendanceExceptionRequest);
        when(attendanceExceptionRequestService.save(any(AttendanceExceptionRequest.class))).thenReturn(attendanceExceptionRequest);
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

        when(accountService.findAccountByUsername(generalAccountUsername)).thenReturn(generalAccount);
        when(monthlyRequestService.findByAccountIdAndMothlyRequestId(any(Account.class), anyLong())).thenReturn(monthlyRequest);
        when(shiftService.findByAccountIdAndBeginWorkBetween(any(Account.class), anyInt(), anyInt())).thenReturn(shifts);
        when(shiftListOtherTimeService.findByShiftIdIn(anyList())).thenReturn(shiftListOtherTimes);
        when(shiftListOverTimeService.findByShiftIdIn(anyList())).thenReturn(shiftListOverTimes);
        when(shiftListShiftRequestService.findByShiftIdIn(anyList())).thenReturn(shiftListShiftRequests);
        when(shiftListVacationService.findByShiftIdIn(anyList())).thenReturn(shiftListVacations);
        when(attendanceExceptionRequestService.save(any(AttendanceExceptionRequest.class))).thenReturn(attendanceExceptionRequest);
        when(overTimeRequestService.save(any(OverTimeRequest.class))).thenReturn(overTimeRequest);
        when(shiftRequestService.save(any(ShiftRequest.class))).thenReturn(shiftRequest);
        when(shiftChangeRequestService.save(any(ShiftChangeRequest.class))).thenReturn(shiftChangeRequest);
        when(stampRequestService.save(any(StampRequest.class))).thenReturn(stampRequest);
        when(vacationRequestService.save(any(VacationRequest.class))).thenReturn(vacationRequest);
        when(monthlyRequestService.save(any(MonthlyRequest.class))).thenReturn(monthlyRequest);
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
}