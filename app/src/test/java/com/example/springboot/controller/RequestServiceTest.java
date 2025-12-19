package com.example.springboot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;

import com.example.springboot.Config;
import com.example.springboot.dto.change.DurationToString;
import com.example.springboot.dto.change.StringToDuration;
import com.example.springboot.dto.change.StringToLocalDateTime;
import com.example.springboot.dto.input.MonthlyInput;
import com.example.springboot.dto.input.OtherTimeInput;
import com.example.springboot.dto.input.OverTimeInput;
import com.example.springboot.dto.input.RequestJudgmentInput;
import com.example.springboot.dto.input.ShiftChangeInput;
import com.example.springboot.dto.input.ShiftInput;
import com.example.springboot.dto.input.StampInput;
import com.example.springboot.dto.input.VacationInput;
import com.example.springboot.dto.input.WithDrowInput;
import com.example.springboot.dto.response.RequestDetailMonthlyResponse;
import com.example.springboot.dto.response.RequestDetailOtherTimeResponse;
import com.example.springboot.dto.response.RequestDetailOverTimeResponse;
import com.example.springboot.dto.response.RequestDetailShiftChangeResponse;
import com.example.springboot.dto.response.RequestDetailShiftResponse;
import com.example.springboot.dto.response.RequestDetailStampResponse;
import com.example.springboot.dto.response.RequestDetailVacationResponse;
import com.example.springboot.model.Account;
import com.example.springboot.model.AccountApprover;
import com.example.springboot.model.Attend;
import com.example.springboot.model.AttendanceExceptionRequest;
import com.example.springboot.model.AttendanceExceptionType;
import com.example.springboot.model.AttendanceListSource;
import com.example.springboot.model.LegalTime;
import com.example.springboot.model.MonthlyRequest;
import com.example.springboot.model.NewsList;
import com.example.springboot.model.OverTimeRequest;
import com.example.springboot.model.PaydHoliday;
import com.example.springboot.model.PaydHolidayUse;
import com.example.springboot.model.Shift;
import com.example.springboot.model.ShiftChangeRequest;
import com.example.springboot.model.ShiftListOtherTime;
import com.example.springboot.model.ShiftListOverTime;
import com.example.springboot.model.ShiftListShiftRequest;
import com.example.springboot.model.ShiftListVacation;
import com.example.springboot.model.ShiftRequest;
import com.example.springboot.model.StampRequest;
import com.example.springboot.model.Vacation;
import com.example.springboot.model.VacationRequest;
import com.example.springboot.model.VacationType;
import com.example.springboot.service.AccountApproverService;
import com.example.springboot.service.AttendService;
import com.example.springboot.service.AttendanceExceptionRequestService;
import com.example.springboot.service.AttendanceExceptionTypeService;
import com.example.springboot.service.AttendanceListSourceService;
import com.example.springboot.service.LegalTimeService;
import com.example.springboot.service.MonthlyRequestService;
import com.example.springboot.service.NewsListService;
import com.example.springboot.service.OverTimeRequestService;
import com.example.springboot.service.PaydHolidayService;
import com.example.springboot.service.PaydHolidayUseService;
import com.example.springboot.service.RequestService;
import com.example.springboot.service.ShiftChangeRequestService;
import com.example.springboot.service.ShiftListOtherTimeService;
import com.example.springboot.service.ShiftListOverTimeService;
import com.example.springboot.service.ShiftListShiftRequestService;
import com.example.springboot.service.ShiftListVacationService;
import com.example.springboot.service.ShiftRequestService;
import com.example.springboot.service.ShiftService;
import com.example.springboot.service.StampRequestService;
import com.example.springboot.service.VacationRequestService;
import com.example.springboot.service.VacationService;
import com.example.springboot.service.VacationTypeService;

@ContextConfiguration(classes = Config.class)
@ExtendWith(MockitoExtension.class)
public class RequestServiceTest
{
    @InjectMocks
    @Spy
    RequestService requestService;

    @Mock
    ShiftRequestService shiftRequestService;

    @Mock
    ShiftChangeRequestService shiftChangeRequestService;

    @Mock
    StampRequestService stampRequestService;

    @Mock
    VacationRequestService vacationRequestService;

    @Mock
    AttendanceExceptionRequestService attendanceExceptionRequestService;

    @Mock
    OverTimeRequestService overTimeRequestService;

    @Mock
    NewsListService newsListService;

    @Mock
    ShiftListOtherTimeService shiftListOtherTimeService;

    @Mock
    ShiftListVacationService shiftListVacationService;

    @Mock
    ShiftListOverTimeService shiftListOverTimeService;

    @Mock
    ShiftListShiftRequestService shiftListShiftRequestService;

    @Mock
    ShiftService shiftService;

    @Mock
    AttendService attendService;

    @Mock
    AttendanceListSourceService attendanceListSourceService;

    @Mock
    VacationService vacationService;

    @Mock
    DurationToString durationToString;

    @Mock
    StringToLocalDateTime stringToLocalDateTime;

    @Mock
    MonthlyRequestService monthlyRequestService;

    @Mock
    PaydHolidayService paydHolidayService;

    @Mock
    StringToDuration stringToDuration;

    @Mock
    VacationTypeService vacationTypeService;

    @Mock
    AttendanceExceptionTypeService attendanceExceptionTypeService;

    @Mock
    LegalTimeService legalTimeService;

    @Mock
    AccountApproverService accountApproverService;

    @Mock
    PaydHolidayUseService paydHolidayUseService;

