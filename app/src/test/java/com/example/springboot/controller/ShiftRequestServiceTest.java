package com.example.springboot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
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
import com.example.springboot.dto.input.ShiftInput;
import com.example.springboot.model.Account;
import com.example.springboot.model.Shift;
import com.example.springboot.model.ShiftRequest;
import com.example.springboot.service.AccountService;
import com.example.springboot.service.ShiftRequestService;
import com.example.springboot.service.ShiftService;

@ContextConfiguration(classes = Config.class)
@ExtendWith(MockitoExtension.class)
public class ShiftRequestServiceTest
{
    @InjectMocks
    @Spy
    ShiftRequestService shiftRequestService;

    @Mock
    ShiftService shiftService;

    @Mock
    AccountService accountService;

    @Mock
    StringToLocalDateTime stringToLocalDateTime = new StringToLocalDateTime();

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

        doReturn(shiftRequests).when(shiftRequestService).findAccountIdAndBeginWorkBetweenDay(any(Account.class), any(LocalDateTime.class));
        doReturn(shiftRequest).when(shiftRequestService).save(any(ShiftRequest.class));
        // 実行
        int result = shiftRequestService.createShiftRequest(generalAccount, shiftInput);

        assertEquals(1, result);
    }
}
