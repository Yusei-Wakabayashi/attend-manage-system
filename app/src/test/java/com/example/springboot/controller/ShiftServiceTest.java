package com.example.springboot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;

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
import com.example.springboot.dto.input.UserShiftInput;
import com.example.springboot.dto.input.YearMonthInput;
import com.example.springboot.dto.response.ShiftListResponse;
import com.example.springboot.model.Account;
import com.example.springboot.model.AccountApprover;
import com.example.springboot.model.Shift;
import com.example.springboot.service.ShiftService;

@ContextConfiguration(classes = Config.class)
@ExtendWith(MockitoExtension.class)
public class ShiftServiceTest
{
    @InjectMocks
    @Spy
    ShiftService shiftService;

    @Test
    void findByShiftListForSuccess()
    {
        Account account = new Account();
        account.setId(1L);

        YearMonthInput request = new YearMonthInput();
        request.setYear(2025);
        request.setMonth(4);

        Shift shiftFirst = new Shift();
        Shift shiftSecond = new Shift();

        List<Shift> shifts = List.of(shiftFirst, shiftSecond);

        // shiftToShiftListResponse の挙動も簡略化してモックする
        ShiftListResponse shiftListResponseFirst = new ShiftListResponse();
        ShiftListResponse shiftListResponseSecond = new ShiftListResponse();

        doReturn(shifts).when(shiftService).findByAccountIdAndBeginWorkBetween(anyLong(), anyInt(), anyInt());
        doReturn(shiftListResponseFirst).when(shiftService).shiftToShiftListResponse(shiftFirst);
        doReturn(shiftListResponseSecond).when(shiftService).shiftToShiftListResponse(shiftSecond);

        // 実行
        List<ShiftListResponse> result = shiftService.findByShiftListFor(account, request);

        assertEquals(2, result.size());
    }

    @Test
    void returnShiftListResponsesSuccess()
    {
        Long generalAccountId = 1L;
        String generalAccountName = "testuser";
        String time = "00:00:00";
        Long generalShiftId = 1L;
        LocalDateTime generalShiftBeginWork = LocalDateTime.parse("2025/06/21/08/30/00",DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        LocalDateTime generalShiftEndWork = LocalDateTime.parse("2025/06/21/17/30/00",DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        LocalDateTime generalShiftBeginBreak = LocalDateTime.parse("2025/06/21/11/30/00",DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        LocalDateTime generalShiftEndBreak = LocalDateTime.parse("2025/06/21/12/30/30",DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        Time generalShiftLateness = Time.valueOf(time);
        Time generalShiftLeaveEarly = Time.valueOf(time);
        Time generalShiftOuting = Time.valueOf(time);
        Time generalShiftOverWork = Time.valueOf(time);
        Account generalAccount = new Account();
        generalAccount.setId(generalAccountId);
        generalAccount.setName(generalAccountName);
        Account adminAccount = new Account();
        Long adminAccountId = 24L;
        String adminAccoutUsername = "hogehoge";
        adminAccount.setId(adminAccountId);
        adminAccount.setUsername(adminAccoutUsername);

        AccountApprover accountApprover = new AccountApprover();
        accountApprover.setAccountId(generalAccount);
        accountApprover.setApproverId(adminAccount);
        Shift generalShift = new Shift
        (
            generalShiftId, generalAccount, generalShiftBeginWork,
            generalShiftEndWork, generalShiftBeginBreak, generalShiftEndBreak,
            generalShiftLateness, generalShiftLeaveEarly, generalShiftOuting, generalShiftOverWork
        );
        ShiftListResponse generalShiftListResponse = new ShiftListResponse
        (
            generalShiftId,
            generalShiftBeginWork.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + generalShiftBeginWork.format(DateTimeFormatter.ofPattern("HH:mm:ss")),
            generalShiftEndWork.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + generalShiftEndWork.format(DateTimeFormatter.ofPattern("HH:mm:ss")),
            generalShiftBeginBreak.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + generalShiftBeginBreak.format(DateTimeFormatter.ofPattern("HH:mm:ss")),
            generalShiftEndBreak.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + generalShiftEndBreak.format(DateTimeFormatter.ofPattern("HH:mm:ss")),
            generalShiftLateness,
            generalShiftLeaveEarly,
            generalShiftOuting,
            generalShiftOverWork
        );
        List<Shift> generalShifts = new ArrayList<Shift>();
        generalShifts.add(generalShift);

        UserShiftInput userShiftInput = new UserShiftInput();
        userShiftInput.setAccountId(3L);
        userShiftInput.setYear(2025);
        userShiftInput.setMonth(9);

        doReturn(generalShifts).when(shiftService).findByAccountIdAndBeginWorkBetween(anyLong(), anyInt(), anyInt());
        doReturn(generalShiftListResponse).when(shiftService).shiftToShiftListResponse(any(Shift.class));

        List<ShiftListResponse> shiftListResponses = shiftService.returnShiftListResponses(adminAccount, accountApprover, userShiftInput);
        assertEquals(1, shiftListResponses.size());
    }

}
