package com.example.springboot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;

import com.example.springboot.Config;
import com.example.springboot.dto.input.YearMonthInput;
import com.example.springboot.dto.response.ShiftListResponse;
import com.example.springboot.model.Account;
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
}
