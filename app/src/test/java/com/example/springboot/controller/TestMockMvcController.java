package com.example.springboot.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import java.security.MessageDigest;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
// import java.util.stream.Collectors;
import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.springboot.Config;
import com.example.springboot.dto.AllStyleListResponse;
import com.example.springboot.dto.change.LocalDateTimeToString;
import com.example.springboot.dto.response.ApproverListResponse;
import com.example.springboot.dto.response.ShiftListResponse;
import com.example.springboot.model.Account;
import com.example.springboot.model.AccountApprover;
import com.example.springboot.model.Role;
import com.example.springboot.model.Salt;
import com.example.springboot.model.Shift;
import com.example.springboot.model.ShiftChangeRequest;
import com.example.springboot.model.ShiftRequest;
import com.example.springboot.model.Style;
import com.example.springboot.model.StylePlace;
import com.example.springboot.model.ApprovalSetting;
import com.example.springboot.model.Department;
import com.example.springboot.model.LegalTime;
import com.example.springboot.service.AccountApproverService;
import com.example.springboot.service.AccountService;
import com.example.springboot.service.ApprovalSettingService;
import com.example.springboot.service.DepartmentService;
import com.example.springboot.service.LegalTimeService;
import com.example.springboot.service.RoleService;
import com.example.springboot.service.ShiftChangeRequestService;
import com.example.springboot.service.ShiftRequestService;
import com.example.springboot.service.ShiftService;
import com.example.springboot.service.StylePlaceService;
import com.example.springboot.service.StyleService;

@ContextConfiguration(classes = Config.class)
@WebMvcTest({PostController.class,GetController.class})
@AutoConfigureMockMvc
@Import({PostController.class,GetController.class})
public class TestMockMvcController
{
    @Autowired
    private MockMvc mockMvc;

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

