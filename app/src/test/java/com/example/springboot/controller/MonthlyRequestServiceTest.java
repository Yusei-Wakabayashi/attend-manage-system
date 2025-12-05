package com.example.springboot.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doReturn;
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
import com.example.springboot.dto.change.StringToLocalDateTime;
import com.example.springboot.dto.input.MonthlyInput;
import com.example.springboot.model.Account;
import com.example.springboot.model.Attend;
import com.example.springboot.model.AttendanceExceptionRequest;
import com.example.springboot.model.AttendanceListSource;
import com.example.springboot.model.MonthlyRequest;
import com.example.springboot.model.NewsList;
import com.example.springboot.model.OverTimeRequest;
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
import com.example.springboot.service.AttendService;
import com.example.springboot.service.AttendanceExceptionRequestService;
import com.example.springboot.service.AttendanceListSourceService;
import com.example.springboot.service.MonthlyRequestService;
import com.example.springboot.service.NewsListService;
import com.example.springboot.service.OverTimeRequestService;
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

@ContextConfiguration(classes = Config.class)
@ExtendWith(MockitoExtension.class)
public class MonthlyRequestServiceTest
{
    @InjectMocks
    @Spy
    MonthlyRequestService monthlyRequestService;

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

        doReturn(monthlyRequest).when(monthlyRequestService).save(any(MonthlyRequest.class));

        int result = monthlyRequestService.createMonthlyRequest(generalAccount, monthlyInput);
        assertEquals(1, result);
    }
}
