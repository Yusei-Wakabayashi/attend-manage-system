package com.example.springboot.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalDateTime;
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
import com.example.springboot.dto.change.StringToDuration;
import com.example.springboot.dto.change.StringToLocalDateTime;
import com.example.springboot.dto.input.LegalCheckShiftChangeInput;
import com.example.springboot.dto.input.LegalCheckShiftInput;
import com.example.springboot.model.Account;
import com.example.springboot.model.LegalTime;
import com.example.springboot.model.Shift;
import com.example.springboot.model.ShiftChangeRequest;
import com.example.springboot.model.ShiftListShiftRequest;
import com.example.springboot.model.ShiftRequest;
import com.example.springboot.model.Vacation;
import com.example.springboot.service.AccountService;
import com.example.springboot.service.LegalCheckService;
import com.example.springboot.service.LegalTimeService;
import com.example.springboot.service.ShiftListShiftRequestService;
import com.example.springboot.service.ShiftService;
import com.example.springboot.service.VacationService;

@ContextConfiguration(classes = Config.class)
@ExtendWith(MockitoExtension.class)
public class LegalCheckServiceTest
{
    @InjectMocks
    @Spy
    LegalCheckService legalCheckService;

    @Mock
    AccountService accountService;

    @Mock
    ShiftService shiftService;

    @Mock
    ShiftListShiftRequestService shiftListShiftRequestService;

    @Mock
    VacationService vacationService;

    @Mock
    LegalTimeService legalTimeService;

    @Mock
    StringToDuration stringToDuration;

    @Mock
    StringToLocalDateTime stringToLocalDateTime;

