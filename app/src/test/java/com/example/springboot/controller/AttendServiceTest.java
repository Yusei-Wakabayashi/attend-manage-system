package com.example.springboot.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
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
import com.example.springboot.dto.change.DurationToString;
import com.example.springboot.dto.input.UserAttendInput;
import com.example.springboot.dto.input.UserMonthWorkInfoInput;
import com.example.springboot.dto.input.YearMonthInput;
import com.example.springboot.dto.response.AttendListResponse;
import com.example.springboot.dto.response.MonthWorkInfoResponse;
import com.example.springboot.model.Account;
import com.example.springboot.model.AccountApprover;
import com.example.springboot.model.Attend;
import com.example.springboot.model.Vacation;
import com.example.springboot.service.AttendService;
import com.example.springboot.service.VacationService;

@ContextConfiguration(classes = Config.class)
@ExtendWith(MockitoExtension.class)
public class AttendServiceTest
{
    @InjectMocks
    @Spy
    AttendService attendService;

    @Mock
    DurationToString durationToString;

    @Mock
    VacationService vacationService;

    @Test
    void attendListSuccess()
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

        YearMonthInput request = new YearMonthInput();
        request.setYear(2025);
        request.setMonth(8);

        List<Attend> generalAttends = new ArrayList<Attend>();
        generalAttends.add(generalAttend);

        doReturn(generalAttends).when(attendService).findByAccountIdAndBeginWorkBetween(anyLong(), anyInt(), anyInt());
        doReturn(generalAttendListResponse).when(attendService).attendToAttendListResponse(any(Attend.class));

        List<AttendListResponse> resultAttendListResponses = attendService.findAttendListFor(generalAccount, request);
        assertEquals(1, resultAttendListResponses.size());
    }

    @Test
    void monthWorkInfoResponseSuccess()
    {
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
        vacation.setBeginVacation(LocalDateTime.parse(LocalDateTime.parse("2025/09/08T01:00:00",DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        vacation.setEndVacation(LocalDateTime.parse(LocalDateTime.parse("2025/09/08T02:00:00",DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        vacations.add(vacation);

        YearMonthInput yearMonthInput = new YearMonthInput();
        yearMonthInput.setYear(2025);
        yearMonthInput.setMonth(9);

        when(vacationService.findByAccountIdAndBeginVacationBetweenMonthAndPaydHoliday(any(Account.class), anyInt(), anyInt())).thenReturn(vacations);
        when(durationToString.durationToString(any(Duration.class))).thenReturn("00:00:00");

        doReturn(attends).when(attendService).findByAccountIdAndBeginWorkBetween(any(Account.class), anyInt(),anyInt());

        MonthWorkInfoResponse result = attendService.monthWorkInfoResponse(generalAccount, yearMonthInput);
        assertEquals(1, result.getStatus());
    }

    @Test
    void returnUserAttendListSuccess()
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

        UserAttendInput userAttendInput = new UserAttendInput();
        userAttendInput.setAccountId(3L);
        userAttendInput.setYear(2025);
        userAttendInput.setMonth(6);

        List<Attend> generalAttends = new ArrayList<Attend>();
        generalAttends.add(generalAttend);

        doReturn(generalAttends).when(attendService).findByAccountIdAndBeginWorkBetween(anyLong(), anyInt(), anyInt());
        doReturn(generalAttendListResponse).when(attendService).attendToAttendListResponse(any(Attend.class));

        List<AttendListResponse> attendListResponses = attendService.getAttendListResponses(adminAccount, userAttendInput);
        assertEquals(1, attendListResponses.size());
    }

    @Test
    void getMonthWorkInfoResponseSuccess()
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

        UserMonthWorkInfoInput userMonthWorkInfoInput = new UserMonthWorkInfoInput();
        userMonthWorkInfoInput.setAccountId(3L);
        userMonthWorkInfoInput.setYear(2025);
        userMonthWorkInfoInput.setMonth(9);

        List<Vacation> vacations = new ArrayList<Vacation>();
        Vacation vacation = new Vacation();
        vacation.setBeginVacation(LocalDateTime.parse(LocalDateTime.parse("2025/09/08T01:00:00",DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        vacation.setEndVacation(LocalDateTime.parse(LocalDateTime.parse("2025/09/08T02:00:00",DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        vacations.add(vacation);

        doReturn(attends).when(attendService).findByAccountIdAndBeginWorkBetween(any(Account.class), anyInt(), anyInt());
        when(vacationService.findByAccountIdAndBeginVacationBetweenMonthAndPaydHoliday(any(Account.class), anyInt(), anyInt())).thenReturn(vacations);
        when(durationToString.durationToString(any(Duration.class))).thenReturn("000:00:00");

        MonthWorkInfoResponse result = attendService.getMonthWorkInfoResponse(generalAccount, accountApprover, userMonthWorkInfoInput);
        assertEquals(1, result.getStatus());
    }

}
