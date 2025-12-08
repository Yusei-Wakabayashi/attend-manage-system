package com.example.springboot.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;

import com.example.springboot.Config;
import com.example.springboot.dto.input.YearMonthInput;
import com.example.springboot.dto.response.AttendListResponse;
import com.example.springboot.model.Account;
import com.example.springboot.model.Attend;
import com.example.springboot.service.AttendService;

@ContextConfiguration(classes = Config.class)
@ExtendWith(MockitoExtension.class)
public class AttendServiceTest
{
    @InjectMocks
    @Spy
    AttendService attendService;

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
}
