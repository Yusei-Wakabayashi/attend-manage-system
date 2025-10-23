package com.example.springboot.service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.model.Account;
import com.example.springboot.model.ShiftRequest;
import com.example.springboot.repository.ShiftRequestRepository;

@Service
public class ShiftRequestService
{
    @Autowired
    private ShiftRequestRepository shiftRequestRepository;

    public List<ShiftRequest> getAllShiftRequest()
    {
        return shiftRequestRepository.findAll();
    }

    public ShiftRequest findById(Long id)
    {
        return shiftRequestRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("シフト申請が見つかりません"));
    }

    public List<ShiftRequest> getAccountIdAndBeginWorkBetween(Account accountId, LocalDateTime beginWork, LocalDateTime endWork)
    {
        return shiftRequestRepository.findByAccountIdAndBeginWorkBetween(accountId, beginWork, endWork);
    }

    public List<ShiftRequest> getAccountIdAndBeginWorkBetweenDay(Account accountId, LocalDateTime begin)
    {
        // 始業時間から1日の開始と終了を作成
        LocalDateTime beginWork = LocalDateTime.of(begin.toLocalDate(), LocalTime.MIN);
        LocalDateTime endWork = LocalDateTime.of(begin.toLocalDate(), LocalTime.MAX);
        return shiftRequestRepository.findByAccountIdAndBeginWorkBetween(accountId, beginWork, endWork);
    }

    public List<ShiftRequest> getAccountIdAndBeginWorkBetweenAndRequestStatusWaitWeek(Long id, LocalDateTime begin)
    {
        Account account = new Account();
        account.setId(id);
        LocalDateTime beginWork = begin.toLocalDate().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).atStartOfDay();
        LocalDateTime endWork = begin.toLocalDate().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).atTime(LocalTime.MAX);
        // 承認済みステータスが存在してほしくない
        int requestStatus = 2;
        List<ShiftRequest> shiftRequests = shiftRequestRepository.findByAccountIdAndBeginWorkBetweenAndRequestStatus(account, beginWork, endWork, requestStatus);
        return shiftRequests;
    }

    public List<ShiftRequest> getAccountIdAndBeginWorkBetweenAndRequestStatusWaitWeek(Account account, LocalDateTime begin)
    {
        LocalDateTime beginWork = begin.toLocalDate().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).atStartOfDay();
        LocalDateTime endWork = begin.toLocalDate().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).atTime(LocalTime.MAX);
        // 承認済みステータスが存在してほしくない
        int requestStatus = 2;
        List<ShiftRequest> shiftRequests = shiftRequestRepository.findByAccountIdAndBeginWorkBetweenAndRequestStatus(account, beginWork, endWork, requestStatus);
        return shiftRequests;
    }

    public ShiftRequest findByAccountIdAndShiftRequestId(Account account, Long id)
    {
        return shiftRequestRepository.findByAccountIdAndShiftRequestId(account, id)
            .orElseThrow(() -> new RuntimeException("シフト申請が見つかりません"));
    }

    public ShiftRequest findByAccountIdAndShiftRequestId(Long accountId, Long id)
    {
        Account account = new Account();
        account.setId(accountId);
        return shiftRequestRepository.findByAccountIdAndShiftRequestId(account, id)
            .orElseThrow(() -> new RuntimeException("シフト申請が見つかりません"));
    }

    public List<ShiftRequest> findByAccountId(Long accountId)
    {
        Account account = new Account();
        account.setId(accountId);
        return shiftRequestRepository.findByAccountId(account);
    }

    public List<ShiftRequest> findByAccountId(Account account)
    {
        return shiftRequestRepository.findByAccountId(account);
    }    

    public List<ShiftRequest> findByAccountIdIn(List<Account> accounts)
    {
        List<ShiftRequest> shiftRequests = shiftRequestRepository.findByAccountIdIn(accounts);
        return shiftRequests;
    }

    public String save(ShiftRequest shiftRequest)
    {
        shiftRequestRepository.save(shiftRequest);
        return "ok";
    }

    @Transactional
    public void resetAllTables()
    {
        shiftRequestRepository.deleteAll();
        shiftRequestRepository.resetAutoIncrement();
    }
}