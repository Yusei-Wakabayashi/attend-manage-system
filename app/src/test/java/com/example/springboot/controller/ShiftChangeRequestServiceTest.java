package com.example.springboot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
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
import com.example.springboot.dto.input.ShiftChangeInput;
import com.example.springboot.model.Account;
import com.example.springboot.model.Shift;
import com.example.springboot.model.ShiftChangeRequest;
import com.example.springboot.service.ShiftChangeRequestService;
import com.example.springboot.service.ShiftService;

@ContextConfiguration(classes = Config.class)
@ExtendWith(MockitoExtension.class)
public class ShiftChangeRequestServiceTest
{
    @InjectMocks
    @Spy
    ShiftChangeRequestService shiftChangeRequestService;
    
    @Mock
    ShiftService shiftService;

    @Mock
    StringToLocalDateTime stringToLocalDateTime;
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

        doReturn(generalShiftChangeRequests).when(shiftChangeRequestService).findByAccountIdAndShiftIdAndRequestStatusWait(any(Account.class), anyLong());
        doReturn(shiftChangeRequest).when(shiftChangeRequestService).save(any(ShiftChangeRequest.class));
        int result = shiftChangeRequestService.createShiftChangeRequest(generalAccount, shiftChangeInput);

        assertEquals(1, result);
    }
}
