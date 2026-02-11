package com.example.springboot.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.dto.change.LocalDateTimeToString;
import com.example.springboot.dto.response.PaydHolidayHistoryListResponse;
import com.example.springboot.model.Account;
import com.example.springboot.model.PaydHoliday;
import com.example.springboot.model.PaydHolidayUse;
import com.example.springboot.repository.PaydHolidayRepository;

@Service
public class PaydHolidayService
{
    private final PaydHolidayRepository paydHolidayRepository;

    @Autowired
    private final PaydHolidayUseService paydHolidayUseService;

    @Autowired
    private final LocalDateTimeToString localDateTimeToString;

    public PaydHolidayService
    (
        PaydHolidayRepository paydHolidayRepository,
        PaydHolidayUseService paydHolidayUseService,
        LocalDateTimeToString localDateTimeToString
    )
    {
        this.paydHolidayRepository = paydHolidayRepository;
        this.paydHolidayUseService = paydHolidayUseService;
        this.localDateTimeToString = localDateTimeToString;
    }

    public PaydHoliday findByPaydHolidayId(Long id)
    {
        return paydHolidayRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("有給が見つかりません"));
    }

    public List<PaydHoliday> findByAccountId(Long accountId)
    {
        Account account = new Account();
        account.setId(accountId);
        List<PaydHoliday> paydHolidays = paydHolidayRepository.findByAccountId(account);
        return paydHolidays;
    }

    public List<PaydHoliday> findByAccountId(Account account)
    {
        List<PaydHoliday> paydHolidays = paydHolidayRepository.findByAccountId(account);
        return paydHolidays;
    }

    public List<PaydHoliday> findByAccountIdAndLimitAfter(Long accountId)
    {
        Account account = new Account();
        account.setId(accountId);

        LocalDateTime nowTime = LocalDateTime.now();
        List<PaydHoliday> paydHolidays = paydHolidayRepository.findByAccountIdAndLimitAfter(account, nowTime);
        return paydHolidays;
    }

    public List<PaydHoliday> findByAccountIdAndLimitAfter(Account account)
    {
        LocalDateTime nowTime = LocalDateTime.now();
        List<PaydHoliday> paydHolidays = paydHolidayRepository.findByAccountIdAndLimitAfter(account, nowTime);
        return paydHolidays;
    }

    public List<PaydHolidayHistoryListResponse> returnPaydHolidayHistoryListResponses(Account account)
    {
        List<PaydHoliday> paydHolidays = findByAccountId(account);
        // 消化の取得
        List<PaydHolidayUse> paydHolidayUses = paydHolidayUseService.findByAccountId(account);
        List<PaydHolidayHistoryListResponse> paydHolidayHistoryListResponses = new ArrayList<PaydHolidayHistoryListResponse>();
        for(PaydHoliday paydHoliday : paydHolidays)
        {
            PaydHolidayHistoryListResponse paydHolidayHistoryListResponse = new PaydHolidayHistoryListResponse();
            paydHolidayHistoryListResponse.setType("付与");
            paydHolidayHistoryListResponse.setDate(localDateTimeToString.localDateTimeToString(paydHoliday.getGrant()));
            paydHolidayHistoryListResponse.setTime(paydHoliday.getTime());
            paydHolidayHistoryListResponses.add(paydHolidayHistoryListResponse);
        }
        for(PaydHolidayUse paydHolidayUse : paydHolidayUses)
        {
            PaydHolidayHistoryListResponse paydHolidayHistoryListResponseUse = new PaydHolidayHistoryListResponse();
            paydHolidayHistoryListResponseUse.setType("消化");
            paydHolidayHistoryListResponseUse.setDate(localDateTimeToString.localDateTimeToString(paydHolidayUse.getUseDate()));
            paydHolidayHistoryListResponseUse.setTime(paydHolidayUse.getTime().toString());
            paydHolidayHistoryListResponses.add(paydHolidayHistoryListResponseUse);
        }
        return paydHolidayHistoryListResponses;
    }

    public PaydHoliday save(PaydHoliday paydHoliday)
    {
        return paydHolidayRepository.save(paydHoliday);
    }
}