    @Test
    void loginSuccess() throws Exception
    {
        Salt salt = new Salt();
        salt.setId(1L);
        salt.setText("somesalt");

        Account account = new Account();
        account.setUsername("testuser");
        account.setSaltId(salt);
        byte[] hashed = MessageDigest.getInstance("SHA-256").digest("passwordsomesalt".getBytes());
        account.setPassword(hashed);

        when(accountService.getAccountByUsername(any())).thenReturn(account);
        mockMvc.perform(
            post("/api/send/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content
            ("""
                {
                    "username": "testuser",
                    "password": "password"
                }
            """)
            .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(1));
    }

    @Test
    void logoutAuthenticatedUserSuccess() throws Exception
    {
        mockMvc.perform(post("/api/send/logout")
            .with(csrf())
            .with(user("testuser")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(1));
    }

    @Test
    void getapproverlistSuccess() throws Exception
    {
        Long generalRoleId = 1L;
        String generalAccountName = "testuser";
        Long adminRoleId = 2L;
        Long adminAccountId = 5L;
        String adminAccountName = "adminuser";
        String adminDepartmentName = "soumu";
        String adminRoleName = "katyou";

        // whenの処理が実行された際返すaccountの作成
        Account generalAccount = new Account();
        generalAccount.setUsername(generalAccountName);
        // 一般役職と管理役職
        Role generalRole = new Role();
        generalRole.setId(generalRoleId);
        generalAccount.setRoleId(generalRole);
        Role adminRole = new Role();
        adminRole.setId(adminRoleId);
        // whenの処理が実行された際返すapprovalSettingの作成
        List<ApprovalSetting> approvalSettings = new ArrayList<ApprovalSetting>();
        ApprovalSetting approvalSetting = new ApprovalSetting();
        approvalSetting.setRoleId(generalRole);
        approvalSetting.setApprovalId(adminRole);
        approvalSettings.add(approvalSetting);
        // whenの処理が実行された際返すaccountsの作成
        List<Account> accounts = new ArrayList<Account>();
        Account adminAccount = new Account();
        adminAccount.setRoleId(adminRole);
        adminAccount.setUsername(adminAccountName);
        accounts.add(adminAccount);
        ApproverListResponse approverListResponse = new ApproverListResponse(adminAccountId, adminAccountName, adminDepartmentName, adminRoleName);
        List<ApproverListResponse> approverListResponses = new ArrayList();
        approverListResponses.add(approverListResponse);

        when(accountService.getAccountByUsername(generalAccountName)).thenReturn(generalAccount);
        when(approvalSettingService.getApprovalSettings(generalAccount.getRoleId())).thenReturn(approvalSettings);
        when(accountService.getAccountByApprovalSetting(approvalSettings)).thenReturn(accounts);
        when(accountService.getApproverList(accounts)).thenReturn(approverListResponses);
        mockMvc.perform(get("/api/reach/approverlist")
            .with(csrf())
            .with(user("testuser")))
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
        Long generalAccountId = 1L;
        String generalAccountName = "testuser";
        Long adminAccountId = 2L;
        String adminAccountName = "adminuser";
        Long newAdminAccountId = 3L;
        String newAdminAccountName = "newadmin";
        Account generalAccount = new Account();
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountName);
        Account adminAccount = new Account();
        adminAccount.setId(adminAccountId);
        adminAccount.setUsername(adminAccountName);
        Account newAdminAccount = new Account();
        newAdminAccount.setId(newAdminAccountId);
        newAdminAccount.setUsername(newAdminAccountName);
        AccountApprover accountApprover = new AccountApprover();
        accountApprover.setAccountId(generalAccount);
        accountApprover.setApproverId(adminAccount);
        AccountApprover newAccountApprover = new AccountApprover();
        newAccountApprover.setAccountId(generalAccount);
        newAccountApprover.setApproverId(newAdminAccount);
        when(accountService.getAccountByUsername(generalAccountName)).thenReturn(generalAccount);
        when(accountApproverService.getAccountApproverByAccount(generalAccount)).thenReturn(accountApprover);
        when(accountService.getAccountByAccountId(newAdminAccountId)).thenReturn(newAdminAccount);
        when(accountApproverService.save(any())).thenReturn("ok");
        mockMvc.perform(
            post("/api/send/approverset")
            .contentType(MediaType.APPLICATION_JSON)
            .content
            ("""
                {
                    "approver": "3"
                }
            """)
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
        List<AllStyleListResponse> styleListResponse = new ArrayList();
        AllStyleListResponse styleTypeWork = new AllStyleListResponse();
        AllStyleListResponse styleTypeHome = new AllStyleListResponse();
        styleTypeWork.setId(workStylePlaceId);
        styleTypeHome.setId(homeStylePlaceId);
        styleTypeWork.setName(workStylePlaceName);
        styleTypeHome.setName(homeStylePlaceName);

        styleListResponse.add(styleTypeWork);
        styleListResponse.add(styleTypeHome);

        when(accountService.getAccountByUsername(generalAccountName)).thenReturn(generalAccount);
        when(stylePlaceService.getStyleList()).thenReturn(styleListResponse);
        mockMvc.perform(
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
        // whenで返すスタイルのオブジェクト
        Long generalStyleId = 1L;
        Long generalStylePlaceId = 1L;
        Style style = new Style();
        StylePlace stylePlace = new StylePlace();
        stylePlace.setId(generalStylePlaceId);
        style.setStyleId(generalStyleId);
        style.setAccountId(generalAccount);
        style.setStylePlaceId(stylePlace);
        // whenで返すスタイルプレイスのオブジェクト
        Long generalNewStylePlaceId = 2L;
        StylePlace newStylePlace = new StylePlace();
        newStylePlace.setId(generalNewStylePlaceId);
        // whenでセーブする際の条件に使う
        Style newStyle = new Style();
        newStyle.setStyleId(generalStyleId);
        newStyle.setAccountId(generalAccount);
        newStyle.setStylePlaceId(newStylePlace);
        // whenでセーブした際に返す文字列
        String responseText = "ok";

        when(accountService.getAccountByUsername(generalAccountUsername)).thenReturn(generalAccount);
        when(styleService.getStyleByAccountId(generalAccount.getId())).thenReturn(style);
        when(stylePlaceService.getStylePlaceById(generalNewStylePlaceId)).thenReturn(newStylePlace);
        when(styleService.save(any())).thenReturn(responseText);
        mockMvc.perform(
            post("/api/send/style")
            .contentType(MediaType.APPLICATION_JSON)
            .content
            ("""
                {
                    "style": "2"
                }
            """)
            .with(csrf())
            .with(user("testuser"))
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

        List<ApprovalSetting> approvalSettings = new ArrayList();
        when(accountService.getAccountByUsername(any())).thenReturn(account);
        when(roleService.getRoleById(any())).thenReturn(role);
        when(departmentService.getDepartmentById(any())).thenReturn(department);
        when(approvalSettingService.getApprovalSettingsByApprover(any())).thenReturn(approvalSettings);
        mockMvc.perform(
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
    void shiftListSuccess() throws Exception
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
        Shift generalShift = new Shift
        (
            generalShiftId, generalAccount, generalShiftBeginWork,
            generalShiftEndWork, generalShiftBeginBreak, generalShiftEndBreak,
            generalShiftLateness, generalShiftLeaveEarly, generalShiftOuting, generalShiftOverWork
        );
        ShiftListResponse generalShiftListResponse = new ShiftListResponse
        (
            generalShiftId,
            generalShiftBeginWork.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + generalShiftBeginWork.format(DateTimeFormatter.ofPattern("HH:mm:ss")),
            generalShiftEndWork.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + generalShiftEndWork.format(DateTimeFormatter.ofPattern("HH:mm:ss")),
            generalShiftBeginBreak.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + generalShiftBeginBreak.format(DateTimeFormatter.ofPattern("HH:mm:ss")),
            generalShiftEndBreak.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + generalShiftEndBreak.format(DateTimeFormatter.ofPattern("HH:mm:ss")),
            generalShiftLateness,
            generalShiftLeaveEarly,
            generalShiftOuting,
            generalShiftOverWork
        );
        List<Shift> generalShifts = new ArrayList();
        generalShifts.add(generalShift);
        List<ShiftListResponse> generalShiftList = new ArrayList();
        generalShiftList.add(shiftService.shiftToShiftListResponse(generalShift));

        when(accountService.getAccountByUsername(any())).thenReturn(generalAccount);
        // when(shiftService.findByAccountId(anyLong())).thenReturn(generalShifts);
        when(shiftService.findByAccountIdAndBeginWorkBetween(anyLong(), anyInt(), anyInt())).thenReturn(generalShifts);
        when(shiftService.shiftToShiftListResponse(any())).thenReturn(generalShiftListResponse);
        mockMvc.perform(
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
    void shiftSetSuccess() throws Exception
    {
        String generalBeginWork = "2025/09/02T09:00:00";
        String generalBeginBreak = "2025/09/02T12:00:00";
        String generalEndBreak = "2025/09/02T13:00:00";
        String generalEndWork = "2025/09/02T18:00:00";
        String generalRequestComment = "";
        String generalRequestDate = "2025/08/02T00:00:11";
        String json = String.format
        ("""
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
        LocalDateTime legalTimeBegin = LocalDateTime.parse(LocalDateTime.parse("2025/08/08T21:00:00",DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        Time legalTimeScheduleWorkTime = Time.valueOf("08:00:00");
        Time legalTimeWeeklyWorkTime = Time.valueOf("40:00:00");
        Time legalTimeMonthlyOverWork = Time.valueOf("45:00:00");
        Time legalTimeYearOverWork = Time.valueOf("360:00:00");
        Time legalTimeMaxOverWorkTime = Time.valueOf("100:00:00");
        Time legalTimeMonthlyOverWorkAverage = Time.valueOf("80:00:00");
        Time legalTimeLateNightWorkTimeBegin = Time.valueOf("22:00:00");
        Time legalTimeLateNightWorkTimeEnd = Time.valueOf("05:00:00");
        Time legalTimeScheduleBreakTime = Time.valueOf("01:00:00");

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

        Account generalAccount = new Account();
        Long generalAccountId = 1L;
        String generalAccountName = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setName(generalAccountName);

        List<Shift> shifts = new ArrayList();
        List<ShiftRequest> shiftRequests = new ArrayList();

        when(accountService.getAccountByUsername(anyString())).thenReturn(generalAccount);
        when(shiftService.findByAccountIdAndDayBeginWorkBetween(any(Account.class), any(LocalDateTime.class))).thenReturn(shifts);
        when(shiftRequestService.getAccountIdAndBeginWorkBetweenDay(any(Account.class), any(LocalDateTime.class))).thenReturn(shiftRequests);
        when(legalTimeService.getFirstByOrderByBeginDesc()).thenReturn(legalTime);
        when(shiftService.findByAccountIdAndBeginWorkBetweenWeek(any(Account.class), any(LocalDateTime.class))).thenReturn(shifts);
        when(shiftRequestService.save(any(ShiftRequest.class))).thenReturn("ok");
        mockMvc.perform(
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

        when(accountService.getAccountByUsername(anyString())).thenReturn(generalAccount);
        when(shiftRequestService.findByAccountIdAndShiftRequestId(any(Account.class),anyLong())).thenReturn(shiftRequest);
        mockMvc.perform(
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
    void shiftChangeRequest() throws Exception
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
        shiftChangeRequest.setShiftChangeId(shiftRequestId);
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

        when(accountService.getAccountByUsername(anyString())).thenReturn(generalAccount);
        when(shiftChangeRequestService.findByAccountIdAndShiftChangeRequestId(any(Account.class),anyLong())).thenReturn(shiftChangeRequest);
        mockMvc.perform(
            get("/api/reach/requestdetil/changetime")
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
        .andExpect(jsonPath("$.approverId").value(adminAccountId))
        .andExpect(jsonPath("$.approverName").value(adminAccountName))
        .andExpect(jsonPath("$.approverComment").value(approverComment))
        .andExpect(jsonPath("$.approvalTime").value(Objects.isNull(approvalTime) ? "" : localDateTimeToString.localDateTimeToString(approvalTime)))
        .andExpect(jsonPath("$.shiftId").value(shiftId));
    }
}