package com.example.springboot.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
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
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;

import com.example.springboot.Config;
import com.example.springboot.dto.change.LocalDateTimeToString;
import com.example.springboot.dto.response.PaydHolidayHistoryListResponse;
import com.example.springboot.model.Account;
import com.example.springboot.model.PaydHoliday;
import com.example.springboot.model.PaydHolidayUse;
import com.example.springboot.service.PaydHolidayService;
import com.example.springboot.service.PaydHolidayUseService;

@ContextConfiguration(classes = Config.class)
@ExtendWith(MockitoExtension.class)
public class PaydHolidayServiceTest
{
    @InjectMocks
    @Spy
    PaydHolidayService paydHolidayService;

    @Mock
    PaydHolidayUseService paydHolidayUseService;

    @Mock
    LocalDateTimeToString localDateTimeToString;

    @Test
    void returnPaydHolidayHistoryListResponsesSuccess()
    {
        Account generalAccount = new Account();
        String generalAccountUsername = "testuser";
        generalAccount.setUsername(generalAccountUsername);

        List<PaydHoliday> paydHolidays = new ArrayList<PaydHoliday>();
        PaydHoliday generalPaydHoliday = new PaydHoliday();
        String generalPaydHolidayGrant = "2025/04/01T09:00:00";
        String generalPaydHolidayTime = "160:00:00";
        generalPaydHoliday.setGrant(LocalDateTime.parse(LocalDateTime.parse(generalPaydHolidayGrant,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        generalPaydHoliday.setTime(generalPaydHolidayTime);
        paydHolidays.add(generalPaydHoliday);

        List<PaydHolidayUse> paydHolidayUses = new ArrayList<PaydHolidayUse>();
        PaydHolidayUse generalPaydHolidayUse = new PaydHolidayUse();
        String generalPaydHolidayUseDate = "2025/11/02T10:00:00";
        String generalPaydHolidayUseTime = "08:00:00";
        generalPaydHolidayUse.setUseDate(LocalDateTime.parse(LocalDateTime.parse(generalPaydHolidayUseDate,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        generalPaydHolidayUse.setTime(Time.valueOf(generalPaydHolidayUseTime));
        paydHolidayUses.add(generalPaydHolidayUse);

        when(paydHolidayUseService.findByAccountId(any(Account.class))).thenReturn(paydHolidayUses);
        when(localDateTimeToString.localDateTimeToString(generalPaydHoliday.getGrant())).thenReturn(generalPaydHolidayTime);
        when(localDateTimeToString.localDateTimeToString(generalPaydHolidayUse.getUseDate())).thenReturn(generalPaydHolidayUseTime);

        doReturn(paydHolidays).when(paydHolidayService).findByAccountId(any(Account.class));

        List<PaydHolidayHistoryListResponse> paydHolidayHistoryListResponses = paydHolidayService.returnPaydHolidayHistoryListResponses(generalAccount);
        assertEquals(2, paydHolidayHistoryListResponses.size());
    }
}
