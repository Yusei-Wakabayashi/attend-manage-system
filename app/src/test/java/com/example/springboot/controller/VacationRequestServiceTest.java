package com.example.springboot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

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
import com.example.springboot.dto.input.VacationInput;
import com.example.springboot.model.Account;
import com.example.springboot.model.OverTimeRequest;
import com.example.springboot.model.PaydHoliday;
import com.example.springboot.model.Shift;
import com.example.springboot.model.ShiftListOverTime;
import com.example.springboot.model.VacationRequest;
import com.example.springboot.model.VacationType;
import com.example.springboot.service.OverTimeRequestService;
import com.example.springboot.service.PaydHolidayService;
import com.example.springboot.service.ShiftListOverTimeService;
import com.example.springboot.service.ShiftService;
import com.example.springboot.service.VacationRequestService;
import com.example.springboot.service.VacationTypeService;

@ContextConfiguration(classes = Config.class)
@ExtendWith(MockitoExtension.class)
public class VacationRequestServiceTest
{
    @InjectMocks
    @Spy
    VacationRequestService vacationRequestService;

    @Mock
    ShiftService shiftService;

    @Mock
    ShiftListOverTimeService shiftListOverTimeService;

    @Mock
    OverTimeRequestService overTimeRequestService;
    
    @Mock
    PaydHolidayService paydHolidayService;

    @Mock
    VacationTypeService vacationTypeService;

    @Mock
    StringToLocalDateTime stringToLocalDateTime;

    @Mock
    StringToDuration stringToDuration;

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

        doReturn(vacationRequests).when(vacationRequestService).findByAccountIdAndShiftId(any(Account.class), any(Shift.class));
        doReturn(paydHolidayVacationRequests).when(vacationRequestService).findByAccountIdAndRequestStatusWaitAndVacationTypePaydHoiday(any(Account.class));
        doReturn(vacationRequest).when(vacationRequestService).save(any(VacationRequest.class));
        int result = vacationRequestService.createVacationRequest(generalAccount, vacationInput);
        assertEquals(1, result);
    }
}
