package com.example.springboot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
import com.example.springboot.dto.input.ShiftChangeInput;
import com.example.springboot.dto.input.ShiftInput;
import com.example.springboot.dto.input.StampInput;
import com.example.springboot.dto.input.VacationInput;
import com.example.springboot.dto.response.RequestDetailShiftChangeResponse;
import com.example.springboot.dto.response.RequestDetailShiftResponse;
import com.example.springboot.model.Account;
import com.example.springboot.model.Attend;
import com.example.springboot.model.AttendanceExceptionRequest;
import com.example.springboot.model.AttendanceExceptionType;
import com.example.springboot.model.AttendanceListSource;
import com.example.springboot.model.LegalTime;
import com.example.springboot.model.MonthlyRequest;
import com.example.springboot.model.NewsList;
import com.example.springboot.model.OverTimeRequest;
import com.example.springboot.model.PaydHoliday;
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
import com.example.springboot.service.AttendService;
import com.example.springboot.service.AttendanceExceptionRequestService;
import com.example.springboot.service.AttendanceExceptionTypeService;
import com.example.springboot.service.AttendanceListSourceService;
import com.example.springboot.service.LegalTimeService;
import com.example.springboot.service.MonthlyRequestService;
import com.example.springboot.service.NewsListService;
import com.example.springboot.service.OverTimeRequestService;
import com.example.springboot.service.PaydHolidayService;
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
        generalShift.setShiftId(generalShiftId);
        generalShift.setBeginWork(LocalDateTime.parse(LocalDateTime.parse(generalShiftBeginWork,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        generalShift.setEndWork(LocalDateTime.parse(LocalDateTime.parse(generalShiftEndWork,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));

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

        List<Shift> shifts = new ArrayList<Shift>();

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
        when(shiftService.shiftOverLapping(any(Account.class), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(shifts);
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

        List<Shift> shifts = new ArrayList<Shift>();

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
        when(shiftService.shiftOverLapping(any(Account.class), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(shifts);
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
}
