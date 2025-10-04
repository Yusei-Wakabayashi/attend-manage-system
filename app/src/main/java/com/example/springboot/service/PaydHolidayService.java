package com.example.springboot.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.model.Account;
import com.example.springboot.model.PaydHoliday;
import com.example.springboot.repository.PaydHolidayRepository;

@Service
public class PaydHolidayService
{
    @Autowired
    private PaydHolidayRepository paydHolidayRepository;

    public PaydHoliday findByPaydHolidayId(Long id)
    {
        return paydHolidayRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("有給が見つかりません"));
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
}
