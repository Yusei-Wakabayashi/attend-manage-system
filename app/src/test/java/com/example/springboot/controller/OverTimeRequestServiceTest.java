package com.example.springboot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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
import com.example.springboot.dto.input.OverTimeInput;
import com.example.springboot.model.Account;
import com.example.springboot.model.LegalTime;
import com.example.springboot.model.OverTimeRequest;
import com.example.springboot.model.Shift;
import com.example.springboot.model.ShiftListOtherTime;
import com.example.springboot.service.LegalTimeService;
import com.example.springboot.service.OverTimeRequestService;
import com.example.springboot.service.ShiftListOtherTimeService;
import com.example.springboot.service.ShiftService;

@ContextConfiguration(classes = Config.class)
@ExtendWith(MockitoExtension.class)
public class OverTimeRequestServiceTest
{
    @InjectMocks
    @Spy
    OverTimeRequestService overTimeRequestService;

    @Mock
    StringToLocalDateTime stringToLocalDateTime;

    @Mock
    StringToDuration stringToDuration;

    @Mock
    ShiftService shiftService;

    @Mock
    ShiftListOtherTimeService shiftListOtherTimeService;

    @Mock
    LegalTimeService legalTimeService;

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

        doReturn(overTimeRequests).when(overTimeRequestService).findByAccounIdAndRequestStatusWaitOrApprovedAndBeginWorkOrEndWorkBetween(any(Account.class), any(LocalDateTime.class), any(LocalDateTime.class));
        doReturn(overTimeRequestMonth).when(overTimeRequestService).findByAccountIdAndRequestStatusWaitOrApprovedAndBeginWorkBetweenMonth(any(Account.class), anyInt(), anyInt());
        doReturn(newOverTimeRequest).when(overTimeRequestService).save(any(OverTimeRequest.class));

        int result = overTimeRequestService.createOverTimeRequest(generalAccount, overTimeInput);
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

        doReturn(overTimeRequests).when(overTimeRequestService).findByAccounIdAndRequestStatusWaitOrApprovedAndBeginWorkOrEndWorkBetween(any(Account.class), any(LocalDateTime.class), any(LocalDateTime.class));
        doReturn(overTimeRequestMonth).when(overTimeRequestService).findByAccountIdAndRequestStatusWaitOrApprovedAndBeginWorkBetweenMonth(any(Account.class), anyInt(), anyInt());
        doReturn(newOverTimeRequest).when(overTimeRequestService).save(any(OverTimeRequest.class));

        int result = overTimeRequestService.createOverTimeRequest(generalAccount, overTimeInput);
        assertEquals(1, result);
    }
}