    @Test
    void legalCheckShiftSuccess() throws Exception
    {
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
        legalTime.setLateNightWorkBegin(Time.valueOf(legalTimeLateNightWorkTimeBegin));
        legalTime.setLateNightWorkEnd(Time.valueOf(legalTimeLateNightWorkTimeEnd));
        legalTime.setScheduleBreakTime(legalTimeScheduleBreakTime);
        legalTime.setWeeklyHoliday(legalTimeWeeklyHoliday);

        String generalBeginWork = "2026/09/02T09:00:00";
        String generalBeginBreak = "2026/09/02T12:00:00";
        String generalEndBreak = "2026/09/02T13:00:00";
        String generalEndWork = "2026/09/02T18:00:00";
        LegalCheckShiftInput legalCheckShiftInput = new LegalCheckShiftInput();
        legalCheckShiftInput.setBeginWork(generalBeginWork);
        legalCheckShiftInput.setEndWork(generalEndWork);
        legalCheckShiftInput.setBeginBreak(generalBeginBreak);
        legalCheckShiftInput.setEndBreak(generalEndBreak);

        Account generalAccount = new Account();
        Long generalAccountId = 1L;
        String generalAccountName = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setName(generalAccountName);

        Long shiftId = 22L;
        LocalDateTime BeginWorkTimeShift = LocalDateTime.parse(LocalDateTime.parse("2025/09/17T09:00:00", DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        LocalDateTime EndWorkTimeShift = LocalDateTime.parse(LocalDateTime.parse("2025/09/17T18:00:00",DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        LocalDateTime BeginBreakTimeShift = LocalDateTime.parse(LocalDateTime.parse("2025/09/17T12:00:00",DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        LocalDateTime EndBreakTimeShift = LocalDateTime.parse(LocalDateTime.parse("2025/09/17T13:00:00",DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));

        Long generalShiftId = 20L;
        LocalDateTime generalBeginWorkTimeShift = LocalDateTime.parse(LocalDateTime.parse("2025/09/18T09:00:00", DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        LocalDateTime generalEndWorkTimeShift = LocalDateTime.parse(LocalDateTime.parse("2025/09/18T18:00:00", DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        LocalDateTime generalBeginBreakTimeShift = LocalDateTime.parse(LocalDateTime.parse("2025/09/18T12:00:00", DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        LocalDateTime generalEndBreakTimeShift = LocalDateTime.parse(LocalDateTime.parse("2025/09/18T13:00:00", DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));

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
        ShiftListShiftRequest generalShiftListShiftRequest = new ShiftListShiftRequest();
        generalShiftListShiftRequest.setShiftRequestId(shiftRequest);
        generalShiftListShiftRequest.setShiftChangeRequestId(shiftChangeRequest);
        shiftListShiftRequests.add(shiftListShiftRequest);
        shiftListShiftRequests.add(generalShiftListShiftRequest);

        List<Vacation> vacations = new ArrayList<Vacation>();

        when(stringToLocalDateTime.stringToLocalDateTime(legalCheckShiftInput.getBeginWork())).thenReturn(LocalDateTime.parse(LocalDateTime.parse(legalCheckShiftInput.getBeginWork(),DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(stringToLocalDateTime.stringToLocalDateTime(legalCheckShiftInput.getEndWork())).thenReturn(LocalDateTime.parse(LocalDateTime.parse(legalCheckShiftInput.getEndWork(),DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(stringToLocalDateTime.stringToLocalDateTime(legalCheckShiftInput.getBeginBreak())).thenReturn(LocalDateTime.parse(LocalDateTime.parse(legalCheckShiftInput.getBeginBreak(),DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(stringToLocalDateTime.stringToLocalDateTime(legalCheckShiftInput.getEndBreak())).thenReturn(LocalDateTime.parse(LocalDateTime.parse(legalCheckShiftInput.getEndBreak(),DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(legalTimeService.findFirstByOrderByBeginDesc()).thenReturn(legalTime);
        when(stringToDuration.stringToDuration(legalTime.getScheduleWorkTime())).thenReturn(Duration.ofHours(Long.parseLong(legalTime.getScheduleWorkTime().split(":")[0])).plusMinutes(Long.parseLong(legalTime.getScheduleWorkTime().split(":")[1])).plusSeconds(Long.parseLong(legalTime.getScheduleWorkTime().split(":")[2])));
        when(stringToDuration.stringToDuration(legalTime.getScheduleBreakTime())).thenReturn(Duration.ofHours(Long.parseLong(legalTime.getScheduleBreakTime().split(":")[0])).plusMinutes(Long.parseLong(legalTime.getScheduleBreakTime().split(":")[1])).plusSeconds(Long.parseLong(legalTime.getScheduleBreakTime().split(":")[2])));
        when(stringToDuration.stringToDuration(legalTime.getWeeklyWorkTime())).thenReturn(Duration.ofHours(Long.parseLong(legalTime.getWeeklyWorkTime().split(":")[0])).plusMinutes(Long.parseLong(legalTime.getWeeklyWorkTime().split(":")[1])).plusSeconds(Long.parseLong(legalTime.getWeeklyWorkTime().split(":")[2])));
        when(shiftService.findByAccountIdAndBeginWorkBetweenWeek(any(Account.class), any(LocalDateTime.class))).thenReturn(generalShifts);
        when(shiftListShiftRequestService.findByShiftIdIn(anyList())).thenReturn(shiftListShiftRequests);
        when(vacationService.findByAccountIdAndBeginVacationBetweenWeek(any(Account.class), any(LocalDateTime.class))).thenReturn(vacations);

        int result = legalCheckService.shiftLegalCheck(generalAccount, legalCheckShiftInput);
        assertEquals(1, result);
    }

    @Test
    void legalCheckShiftChangeSuccess() throws Exception
    {
        String generalBeginWork = "2026/09/02T09:00:00";
        String generalBeginBreak = "2026/09/02T12:00:00";
        String generalEndBreak = "2026/09/02T13:00:00";
        String generalEndWork = "2026/09/02T18:00:00";

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
        legalTime.setLateNightWorkBegin(Time.valueOf(legalTimeLateNightWorkTimeBegin));
        legalTime.setLateNightWorkEnd(Time.valueOf(legalTimeLateNightWorkTimeEnd));
        legalTime.setScheduleBreakTime(legalTimeScheduleBreakTime);
        legalTime.setWeeklyHoliday(legalTimeWeeklyHoliday);

        LegalCheckShiftChangeInput legalCheckShiftChangeInput = new LegalCheckShiftChangeInput();
        legalCheckShiftChangeInput.setBeginWork(generalBeginWork);
        legalCheckShiftChangeInput.setEndWork(generalEndWork);
        legalCheckShiftChangeInput.setBeginBreak(generalBeginBreak);
        legalCheckShiftChangeInput.setEndBreak(generalEndBreak);
        legalCheckShiftChangeInput.setShiftId(3L);

        Account generalAccount = new Account();
        Long generalAccountId = 1L;
        String generalAccountName = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setName(generalAccountName);

        Shift shift = new Shift();
        Long shiftId = 1L;
        shift.setShiftId(shiftId);
        shift.setBeginWork(LocalDateTime.parse(LocalDateTime.parse(generalBeginWork,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));

        Long generalShiftId = 22L;
        LocalDateTime generalBeginWorkTimeShift = LocalDateTime.parse(LocalDateTime.parse("2025/09/17T09:00:00", DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        LocalDateTime generalEndWorkTimeShift = LocalDateTime.parse(LocalDateTime.parse("2025/09/17T18:00:00",DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        LocalDateTime generalBeginBreakTimeShift = LocalDateTime.parse(LocalDateTime.parse("2025/09/17T12:00:00",DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        LocalDateTime generalEndBreakTimeShift = LocalDateTime.parse(LocalDateTime.parse("2025/09/17T13:00:00",DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));

        Long adminShiftId = 20L;
        LocalDateTime adminBeginWorkTimeShift = LocalDateTime.parse(LocalDateTime.parse("2025/09/18T09:00:00", DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        LocalDateTime adminEndWorkTimeShift = LocalDateTime.parse(LocalDateTime.parse("2025/09/18T18:00:00", DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        LocalDateTime adminBeginBreakTimeShift = LocalDateTime.parse(LocalDateTime.parse("2025/09/18T12:00:00", DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        LocalDateTime adminEndBreakTimeShift = LocalDateTime.parse(LocalDateTime.parse("2025/09/18T13:00:00", DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));

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
        generalShiftListShiftRequest.setShiftRequestId(shiftRequest);
        generalShiftListShiftRequest.setShiftChangeRequestId(shiftChangeRequest);
        shiftListShiftRequests.add(shiftListShiftRequest);
        shiftListShiftRequests.add(generalShiftListShiftRequest);

        List<Vacation> vacations = new ArrayList<Vacation>();
        Vacation vacation = new Vacation();
        vacation.setBeginVacation(adminBeginBreakTimeShift);
        vacation.setEndVacation(adminEndBreakTimeShift);
        vacations.add(vacation);

        when(stringToLocalDateTime.stringToLocalDateTime(legalCheckShiftChangeInput.getBeginWork())).thenReturn(LocalDateTime.parse(LocalDateTime.parse(legalCheckShiftChangeInput.getBeginWork(),DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(stringToLocalDateTime.stringToLocalDateTime(legalCheckShiftChangeInput.getEndWork())).thenReturn(LocalDateTime.parse(LocalDateTime.parse(legalCheckShiftChangeInput.getEndWork(),DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(stringToLocalDateTime.stringToLocalDateTime(legalCheckShiftChangeInput.getBeginBreak())).thenReturn(LocalDateTime.parse(LocalDateTime.parse(legalCheckShiftChangeInput.getBeginBreak(),DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(stringToLocalDateTime.stringToLocalDateTime(legalCheckShiftChangeInput.getEndBreak())).thenReturn(LocalDateTime.parse(LocalDateTime.parse(legalCheckShiftChangeInput.getEndBreak(),DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(shiftService.findByAccountIdAndShiftId(any(Account.class), anyLong())).thenReturn(shift);
        when(legalTimeService.findFirstByOrderByBeginDesc()).thenReturn(legalTime);
        when(stringToDuration.stringToDuration(legalTime.getScheduleWorkTime())).thenReturn(Duration.ofHours(Long.parseLong(legalTime.getScheduleWorkTime().split(":")[0])).plusMinutes(Long.parseLong(legalTime.getScheduleWorkTime().split(":")[1])).plusSeconds(Long.parseLong(legalTime.getScheduleWorkTime().split(":")[2])));
        when(shiftService.findByAccountIdAndBeginWorkBetweenWeek(any(Account.class), any(LocalDateTime.class))).thenReturn(generalShifts);
        when(shiftListShiftRequestService.findByShiftIdIn(anyList())).thenReturn(shiftListShiftRequests);
        when(vacationService.findByAccountIdAndBeginVacationBetweenWeek(any(Account.class), any(LocalDateTime.class))).thenReturn(vacations);
        when(stringToDuration.stringToDuration(legalTime.getScheduleBreakTime())).thenReturn(Duration.ofHours(Long.parseLong(legalTime.getScheduleBreakTime().split(":")[0])).plusMinutes(Long.parseLong(legalTime.getScheduleBreakTime().split(":")[1])).plusSeconds(Long.parseLong(legalTime.getScheduleBreakTime().split(":")[2])));
        when(stringToDuration.stringToDuration(legalTime.getWeeklyWorkTime())).thenReturn(Duration.ofHours(Long.parseLong(legalTime.getWeeklyWorkTime().split(":")[0])).plusMinutes(Long.parseLong(legalTime.getWeeklyWorkTime().split(":")[1])).plusSeconds(Long.parseLong(legalTime.getWeeklyWorkTime().split(":")[2])));


        int result = legalCheckService.shiftChangeLegalCheck(generalAccount, legalCheckShiftChangeInput);
        assertEquals(1, result);
    }

}