    @Test
    void ShiftRequestSuccess()
    {
        Account generalAccount = new Account();
        Long generalAccountId = 1L;
        String generalAccountName = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setName(generalAccountName);

        String generalBeginWork = "2026/09/02T09:00:00";
        String generalBeginBreak = "2026/09/02T12:00:00";
        String generalEndBreak = "2026/09/02T13:00:00";
        String generalEndWork = "2026/09/02T18:00:00";
        String generalRequestComment = "";
        String generalRequestDate = "2025/08/02T00:00:11";

        ShiftInput shiftInput = new ShiftInput();
        shiftInput.setBeginWork(generalBeginWork);
        shiftInput.setEndWork(generalEndWork);
        shiftInput.setBeginBreak(generalBeginBreak);
        shiftInput.setEndBreak(generalEndBreak);
        shiftInput.setRequestComment(generalRequestComment);
        shiftInput.setRequestDate(generalRequestDate);

        List<Shift> shifts = new ArrayList<Shift>();
        List<ShiftRequest> shiftRequests = new ArrayList<ShiftRequest>();

        ShiftRequest shiftRequest = new ShiftRequest();
        Long shiftRequestId = 2L;
        shiftRequest.setShiftRequestId(shiftRequestId);

        when(stringToLocalDateTime.stringToLocalDateTime(shiftInput.getBeginWork())).thenReturn(LocalDateTime.parse(LocalDateTime.parse(shiftInput.getBeginWork(),DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(stringToLocalDateTime.stringToLocalDateTime(shiftInput.getEndWork())).thenReturn(LocalDateTime.parse(LocalDateTime.parse(shiftInput.getEndWork(),DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(stringToLocalDateTime.stringToLocalDateTime(shiftInput.getBeginBreak())).thenReturn(LocalDateTime.parse(LocalDateTime.parse(shiftInput.getBeginBreak(),DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(stringToLocalDateTime.stringToLocalDateTime(shiftInput.getEndBreak())).thenReturn(LocalDateTime.parse(LocalDateTime.parse(shiftInput.getEndBreak(),DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(stringToLocalDateTime.stringToLocalDateTime(shiftInput.getRequestDate())).thenReturn(LocalDateTime.parse(LocalDateTime.parse(shiftInput.getRequestDate(),DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(shiftService.findByAccountIdAndDayBeginWorkBetween(any(Account.class), any(LocalDateTime.class))).thenReturn(shifts);

        when(shiftRequestService.findAccountIdAndBeginWorkBetweenDay(any(Account.class), any(LocalDateTime.class))).thenReturn(shiftRequests);
        when(shiftRequestService.save(any(ShiftRequest.class))).thenReturn(shiftRequest);
        // 実行
        int result = requestService.createShiftRequest(generalAccount, shiftInput);

        assertEquals(1, result);
    }

    @Test
    void shiftChangeRequestSuccess()
    {
        String generalBeginWork = "2026/09/02T09:00:00";
        String generalBeginBreak = "2026/09/02T12:00:00";
        String generalEndBreak = "2026/09/02T13:00:00";
        String generalEndWork = "2026/09/02T18:00:00";
        String generalRequestComment = "";
        String generalRequestDate = "2025/08/02T00:00:11";

        Shift shift = new Shift();
        Long shiftId = 1L;
        shift.setShiftId(shiftId);
        shift.setBeginWork(stringToLocalDateTime.stringToLocalDateTime(generalBeginWork));
        ShiftChangeInput shiftChangeInput = new ShiftChangeInput();
    
        shiftChangeInput.setShiftId(shiftId);
        shiftChangeInput.setBeginWork(generalBeginWork);
        shiftChangeInput.setEndWork(generalEndWork);
        shiftChangeInput.setBeginBreak(generalBeginBreak);
        shiftChangeInput.setEndBreak(generalEndBreak);
        shiftChangeInput.setRequestComment(generalRequestComment);
        shiftChangeInput.setRequestDate(generalRequestDate);

        Account generalAccount = new Account();
        Long generalAccountId = 1L;
        String generalAccountName = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setName(generalAccountName);

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

        when(shiftService.findByAccountIdAndShiftId(any(Account.class), anyLong())).thenReturn(shift);
        when(stringToLocalDateTime.stringToLocalDateTime(generalBeginWork)).thenReturn(LocalDateTime.parse(LocalDateTime.parse(generalBeginWork,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(stringToLocalDateTime.stringToLocalDateTime(generalEndWork)).thenReturn(LocalDateTime.parse(LocalDateTime.parse(generalEndWork,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(stringToLocalDateTime.stringToLocalDateTime(generalBeginBreak)).thenReturn(LocalDateTime.parse(LocalDateTime.parse(generalBeginBreak,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(stringToLocalDateTime.stringToLocalDateTime(generalEndBreak)).thenReturn(LocalDateTime.parse(LocalDateTime.parse(generalEndBreak,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(stringToLocalDateTime.stringToLocalDateTime(generalRequestDate)).thenReturn(LocalDateTime.parse(LocalDateTime.parse(generalRequestDate,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(shiftChangeRequestService.findByAccountIdAndShiftIdAndRequestStatusWait(any(Account.class), anyLong())).thenReturn(generalShiftChangeRequests);
        when(shiftChangeRequestService.save(any(ShiftChangeRequest.class))).thenReturn(shiftChangeRequest);

        int result = requestService.createShiftChangeRequest(generalAccount, shiftChangeInput);

        assertEquals(1, result);
    }

    @Test
    void stampRequestSuccess()
    {
        String generalBeginWork = "2025/09/02T09:00:00";
        String generalBeginBreak = "2025/09/02T12:00:00";
        String generalEndBreak = "2025/09/02T13:00:00";
        String generalEndWork = "2025/09/02T18:00:00";
        String generalRequestComment = "";
        String generalRequestDate = "2025/08/02T00:00:11";

        Account generalAccount = new Account();
        Long generalAccountId = 1L;
        String generalAccountName = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setName(generalAccountName);

        Shift generalShift = new Shift();
        Long shiftId = 34L;
        generalShift.setBeginWork(LocalDateTime.parse(LocalDateTime.parse(generalBeginWork,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        generalShift.setEndWork(LocalDateTime.parse(LocalDateTime.parse(generalEndWork,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        generalShift.setBeginBreak(LocalDateTime.parse(LocalDateTime.parse(generalBeginBreak,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        generalShift.setEndBreak(LocalDateTime.parse(LocalDateTime.parse(generalEndBreak,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));

        List<StampRequest> stampRequests = new ArrayList<StampRequest>();
        StampRequest stampRequest = new StampRequest();
        Long stampRequestId = 39L;
        stampRequest.setStampId(stampRequestId);

        StampInput stampInput = new StampInput();
        stampInput.setShiftId(shiftId);
        stampInput.setBeginWork(generalBeginWork);
        stampInput.setEndWork(generalEndWork);
        stampInput.setBeginBreak(generalBeginBreak);
        stampInput.setEndBreak(generalEndBreak);
        stampInput.setRequestComment(generalRequestComment);
        stampInput.setRequestDate(generalRequestDate);

        List<MonthlyRequest> monthlyRequests = new ArrayList<MonthlyRequest>();

        when(shiftService.findByAccountIdAndShiftId(any(Account.class), anyLong())).thenReturn(generalShift);
        when(monthlyRequestService.findByAccountIdAndYearAndMonth(any(Account.class), anyInt(), anyInt())).thenReturn(monthlyRequests);
        when(stringToLocalDateTime.stringToLocalDateTime(generalBeginWork)).thenReturn(LocalDateTime.parse(LocalDateTime.parse(generalBeginWork,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(stringToLocalDateTime.stringToLocalDateTime(generalEndWork)).thenReturn(LocalDateTime.parse(LocalDateTime.parse(generalEndWork,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(stringToLocalDateTime.stringToLocalDateTime(generalBeginBreak)).thenReturn(LocalDateTime.parse(LocalDateTime.parse(generalBeginBreak,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(stringToLocalDateTime.stringToLocalDateTime(generalEndBreak)).thenReturn(LocalDateTime.parse(LocalDateTime.parse(generalEndBreak,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(stringToLocalDateTime.stringToLocalDateTime(generalRequestDate)).thenReturn(LocalDateTime.parse(LocalDateTime.parse(generalRequestDate,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(stampRequestService.findByShiftIdAndRequestStatusWait(any(Shift.class))).thenReturn(stampRequests);
        when(stampRequestService.save(any(StampRequest.class))).thenReturn(stampRequest);

        int result = requestService.createStampRequest(generalAccount, stampInput);

        assertEquals(1, result);
    }

    @Test
    void vacationRequestSuccess()
    {
        String generalBeginVacation = "2026/11/22T10:00:00";
        String generalEndVacation = "2026/11/22T11:00:00";
        String generalRequestComment = "";
        String generalRequestDate = "2026/08/02T00:00:11";
        Long vacationType = 1L;

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
        generalShift.setBeginWork(LocalDateTime.parse(LocalDateTime.parse(generalBeginWork,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        generalShift.setEndWork(LocalDateTime.parse(LocalDateTime.parse(generalEndWork,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        generalShift.setBeginBreak(LocalDateTime.parse(LocalDateTime.parse(generalBeginBreak,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        generalShift.setEndBreak(LocalDateTime.parse(LocalDateTime.parse(generalEndBreak,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));

        List<VacationRequest> vacationRequests = new ArrayList<VacationRequest>();
        VacationRequest generalVacationRequest = new VacationRequest();
        Long generalVacationRequestId = 5L;
        String generalVacationRequestBegin = "2026/11/22T09:00:00";
        String generalVacationRequestEnd = "2026/11/22T10:00:00";
        generalVacationRequest.setVacationId(generalVacationRequestId);
        generalVacationRequest.setAccountId(generalAccount);
        generalVacationRequest.setShiftId(generalShift);
        generalVacationRequest.setBeginVacation(LocalDateTime.parse(LocalDateTime.parse(generalVacationRequestBegin,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        generalVacationRequest.setEndVacation(LocalDateTime.parse(LocalDateTime.parse(generalVacationRequestEnd,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        vacationRequests.add(generalVacationRequest);

        List<ShiftListOverTime> shiftListOverTimes = new ArrayList<ShiftListOverTime>();
        ShiftListOverTime generalShiftListOverTime = new ShiftListOverTime();
        OverTimeRequest generalOverTimeRequest = new OverTimeRequest();
        String generalOverTimeRequestBegin = "2026/11/22T14:00:00";
        String generalOverTimeRequestEnd = "2026/11/22T15:00:00";
        generalOverTimeRequest.setBeginWork(LocalDateTime.parse(LocalDateTime.parse(generalOverTimeRequestBegin,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        generalOverTimeRequest.setEndWork(LocalDateTime.parse(LocalDateTime.parse(generalOverTimeRequestEnd,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        generalShiftListOverTime.setOverTimeId(generalOverTimeRequest);
        shiftListOverTimes.add(generalShiftListOverTime);

        List<OverTimeRequest> overTimeRequests = new ArrayList<OverTimeRequest>();
        OverTimeRequest adminOverTimeRequest = new OverTimeRequest();
        String adminOverTimeRequestBegin = "2026/11/22T18:00:00";
        String adminOverTimeRequestEnd = "2026/11/22T20:00:00";
        adminOverTimeRequest.setAccountId(generalAccount);
        adminOverTimeRequest.setBeginWork(LocalDateTime.parse(LocalDateTime.parse(adminOverTimeRequestBegin,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        adminOverTimeRequest.setEndWork(LocalDateTime.parse(LocalDateTime.parse(adminOverTimeRequestEnd,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
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
        paydHolidayVacationRequest.setBeginVacation(LocalDateTime.parse(LocalDateTime.parse(paydHolidayVacationRequestBegin,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        paydHolidayVacationRequest.setEndVacation(LocalDateTime.parse(LocalDateTime.parse(paydHolidayVacationRequestEnd,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        paydHolidayVacationRequests.add(paydHolidayVacationRequest);

        VacationRequest vacationRequest = new VacationRequest();
        Long vacationRequestId = 3L;
        vacationRequest.setVacationId(vacationRequestId);

        VacationInput vacationInput = new VacationInput();
        vacationInput.setShiftId(generalShiftId);
        vacationInput.setVacationType(vacationType);
        vacationInput.setBeginVacation(generalBeginVacation);
        vacationInput.setEndVacation(generalEndVacation);
        vacationInput.setRequestComment(generalRequestComment);
        vacationInput.setRequestDate(generalRequestDate);

        when(stringToLocalDateTime.stringToLocalDateTime(generalBeginVacation)).thenReturn(LocalDateTime.parse(LocalDateTime.parse(generalBeginVacation,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(stringToLocalDateTime.stringToLocalDateTime(generalEndVacation)).thenReturn(LocalDateTime.parse(LocalDateTime.parse(generalEndVacation,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(shiftService.findByAccountIdAndShiftId(any(Account.class), anyLong())).thenReturn(generalShift);
        when(shiftListOverTimeService.findByShiftId(any(Shift.class))).thenReturn(shiftListOverTimes);
        when(overTimeRequestService.findByAccountIdAndShiftIdAndRequestStatusWait(any(Account.class), any(Shift.class))).thenReturn(overTimeRequests);
        when(paydHolidayService.findByAccountIdAndLimitAfter(any(Account.class))).thenReturn(paydHolidays);
        when(stringToDuration.stringToDuration(generalPaydHolidayTime)).thenReturn(Duration.ofHours(Long.parseLong(generalPaydHolidayTime.split(":")[0])).plusMinutes(Long.parseLong(generalPaydHolidayTime.split(":")[1])).plusSeconds(Long.parseLong(generalPaydHolidayTime.split(":")[2])));
        when(vacationTypeService.findById(anyLong())).thenReturn(generalVacationType);
        when(vacationRequestService.findByAccountIdAndShiftId(any(Account.class), any(Shift.class))).thenReturn(vacationRequests);
        when(vacationRequestService.findByAccountIdAndRequestStatusWaitAndVacationTypePaydHoiday(any(Account.class))).thenReturn(paydHolidayVacationRequests);
        when(vacationRequestService.save(any(VacationRequest.class))).thenReturn(vacationRequest);

        int result = requestService.createVacationRequest(generalAccount, vacationInput);
        assertEquals(1, result);
    }

    @Test
    void attendanceExceptionRequestOutingSuccess()
    {
        Long requestShiftId = 1L;
        Long requestOtherType = 1L;
        String requestBeginOtherTime = "2025/12/23T09:00:00";
        String requestEndOtherTime = "2025/12/23T10:00:00";
        String requestComment = "";
        String requestDate = "2025/09/02T10:00:00";

        Account generalAccount = new Account();
        Long generalAccountId = 3L;
        String generalAccountUsername = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        Shift generalShift = new Shift();
        Long generalShiftId = 5L;
        String generalShiftBeginWork = "2025/12/23T09:00:00";
        String generalShiftEndWork = "2025/12/23T18:00:00";
        String generalShiftBeginBreak = "2025/12/23T12:00:00";
        String generalShiftEndBreak = "2025/12/23T13:00:00";
        generalShift.setShiftId(generalShiftId);
        generalShift.setBeginWork(LocalDateTime.parse(LocalDateTime.parse(generalShiftBeginWork,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        generalShift.setEndWork(LocalDateTime.parse(LocalDateTime.parse(generalShiftEndWork,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        generalShift.setBeginBreak(LocalDateTime.parse(LocalDateTime.parse(generalShiftBeginBreak,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        generalShift.setEndBreak(LocalDateTime.parse(LocalDateTime.parse(generalShiftEndBreak,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));

        List<ShiftListOverTime> shiftListOverTimes = new ArrayList<ShiftListOverTime>();

        ShiftListShiftRequest shiftListShiftRequest = new ShiftListShiftRequest();
        ShiftRequest shiftRequest = new ShiftRequest();
        Long shiftRequestId = 51L;
        String shiftRequestBegin = "2025/12/23T09:00:00";
        String shiftRequestEnd = "2025/12/23T18:00:00";
        shiftRequest.setShiftRequestId(shiftRequestId);
        shiftRequest.setBeginWork(LocalDateTime.parse(LocalDateTime.parse(shiftRequestBegin,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        shiftRequest.setEndWork(LocalDateTime.parse(LocalDateTime.parse(shiftRequestEnd,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        shiftListShiftRequest.setShiftRequestId(shiftRequest);

        List<AttendanceExceptionRequest> attendanceExceptionRequests = new ArrayList<AttendanceExceptionRequest>();
        AttendanceExceptionRequest attendanceExceptionRequest = new AttendanceExceptionRequest();
        Long attendanceExceptionRequestId = 34L;
        attendanceExceptionRequest.setAttendanceExceptionId(attendanceExceptionRequestId);

        AttendanceExceptionType attendanceExceptionType = new AttendanceExceptionType();
        attendanceExceptionType.setAttendanceExceptionTypeId(requestOtherType);

        OtherTimeInput otherTimeInput = new OtherTimeInput();
        otherTimeInput.setShiftId(requestShiftId);
        otherTimeInput.setOtherType(requestOtherType);
        otherTimeInput.setBeginOtherTime(requestBeginOtherTime);
        otherTimeInput.setEndOtherTime(requestEndOtherTime);
        otherTimeInput.setRequestComment(requestComment);
        otherTimeInput.setRequestDate(requestDate);

        when(shiftService.findByAccountIdAndShiftId(any(Account.class), anyLong())).thenReturn(generalShift);
        when(stringToLocalDateTime.stringToLocalDateTime(requestBeginOtherTime)).thenReturn(LocalDateTime.parse(LocalDateTime.parse(requestBeginOtherTime,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(stringToLocalDateTime.stringToLocalDateTime(requestEndOtherTime)).thenReturn(LocalDateTime.parse(LocalDateTime.parse(requestEndOtherTime,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(shiftListOverTimeService.findByShiftId(any(Shift.class))).thenReturn(shiftListOverTimes);
        when(shiftListShiftRequestService.findByShiftId(any(Shift.class))).thenReturn(shiftListShiftRequest);
        when(attendanceExceptionTypeService.findByAttendanceExceptionTypeId(anyLong())).thenReturn(attendanceExceptionType);
        when(stringToLocalDateTime.stringToLocalDateTime(requestDate)).thenReturn(LocalDateTime.parse(LocalDateTime.parse(requestDate,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(attendanceExceptionRequestService.findByAccountIdAndShiftIdAndOutingAndBeginTimeBetweenOrEndTimeBetweenAndRequestStatusWaitOrRequestStatusApproved(any(Account.class), any(Shift.class), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(attendanceExceptionRequests);
        when(attendanceExceptionRequestService.save(any(AttendanceExceptionRequest.class))).thenReturn(attendanceExceptionRequest);

        int result = requestService.createAttendanceExceptionRequest(generalAccount, otherTimeInput);
        assertEquals(1, result);
    }

    @Test
    void attendanceExceptionRequestLatenessSuccess()
    {
        Long requestShiftId = 1L;
        Long requestOtherType = 2L;
        String requestBeginOtherTime = "2025/12/23T09:00:00";
        String requestEndOtherTime = "2025/12/23T10:00:00";
        String requestComment = "";
        String requestDate = "2025/09/02T10:00:00";

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
        generalShift.setBeginWork(LocalDateTime.parse(LocalDateTime.parse(generalShiftBeginWork,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        generalShift.setEndWork(LocalDateTime.parse(LocalDateTime.parse(generalShiftEndWork,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));

        List<ShiftListOverTime> shiftListOverTimes = new ArrayList<ShiftListOverTime>();

        OverTimeRequest overTimeRequest = new OverTimeRequest();
        String shiftListOverTimeBeginOverTime = "2025/12/23T18:00:00";
        String shiftListOverTimeEndOverTime = "2025/12/23T19:00:00";
        overTimeRequest.setBeginWork(LocalDateTime.parse(LocalDateTime.parse(shiftListOverTimeBeginOverTime,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        overTimeRequest.setEndWork(LocalDateTime.parse(LocalDateTime.parse(shiftListOverTimeEndOverTime,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));

        ShiftListShiftRequest shiftListShiftRequest = new ShiftListShiftRequest();
        ShiftRequest shiftRequest = new ShiftRequest();
        ShiftChangeRequest shiftChangeRequest = new ShiftChangeRequest();
        Long shiftChangeRequestId = 51L;
        String shiftChangeRequestBegin = "2025/12/23T09:00:00";
        String shiftChangeRequestEnd = "2025/12/23T18:00:00";
        shiftChangeRequest.setShiftChangeId(shiftChangeRequestId);
        shiftChangeRequest.setBeginWork(LocalDateTime.parse(LocalDateTime.parse(shiftChangeRequestBegin,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        shiftChangeRequest.setEndWork(LocalDateTime.parse(LocalDateTime.parse(shiftChangeRequestEnd,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        shiftListShiftRequest.setShiftRequestId(shiftRequest);
        shiftListShiftRequest.setShiftChangeRequestId(shiftChangeRequest);

        AttendanceExceptionRequest attendanceExceptionRequest = new AttendanceExceptionRequest();
        Long attendanceExceptionRequestId = 34L;
        attendanceExceptionRequest.setAttendanceExceptionId(attendanceExceptionRequestId);

        AttendanceExceptionType attendanceExceptionType = new AttendanceExceptionType();
        attendanceExceptionType.setAttendanceExceptionTypeId(requestOtherType);

        OtherTimeInput otherTimeInput = new OtherTimeInput();
        otherTimeInput.setShiftId(requestShiftId);
        otherTimeInput.setOtherType(requestOtherType);
        otherTimeInput.setBeginOtherTime(requestBeginOtherTime);
        otherTimeInput.setEndOtherTime(requestEndOtherTime);
        otherTimeInput.setRequestComment(requestComment);
        otherTimeInput.setRequestDate(requestDate);

        when(shiftService.findByAccountIdAndShiftId(any(Account.class), anyLong())).thenReturn(generalShift);
        when(stringToLocalDateTime.stringToLocalDateTime(requestBeginOtherTime)).thenReturn(LocalDateTime.parse(LocalDateTime.parse(requestBeginOtherTime,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(stringToLocalDateTime.stringToLocalDateTime(requestEndOtherTime)).thenReturn(LocalDateTime.parse(LocalDateTime.parse(requestEndOtherTime,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(shiftListOverTimeService.findByShiftId(any(Shift.class))).thenReturn(shiftListOverTimes);
        when(shiftListShiftRequestService.findByShiftId(any(Shift.class))).thenReturn(shiftListShiftRequest);
        when(attendanceExceptionTypeService.findByAttendanceExceptionTypeId(anyLong())).thenReturn(attendanceExceptionType);
        when(stringToLocalDateTime.stringToLocalDateTime(requestDate)).thenReturn(LocalDateTime.parse(LocalDateTime.parse(requestDate,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));

        when(attendanceExceptionRequestService.save(any(AttendanceExceptionRequest.class))).thenReturn(attendanceExceptionRequest);

        int result = requestService.createAttendanceExceptionRequest(generalAccount, otherTimeInput);
        assertEquals(1, result);
    }

    @Test
    void attendanceExceptionRequestLeaveEarlySuccess()
    {
        Long requestShiftId = 1L;
        Long requestOtherType = 3L;
        String requestBeginOtherTime = "2025/12/23T17:00:00";
        String requestEndOtherTime = "2025/12/23T18:00:00";
        String requestComment = "";
        String requestDate = "2025/09/02T10:00:00";

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
        generalShift.setBeginWork(LocalDateTime.parse(LocalDateTime.parse(generalShiftBeginWork,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        generalShift.setEndWork(LocalDateTime.parse(LocalDateTime.parse(generalShiftEndWork,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));

        List<ShiftListOverTime> shiftListOverTimes = new ArrayList<ShiftListOverTime>();

        OverTimeRequest overTimeRequest = new OverTimeRequest();
        String shiftListOverTimeBeginOverTime = "2025/12/23T08:00:00";
        String shiftListOverTimeEndOverTime = "2025/12/23T09:00:00";
        overTimeRequest.setBeginWork(LocalDateTime.parse(LocalDateTime.parse(shiftListOverTimeBeginOverTime,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        overTimeRequest.setEndWork(LocalDateTime.parse(LocalDateTime.parse(shiftListOverTimeEndOverTime,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));

        ShiftListShiftRequest shiftListShiftRequest = new ShiftListShiftRequest();
        ShiftRequest shiftRequest = new ShiftRequest();
        ShiftChangeRequest shiftChangeRequest = new ShiftChangeRequest();
        Long shiftChangeRequestId = 51L;
        String shiftChangeRequestBegin = "2025/12/23T09:00:00";
        String shiftChangeRequestEnd = "2025/12/23T18:00:00";
        shiftChangeRequest.setShiftChangeId(shiftChangeRequestId);
        shiftChangeRequest.setBeginWork(LocalDateTime.parse(LocalDateTime.parse(shiftChangeRequestBegin,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        shiftChangeRequest.setEndWork(LocalDateTime.parse(LocalDateTime.parse(shiftChangeRequestEnd,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        shiftListShiftRequest.setShiftRequestId(shiftRequest);
        shiftListShiftRequest.setShiftChangeRequestId(shiftChangeRequest);

        AttendanceExceptionRequest attendanceExceptionRequest = new AttendanceExceptionRequest();
        Long attendanceExceptionRequestId = 43L;
        attendanceExceptionRequest.setAttendanceExceptionId(attendanceExceptionRequestId);

        AttendanceExceptionType attendanceExceptionType = new AttendanceExceptionType();
        attendanceExceptionType.setAttendanceExceptionTypeId(requestOtherType);

        OtherTimeInput otherTimeInput = new OtherTimeInput();
        otherTimeInput.setShiftId(requestShiftId);
        otherTimeInput.setOtherType(requestOtherType);
        otherTimeInput.setBeginOtherTime(requestBeginOtherTime);
        otherTimeInput.setEndOtherTime(requestEndOtherTime);
        otherTimeInput.setRequestComment(requestComment);
        otherTimeInput.setRequestDate(requestDate);

        when(shiftService.findByAccountIdAndShiftId(any(Account.class), anyLong())).thenReturn(generalShift);
        when(stringToLocalDateTime.stringToLocalDateTime(requestBeginOtherTime)).thenReturn(LocalDateTime.parse(LocalDateTime.parse(requestBeginOtherTime,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(stringToLocalDateTime.stringToLocalDateTime(requestEndOtherTime)).thenReturn(LocalDateTime.parse(LocalDateTime.parse(requestEndOtherTime,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(shiftListOverTimeService.findByShiftId(any(Shift.class))).thenReturn(shiftListOverTimes);
        when(shiftListShiftRequestService.findByShiftId(any(Shift.class))).thenReturn(shiftListShiftRequest);
        when(attendanceExceptionTypeService.findByAttendanceExceptionTypeId(anyLong())).thenReturn(attendanceExceptionType);
        when(stringToLocalDateTime.stringToLocalDateTime(requestDate)).thenReturn(LocalDateTime.parse(LocalDateTime.parse(requestDate,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(attendanceExceptionRequestService.save(any(AttendanceExceptionRequest.class))).thenReturn(attendanceExceptionRequest);

        int result = requestService.createAttendanceExceptionRequest(generalAccount, otherTimeInput);
        assertEquals(1, result);
    }

    @Test
    void overTimeRequstBeforeSuccess()
    {
        Long requestShiftId = 1L;
        String requestBeginOverTime = "2025/12/23T07:00:00";
        String requestEndOverTime = "2025/12/23T09:00:00";
        String requestComment = "";
        String requestDate = "2025/09/02T10:00:00";

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
        shift.setBeginWork(LocalDateTime.parse(LocalDateTime.parse(shiftBegin,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        shift.setEndWork(LocalDateTime.parse(LocalDateTime.parse(shiftEnd,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));

        List<OverTimeRequest> overTimeRequests = new ArrayList<OverTimeRequest>();

        List<ShiftListOtherTime> shiftListOtherTimes = new ArrayList<ShiftListOtherTime>();

        List<OverTimeRequest> overTimeRequestMonth = new ArrayList<OverTimeRequest>();
        // 45時間ちょうどでエラーを起こさないかチェックするため10時間の残業を用意する
        OverTimeRequest firstOverTimeRequest = new OverTimeRequest();
        String firstOverTimeRequestBegin = "2025/12/10T00:00:00";
        String firstOverTimeRequestEnd = "2025/12/10T10:00:00";
        firstOverTimeRequest.setBeginWork(LocalDateTime.parse(LocalDateTime.parse(firstOverTimeRequestBegin,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        firstOverTimeRequest.setEndWork(LocalDateTime.parse(LocalDateTime.parse(firstOverTimeRequestEnd,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));

        OverTimeRequest secondOverTimeRequest = new OverTimeRequest();
        String secondOverTimeRequestBegin = "2025/12/11T00:00:00";
        String secondOverTimeRequestEnd = "2025/12/11T10:00:00";
        secondOverTimeRequest.setBeginWork(LocalDateTime.parse(LocalDateTime.parse(secondOverTimeRequestBegin,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        secondOverTimeRequest.setEndWork(LocalDateTime.parse(LocalDateTime.parse(secondOverTimeRequestEnd,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));

        OverTimeRequest thirdOverTimeRequest = new OverTimeRequest();
        String thirdOverTimeRequestBegin = "2025/12/12T00:00:00";
        String thirdOverTimeRequestEnd = "2025/12/12T10:00:00";
        thirdOverTimeRequest.setBeginWork(LocalDateTime.parse(LocalDateTime.parse(thirdOverTimeRequestBegin,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        thirdOverTimeRequest.setEndWork(LocalDateTime.parse(LocalDateTime.parse(thirdOverTimeRequestEnd,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));

        OverTimeRequest fourthOverTimeRequest = new OverTimeRequest();
        String fourthOverTimeRequestBegin = "2025/12/13T00:00:00";
        String fourthOverTimeRequestEnd = "2025/12/13T10:00:00";
        fourthOverTimeRequest.setBeginWork(LocalDateTime.parse(LocalDateTime.parse(fourthOverTimeRequestBegin,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        fourthOverTimeRequest.setEndWork(LocalDateTime.parse(LocalDateTime.parse(fourthOverTimeRequestEnd,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));

        OverTimeRequest fifthOverTimeRequest = new OverTimeRequest();
        String fifthOverTimeRequestBegin = "2025/12/14T06:00:00";
        String fifthOverTimeRequestEnd = "2025/12/14T09:00:00";
        fifthOverTimeRequest.setBeginWork(LocalDateTime.parse(LocalDateTime.parse(fifthOverTimeRequestBegin,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        fifthOverTimeRequest.setEndWork(LocalDateTime.parse(LocalDateTime.parse(fifthOverTimeRequestEnd,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));

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

        OverTimeInput overTimeInput = new OverTimeInput();
        overTimeInput.setShiftId(requestShiftId);
        overTimeInput.setBeginOverTime(requestBeginOverTime);
        overTimeInput.setEndOverTime(requestEndOverTime);
        overTimeInput.setRequestComment(requestComment);
        overTimeInput.setRequestDate(requestDate);
        
        when(shiftService.findByAccountIdAndShiftId(any(Account.class), anyLong())).thenReturn(shift);
        when(stringToLocalDateTime.stringToLocalDateTime(requestBeginOverTime)).thenReturn(LocalDateTime.parse(LocalDateTime.parse(requestBeginOverTime,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(stringToLocalDateTime.stringToLocalDateTime(requestEndOverTime)).thenReturn(LocalDateTime.parse(LocalDateTime.parse(requestEndOverTime,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(shiftListOtherTimeService.findByShiftId(any(Shift.class))).thenReturn(shiftListOtherTimes);
        when(legalTimeService.findFirstByOrderByBeginDesc()).thenReturn(legalTime);
        when(stringToDuration.stringToDuration(legalTime.getMonthlyOverWorkTime())).thenReturn(Duration.ofHours(Long.parseLong(monthlyOverTime.split(":")[0])).plusMinutes(Long.parseLong(monthlyOverTime.split(":")[1])).plusSeconds(Long.parseLong(monthlyOverTime.split(":")[2])));
        when(stringToLocalDateTime.stringToLocalDateTime(requestDate)).thenReturn(LocalDateTime.parse(LocalDateTime.parse(requestDate,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));

        when(overTimeRequestService.findByAccounIdAndRequestStatusWaitOrApprovedAndBeginWorkOrEndWorkBetween(any(Account.class), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(overTimeRequests);
        when(overTimeRequestService.findByAccountIdAndRequestStatusWaitOrApprovedAndBeginWorkBetweenMonth(any(Account.class), anyInt(), anyInt())).thenReturn(overTimeRequestMonth);
        when(overTimeRequestService.save(any(OverTimeRequest.class))).thenReturn(newOverTimeRequest);

        int result = requestService.createOverTimeRequest(generalAccount, overTimeInput);
        assertEquals(1, result);
    }
    
    @Test
    void overTimeRequstAfterSuccess()
    {
        Long requestShiftId = 1L;
        String requestBeginOverTime = "2025/12/23T18:00:00";
        String requestEndOverTime = "2025/12/23T20:00:00";
        String requestComment = "";
        String requestDate = "2025/09/02T10:00:00";

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
        shift.setBeginWork(LocalDateTime.parse(LocalDateTime.parse(shiftBegin,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        shift.setEndWork(LocalDateTime.parse(LocalDateTime.parse(shiftEnd,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));

        List<OverTimeRequest> overTimeRequests = new ArrayList<OverTimeRequest>();

        List<ShiftListOtherTime> shiftListOtherTimes = new ArrayList<ShiftListOtherTime>();

        List<OverTimeRequest> overTimeRequestMonth = new ArrayList<OverTimeRequest>();
        // 45時間ちょうどでエラーを起こさないかチェックするため10時間の残業を用意する
        OverTimeRequest firstOverTimeRequest = new OverTimeRequest();
        String firstOverTimeRequestBegin = "2025/12/10T00:00:00";
        String firstOverTimeRequestEnd = "2025/12/10T10:00:00";
        firstOverTimeRequest.setBeginWork(LocalDateTime.parse(LocalDateTime.parse(firstOverTimeRequestBegin,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        firstOverTimeRequest.setEndWork(LocalDateTime.parse(LocalDateTime.parse(firstOverTimeRequestEnd,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));

        OverTimeRequest secondOverTimeRequest = new OverTimeRequest();
        String secondOverTimeRequestBegin = "2025/12/11T00:00:00";
        String secondOverTimeRequestEnd = "2025/12/11T10:00:00";
        secondOverTimeRequest.setBeginWork(LocalDateTime.parse(LocalDateTime.parse(secondOverTimeRequestBegin,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        secondOverTimeRequest.setEndWork(LocalDateTime.parse(LocalDateTime.parse(secondOverTimeRequestEnd,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));

        OverTimeRequest thirdOverTimeRequest = new OverTimeRequest();
        String thirdOverTimeRequestBegin = "2025/12/12T00:00:00";
        String thirdOverTimeRequestEnd = "2025/12/12T10:00:00";
        thirdOverTimeRequest.setBeginWork(LocalDateTime.parse(LocalDateTime.parse(thirdOverTimeRequestBegin,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        thirdOverTimeRequest.setEndWork(LocalDateTime.parse(LocalDateTime.parse(thirdOverTimeRequestEnd,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));

        OverTimeRequest fourthOverTimeRequest = new OverTimeRequest();
        String fourthOverTimeRequestBegin = "2025/12/13T00:00:00";
        String fourthOverTimeRequestEnd = "2025/12/13T10:00:00";
        fourthOverTimeRequest.setBeginWork(LocalDateTime.parse(LocalDateTime.parse(fourthOverTimeRequestBegin,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        fourthOverTimeRequest.setEndWork(LocalDateTime.parse(LocalDateTime.parse(fourthOverTimeRequestEnd,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));

        OverTimeRequest fifthOverTimeRequest = new OverTimeRequest();
        String fifthOverTimeRequestBegin = "2025/12/14T06:00:00";
        String fifthOverTimeRequestEnd = "2025/12/14T09:00:00";
        fifthOverTimeRequest.setBeginWork(LocalDateTime.parse(LocalDateTime.parse(fifthOverTimeRequestBegin,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        fifthOverTimeRequest.setEndWork(LocalDateTime.parse(LocalDateTime.parse(fifthOverTimeRequestEnd,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
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

        OverTimeInput overTimeInput = new OverTimeInput();
        overTimeInput.setShiftId(requestShiftId);
        overTimeInput.setBeginOverTime(requestBeginOverTime);
        overTimeInput.setEndOverTime(requestEndOverTime);
        overTimeInput.setRequestComment(requestComment);
        overTimeInput.setRequestDate(requestDate);
        
        when(shiftService.findByAccountIdAndShiftId(any(Account.class), anyLong())).thenReturn(shift);
        when(stringToLocalDateTime.stringToLocalDateTime(requestBeginOverTime)).thenReturn(LocalDateTime.parse(LocalDateTime.parse(requestBeginOverTime,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(stringToLocalDateTime.stringToLocalDateTime(requestEndOverTime)).thenReturn(LocalDateTime.parse(LocalDateTime.parse(requestEndOverTime,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(shiftListOtherTimeService.findByShiftId(any(Shift.class))).thenReturn(shiftListOtherTimes);
        when(legalTimeService.findFirstByOrderByBeginDesc()).thenReturn(legalTime);
        when(stringToDuration.stringToDuration(legalTime.getMonthlyOverWorkTime())).thenReturn(Duration.ofHours(Long.parseLong(monthlyOverTime.split(":")[0])).plusMinutes(Long.parseLong(monthlyOverTime.split(":")[1])).plusSeconds(Long.parseLong(monthlyOverTime.split(":")[2])));
        when(stringToLocalDateTime.stringToLocalDateTime(requestDate)).thenReturn(LocalDateTime.parse(LocalDateTime.parse(requestDate,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));

        when(overTimeRequestService.findByAccounIdAndRequestStatusWaitOrApprovedAndBeginWorkOrEndWorkBetween(any(Account.class), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(overTimeRequests);
        when(overTimeRequestService.findByAccountIdAndRequestStatusWaitOrApprovedAndBeginWorkBetweenMonth(any(Account.class), anyInt(), anyInt())).thenReturn(overTimeRequestMonth);
        when(overTimeRequestService.save(any(OverTimeRequest.class))).thenReturn(newOverTimeRequest);

        int result = requestService.createOverTimeRequest(generalAccount, overTimeInput);
        assertEquals(1, result);
    }

    @Test
    void monthlyRequestSuccess()
    {
        int year = 2025;
        int month = 1;
        String requestComment = "";
        String requestDate = "2025/09/02T10:00:00";

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
        generalShiftListShiftRequest.setShiftRequestId(shiftRequest);
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
        for(Attend generalAttend : attends)
        {
            monthWorkTime = monthWorkTime.plus(Duration.between(LocalTime.MIDNIGHT, generalAttend.getWorkTime().toLocalTime()));
            monthLateness = monthLateness.plus(Duration.between(LocalTime.MIDNIGHT, generalAttend.getLateness().toLocalTime()));
            monthLeaveEarly = monthLeaveEarly.plus(Duration.between(LocalTime.MIDNIGHT, generalAttend.getLeaveEarly().toLocalTime()));
            monthOuting = monthOuting.plus(Duration.between(LocalTime.MIDNIGHT, generalAttend.getOuting().toLocalTime()));
            monthOverWork = monthOverWork.plus(Duration.between(LocalTime.MIDNIGHT, generalAttend.getOverWork().toLocalTime()));
            monthAbsenceTime = monthAbsenceTime.plus(Duration.between(LocalTime.MIDNIGHT, generalAttend.getAbsenceTime().toLocalTime()));
            monthSpecialTime = monthSpecialTime.plus(Duration.between(LocalTime.MIDNIGHT, generalAttend.getVacationTime().toLocalTime()));
            monthLateNightWorkTime = monthLateNightWorkTime.plus(Duration.between(LocalTime.MIDNIGHT, generalAttend.getLateNightWork().toLocalTime()));
            monthHolidayWorkTime = monthHolidayWorkTime.plus(Duration.between(LocalTime.MIDNIGHT, generalAttend.getHolidayWork().toLocalTime()));
        }
        for(Vacation vacation : vacations)
        {
            monthPaydHolidayTime = monthPaydHolidayTime.plus(Duration.between(vacation.getBeginVacation(), vacation.getEndVacation()));
        }
        monthSpecialTime = monthSpecialTime.minus(monthPaydHolidayTime);

        MonthlyInput monthlyInput = new MonthlyInput();
        monthlyInput.setYear(year);
        monthlyInput.setMonth(month);
        monthlyInput.setRequestComment(requestComment);
        monthlyInput.setRequestDate(requestDate);

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
        when(durationToString.durationToString(monthWorkTime)).thenReturn(String.format("%03d:%02d:%02d", monthWorkTime.getSeconds() / 3600, (monthWorkTime.getSeconds() % 3600) / 60, monthWorkTime.getSeconds() % 60));
        when(durationToString.durationToString(monthLateness)).thenReturn(String.format("%03d:%02d:%02d", monthLateness.getSeconds() / 3600, (monthLateness.getSeconds() % 3600) / 60, monthLateness.getSeconds() % 60));
        when(durationToString.durationToString(monthLeaveEarly)).thenReturn(String.format("%03d:%02d:%02d", monthLeaveEarly.getSeconds() / 3600, (monthLeaveEarly.getSeconds() % 3600) / 60, monthLeaveEarly.getSeconds() % 60));
        when(durationToString.durationToString(monthOuting)).thenReturn(String.format("%03d:%02d:%02d", monthOuting.getSeconds() / 3600, (monthOuting.getSeconds() % 3600) / 60, monthOuting.getSeconds() % 60));
        when(durationToString.durationToString(monthOverWork)).thenReturn(String.format("%03d:%02d:%02d", monthOverWork.getSeconds() / 3600, (monthOverWork.getSeconds() % 3600) / 60, monthOverWork.getSeconds() % 60));
        when(durationToString.durationToString(monthAbsenceTime)).thenReturn(String.format("%03d:%02d:%02d", monthAbsenceTime.getSeconds() / 3600, (monthAbsenceTime.getSeconds() % 3600) / 60, monthAbsenceTime.getSeconds() % 60));
        when(durationToString.durationToString(monthPaydHolidayTime)).thenReturn(String.format("%03d:%02d:%02d", monthPaydHolidayTime.getSeconds() / 3600, (monthPaydHolidayTime.getSeconds() % 3600) / 60, monthPaydHolidayTime.getSeconds() % 60));
        when(durationToString.durationToString(monthSpecialTime)).thenReturn(String.format("%03d:%02d:%02d", monthSpecialTime.getSeconds() / 3600, (monthSpecialTime.getSeconds() % 3600) / 60, monthSpecialTime.getSeconds() % 60));
        when(durationToString.durationToString(monthHolidayWorkTime)).thenReturn(String.format("%03d:%02d:%02d", monthHolidayWorkTime.getSeconds() / 3600, (monthHolidayWorkTime.getSeconds() % 3600) / 60, monthHolidayWorkTime.getSeconds() % 60));
        when(durationToString.durationToString(monthLateNightWorkTime)).thenReturn(String.format("%03d:%02d:%02d", monthLateNightWorkTime.getSeconds() / 3600, (monthLateNightWorkTime.getSeconds() % 3600) / 60, monthLateNightWorkTime.getSeconds() % 60));
        when(stringToLocalDateTime.stringToLocalDateTime(requestDate)).thenReturn(LocalDateTime.parse(LocalDateTime.parse(requestDate,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));

        when(monthlyRequestService.save(any(MonthlyRequest.class))).thenReturn(monthlyRequest);

        int result = requestService.createMonthlyRequest(generalAccount, monthlyInput);
        assertEquals(1, result);
    }

    @Test
    void shiftRequestDatailSuccess()
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

        ShiftRequest shiftRequest = new ShiftRequest();

        LocalDateTime beginWork = LocalDateTime.parse("2025/08/08/09/30/00",DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        LocalDateTime endWork = LocalDateTime.parse("2025/08/08/12/30/00",DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        LocalDateTime beginBreak = LocalDateTime.parse("2025/08/13/09/30/00",DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        LocalDateTime endBreak = LocalDateTime.parse("2025/08/08/18/30/00",DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        String requestComment = "";
        LocalDateTime requestDate = LocalDateTime.parse("2025/07/07/00/00/00",DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        int requestStatus = 1;
        LocalDateTime approvalTime = null;
        String approverComment = null;

        Long shiftRequestId = 1L;

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

        when(shiftRequestService.findByAccountIdAndShiftRequestId(any(Account.class),anyLong())).thenReturn(shiftRequest);
        when(shiftRequestService.mapToDetailResponse(any(ShiftRequest.class))).thenReturn(requestDetailShiftResponse);

        RequestDetailShiftResponse resultRequestDetailShiftResponse = requestService.getShiftDetail(adminAccount, shiftRequestId);
        assertEquals(1, resultRequestDetailShiftResponse.getStatus());
    }

    @Test
    void shiftChangeRequestDetailSuccess()
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

        Long shiftId = 1L;

        ShiftChangeRequest shiftChangeRequest = new ShiftChangeRequest();
        LocalDateTime beginWork = LocalDateTime.parse("2025/08/08/09/30/00",DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        LocalDateTime endWork = LocalDateTime.parse("2025/08/08/12/30/00",DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        LocalDateTime beginBreak = LocalDateTime.parse("2025/08/13/09/30/00",DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        LocalDateTime endBreak = LocalDateTime.parse("2025/08/08/18/30/00",DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        String requestComment = "";
        LocalDateTime requestDate = LocalDateTime.parse("2025/07/07/00/00/00",DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        int requestStatus = 1;
        LocalDateTime approvalTime = null;
        String approverComment = null;

        Long shiftChangeRequestId = 1L;

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

        when(shiftChangeRequestService.findByAccountIdAndShiftChangeRequestId(any(Account.class), anyLong())).thenReturn(shiftChangeRequest);
        when(shiftChangeRequestService.mapToDetailResponse(any(ShiftChangeRequest.class))).thenReturn(requestDetailShiftChangeResponse);

        RequestDetailShiftChangeResponse resultRequestDetailShiftChangeResponse = requestService.getShiftChangeDetail(adminAccount, shiftChangeRequestId);

        assertEquals(1, resultRequestDetailShiftChangeResponse.getStatus());
    }

    @Test
    void stampRequestDetailSuccess()
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

        when(stampRequestService.findByAccountIdAndStampId(any(Account.class), anyLong())).thenReturn(stampRequest);
        when(stampRequestService.mapToDetailResponse(any(StampRequest.class))).thenReturn(requestDetailStampResponse);

        RequestDetailStampResponse resultRequestDetailStampResponse = requestService.getStampDetail(adminAccount, stampId);

        assertEquals(1, resultRequestDetailStampResponse.getStatus());
    }

    @Test
    void vacationRequestDetailSuccess()
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

        when(vacationRequestService.findByAccountIdAndVacationId(any(Account.class),anyLong())).thenReturn(vacationRequest);
        when(vacationRequestService.mapToDetailResponse(vacationRequest)).thenReturn(requestDetailVacationResponse);

        RequestDetailVacationResponse resutlRequestDetailVacationResponse = requestService.getVacationDetail(adminAccount, vacationRequestId);
        assertEquals(1, resutlRequestDetailVacationResponse.getStatus());
    }

    @Test
    void overTimeRequestDetailSuccess()
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

        when(overTimeRequestService.findByAccountIdAndOverTimeRequestId(any(Account.class),anyLong())).thenReturn(overTimeRequest);
        when(overTimeRequestService.mapToDetailResponse(any(OverTimeRequest.class))).thenReturn(requestDetailOverTimeResponse);

        RequestDetailOverTimeResponse resultRequestDetailOverTimeResponse = requestService.getOverTimeDetail(adminAccount, overTimeRequestId);
        assertEquals(1, resultRequestDetailOverTimeResponse.getStatus());
    }

    @Test
    void otherTimeRequestDetailSuccess()
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

        when(attendanceExceptionRequestService.findByAccountIdAndAttendanceExceptionId(any(Account.class),anyLong())).thenReturn(attendanceExceptionRequest);
        when(attendanceExceptionRequestService.mapTorequestDetail(any(AttendanceExceptionRequest.class))).thenReturn(requestDetailOtherTimeResponse);

        RequestDetailOtherTimeResponse resultRequestDetailOtherTimeResponse = requestService.getOtherTimeDetail(adminAccount, attendanceExceptionRequestId);
        assertEquals(1, resultRequestDetailOtherTimeResponse.getStatus());
    }

    @Test
    void monthlyRequestDetailSuccess()
    {
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
        generalMonthlyRequest.setApprovalDate(Objects.isNull(generalApprovalTime) ? null : LocalDateTime.parse(LocalDateTime.parse(generalApprovalTime,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
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

        when(monthlyRequestService.findByAccountIdAndMothlyRequestId(any(Account.class), anyLong())).thenReturn(generalMonthlyRequest);
        when(monthlyRequestService.mapToDetailResponse(generalMonthlyRequest)).thenReturn(requestDetailMonthlyResponse);

        RequestDetailMonthlyResponse resultRequestDetailMonthlyResponse = requestService.getMonthlyDetail(adminAccount, adminAccountId);
        assertEquals(1, resultRequestDetailMonthlyResponse.getStatus());
    }

    @Test
    void withdrowShiftSuccess()
    {
        Long requestId = 1L;
        int requestType = 1;

        Account generalAccount = new Account();
        Long generalAccountId = 3L;
        String generalAccountUsername = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        ShiftRequest shiftRequest = new ShiftRequest();
        Long shiftRequestid = 43L;
        shiftRequest.setShiftRequestId(shiftRequestid);
        shiftRequest.setRequestStatus(1);

        WithDrowInput withDrowInput = new WithDrowInput();
        withDrowInput.setRequestType(requestType);
        withDrowInput.setRequestId(requestId);

        when(shiftRequestService.findByAccountIdAndShiftRequestId(any(Account.class), anyLong())).thenReturn(shiftRequest);
        when(shiftRequestService.save(any(ShiftRequest.class))).thenReturn(shiftRequest);

        int result = requestService.withdrow(generalAccount, withDrowInput);
        assertEquals(1, result);
    }

    @Test
    void withdrowShiftChangeSuccess()
    {
        Long requestId = 5L;
        int requestType = 2;

        Account generalAccount = new Account();
        Long generalAccountId = 3L;
        String generalAccountUsername = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        ShiftChangeRequest shiftChangeRequest = new ShiftChangeRequest();
        Long shiftChangeRequestId = 97L;
        shiftChangeRequest.setShiftChangeId(shiftChangeRequestId);
        shiftChangeRequest.setRequestStatus(1);

        WithDrowInput withDrowInput = new WithDrowInput();
        withDrowInput.setRequestType(requestType);
        withDrowInput.setRequestId(requestId);

        when(shiftChangeRequestService.findByAccountIdAndShiftChangeRequestId(any(Account.class), anyLong())).thenReturn(shiftChangeRequest);
        when(shiftChangeRequestService.save(any(ShiftChangeRequest.class))).thenReturn(shiftChangeRequest);

        int result = requestService.withdrow(generalAccount, withDrowInput);
        assertEquals(1, result);
    }

    @Test
    void withdrowStampSuccess()
    {
        Long requestId = 3L;
        int requestType = 3;

        Account generalAccount = new Account();
        Long generalAccountId = 3L;
        String generalAccountUsername = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        StampRequest stampRequest = new StampRequest();
        Long stampRequestId = 9L;
        stampRequest.setStampId(stampRequestId);
        stampRequest.setRequestStatus(1);

        WithDrowInput withDrowInput = new WithDrowInput();
        withDrowInput.setRequestType(requestType);
        withDrowInput.setRequestId(requestId);

        when(stampRequestService.findByAccountIdAndStampId(any(Account.class), anyLong())).thenReturn(stampRequest);
        when(stampRequestService.save(any(StampRequest.class))).thenReturn(stampRequest);

        int result = requestService.withdrow(generalAccount, withDrowInput);
        assertEquals(1, result);
    }

    @Test
    void withdrowVacationSuccess()
    {
        Long requestId = 8L;
        int requestType = 4;

        Account generalAccount = new Account();
        Long generalAccountId = 3L;
        String generalAccountUsername = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        VacationRequest vacationRequest = new VacationRequest();
        Long vacationRequestId = 30L;
        vacationRequest.setVacationId(vacationRequestId);
        vacationRequest.setRequestStatus(1);

        WithDrowInput withDrowInput = new WithDrowInput();
        withDrowInput.setRequestType(requestType);
        withDrowInput.setRequestId(requestId);

        when(vacationRequestService.findByAccountIdAndVacationId(any(Account.class), anyLong())).thenReturn(vacationRequest);
        when(vacationRequestService.save(any(VacationRequest.class))).thenReturn(vacationRequest);

        int result = requestService.withdrow(generalAccount, withDrowInput);
        assertEquals(1, result);
    }
    
    @Test
    void withdrowOverTimeSuccess()
    {
        Long requestId = 2L;
        int requestType = 5;

        Account generalAccount = new Account();
        Long generalAccountId = 3L;
        String generalAccountUsername = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        OverTimeRequest overTimeRequest = new OverTimeRequest();
        Long overTimeRequestId = 498L;
        overTimeRequest.setOverTimeId(overTimeRequestId);
        overTimeRequest.setRequestStatus(1);

        WithDrowInput withDrowInput = new WithDrowInput();
        withDrowInput.setRequestType(requestType);
        withDrowInput.setRequestId(requestId);

        when(overTimeRequestService.findByAccountIdAndOverTimeRequestId(any(Account.class), anyLong())).thenReturn(overTimeRequest);
        when(overTimeRequestService.save(any(OverTimeRequest.class))).thenReturn(overTimeRequest);

        int result = requestService.withdrow(generalAccount, withDrowInput);
        assertEquals(1, result);
    }

    @Test
    void withdrowOtherTimeSuccess()
    {
        Long requestId = 38L;
        int requestType = 6;

        Account generalAccount = new Account();
        Long generalAccountId = 3L;
        String generalAccountUsername = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        AttendanceExceptionRequest attendanceExceptionRequest = new AttendanceExceptionRequest();
        Long attendanceExceptionRequestId = 49L;
        attendanceExceptionRequest.setAttendanceExceptionId(attendanceExceptionRequestId);
        attendanceExceptionRequest.setRequestStatus(1);

        WithDrowInput withDrowInput = new WithDrowInput();
        withDrowInput.setRequestType(requestType);
        withDrowInput.setRequestId(requestId);

        when(attendanceExceptionRequestService.findByAccountIdAndAttendanceExceptionId(any(Account.class), anyLong())).thenReturn(attendanceExceptionRequest);
        when(attendanceExceptionRequestService.save(any(AttendanceExceptionRequest.class))).thenReturn(attendanceExceptionRequest);

        int result = requestService.withdrow(generalAccount, withDrowInput);
        assertEquals(1, result);
    }

    @Test
    void withdrowMonthlySuccess()
    {
        Long requestId = 29L;
        int requestType = 7;

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

        WithDrowInput withDrowInput = new WithDrowInput();
        withDrowInput.setRequestType(requestType);
        withDrowInput.setRequestId(requestId);

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
        when(vacationRequestService.save(any(VacationRequest.class))).thenReturn(vacationRequest);
        when(attendService.findByAccountIdAndBeginWorkBetween(any(Account.class), anyInt(), anyInt())).thenReturn(attends);
        when(attendanceListSourceService.findByAttendIdIn(anyList())).thenReturn(attendanceListSources);
        when(stampRequestService.save(any(StampRequest.class))).thenReturn(stampRequest);
        when(monthlyRequestService.save(any(MonthlyRequest.class))).thenReturn(monthlyRequest);

        int result = requestService.withdrow(generalAccount, withDrowInput);
        assertEquals(1, result);
    }

    @Test
    void rejectShiftSuccess()
    {
        Long requestId = 1L;
        int requestType = 1;

        Account generalAccount = new Account();
        Long generalAccountId = 3L;
        String generalAccountUsername = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        Account adminAccount = new Account();
        Long adminAccountId = 4L;
        String adminAccountUsername = "hoge";
        adminAccount.setId(adminAccountId);
        adminAccount.setUsername(adminAccountUsername);

        AccountApprover accountApprover = new AccountApprover();

        ShiftRequest shiftRequest = new ShiftRequest();
        Long shiftRequestid = 43L;
        shiftRequest.setShiftRequestId(shiftRequestid);
        shiftRequest.setAccountId(generalAccount);
        shiftRequest.setRequestStatus(1);

        String requestComment = "hogehoge";
        String requestDate = "2025/12/13T00:00:00";
        RequestJudgmentInput requestJudgmentInput = new RequestJudgmentInput();
        requestJudgmentInput.setRequestType(requestType);
        requestJudgmentInput.setRequestId(requestId);
        requestJudgmentInput.setApprovalComment(requestComment);
        requestJudgmentInput.setRequestTime(requestDate);

        when(shiftRequestService.findById(anyLong())).thenReturn(shiftRequest);
        when(accountApproverService.findAccountAndApprover(any(Account.class), any(Account.class))).thenReturn(accountApprover);
        when(stringToLocalDateTime.stringToLocalDateTime(anyString())).thenReturn(LocalDateTime.parse(LocalDateTime.parse(requestDate,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(shiftRequestService.save(any(ShiftRequest.class))).thenReturn(shiftRequest);

        int result = requestService.reject(adminAccount, requestJudgmentInput);
        assertEquals(1, result);
    }

    @Test
    void rejectShiftChangeSuccess()
    {
        Long requestId = 5L;
        int requestType = 2;

        Account generalAccount = new Account();
        Long generalAccountId = 3L;
        String generalAccountUsername = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        Account adminAccount = new Account();
        Long adminAccountId = 4L;
        String adminAccountUsername = "hoge";
        adminAccount.setId(adminAccountId);
        adminAccount.setUsername(adminAccountUsername);

        AccountApprover accountApprover = new AccountApprover();

        ShiftChangeRequest shiftChangeRequest = new ShiftChangeRequest();
        Long shiftChangeRequestId = 97L;
        shiftChangeRequest.setShiftChangeId(shiftChangeRequestId);
        shiftChangeRequest.setAccountId(generalAccount);
        shiftChangeRequest.setRequestStatus(1);

        String requestComment = "hogehoge";
        String requestDate = "2025/12/13T00:00:00";
        RequestJudgmentInput requestJudgmentInput = new RequestJudgmentInput();
        requestJudgmentInput.setRequestType(requestType);
        requestJudgmentInput.setRequestId(requestId);
        requestJudgmentInput.setApprovalComment(requestComment);
        requestJudgmentInput.setRequestTime(requestDate);

        when(shiftChangeRequestService.findById(anyLong())).thenReturn(shiftChangeRequest);
        when(accountApproverService.findAccountAndApprover(any(Account.class), any(Account.class))).thenReturn(accountApprover);
        when(stringToLocalDateTime.stringToLocalDateTime(anyString())).thenReturn(LocalDateTime.parse(LocalDateTime.parse(requestDate,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(shiftChangeRequestService.save(any(ShiftChangeRequest.class))).thenReturn(shiftChangeRequest);

        int result = requestService.reject(adminAccount, requestJudgmentInput);
        assertEquals(1, result);
    }

    @Test
    void rejectStampSuccess()
    {
        Long requestId = 3L;
        int requestType = 3;

        Account generalAccount = new Account();
        Long generalAccountId = 3L;
        String generalAccountUsername = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        Account adminAccount = new Account();
        Long adminAccountId = 4L;
        String adminAccountUsername = "hoge";
        adminAccount.setId(adminAccountId);
        adminAccount.setUsername(adminAccountUsername);

        AccountApprover accountApprover = new AccountApprover();

        StampRequest stampRequest = new StampRequest();
        Long stampRequestId = 9L;
        stampRequest.setStampId(stampRequestId);
        stampRequest.setAccountId(generalAccount);
        stampRequest.setRequestStatus(1);

        String requestComment = "hogehoge";
        String requestDate = "2025/12/13T00:00:00";
        RequestJudgmentInput requestJudgmentInput = new RequestJudgmentInput();
        requestJudgmentInput.setRequestType(requestType);
        requestJudgmentInput.setRequestId(requestId);
        requestJudgmentInput.setApprovalComment(requestComment);
        requestJudgmentInput.setRequestTime(requestDate);

        when(stampRequestService.findById(anyLong())).thenReturn(stampRequest);
        when(accountApproverService.findAccountAndApprover(any(Account.class), any(Account.class))).thenReturn(accountApprover);
        when(stringToLocalDateTime.stringToLocalDateTime(anyString())).thenReturn(LocalDateTime.parse(LocalDateTime.parse(requestDate,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(stampRequestService.save(any(StampRequest.class))).thenReturn(stampRequest);

        int result = requestService.reject(adminAccount, requestJudgmentInput);
        assertEquals(1, result);
    }

    @Test
    void rejectVacationSuccess()
    {
        Long requestId = 8L;
        int requestType = 4;

        Account generalAccount = new Account();
        Long generalAccountId = 3L;
        String generalAccountUsername = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        Account adminAccount = new Account();
        Long adminAccountId = 4L;
        String adminAccountUsername = "hoge";
        adminAccount.setId(adminAccountId);
        adminAccount.setUsername(adminAccountUsername);

        AccountApprover accountApprover = new AccountApprover();

        VacationRequest vacationRequest = new VacationRequest();
        Long vacationRequestId = 30L;
        vacationRequest.setVacationId(vacationRequestId);
        vacationRequest.setAccountId(generalAccount);
        vacationRequest.setRequestStatus(1);

        String requestComment = "hogehoge";
        String requestDate = "2025/12/13T00:00:00";
        RequestJudgmentInput requestJudgmentInput = new RequestJudgmentInput();
        requestJudgmentInput.setRequestType(requestType);
        requestJudgmentInput.setRequestId(requestId);
        requestJudgmentInput.setApprovalComment(requestComment);
        requestJudgmentInput.setRequestTime(requestDate);

        when(vacationRequestService.findById(anyLong())).thenReturn(vacationRequest);
        when(accountApproverService.findAccountAndApprover(any(Account.class), any(Account.class))).thenReturn(accountApprover);
        when(stringToLocalDateTime.stringToLocalDateTime(anyString())).thenReturn(LocalDateTime.parse(LocalDateTime.parse(requestDate,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(vacationRequestService.save(any(VacationRequest.class))).thenReturn(vacationRequest);

        int result = requestService.reject(adminAccount, requestJudgmentInput);
        assertEquals(1, result);
    }
    
    @Test
    void rejectOverTimeSuccess()
    {
        Long requestId = 2L;
        int requestType = 5;

        Account generalAccount = new Account();
        Long generalAccountId = 3L;
        String generalAccountUsername = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        Account adminAccount = new Account();
        Long adminAccountId = 4L;
        String adminAccountUsername = "hoge";
        adminAccount.setId(adminAccountId);
        adminAccount.setUsername(adminAccountUsername);

        AccountApprover accountApprover = new AccountApprover();

        OverTimeRequest overTimeRequest = new OverTimeRequest();
        Long overTimeRequestId = 498L;
        overTimeRequest.setOverTimeId(overTimeRequestId);
        overTimeRequest.setAccountId(generalAccount);
        overTimeRequest.setRequestStatus(1);

        String requestComment = "hogehoge";
        String requestDate = "2025/12/13T00:00:00";
        RequestJudgmentInput requestJudgmentInput = new RequestJudgmentInput();
        requestJudgmentInput.setRequestType(requestType);
        requestJudgmentInput.setRequestId(requestId);
        requestJudgmentInput.setApprovalComment(requestComment);
        requestJudgmentInput.setRequestTime(requestDate);

        when(overTimeRequestService.findById(anyLong())).thenReturn(overTimeRequest);
        when(accountApproverService.findAccountAndApprover(any(Account.class), any(Account.class))).thenReturn(accountApprover);
        when(stringToLocalDateTime.stringToLocalDateTime(anyString())).thenReturn(LocalDateTime.parse(LocalDateTime.parse(requestDate,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(overTimeRequestService.save(any(OverTimeRequest.class))).thenReturn(overTimeRequest);

        int result = requestService.reject(adminAccount, requestJudgmentInput);
        assertEquals(1, result);
    }

    @Test
    void rejectOtherTimeSuccess()
    {
        Long requestId = 38L;
        int requestType = 6;

        Account generalAccount = new Account();
        Long generalAccountId = 3L;
        String generalAccountUsername = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        Account adminAccount = new Account();
        Long adminAccountId = 4L;
        String adminAccountUsername = "hoge";
        adminAccount.setId(adminAccountId);
        adminAccount.setUsername(adminAccountUsername);

        AccountApprover accountApprover = new AccountApprover();

        AttendanceExceptionRequest attendanceExceptionRequest = new AttendanceExceptionRequest();
        Long attendanceExceptionRequestId = 49L;
        attendanceExceptionRequest.setAttendanceExceptionId(attendanceExceptionRequestId);
        attendanceExceptionRequest.setAccountId(generalAccount);
        attendanceExceptionRequest.setRequestStatus(1);

        String requestComment = "hogehoge";
        String requestDate = "2025/12/13T00:00:00";
        RequestJudgmentInput requestJudgmentInput = new RequestJudgmentInput();
        requestJudgmentInput.setRequestType(requestType);
        requestJudgmentInput.setRequestId(requestId);
        requestJudgmentInput.setApprovalComment(requestComment);
        requestJudgmentInput.setRequestTime(requestDate);

        when(attendanceExceptionRequestService.findById(anyLong())).thenReturn(attendanceExceptionRequest);
        when(accountApproverService.findAccountAndApprover(any(Account.class), any(Account.class))).thenReturn(accountApprover);
        when(stringToLocalDateTime.stringToLocalDateTime(anyString())).thenReturn(LocalDateTime.parse(LocalDateTime.parse(requestDate,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(attendanceExceptionRequestService.save(any(AttendanceExceptionRequest.class))).thenReturn(attendanceExceptionRequest);

        int result = requestService.reject(adminAccount, requestJudgmentInput);
        assertEquals(1, result);
    }

    @Test
    void rejectMonthlySuccess()
    {
        Long requestId = 29L;
        int requestType = 7;

        Account generalAccount = new Account();
        Long generalAccountId = 3L;
        String generalAccountUsername = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        Account adminAccount = new Account();
        Long adminAccountId = 4L;
        String adminAccountUsername = "hoge";
        adminAccount.setId(adminAccountId);
        adminAccount.setUsername(adminAccountUsername);

        AccountApprover accountApprover = new AccountApprover();

        MonthlyRequest monthlyRequest = new MonthlyRequest();
        monthlyRequest.setAccountId(generalAccount);
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

        String requestComment = "hogehoge";
        String requestDate = "2025/12/13T00:00:00";
        RequestJudgmentInput requestJudgmentInput = new RequestJudgmentInput();
        requestJudgmentInput.setRequestType(requestType);
        requestJudgmentInput.setRequestId(requestId);
        requestJudgmentInput.setApprovalComment(requestComment);
        requestJudgmentInput.setRequestTime(requestDate);

        when(monthlyRequestService.findById(anyLong())).thenReturn(monthlyRequest);
        when(accountApproverService.findAccountAndApprover(any(Account.class), any(Account.class))).thenReturn(accountApprover);
        when(shiftService.findByAccountIdAndBeginWorkBetween(any(Account.class), anyInt(), anyInt())).thenReturn(shifts);
        when(shiftListOtherTimeService.findByShiftIdIn(anyList())).thenReturn(shiftListOtherTimes);
        when(shiftListOverTimeService.findByShiftIdIn(anyList())).thenReturn(shiftListOverTimes);
        when(shiftListShiftRequestService.findByShiftIdIn(anyList())).thenReturn(shiftListShiftRequests);
        when(shiftListVacationService.findByShiftIdIn(anyList())).thenReturn(shiftListVacations);
        when(attendanceExceptionRequestService.save(any(AttendanceExceptionRequest.class))).thenReturn(attendanceExceptionRequest);
        when(overTimeRequestService.save(any(OverTimeRequest.class))).thenReturn(overTimeRequest);
        when(shiftRequestService.save(any(ShiftRequest.class))).thenReturn(shiftRequest);
        when(shiftChangeRequestService.save(any(ShiftChangeRequest.class))).thenReturn(shiftChangeRequest);
        when(vacationRequestService.save(any(VacationRequest.class))).thenReturn(vacationRequest);
        when(attendService.findByAccountIdAndBeginWorkBetween(any(Account.class), anyInt(), anyInt())).thenReturn(attends);
        when(attendanceListSourceService.findByAttendIdIn(anyList())).thenReturn(attendanceListSources);
        when(stampRequestService.save(any(StampRequest.class))).thenReturn(stampRequest);
        when(stringToLocalDateTime.stringToLocalDateTime(anyString())).thenReturn(LocalDateTime.parse(LocalDateTime.parse(requestDate,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(monthlyRequestService.save(any(MonthlyRequest.class))).thenReturn(monthlyRequest);

        int result = requestService.reject(adminAccount, requestJudgmentInput);
        assertEquals(1, result);
    }

    @Test
    void approvalShiftSuccess()
    {
        Account adminAccount = new Account();
        Long adminAccountId = 34L;
        String adminAccountUsername = "kanrisya";
        adminAccount.setId(adminAccountId);
        adminAccount.setUsername(adminAccountUsername);

        Account generalAccount = new Account();
        Long generalAccountId = 3L;
        String generalAccountUsername = "nanasi";
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        AccountApprover accountApprover = new AccountApprover();
        accountApprover.setAccountId(generalAccount);
        accountApprover.setApproverId(adminAccount);

        RequestJudgmentInput requestJudgmentInput = new RequestJudgmentInput();
        requestJudgmentInput.setRequestId(1L);
        requestJudgmentInput.setRequestType(1);
        requestJudgmentInput.setRequestTime("2025/06/06T19:00:00");
        requestJudgmentInput.setApprovalComment("test");

        ShiftRequest shiftRequest = new ShiftRequest();
        shiftRequest.setAccountId(generalAccount);
        shiftRequest.setRequestStatus(1);
        shiftRequest.setBeginWork(LocalDateTime.parse("2025/12/25/08/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));

        List<Shift> shifts = new ArrayList<Shift>();
        Shift firstShift = new Shift();
        Shift secondShift = new Shift();
        Shift thirdShift = new Shift();
        Shift fourthShift = new Shift();
        Shift fifthShift = new Shift();
        Shift sixth = new Shift();
        shifts.add(firstShift);
        shifts.add(secondShift);
        shifts.add(thirdShift);
        shifts.add(fourthShift);
        shifts.add(fifthShift);
        shifts.add(sixth);

        ShiftRequest newShiftRequest = new ShiftRequest();

        Shift shift = new Shift();
        shift.setAccountId(generalAccount);
        shift.setBeginWork(null);
        shift.setEndWork(null);
        shift.setBeginBreak(null);
        shift.setEndBreak(null);
        shift.setLateness(Time.valueOf("00:00:00"));
        shift.setLeaveEarly(Time.valueOf("00:00:00"));
        shift.setOuting(Time.valueOf("00:00:00"));
        shift.setOverWork(Time.valueOf("00:00:00"));

        Shift newShift = new Shift();

        ShiftListShiftRequest shiftListShiftRequest = new ShiftListShiftRequest();

        when(shiftRequestService.findById(anyLong())).thenReturn(shiftRequest);
        when(accountApproverService.findAccountAndApprover(any(Account.class), any(Account.class))).thenReturn(accountApprover);
        when(stringToLocalDateTime.stringToLocalDateTime(anyString())).thenReturn(LocalDateTime.parse(LocalDateTime.parse(requestJudgmentInput.getRequestTime(),DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(shiftService.findByAccountIdAndBeginWorkBetweenWeek(any(Account.class), any(LocalDateTime.class))).thenReturn(shifts);
        when(shiftRequestService.save(any(ShiftRequest.class))).thenReturn(newShiftRequest);
        when(shiftRequestService.shiftRequestToShift(any(ShiftRequest.class))).thenReturn(shift);
        when(shiftService.save(any(Shift.class))).thenReturn(newShift);
        when(shiftListShiftRequestService.save(any(ShiftListShiftRequest.class))).thenReturn(shiftListShiftRequest);

        int result = requestService.approval(adminAccount, requestJudgmentInput);
        assertEquals(1, result);
    }

    @Test
    void approvalShiftChangeSuccess()
    {
        Account adminAccount = new Account();
        Long adminAccountId = 34L;
        String adminAccountUsername = "kanrisya";
        adminAccount.setId(adminAccountId);
        adminAccount.setUsername(adminAccountUsername);

        Account generalAccount = new Account();
        Long generalAccountId = 3L;
        String generalAccountUsername = "nanasi";
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        AccountApprover accountApprover = new AccountApprover();
        accountApprover.setAccountId(generalAccount);
        accountApprover.setApproverId(adminAccount);

        RequestJudgmentInput requestJudgmentInput = new RequestJudgmentInput();
        requestJudgmentInput.setRequestId(1L);
        requestJudgmentInput.setRequestType(2);
        requestJudgmentInput.setRequestTime("2025/06/06T19:00:00");
        requestJudgmentInput.setApprovalComment("test");

        ShiftChangeRequest shiftChangeRequest = new ShiftChangeRequest();
        shiftChangeRequest.setAccountId(generalAccount);
        shiftChangeRequest.setShiftId(new Shift());
        shiftChangeRequest.setRequestStatus(1);
        shiftChangeRequest.setBeginWork(LocalDateTime.parse("2025/12/03/09/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));

        List<Shift> shifts = new ArrayList<Shift>();

        ShiftChangeRequest newShiftChangeRequest = new ShiftChangeRequest();

        Shift shift = new Shift();
        shift.setAccountId(generalAccount);
        shift.setBeginWork(null);
        shift.setEndWork(null);
        shift.setBeginBreak(null);
        shift.setEndBreak(null);
        shift.setLateness(Time.valueOf("00:00:00"));
        shift.setLeaveEarly(Time.valueOf("00:00:00"));
        shift.setOuting(Time.valueOf("00:00:00"));
        shift.setOverWork(Time.valueOf("00:00:00"));

        Shift newShift = new Shift();

        ShiftListShiftRequest shiftListShiftRequest = new ShiftListShiftRequest();

        ShiftListShiftRequest newShiftListShiftRequest = new ShiftListShiftRequest();

        List<ShiftListOtherTime> shiftListOtherTimes = new ArrayList<ShiftListOtherTime>();
        ShiftListOtherTime shiftListOtherTime = new ShiftListOtherTime();
        AttendanceExceptionRequest attendanceExceptionRequest = new AttendanceExceptionRequest();
        shiftListOtherTime.setAttendanceExceptionId(attendanceExceptionRequest);
        shiftListOtherTimes.add(shiftListOtherTime);

        AttendanceExceptionRequest newAttendanceExceptionRequest = new AttendanceExceptionRequest();

        List<ShiftListOverTime> shiftListOverTimes = new ArrayList<ShiftListOverTime>();
        ShiftListOverTime shiftListOverTime = new ShiftListOverTime();
        OverTimeRequest overTimeRequest = new OverTimeRequest();
        shiftListOverTime.setOverTimeId(overTimeRequest);
        shiftListOverTimes.add(shiftListOverTime);

        OverTimeRequest newOverTimeRequest = new OverTimeRequest();

        List<ShiftListVacation> shiftListVacations = new ArrayList<ShiftListVacation>();
        ShiftListVacation shiftListVacation = new ShiftListVacation();
        VacationRequest vacationRequest = new VacationRequest();
        shiftListVacation.setVacationId(vacationRequest);
        shiftListVacations.add(shiftListVacation);

        VacationRequest newVacationRequest = new VacationRequest();

        when(shiftChangeRequestService.findById(anyLong())).thenReturn(shiftChangeRequest);
        when(accountApproverService.findAccountAndApprover(any(Account.class), any(Account.class))).thenReturn(accountApprover);
        when(attendanceListSourceService.findByShiftId(any(Shift.class))).thenReturn(null);
        when(stringToLocalDateTime.stringToLocalDateTime(anyString())).thenReturn(LocalDateTime.parse(LocalDateTime.parse(requestJudgmentInput.getRequestTime(),DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(shiftService.findByAccountIdAndBeginWorkBetweenWeek(any(Account.class), any(LocalDateTime.class))).thenReturn(shifts);
        when(shiftChangeRequestService.save(any(ShiftChangeRequest.class))).thenReturn(newShiftChangeRequest);
        when(shiftChangeRequestService.shiftChangeRequestToShift(any(ShiftChangeRequest.class))).thenReturn(shift);
        when(shiftService.save(any(Shift.class))).thenReturn(newShift);
        when(shiftListShiftRequestService.findByShiftId(any(Shift.class))).thenReturn(shiftListShiftRequest);
        when(shiftListShiftRequestService.save(any(ShiftListShiftRequest.class))).thenReturn(newShiftListShiftRequest);
        when(shiftListOtherTimeService.findByShiftId(any(Shift.class))).thenReturn(shiftListOtherTimes);
        when(attendanceExceptionRequestService.save(any(AttendanceExceptionRequest.class))).thenReturn(newAttendanceExceptionRequest);
        when(shiftListOverTimeService.findByShiftId(any(Shift.class))).thenReturn(shiftListOverTimes);
        when(overTimeRequestService.save(any(OverTimeRequest.class))).thenReturn(newOverTimeRequest);
        when(shiftListVacationService.findByShiftId(any(Shift.class))).thenReturn(shiftListVacations);
        when(vacationRequestService.save(any(VacationRequest.class))).thenReturn(newVacationRequest);


        int result = requestService.approval(adminAccount, requestJudgmentInput);
        assertEquals(1, result);

        verify(shiftListOtherTimeService).deleteByShiftListOtherTime(any(ShiftListOtherTime.class));
        verify(shiftListOverTimeService).deleteByShiftListOverTime(any(ShiftListOverTime.class));
        verify(shiftListVacationService).deleteByShiftListVacationId(any(ShiftListVacation.class));
    }

    @Test
    void approvalStampSuccess()
    {
        Account adminAccount = new Account();
        Long adminAccountId = 34L;
        String adminAccountUsername = "kanrisya";
        adminAccount.setId(adminAccountId);
        adminAccount.setUsername(adminAccountUsername);

        Account generalAccount = new Account();
        Long generalAccountId = 3L;
        String generalAccountUsername = "nanasi";
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        AccountApprover accountApprover = new AccountApprover();
        accountApprover.setAccountId(generalAccount);
        accountApprover.setApproverId(adminAccount);

        RequestJudgmentInput requestJudgmentInput = new RequestJudgmentInput();
        requestJudgmentInput.setRequestId(1L);
        requestJudgmentInput.setRequestType(3);
        requestJudgmentInput.setRequestTime("2025/06/06T19:00:00");
        requestJudgmentInput.setApprovalComment("test");

        StampRequest stampRequest = new StampRequest();
        Shift shift = new Shift();
        shift.setShiftId(2L);
        shift.setBeginWork(LocalDateTime.parse("2025/12/25/15/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        shift.setEndWork(LocalDateTime.parse("2025/12/26/00/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        shift.setBeginBreak(LocalDateTime.parse("2025/12/25/18/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        shift.setEndBreak(LocalDateTime.parse("2025/12/25/19/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        stampRequest.setShiftId(shift);
        stampRequest.setAccountId(generalAccount);
        stampRequest.setRequestStatus(1);

        StampRequest newStampRequest = new StampRequest();
        newStampRequest.setAccountId(generalAccount);
        newStampRequest.setBeginWork(LocalDateTime.parse("2025/12/25/15/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        newStampRequest.setEndWork(LocalDateTime.parse("2025/12/26/00/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        newStampRequest.setBeginBreak(LocalDateTime.parse("2025/12/25/18/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        newStampRequest.setEndBreak(LocalDateTime.parse("2025/12/26/19/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        newStampRequest.setShiftId(shift);

        ShiftListShiftRequest shiftListShiftRequest = new ShiftListShiftRequest();
        ShiftRequest shiftRequest = new ShiftRequest();
        shiftRequest.setVacationWork(false);
        shiftListShiftRequest.setShiftId(shift);
        shiftListShiftRequest.setShiftRequestId(shiftRequest);
    
        List<ShiftListVacation> shiftListVacations = new ArrayList<ShiftListVacation>();

        ShiftListVacation absenceShiftListVacation = new ShiftListVacation();
        VacationRequest absenceVacationRequest = new VacationRequest();
        VacationType absenceVacationType = new VacationType();
        absenceVacationType.setVacationTypeId(3L);
        absenceVacationRequest.setVacationTypeId(absenceVacationType);
        absenceVacationRequest.setBeginVacation(LocalDateTime.parse("2025/12/25/20/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        absenceVacationRequest.setEndVacation(LocalDateTime.parse("2025/12/25/21/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        absenceShiftListVacation.setVacationId(absenceVacationRequest);
        absenceShiftListVacation.setShiftId(shift);

        ShiftListVacation vacationShiftListVacation = new ShiftListVacation();
        VacationRequest vacationVacationRequest = new VacationRequest();
        VacationType vacationVacationType = new VacationType();
        vacationVacationType.setVacationTypeId(2L);
        vacationVacationRequest.setVacationTypeId(vacationVacationType);
        vacationVacationRequest.setBeginVacation(LocalDateTime.parse("2025/12/25/17/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        vacationVacationRequest.setEndVacation(LocalDateTime.parse("2025/12/25/18/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        vacationShiftListVacation.setVacationId(vacationVacationRequest);
        vacationShiftListVacation.setShiftId(shift);
        shiftListVacations.add(absenceShiftListVacation);
        shiftListVacations.add(vacationShiftListVacation);
    
        List<ShiftListOverTime> shiftListOverTimes = new ArrayList<ShiftListOverTime>();

        ShiftListOverTime beforeShiftListOverTime = new ShiftListOverTime();
        OverTimeRequest beforeOverTimeRequest = new OverTimeRequest();
        beforeOverTimeRequest.setBeginWork(LocalDateTime.parse("2025/12/25/08/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        beforeOverTimeRequest.setEndWork(LocalDateTime.parse("2025/12/25/15/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        beforeShiftListOverTime.setOverTimeId(beforeOverTimeRequest);

        ShiftListOverTime afterShiftListOverTime = new ShiftListOverTime();
        OverTimeRequest afterOverTimeRequest = new OverTimeRequest();
        afterOverTimeRequest.setBeginWork(LocalDateTime.parse("2025/12/26/00/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        afterOverTimeRequest.setEndWork(LocalDateTime.parse("2025/12/26/09/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        afterShiftListOverTime.setOverTimeId(afterOverTimeRequest);
        shiftListOverTimes.add(beforeShiftListOverTime);
        shiftListOverTimes.add(afterShiftListOverTime);

        List<ShiftListOtherTime> shiftListOtherTimes = new ArrayList<ShiftListOtherTime>();

        ShiftListOtherTime outingShiftListOtherTime = new ShiftListOtherTime();
        AttendanceExceptionRequest outingAttendanceExceptionRequest = new AttendanceExceptionRequest();
        AttendanceExceptionType outingAttendanceExceptionType = new AttendanceExceptionType();
        outingAttendanceExceptionType.setAttendanceExceptionTypeId(1L);
        outingAttendanceExceptionRequest.setAttendanceExceptionTypeId(outingAttendanceExceptionType);
        outingAttendanceExceptionRequest.setBeginTime(LocalDateTime.parse("2025/12/25/22/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        outingAttendanceExceptionRequest.setEndTime(LocalDateTime.parse("2025/12/25/23/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        outingShiftListOtherTime.setAttendanceExceptionId(outingAttendanceExceptionRequest);

        ShiftListOtherTime latenessShiftListOtherTime = new ShiftListOtherTime();
        AttendanceExceptionRequest latenessAttendanceExceptionRequest = new AttendanceExceptionRequest();
        AttendanceExceptionType latenessAttendanceExceptionType = new AttendanceExceptionType();
        latenessAttendanceExceptionType.setAttendanceExceptionTypeId(2L);
        latenessAttendanceExceptionRequest.setAttendanceExceptionTypeId(latenessAttendanceExceptionType);
        latenessAttendanceExceptionRequest.setBeginTime(LocalDateTime.parse("2025/12/25/15/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        latenessAttendanceExceptionRequest.setEndTime(LocalDateTime.parse("2025/12/25/16/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        latenessShiftListOtherTime.setAttendanceExceptionId(latenessAttendanceExceptionRequest);

        ShiftListOtherTime leaveEarlyShiftListOtherTime = new ShiftListOtherTime();
        AttendanceExceptionRequest leaveEarlyAttendanceExceptionRequest = new AttendanceExceptionRequest();
        AttendanceExceptionType leaveEarlyAttendanceExceptionType = new AttendanceExceptionType();
        leaveEarlyAttendanceExceptionType.setAttendanceExceptionTypeId(3L);
        leaveEarlyAttendanceExceptionRequest.setAttendanceExceptionTypeId(leaveEarlyAttendanceExceptionType);
        leaveEarlyAttendanceExceptionRequest.setBeginTime(LocalDateTime.parse("2025/12/25/23/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        leaveEarlyAttendanceExceptionRequest.setEndTime(LocalDateTime.parse("2025/12/26/00/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        leaveEarlyShiftListOtherTime.setAttendanceExceptionId(leaveEarlyAttendanceExceptionRequest);
        shiftListOtherTimes.add(outingShiftListOtherTime);
        shiftListOtherTimes.add(latenessShiftListOtherTime);
        shiftListOtherTimes.add(leaveEarlyShiftListOtherTime);

        Attend attend = new Attend();

        AttendanceListSource attendanceListSource = new AttendanceListSource();

        when(stampRequestService.findById(anyLong())).thenReturn(stampRequest);
        when(accountApproverService.findAccountAndApprover(any(Account.class), any(Account.class))).thenReturn(accountApprover);
        when(attendanceListSourceService.findByShiftId(any(Shift.class))).thenReturn(null);
        when(stringToLocalDateTime.stringToLocalDateTime(requestJudgmentInput.getRequestTime())).thenReturn(LocalDateTime.parse(LocalDateTime.parse(requestJudgmentInput.getRequestTime(),DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(stampRequestService.save(any(StampRequest.class))).thenReturn(newStampRequest);
        when(shiftListShiftRequestService.findByShiftId(any(Shift.class))).thenReturn(shiftListShiftRequest);
        when(shiftListVacationService.findByShiftId(any(Shift.class))).thenReturn(shiftListVacations);
        when(shiftListOverTimeService.findByShiftId(any(Shift.class))).thenReturn(shiftListOverTimes);
        when(shiftListOtherTimeService.findByShiftId(any(Shift.class))).thenReturn(shiftListOtherTimes);
        when(attendService.save(any(Attend.class))).thenReturn(attend);
        when(attendanceListSourceService.save(any(AttendanceListSource.class))).thenReturn(attendanceListSource);
        
        doReturn(Time.valueOf("02:00:00")).when(requestService).getLateNight(any(LocalDateTime.class), any(LocalDateTime.class), any(LocalDateTime.class), any(LocalDateTime.class), anyList());
        doReturn(Duration.between(absenceShiftListVacation.getVacationId().getBeginVacation(), absenceShiftListVacation.getVacationId().getEndVacation())).when(requestService).getVacation(absenceShiftListVacation.getVacationId().getBeginVacation(), absenceShiftListVacation.getVacationId().getEndVacation(), absenceShiftListVacation.getShiftId().getBeginBreak(), absenceShiftListVacation.getShiftId().getEndBreak());
        doReturn(Duration.between(vacationShiftListVacation.getVacationId().getBeginVacation(), vacationShiftListVacation.getVacationId().getEndVacation())).when(requestService).getVacation(vacationShiftListVacation.getVacationId().getBeginVacation(), vacationShiftListVacation.getVacationId().getEndVacation(), vacationShiftListVacation.getShiftId().getBeginBreak(), vacationShiftListVacation.getShiftId().getEndBreak());

        int result = requestService.approval(generalAccount, requestJudgmentInput);
        
        assertEquals(1, result);
    }

    @Test
    void approvalVacationSuccess()
    {
        Account adminAccount = new Account();
        Long adminAccountId = 34L;
        String adminAccountUsername = "kanrisya";
        adminAccount.setId(adminAccountId);
        adminAccount.setUsername(adminAccountUsername);

        Account generalAccount = new Account();
        Long generalAccountId = 3L;
        String generalAccountUsername = "nanasi";
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        AccountApprover accountApprover = new AccountApprover();
        accountApprover.setAccountId(generalAccount);
        accountApprover.setApproverId(adminAccount);

        RequestJudgmentInput requestJudgmentInput = new RequestJudgmentInput();
        requestJudgmentInput.setRequestId(1L);
        requestJudgmentInput.setRequestType(4);
        requestJudgmentInput.setRequestTime("2025/06/06T19:00:00");
        requestJudgmentInput.setApprovalComment("test");

        VacationRequest vacationRequest = new VacationRequest();
        vacationRequest.setAccountId(generalAccount);
        vacationRequest.setRequestStatus(1);

        VacationRequest newVacationRequest = new VacationRequest();
        newVacationRequest.setBeginVacation(LocalDateTime.parse("2025/12/25/16/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        newVacationRequest.setEndVacation(LocalDateTime.parse("2025/12/25/17/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        Shift shift = new Shift();
        shift.setShiftId(2L);
        shift.setBeginWork(LocalDateTime.parse("2025/12/25/15/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        shift.setEndWork(LocalDateTime.parse("2025/12/26/00/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        shift.setBeginBreak(LocalDateTime.parse("2025/12/25/18/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        shift.setEndBreak(LocalDateTime.parse("2025/12/25/19/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        VacationType vacationType = new VacationType();
        vacationType.setVacationTypeId(1L);
        newVacationRequest.setShiftId(shift);
        newVacationRequest.setVacationTypeId(vacationType);

        ShiftListVacation shiftListVacation = new ShiftListVacation();

        Vacation vacation = new Vacation();

        PaydHolidayUse paydHolidayUse = new PaydHolidayUse();

        when(vacationRequestService.findById(anyLong())).thenReturn(vacationRequest);
        when(accountApproverService.findAccountAndApprover(any(Account.class), any(Account.class))).thenReturn(accountApprover);
        when(stringToLocalDateTime.stringToLocalDateTime(requestJudgmentInput.getRequestTime())).thenReturn(LocalDateTime.parse(LocalDateTime.parse(requestJudgmentInput.getRequestTime(),DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(vacationRequestService.save(any(VacationRequest.class))).thenReturn(newVacationRequest);
        when(shiftListVacationService.save(any(ShiftListVacation.class))).thenReturn(shiftListVacation);
        when(vacationService.save(any(Vacation.class))).thenReturn(vacation);
        when(paydHolidayUseService.save(any(PaydHolidayUse.class))).thenReturn(paydHolidayUse);

        int result = requestService.approval(generalAccount, requestJudgmentInput);
        assertEquals(1, result);
    }

    @Test
    void approvalOverTimeSuccess()
    {
        Account adminAccount = new Account();
        Long adminAccountId = 34L;
        String adminAccountUsername = "kanrisya";
        adminAccount.setId(adminAccountId);
        adminAccount.setUsername(adminAccountUsername);

        Account generalAccount = new Account();
        Long generalAccountId = 3L;
        String generalAccountUsername = "nanasi";
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        AccountApprover accountApprover = new AccountApprover();
        accountApprover.setAccountId(generalAccount);
        accountApprover.setApproverId(adminAccount);

        RequestJudgmentInput requestJudgmentInput = new RequestJudgmentInput();
        requestJudgmentInput.setRequestId(1L);
        requestJudgmentInput.setRequestType(5);
        requestJudgmentInput.setRequestTime("2025/06/06T19:00:00");
        requestJudgmentInput.setApprovalComment("test");

        OverTimeRequest overTimeRequest = new OverTimeRequest();
        overTimeRequest.setAccountId(generalAccount);
        overTimeRequest.setRequestStatus(1);

        OverTimeRequest newOverTimeRequest = new OverTimeRequest();
        newOverTimeRequest.setBeginWork(LocalDateTime.parse("2025/12/25/14/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        newOverTimeRequest.setEndWork(LocalDateTime.parse("2025/12/25/15/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        Shift shift = new Shift();
        shift.setShiftId(2L);
        shift.setBeginWork(LocalDateTime.parse("2025/12/25/15/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        shift.setEndWork(LocalDateTime.parse("2025/12/26/00/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        shift.setBeginBreak(LocalDateTime.parse("2025/12/25/18/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        shift.setEndBreak(LocalDateTime.parse("2025/12/25/19/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        shift.setOverWork(Time.valueOf("00:00:00"));
        newOverTimeRequest.setShiftId(shift);

        Shift newShift = new Shift();

        ShiftListOverTime shiftListOverTime = new ShiftListOverTime();

        when(overTimeRequestService.findById(anyLong())).thenReturn(overTimeRequest);
        when(accountApproverService.findAccountAndApprover(any(Account.class), any(Account.class))).thenReturn(accountApprover);
        when(stringToLocalDateTime.stringToLocalDateTime(requestJudgmentInput.getRequestTime())).thenReturn(LocalDateTime.parse(LocalDateTime.parse(requestJudgmentInput.getRequestTime(),DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(overTimeRequestService.save(any(OverTimeRequest.class))).thenReturn(newOverTimeRequest);
        when(shiftService.save(any(Shift.class))).thenReturn(newShift);
        when(shiftListOverTimeService.save(any(ShiftListOverTime.class))).thenReturn(shiftListOverTime);

        int result = requestService.approval(generalAccount, requestJudgmentInput);
        assertEquals(1, result);
    }

    @Test
    void approvalOtherTimeOutingSuccess()
    {
        Account adminAccount = new Account();
        Long adminAccountId = 34L;
        String adminAccountUsername = "kanrisya";
        adminAccount.setId(adminAccountId);
        adminAccount.setUsername(adminAccountUsername);

        Account generalAccount = new Account();
        Long generalAccountId = 3L;
        String generalAccountUsername = "nanasi";
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        AccountApprover accountApprover = new AccountApprover();
        accountApprover.setAccountId(generalAccount);
        accountApprover.setApproverId(adminAccount);

        RequestJudgmentInput requestJudgmentInput = new RequestJudgmentInput();
        requestJudgmentInput.setRequestId(1L);
        requestJudgmentInput.setRequestType(6);
        requestJudgmentInput.setRequestTime("2025/06/06T19:00:00");
        requestJudgmentInput.setApprovalComment("test");

        AttendanceExceptionRequest attendanceExceptionRequest = new AttendanceExceptionRequest();
        attendanceExceptionRequest.setAccountId(generalAccount);
        attendanceExceptionRequest.setRequestStatus(1);

        AttendanceExceptionRequest newAttendanceExceptionRequest = new AttendanceExceptionRequest();
        newAttendanceExceptionRequest.setBeginTime(LocalDateTime.parse("2025/12/25/15/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        newAttendanceExceptionRequest.setEndTime(LocalDateTime.parse("2025/12/25/16/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        AttendanceExceptionType attendanceExceptionType = new AttendanceExceptionType();
        attendanceExceptionType.setAttendanceExceptionTypeId(1L);
        newAttendanceExceptionRequest.setAttendanceExceptionTypeId(attendanceExceptionType);
        Shift shift = new Shift();
        shift.setShiftId(2L);
        shift.setBeginWork(LocalDateTime.parse("2025/12/25/15/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        shift.setEndWork(LocalDateTime.parse("2025/12/26/00/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        shift.setBeginBreak(LocalDateTime.parse("2025/12/25/18/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        shift.setEndBreak(LocalDateTime.parse("2025/12/25/19/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        shift.setOuting(Time.valueOf("00:00:00"));
        shift.setLateness(Time.valueOf("00:00:00"));
        shift.setLeaveEarly(Time.valueOf("00:00:00"));
        newAttendanceExceptionRequest.setShiftId(shift);

        Shift newShift =  new Shift();

        ShiftListOtherTime shiftListOtherTime = new ShiftListOtherTime();

        when(attendanceExceptionRequestService.findById(anyLong())).thenReturn(attendanceExceptionRequest);
        when(accountApproverService.findAccountAndApprover(any(Account.class), any(Account.class))).thenReturn(accountApprover);
        when(stringToLocalDateTime.stringToLocalDateTime(requestJudgmentInput.getRequestTime())).thenReturn(LocalDateTime.parse(LocalDateTime.parse(requestJudgmentInput.getRequestTime(),DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(attendanceExceptionRequestService.save(any(AttendanceExceptionRequest.class))).thenReturn(newAttendanceExceptionRequest);
        when(shiftService.save(any(Shift.class))).thenReturn(newShift);
        when(shiftListOtherTimeService.save(any(ShiftListOtherTime.class))).thenReturn(shiftListOtherTime);

        int result = requestService.approval(generalAccount, requestJudgmentInput);
        assertEquals(1, result);
    }

        @Test
    void approvalOtherTimeLatenessSuccess()
    {
        Account adminAccount = new Account();
        Long adminAccountId = 34L;
        String adminAccountUsername = "kanrisya";
        adminAccount.setId(adminAccountId);
        adminAccount.setUsername(adminAccountUsername);

        Account generalAccount = new Account();
        Long generalAccountId = 3L;
        String generalAccountUsername = "nanasi";
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        AccountApprover accountApprover = new AccountApprover();
        accountApprover.setAccountId(generalAccount);
        accountApprover.setApproverId(adminAccount);

        RequestJudgmentInput requestJudgmentInput = new RequestJudgmentInput();
        requestJudgmentInput.setRequestId(1L);
        requestJudgmentInput.setRequestType(6);
        requestJudgmentInput.setRequestTime("2025/06/06T19:00:00");
        requestJudgmentInput.setApprovalComment("test");

        AttendanceExceptionRequest attendanceExceptionRequest = new AttendanceExceptionRequest();
        attendanceExceptionRequest.setAccountId(generalAccount);
        attendanceExceptionRequest.setRequestStatus(1);

        AttendanceExceptionRequest newAttendanceExceptionRequest = new AttendanceExceptionRequest();
        newAttendanceExceptionRequest.setBeginTime(LocalDateTime.parse("2025/12/25/15/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        newAttendanceExceptionRequest.setEndTime(LocalDateTime.parse("2025/12/25/16/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        AttendanceExceptionType attendanceExceptionType = new AttendanceExceptionType();
        attendanceExceptionType.setAttendanceExceptionTypeId(2L);
        newAttendanceExceptionRequest.setAttendanceExceptionTypeId(attendanceExceptionType);
        Shift shift = new Shift();
        shift.setShiftId(2L);
        shift.setBeginWork(LocalDateTime.parse("2025/12/25/15/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        shift.setEndWork(LocalDateTime.parse("2025/12/26/00/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        shift.setBeginBreak(LocalDateTime.parse("2025/12/25/18/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        shift.setEndBreak(LocalDateTime.parse("2025/12/25/19/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        shift.setOuting(Time.valueOf("00:00:00"));
        shift.setLateness(Time.valueOf("00:00:00"));
        shift.setLeaveEarly(Time.valueOf("00:00:00"));
        newAttendanceExceptionRequest.setShiftId(shift);

        Shift newShift =  new Shift();

        ShiftListOtherTime shiftListOtherTime = new ShiftListOtherTime();

        when(attendanceExceptionRequestService.findById(anyLong())).thenReturn(attendanceExceptionRequest);
        when(accountApproverService.findAccountAndApprover(any(Account.class), any(Account.class))).thenReturn(accountApprover);
        when(stringToLocalDateTime.stringToLocalDateTime(requestJudgmentInput.getRequestTime())).thenReturn(LocalDateTime.parse(LocalDateTime.parse(requestJudgmentInput.getRequestTime(),DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(attendanceExceptionRequestService.save(any(AttendanceExceptionRequest.class))).thenReturn(newAttendanceExceptionRequest);
        when(shiftService.save(any(Shift.class))).thenReturn(newShift);
        when(shiftListOtherTimeService.save(any(ShiftListOtherTime.class))).thenReturn(shiftListOtherTime);

        int result = requestService.approval(generalAccount, requestJudgmentInput);
        assertEquals(1, result);
    }

        @Test
    void approvalOtherTimeLeaveEarlySuccess()
    {
        Account adminAccount = new Account();
        Long adminAccountId = 34L;
        String adminAccountUsername = "kanrisya";
        adminAccount.setId(adminAccountId);
        adminAccount.setUsername(adminAccountUsername);

        Account generalAccount = new Account();
        Long generalAccountId = 3L;
        String generalAccountUsername = "nanasi";
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        AccountApprover accountApprover = new AccountApprover();
        accountApprover.setAccountId(generalAccount);
        accountApprover.setApproverId(adminAccount);

        RequestJudgmentInput requestJudgmentInput = new RequestJudgmentInput();
        requestJudgmentInput.setRequestId(1L);
        requestJudgmentInput.setRequestType(6);
        requestJudgmentInput.setRequestTime("2025/06/06T19:00:00");
        requestJudgmentInput.setApprovalComment("test");

        AttendanceExceptionRequest attendanceExceptionRequest = new AttendanceExceptionRequest();
        attendanceExceptionRequest.setAccountId(generalAccount);
        attendanceExceptionRequest.setRequestStatus(1);

        AttendanceExceptionRequest newAttendanceExceptionRequest = new AttendanceExceptionRequest();
        newAttendanceExceptionRequest.setBeginTime(LocalDateTime.parse("2025/12/25/15/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        newAttendanceExceptionRequest.setEndTime(LocalDateTime.parse("2025/12/25/16/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        AttendanceExceptionType attendanceExceptionType = new AttendanceExceptionType();
        attendanceExceptionType.setAttendanceExceptionTypeId(3L);
        newAttendanceExceptionRequest.setAttendanceExceptionTypeId(attendanceExceptionType);
        Shift shift = new Shift();
        shift.setShiftId(2L);
        shift.setBeginWork(LocalDateTime.parse("2025/12/25/15/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        shift.setEndWork(LocalDateTime.parse("2025/12/26/00/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        shift.setBeginBreak(LocalDateTime.parse("2025/12/25/18/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        shift.setEndBreak(LocalDateTime.parse("2025/12/25/19/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        shift.setOuting(Time.valueOf("00:00:00"));
        shift.setLateness(Time.valueOf("00:00:00"));
        shift.setLeaveEarly(Time.valueOf("00:00:00"));
        newAttendanceExceptionRequest.setShiftId(shift);

        Shift newShift =  new Shift();

        ShiftListOtherTime shiftListOtherTime = new ShiftListOtherTime();

        when(attendanceExceptionRequestService.findById(anyLong())).thenReturn(attendanceExceptionRequest);
        when(accountApproverService.findAccountAndApprover(any(Account.class), any(Account.class))).thenReturn(accountApprover);
        when(stringToLocalDateTime.stringToLocalDateTime(requestJudgmentInput.getRequestTime())).thenReturn(LocalDateTime.parse(LocalDateTime.parse(requestJudgmentInput.getRequestTime(),DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(attendanceExceptionRequestService.save(any(AttendanceExceptionRequest.class))).thenReturn(newAttendanceExceptionRequest);
        when(shiftService.save(any(Shift.class))).thenReturn(newShift);
        when(shiftListOtherTimeService.save(any(ShiftListOtherTime.class))).thenReturn(shiftListOtherTime);

        int result = requestService.approval(generalAccount, requestJudgmentInput);
        assertEquals(1, result);
    }

    @Test
    void approvalMonthlySuccess()
    {
        Account adminAccount = new Account();
        Long adminAccountId = 34L;
        String adminAccountUsername = "kanrisya";
        adminAccount.setId(adminAccountId);
        adminAccount.setUsername(adminAccountUsername);

        Account generalAccount = new Account();
        Long generalAccountId = 3L;
        String generalAccountUsername = "nanasi";
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        AccountApprover accountApprover = new AccountApprover();
        accountApprover.setAccountId(generalAccount);
        accountApprover.setApproverId(adminAccount);

        RequestJudgmentInput requestJudgmentInput = new RequestJudgmentInput();
        requestJudgmentInput.setRequestId(1L);
        requestJudgmentInput.setRequestType(7);
        requestJudgmentInput.setRequestTime("2025/06/06T19:00:00");
        requestJudgmentInput.setApprovalComment("test");

        MonthlyRequest monthlyRequest = new MonthlyRequest();
        monthlyRequest.setAccountId(generalAccount);
        monthlyRequest.setRequestStatus(1);

        MonthlyRequest newMonthlyRequest = new MonthlyRequest();
        
        when(monthlyRequestService.findById(anyLong())).thenReturn(monthlyRequest);
        when(accountApproverService.findAccountAndApprover(any(Account.class), any(Account.class))).thenReturn(accountApprover);
        when(stringToLocalDateTime.stringToLocalDateTime(requestJudgmentInput.getRequestTime())).thenReturn(LocalDateTime.parse(LocalDateTime.parse(requestJudgmentInput.getRequestTime(),DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(monthlyRequestService.save(any(MonthlyRequest.class))).thenReturn(newMonthlyRequest);

        int result = requestService.approval(generalAccount, requestJudgmentInput);
        assertEquals(1, result);
    }

    @Test
    void approvalCancelShiftSuccess()
    {
        Account adminAccount = new Account();
        adminAccount.setId(1L);
        adminAccount.setUsername("hogehoge");

        Account generalAccount = new Account();
        generalAccount.setId(34L);
        generalAccount.setUsername("hogehoge");

        AccountApprover accountApprover = new AccountApprover();
        accountApprover.setAccountId(generalAccount);
        accountApprover.setApproverId(adminAccount);

        RequestJudgmentInput requestJudgmentInput = new RequestJudgmentInput();
        requestJudgmentInput.setRequestId(1L);
        requestJudgmentInput.setRequestType(1);
        requestJudgmentInput.setRequestTime("2025/12/30T00:00:00");
        requestJudgmentInput.setApprovalComment("hogehoge");

        ShiftRequest shiftRequest = new ShiftRequest();
        shiftRequest.setAccountId(generalAccount);
        shiftRequest.setRequestStatus(2);
        
        ShiftListShiftRequest shiftListShiftRequest = new ShiftListShiftRequest();
        Shift shift = new Shift();
        shiftListShiftRequest.setShiftId(shift);
        shiftListShiftRequest.setShiftChangeRequestId(null);

        List<ShiftListVacation> shiftListVacations = new ArrayList<ShiftListVacation>();

        List<ShiftListOverTime> shiftListOverTimes = new ArrayList<ShiftListOverTime>();
        
        List<ShiftListOtherTime> shiftListOtherTimes = new ArrayList<ShiftListOtherTime>();

        ShiftRequest newShiftRequest = new ShiftRequest();

        when(shiftRequestService.findById(anyLong())).thenReturn(shiftRequest);
        when(accountApproverService.findAccountAndApprover(any(Account.class), any(Account.class))).thenReturn(accountApprover);
        when(shiftListShiftRequestService.findByShiftRequest(any(ShiftRequest.class))).thenReturn(shiftListShiftRequest);
        when(attendanceListSourceService.findByShiftId(any(Shift.class))).thenReturn(null);
        when(shiftListVacationService.findByShiftId(any(Shift.class))).thenReturn(shiftListVacations);
        when(shiftListOverTimeService.findByShiftId(any(Shift.class))).thenReturn(shiftListOverTimes);
        when(shiftListOtherTimeService.findByShiftId(any(Shift.class))).thenReturn(shiftListOtherTimes);
        when(stringToLocalDateTime.stringToLocalDateTime(requestJudgmentInput.getRequestTime())).thenReturn(LocalDateTime.parse(LocalDateTime.parse(requestJudgmentInput.getRequestTime(),DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(shiftRequestService.save(any(ShiftRequest.class))).thenReturn(newShiftRequest);

        int result = requestService.approvalCancel(adminAccount, requestJudgmentInput);
        assertEquals(1, result);

        verify(shiftService).delete(any(Shift.class));
        verify(shiftListShiftRequestService).deleteByShiftListShiftRequest(any(ShiftListShiftRequest.class));
    }   

    @Test
    void approvalCancelShiftChangeSuccess()
    {
        Account adminAccount = new Account();
        adminAccount.setId(1L);
        adminAccount.setUsername("hogehoge");

        Account generalAccount = new Account();
        generalAccount.setId(34L);
        generalAccount.setUsername("hogehoge");

        AccountApprover accountApprover = new AccountApprover();
        accountApprover.setAccountId(generalAccount);
        accountApprover.setApproverId(adminAccount);

        RequestJudgmentInput requestJudgmentInput = new RequestJudgmentInput();
        requestJudgmentInput.setRequestId(1L);
        requestJudgmentInput.setRequestType(2);
        requestJudgmentInput.setRequestTime("2025/12/30T00:00:00");
        requestJudgmentInput.setApprovalComment("hogehoge");

        ShiftChangeRequest shiftChangeRequest = new ShiftChangeRequest();
        shiftChangeRequest.setAccountId(generalAccount);
        shiftChangeRequest.setRequestStatus(2);
        shiftChangeRequest.setShiftId(new Shift());

        ShiftChangeRequest newShiftChangeRequest = new ShiftChangeRequest();

        List<ShiftListVacation> shiftListVacations = new ArrayList<ShiftListVacation>();
        List<ShiftListOverTime> shiftListOverTimes = new ArrayList<ShiftListOverTime>();
        List<ShiftListOtherTime> shiftListOtherTimes = new ArrayList<ShiftListOtherTime>();

        ShiftListShiftRequest shiftListShiftRequest = new ShiftListShiftRequest();

        ShiftListShiftRequest newShiftListShiftRequest = new ShiftListShiftRequest();

        when(shiftChangeRequestService.findById(anyLong())).thenReturn(shiftChangeRequest);
        when(accountApproverService.findAccountAndApprover(any(Account.class), any(Account.class))).thenReturn(accountApprover);
        when(attendanceListSourceService.findByShiftId(any(Shift.class))).thenReturn(null);
        when(shiftListVacationService.findByShiftId(any(Shift.class))).thenReturn(shiftListVacations);
        when(shiftListOverTimeService.findByShiftId(any(Shift.class))).thenReturn(shiftListOverTimes);
        when(shiftListOtherTimeService.findByShiftId(any(Shift.class))).thenReturn(shiftListOtherTimes);
        when(shiftListShiftRequestService.findByShiftId(any(Shift.class))).thenReturn(shiftListShiftRequest);
        when(shiftListShiftRequestService.save(any(ShiftListShiftRequest.class))).thenReturn(newShiftListShiftRequest);
        when(stringToLocalDateTime.stringToLocalDateTime(requestJudgmentInput.getRequestTime())).thenReturn(LocalDateTime.parse(LocalDateTime.parse(requestJudgmentInput.getRequestTime(),DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(shiftChangeRequestService.save(any(ShiftChangeRequest.class))).thenReturn(newShiftChangeRequest);

        int result = requestService.approvalCancel(adminAccount, requestJudgmentInput);
        assertEquals(1, result);
    }

    @Test
    void approvalCancelStampChangeSuccess()
    {
        Account adminAccount = new Account();
        adminAccount.setId(1L);
        adminAccount.setUsername("hogehoge");

        Account generalAccount = new Account();
        generalAccount.setId(34L);
        generalAccount.setUsername("hogehoge");

        AccountApprover accountApprover = new AccountApprover();
        accountApprover.setAccountId(generalAccount);
        accountApprover.setApproverId(adminAccount);

        RequestJudgmentInput requestJudgmentInput = new RequestJudgmentInput();
        requestJudgmentInput.setRequestId(1L);
        requestJudgmentInput.setRequestType(3);
        requestJudgmentInput.setRequestTime("2025/12/30T00:00:00");
        requestJudgmentInput.setApprovalComment("hogehoge");

        StampRequest stampRequest = new StampRequest();
        stampRequest.setAccountId(generalAccount);
        stampRequest.setRequestStatus(2);
        stampRequest.setShiftId(new Shift());

        StampRequest newStampRequest = new StampRequest();

        AttendanceListSource attendanceListSource = new AttendanceListSource();
        attendanceListSource.setAttendanceId(new Attend());

        when(stampRequestService.findById(anyLong())).thenReturn(stampRequest);
        when(accountApproverService.findAccountAndApprover(any(Account.class), any(Account.class))).thenReturn(accountApprover);
        when(attendanceListSourceService.findByShiftId(any(Shift.class))).thenReturn(attendanceListSource);
        when(stringToLocalDateTime.stringToLocalDateTime(requestJudgmentInput.getRequestTime())).thenReturn(LocalDateTime.parse(LocalDateTime.parse(requestJudgmentInput.getRequestTime(),DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(stampRequestService.save(any(StampRequest.class))).thenReturn(newStampRequest);

        int result = requestService.approvalCancel(adminAccount, requestJudgmentInput);
        assertEquals(1, result);

        verify(attendanceListSourceService).delete(any(AttendanceListSource.class));
        verify(attendService).delete(any(Attend.class));
    }

    @Test
    void approvalCancelVacationSuccess()
    {
        Account adminAccount = new Account();
        adminAccount.setId(1L);
        adminAccount.setUsername("hogehoge");

        Account generalAccount = new Account();
        generalAccount.setId(34L);
        generalAccount.setUsername("hogehoge");

        AccountApprover accountApprover = new AccountApprover();
        accountApprover.setAccountId(generalAccount);
        accountApprover.setApproverId(adminAccount);

        RequestJudgmentInput requestJudgmentInput = new RequestJudgmentInput();
        requestJudgmentInput.setRequestId(1L);
        requestJudgmentInput.setRequestType(4);
        requestJudgmentInput.setRequestTime("2025/12/30T00:00:00");
        requestJudgmentInput.setApprovalComment("hogehoge");

        VacationRequest vacationRequest = new VacationRequest();
        vacationRequest.setAccountId(generalAccount);
        vacationRequest.setRequestStatus(2);
        vacationRequest.setShiftId(new Shift());
        VacationType vacationType = new VacationType();
        vacationType.setVacationTypeId(1L);
        vacationRequest.setVacationTypeId(vacationType);

        VacationRequest newVacationRequest = new VacationRequest();

        when(vacationRequestService.findById(anyLong())).thenReturn(vacationRequest);
        when(accountApproverService.findAccountAndApprover(any(Account.class), any(Account.class))).thenReturn(accountApprover);
        when(attendanceListSourceService.findByShiftId(any(Shift.class))).thenReturn(null);
        when(shiftListVacationService.findByVacation(any(VacationRequest.class))).thenReturn(new ShiftListVacation());
        when(vacationService.findByVacation(any(VacationRequest.class))).thenReturn(new Vacation());
        when(paydHolidayUseService.findByVacationId(any(VacationRequest.class))).thenReturn(new PaydHolidayUse());
        when(stringToLocalDateTime.stringToLocalDateTime(requestJudgmentInput.getRequestTime())).thenReturn(LocalDateTime.parse(LocalDateTime.parse(requestJudgmentInput.getRequestTime(),DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(vacationRequestService.save(any())).thenReturn(newVacationRequest);

        int result = requestService.approvalCancel(adminAccount, requestJudgmentInput);
        assertEquals(1, result);

        verify(shiftListVacationService).deleteByShiftListVacationId(any(ShiftListVacation.class));
        verify(vacationService).delete(any(Vacation.class));
        verify(paydHolidayUseService).delete(any(PaydHolidayUse.class));
    }

    @Test
    void approvalCancelOverTimeSuccess()
    {
        Account adminAccount = new Account();
        adminAccount.setId(1L);
        adminAccount.setUsername("hogehoge");

        Account generalAccount = new Account();
        generalAccount.setId(34L);
        generalAccount.setUsername("hogehoge");

        AccountApprover accountApprover = new AccountApprover();
        accountApprover.setAccountId(generalAccount);
        accountApprover.setApproverId(adminAccount);

        RequestJudgmentInput requestJudgmentInput = new RequestJudgmentInput();
        requestJudgmentInput.setRequestId(1L);
        requestJudgmentInput.setRequestType(5);
        requestJudgmentInput.setRequestTime("2025/12/30T00:00:00");
        requestJudgmentInput.setApprovalComment("hogehoge");

        OverTimeRequest overTimeRequest = new OverTimeRequest();
        overTimeRequest.setAccountId(generalAccount);
        overTimeRequest.setRequestStatus(2);
        Shift shift = new Shift();
        shift.setOverWork(Time.valueOf("00:00:00"));
        overTimeRequest.setShiftId(shift);
        overTimeRequest.setBeginWork(LocalDateTime.parse("2025/12/24/14/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        overTimeRequest.setEndWork(LocalDateTime.parse("2025/12/24/18/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));

        OverTimeRequest newOverTimeRequest = new OverTimeRequest();

        when(overTimeRequestService.findById(anyLong())).thenReturn(overTimeRequest);
        when(accountApproverService.findAccountAndApprover(any(Account.class), any(Account.class))).thenReturn(accountApprover);
        when(attendanceListSourceService.findByShiftId(any(Shift.class))).thenReturn(null);
        when(shiftListOverTimeService.findByOverTime(any(OverTimeRequest.class))).thenReturn(new ShiftListOverTime());
        when(shiftService.save(any(Shift.class))).thenReturn(new Shift());
        when(stringToLocalDateTime.stringToLocalDateTime(requestJudgmentInput.getRequestTime())).thenReturn(LocalDateTime.parse(LocalDateTime.parse(requestJudgmentInput.getRequestTime(),DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(overTimeRequestService.save(any(OverTimeRequest.class))).thenReturn(newOverTimeRequest);

        int result = requestService.approvalCancel(adminAccount, requestJudgmentInput);
        assertEquals(1, result);

        verify(shiftListOverTimeService).deleteByShiftListOverTime(any(ShiftListOverTime.class));
    }

    @Test
    void approvalCancelOutingSuccess()
    {
        Account adminAccount = new Account();
        adminAccount.setId(1L);
        adminAccount.setUsername("hogehoge");

        Account generalAccount = new Account();
        generalAccount.setId(34L);
        generalAccount.setUsername("hogehoge");

        AccountApprover accountApprover = new AccountApprover();
        accountApprover.setAccountId(generalAccount);
        accountApprover.setApproverId(adminAccount);

        RequestJudgmentInput requestJudgmentInput = new RequestJudgmentInput();
        requestJudgmentInput.setRequestId(1L);
        requestJudgmentInput.setRequestType(6);
        requestJudgmentInput.setRequestTime("2025/12/30T00:00:00");
        requestJudgmentInput.setApprovalComment("hogehoge");

        AttendanceExceptionRequest attendanceExceptionRequest = new AttendanceExceptionRequest();
        attendanceExceptionRequest.setAccountId(generalAccount);
        attendanceExceptionRequest.setRequestStatus(2);
        attendanceExceptionRequest.setBeginTime(LocalDateTime.parse("2025/12/24/13/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        attendanceExceptionRequest.setEndTime(LocalDateTime.parse("2025/12/24/16/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        AttendanceExceptionType attendanceExceptionType = new AttendanceExceptionType();
        attendanceExceptionType.setAttendanceExceptionTypeId(1L);
        attendanceExceptionRequest.setAttendanceExceptionTypeId(attendanceExceptionType);
        Shift shift = new Shift();
        shift.setOuting(Time.valueOf("00:00:00"));
        shift.setLateness(Time.valueOf("00:00:00"));
        shift.setLeaveEarly(Time.valueOf("00:00:00"));
        attendanceExceptionRequest.setShiftId(shift);

        AttendanceExceptionRequest newAttendanceExceptionRequest = new AttendanceExceptionRequest();

        when(attendanceExceptionRequestService.findById(anyLong())).thenReturn(attendanceExceptionRequest);
        when(accountApproverService.findAccountAndApprover(any(Account.class), any(Account.class))).thenReturn(accountApprover);
        when(attendanceListSourceService.findByShiftId(any(Shift.class))).thenReturn(null);
        when(shiftListOtherTimeService.findByOtherTimeId(any(AttendanceExceptionRequest.class))).thenReturn(new ShiftListOtherTime());
        when(shiftService.save(any(Shift.class))).thenReturn(new Shift());
        when(stringToLocalDateTime.stringToLocalDateTime(requestJudgmentInput.getRequestTime())).thenReturn(LocalDateTime.parse(LocalDateTime.parse(requestJudgmentInput.getRequestTime(),DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(attendanceExceptionRequestService.save(any(AttendanceExceptionRequest.class))).thenReturn(newAttendanceExceptionRequest);

        int result = requestService.approvalCancel(adminAccount, requestJudgmentInput);
        assertEquals(1, result);
        
        verify(shiftListOtherTimeService).deleteByShiftListOtherTime(any(ShiftListOtherTime.class));
    }

    @Test
    void approvalCancelLatenessSuccess()
    {
        Account adminAccount = new Account();
        adminAccount.setId(1L);
        adminAccount.setUsername("hogehoge");

        Account generalAccount = new Account();
        generalAccount.setId(34L);
        generalAccount.setUsername("hogehoge");

        AccountApprover accountApprover = new AccountApprover();
        accountApprover.setAccountId(generalAccount);
        accountApprover.setApproverId(adminAccount);

        RequestJudgmentInput requestJudgmentInput = new RequestJudgmentInput();
        requestJudgmentInput.setRequestId(1L);
        requestJudgmentInput.setRequestType(6);
        requestJudgmentInput.setRequestTime("2025/12/30T00:00:00");
        requestJudgmentInput.setApprovalComment("hogehoge");

        AttendanceExceptionRequest attendanceExceptionRequest = new AttendanceExceptionRequest();
        attendanceExceptionRequest.setAccountId(generalAccount);
        attendanceExceptionRequest.setRequestStatus(2);
        attendanceExceptionRequest.setBeginTime(LocalDateTime.parse("2025/12/24/13/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        attendanceExceptionRequest.setEndTime(LocalDateTime.parse("2025/12/24/16/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        AttendanceExceptionType attendanceExceptionType = new AttendanceExceptionType();
        attendanceExceptionType.setAttendanceExceptionTypeId(2L);
        attendanceExceptionRequest.setAttendanceExceptionTypeId(attendanceExceptionType);
        Shift shift = new Shift();
        shift.setOuting(Time.valueOf("00:00:00"));
        shift.setLateness(Time.valueOf("00:00:00"));
        shift.setLeaveEarly(Time.valueOf("00:00:00"));
        attendanceExceptionRequest.setShiftId(shift);

        AttendanceExceptionRequest newAttendanceExceptionRequest = new AttendanceExceptionRequest();
        when(attendanceExceptionRequestService.findById(anyLong())).thenReturn(attendanceExceptionRequest);
        when(accountApproverService.findAccountAndApprover(any(Account.class), any(Account.class))).thenReturn(accountApprover);
        when(attendanceListSourceService.findByShiftId(any(Shift.class))).thenReturn(null);
        when(shiftListOtherTimeService.findByOtherTimeId(any(AttendanceExceptionRequest.class))).thenReturn(new ShiftListOtherTime());
        when(shiftService.save(any(Shift.class))).thenReturn(new Shift());
        when(stringToLocalDateTime.stringToLocalDateTime(requestJudgmentInput.getRequestTime())).thenReturn(LocalDateTime.parse(LocalDateTime.parse(requestJudgmentInput.getRequestTime(),DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(attendanceExceptionRequestService.save(any(AttendanceExceptionRequest.class))).thenReturn(newAttendanceExceptionRequest);

        int result = requestService.approvalCancel(adminAccount, requestJudgmentInput);
        assertEquals(1, result);

        verify(shiftListOtherTimeService).deleteByShiftListOtherTime(any(ShiftListOtherTime.class));
    }

    @Test
    void approvalCancelLeaveEarlySuccess()
    {
        Account adminAccount = new Account();
        adminAccount.setId(1L);
        adminAccount.setUsername("hogehoge");

        Account generalAccount = new Account();
        generalAccount.setId(34L);
        generalAccount.setUsername("hogehoge");

        AccountApprover accountApprover = new AccountApprover();
        accountApprover.setAccountId(generalAccount);
        accountApprover.setApproverId(adminAccount);

        RequestJudgmentInput requestJudgmentInput = new RequestJudgmentInput();
        requestJudgmentInput.setRequestId(1L);
        requestJudgmentInput.setRequestType(6);
        requestJudgmentInput.setRequestTime("2025/12/30T00:00:00");
        requestJudgmentInput.setApprovalComment("hogehoge");

        AttendanceExceptionRequest attendanceExceptionRequest = new AttendanceExceptionRequest();
        attendanceExceptionRequest.setAccountId(generalAccount);
        attendanceExceptionRequest.setRequestStatus(2);
        attendanceExceptionRequest.setBeginTime(LocalDateTime.parse("2025/12/24/13/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        attendanceExceptionRequest.setEndTime(LocalDateTime.parse("2025/12/24/16/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        AttendanceExceptionType attendanceExceptionType = new AttendanceExceptionType();
        attendanceExceptionType.setAttendanceExceptionTypeId(3L);
        attendanceExceptionRequest.setAttendanceExceptionTypeId(attendanceExceptionType);
        Shift shift = new Shift();
        shift.setOuting(Time.valueOf("00:00:00"));
        shift.setLateness(Time.valueOf("00:00:00"));
        shift.setLeaveEarly(Time.valueOf("00:00:00"));
        attendanceExceptionRequest.setShiftId(shift);

        AttendanceExceptionRequest newAttendanceExceptionRequest = new AttendanceExceptionRequest();
        when(attendanceExceptionRequestService.findById(anyLong())).thenReturn(attendanceExceptionRequest);
        when(accountApproverService.findAccountAndApprover(any(Account.class), any(Account.class))).thenReturn(accountApprover);
        when(attendanceListSourceService.findByShiftId(any(Shift.class))).thenReturn(null);
        when(shiftListOtherTimeService.findByOtherTimeId(any(AttendanceExceptionRequest.class))).thenReturn(new ShiftListOtherTime());
        when(shiftService.save(any(Shift.class))).thenReturn(new Shift());
        when(stringToLocalDateTime.stringToLocalDateTime(requestJudgmentInput.getRequestTime())).thenReturn(LocalDateTime.parse(LocalDateTime.parse(requestJudgmentInput.getRequestTime(),DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(attendanceExceptionRequestService.save(any(AttendanceExceptionRequest.class))).thenReturn(newAttendanceExceptionRequest);

        int result = requestService.approvalCancel(adminAccount, requestJudgmentInput);
        assertEquals(1, result);

        verify(shiftListOtherTimeService).deleteByShiftListOtherTime(any(ShiftListOtherTime.class));
    }

    @Test
    void approvalCancelMonthlySuccess()
    {
        Account adminAccount = new Account();
        adminAccount.setId(1L);
        adminAccount.setUsername("hogehoge");

        Account generalAccount = new Account();
        generalAccount.setId(34L);
        generalAccount.setUsername("hogehoge");

        AccountApprover accountApprover = new AccountApprover();
        accountApprover.setAccountId(generalAccount);
        accountApprover.setApproverId(adminAccount);

        RequestJudgmentInput requestJudgmentInput = new RequestJudgmentInput();
        requestJudgmentInput.setRequestId(1L);
        requestJudgmentInput.setRequestType(7);
        requestJudgmentInput.setRequestTime("2025/12/30T00:00:00");
        requestJudgmentInput.setApprovalComment("hogehoge");

        MonthlyRequest monthlyRequest = new MonthlyRequest();
        monthlyRequest.setAccountId(generalAccount);
        monthlyRequest.setRequestStatus(2);

        List<ShiftListShiftRequest> shiftListShiftRequests = new ArrayList<ShiftListShiftRequest>();
        ShiftListShiftRequest shiftRequestShiftListShiftRequest = new ShiftListShiftRequest();
        ShiftRequest shiftRequest = new ShiftRequest();
        shiftRequestShiftListShiftRequest.setShiftRequestId(shiftRequest);
        ShiftListShiftRequest shiftChangeRequestShiftListShiftRequest = new ShiftListShiftRequest();
        ShiftChangeRequest shiftChangeRequest = new ShiftChangeRequest();
        shiftChangeRequestShiftListShiftRequest.setShiftRequestId(shiftRequest);
        shiftChangeRequestShiftListShiftRequest.setShiftChangeRequestId(shiftChangeRequest);
        shiftListShiftRequests.add(shiftRequestShiftListShiftRequest);
        shiftListShiftRequests.add(shiftChangeRequestShiftListShiftRequest);

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

        List<AttendanceListSource> attendanceListSources = new ArrayList<AttendanceListSource>();
        AttendanceListSource attendanceListSource = new AttendanceListSource();
        StampRequest stampRequest = new StampRequest();
        attendanceListSource.setStampRequestId(stampRequest);
        attendanceListSources.add(attendanceListSource);

        when(monthlyRequestService.findById(anyLong())).thenReturn(monthlyRequest);
        when(accountApproverService.findAccountAndApprover(any(Account.class), any(Account.class))).thenReturn(accountApprover);
        when(shiftService.findByAccountIdAndBeginWorkBetween(any(Account.class), anyInt(), anyInt())).thenReturn(new ArrayList<Shift>());
        when(shiftListOtherTimeService.findByShiftIdIn(anyList())).thenReturn(shiftListOtherTimes);
        when(shiftListOverTimeService.findByShiftIdIn(anyList())).thenReturn(shiftListOverTimes);
        when(shiftListShiftRequestService.findByShiftIdIn(anyList())).thenReturn(shiftListShiftRequests);
        when(shiftListVacationService.findByShiftIdIn(anyList())).thenReturn(shiftListVacations);
        when(attendanceExceptionRequestService.save(any(AttendanceExceptionRequest.class))).thenReturn(new AttendanceExceptionRequest());
        when(overTimeRequestService.save(any(OverTimeRequest.class))).thenReturn(new OverTimeRequest());
        when(shiftRequestService.save(any(ShiftRequest.class))).thenReturn(new ShiftRequest());
        when(shiftChangeRequestService.save(any(ShiftChangeRequest.class))).thenReturn(new ShiftChangeRequest());
        when(vacationRequestService.save(any(VacationRequest.class))).thenReturn(new VacationRequest());
        when(attendService.findByAccountIdAndBeginWorkBetween(any(Account.class), anyInt(), anyInt())).thenReturn(new ArrayList<Attend>());
        when(attendanceListSourceService.findByAttendIdIn(anyList())).thenReturn(attendanceListSources);
        when(stampRequestService.save(any(StampRequest.class))).thenReturn(new StampRequest());
        when(stringToLocalDateTime.stringToLocalDateTime(requestJudgmentInput.getRequestTime())).thenReturn(LocalDateTime.parse(LocalDateTime.parse(requestJudgmentInput.getRequestTime(),DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(monthlyRequestService.save(any(MonthlyRequest.class))).thenReturn(new MonthlyRequest());

        int result = requestService.approvalCancel(adminAccount, requestJudgmentInput);
        assertEquals(1, result);

    }

    @Test
    void getLateNightVacationFirstSuccess()
    {
        LegalTime legalTime = new LegalTime();
        legalTime.setLateNightWorkBegin(Time.valueOf("22:00:00"));
        legalTime.setLateNightWorkEnd(Time.valueOf("05:00:00"));

        LocalDateTime beginWork = LocalDateTime.parse("2025/12/31/22/34/00",DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        LocalDateTime endWork = LocalDateTime.parse("2026/01/01/08/34/00",DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        LocalDateTime beginBreak = LocalDateTime.parse("2025/12/31/23/59/00",DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        LocalDateTime endBreak = LocalDateTime.parse("2026/01/01/00/59/00",DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));

        List<ShiftListVacation> shiftListVacations = new ArrayList<ShiftListVacation>();
        ShiftListVacation shiftListVacation = new ShiftListVacation();
        VacationRequest vacationRequest = new VacationRequest();
        vacationRequest.setBeginVacation(LocalDateTime.parse("2025/12/31/22/50/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        vacationRequest.setEndVacation(LocalDateTime.parse("2025/12/31/23/50/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        shiftListVacation.setVacationId(vacationRequest);
        shiftListVacations.add(shiftListVacation);

        when(legalTimeService.findFirstByOrderByBeginDesc()).thenReturn(legalTime);

        Time result = requestService.getLateNight(beginWork, endWork, beginBreak, endBreak, shiftListVacations);

        assertEquals(result, Time.valueOf("04:26:00"));
    }

    @Test
    void getVacationFirstSuccess()
    {
        LocalDateTime beginBreak = LocalDateTime.parse("2025/12/24/12/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        LocalDateTime endBreak = LocalDateTime.parse("2025/12/24/13/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        LocalDateTime beginVacation = LocalDateTime.parse("2025/12/24/10/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        LocalDateTime endVacation = LocalDateTime.parse("2025/12/24/11/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));

        Duration result = requestService.getVacation(beginVacation, endVacation, beginBreak, endBreak);
        assertEquals(Duration.between(beginVacation, endVacation), result);
    }

    @Test
    void getVacationSecondSuccess()
    {
        LocalDateTime beginBreak = LocalDateTime.parse("2025/12/24/12/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        LocalDateTime endBreak = LocalDateTime.parse("2025/12/24/13/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        LocalDateTime beginVacation = LocalDateTime.parse("2025/12/24/09/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        LocalDateTime endVacation = LocalDateTime.parse("2025/12/24/18/00/00", DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));

        Duration result = requestService.getVacation(beginVacation, endVacation, beginBreak, endBreak);
        assertEquals(Duration.between(beginVacation, endVacation).minus(Duration.between(beginBreak, endBreak)), result);
    }


}