package com.example.springboot.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.model.Account;
import com.example.springboot.model.Shift;
import com.example.springboot.model.StampRequest;
import com.example.springboot.repository.StampRequestRepository;

@Service
public class StampRequestService
{
    @Autowired
    private StampRequestRepository stampRequestRepository;

    public StampRequest findByAccountIdAndStampId(Account account, Long id)
    {
        return stampRequestRepository.findByAccountIdAndStampId(account, id)
            .orElseThrow(() -> new RuntimeException("打刻漏れ申請がありません"));
    }

    public StampRequest findByAccountIdAndStampId(Long accountId, Long id)
    {
        Account account = new Account();
        account.setId(accountId);
        return stampRequestRepository.findByAccountIdAndStampId(account, id)
            .orElseThrow(() -> new RuntimeException("打刻漏れ申請がありません"));
    }

    public List<StampRequest> findByShiftIdAndRequestStatusWait(Shift shift)
    {
        int status = 1;
        List<StampRequest> stampRequests = stampRequestRepository.findByShiftIdAndRequestStatus(shift, status);
        return stampRequests;
    }
    public List<StampRequest> findByShiftIdAndRequestStatusWait(Long shiftId)
    {
        int status = 1;
        Shift shift = new Shift();
        shift.setShiftId(shiftId);
        List<StampRequest> stampRequests = stampRequestRepository.findByShiftIdAndRequestStatus(shift, status);
        return stampRequests;
    }

    public List<StampRequest> findByAccountId(Long accountId)
    {
        Account account = new Account();
        account.setId(accountId);
        return stampRequestRepository.findByAccountId(account);
    }

    public List<StampRequest> findByAccountIdIn(List<Account> accounts)
    {
        List<StampRequest> stampRequests = stampRequestRepository.findByAccountIdIn(accounts);
        return stampRequests;
    }

    public List<StampRequest> findByAccountId(Account account)
    {
        return stampRequestRepository.findByAccountId(account);
    }

    public List<StampRequest> findByAccountIdAndBeginWorkBetweenAndRequestStatusWait(Long accountId, int year, int month)
    {
        Account account = new Account();
        account.setId(accountId);
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();
        LocalDateTime startPeriod = firstDay.atStartOfDay();
        LocalDateTime endPeriod = lastDay.atTime(23,59,59);
        int wait = 1;
        List<StampRequest> stampRequests = stampRequestRepository.findByAccountIdAndBeginWorkBetweenAndRequestStatus(account, startPeriod, endPeriod, wait);
        return stampRequests;
    }

    public List<StampRequest> findByAccountIdAndBeginWorkBetweenAndRequestStatusWait(Account account, int year, int month)
    {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();
        LocalDateTime startPeriod = firstDay.atStartOfDay();
        LocalDateTime endPeriod = lastDay.atTime(23,59,59);
        int wait = 1;
        List<StampRequest> stampRequests = stampRequestRepository.findByAccountIdAndBeginWorkBetweenAndRequestStatus(account, startPeriod, endPeriod, wait);
        return stampRequests;
    }

    public String save(StampRequest stampRequest)
    {
        stampRequestRepository.save(stampRequest);
        return "ok";
    }

    @Transactional
    public void resetAllTables()
    {
        stampRequestRepository.deleteAll();
        stampRequestRepository.resetAutoIncrement();
    }

    public void init()
    {
        
    }
}
