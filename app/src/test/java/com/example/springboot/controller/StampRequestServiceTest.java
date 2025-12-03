package com.example.springboot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

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
import com.example.springboot.dto.change.StringToLocalDateTime;
import com.example.springboot.dto.input.StampInput;
import com.example.springboot.model.Account;
import com.example.springboot.model.MonthlyRequest;
import com.example.springboot.model.Shift;
import com.example.springboot.model.StampRequest;
import com.example.springboot.service.MonthlyRequestService;
import com.example.springboot.service.ShiftService;
import com.example.springboot.service.StampRequestService;

@ContextConfiguration(classes = Config.class)
@ExtendWith(MockitoExtension.class)
public class StampRequestServiceTest
{
    @InjectMocks
    @Spy
    StampRequestService stampRequestService;

    @Mock
    StringToLocalDateTime stringToLocalDateTime;

    @Mock
    ShiftService shiftService;

    @Mock
    MonthlyRequestService monthlyRequestService;

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

        doReturn(stampRequests).when(stampRequestService).findByShiftIdAndRequestStatusWait(any(Shift.class));
        doReturn(stampRequest).when(stampRequestService).save(any(StampRequest.class));

        int result = stampRequestService.createStampRequest(generalAccount, stampInput);

        assertEquals(1, result);
    }
}
