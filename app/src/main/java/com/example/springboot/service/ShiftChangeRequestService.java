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
import com.example.springboot.model.Shift;
import com.example.springboot.model.ShiftChangeRequest;
import com.example.springboot.repository.ShiftChangeRequestRepository;

@Service
public class ShiftChangeRequestService
{
    @Autowired
    private ShiftChangeRequestRepository shiftChangeRequestRepository;

    public List<ShiftChangeRequest> getAllShiftChangeRequest()
    {
        return shiftChangeRequestRepository.findAll();
    }

    public ShiftChangeRequest findById(Long id)
    {
        return shiftChangeRequestRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("シフト時間変更申請が見つかりません"));
    }
    public ShiftChangeRequest findByAccountIdAndShiftChangeRequestId(Account account, Long id)
    {
        return shiftChangeRequestRepository.findByAccountIdAndShiftChangeId(account, id)
            .orElseThrow(() -> new RuntimeException("シフト時間変更申請が見つかりません"));
    }
    public ShiftChangeRequest findByAccountIdAndShiftChangeRequestId(Long accountId, Long id)
    {
        Account account = new Account();
        account.setId(accountId);
        return shiftChangeRequestRepository.findByAccountIdAndShiftChangeId(account, id)
            .orElseThrow(() -> new RuntimeException("シフト時間変更申請が見つかりません"));
    }

    public List<ShiftChangeRequest> findByAccountIdAndShiftIdAndRequestStatusWait(Account account, Long id)
    {
        int requestStatus = 1;
        Shift shift = new Shift();
        shift.setShiftId(id);
        List<ShiftChangeRequest> shiftChangeRequests = shiftChangeRequestRepository.findByAccountIdAndShiftIdAndRequestStatus(account, shift, requestStatus);
        return shiftChangeRequests;
    }

    public List<ShiftChangeRequest> findByAccountIdAndShiftIdAndRequestStatusWait(Long accountId, Long id)
    {
        Account account = new Account();
        account.setId(accountId);
        int requestStatus = 1;
        Shift shift = new Shift();
        shift.setShiftId(id);
        List<ShiftChangeRequest> shiftChangeRequests = shiftChangeRequestRepository.findByAccountIdAndShiftIdAndRequestStatus(account, shift, requestStatus);
        return shiftChangeRequests;
    }

    public List<ShiftChangeRequest> getAccountIdAndBeginWorkBetweenAndRequestStatusWaitWeek(Account account, LocalDateTime begin)
    {
        LocalDateTime beginWork = begin.toLocalDate().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).atStartOfDay();
        LocalDateTime endWork = begin.toLocalDate().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).atTime(LocalTime.MAX);
        int requestStatus = 1;
        List<ShiftChangeRequest> shiftChangeRequests = shiftChangeRequestRepository.findByAccountIdAndBeginWorkBetweenAndRequestStatus(account, beginWork, endWork, requestStatus);
        return shiftChangeRequests;
    }

    public List<ShiftChangeRequest> getAccountIdAndBeginWorkBetweenAndRequestStatusWaitWeek(Long id, LocalDateTime begin)
    {
        Account account = new Account();
        account.setId(id);
        LocalDateTime beginWork = begin.toLocalDate().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).atStartOfDay();
        LocalDateTime endWork = begin.toLocalDate().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).atTime(LocalTime.MAX);
        int requestStatus = 1;
        List<ShiftChangeRequest> shiftChangeRequests = shiftChangeRequestRepository.findByAccountIdAndBeginWorkBetweenAndRequestStatus(account, beginWork, endWork, requestStatus);
        return shiftChangeRequests;
    }

    public List<ShiftChangeRequest> getAccountIdAndShiftIdInAndRequestStatusWait(Account account, List<Shift> shifts)
    {
        int status = 1;
        List<ShiftChangeRequest> shiftChangeRequests = shiftChangeRequestRepository.findByAccountIdAndShiftIdInAndRequestStatus(account, shifts, status);
        return shiftChangeRequests;
    }

    public List<ShiftChangeRequest> findByAccountId(Long accountId)
    {
        Account account = new Account();
        account.setId(accountId);
        return shiftChangeRequestRepository.findByAccountId(account);
    }

    public List<ShiftChangeRequest> findByAccountId(Account account)
    {
        return shiftChangeRequestRepository.findByAccountId(account);
    }

    public List<ShiftChangeRequest> findByAccountIdIn(List<Account> accounts)
    {
        List<ShiftChangeRequest> shiftChangeRequests = shiftChangeRequestRepository.findByAccountIdIn(accounts);
        return shiftChangeRequests;
    }

    public String save(ShiftChangeRequest shiftChangeRequest)
    {
        shiftChangeRequestRepository.save(shiftChangeRequest);
        return "ok";
    }

    @Transactional
    public void resetAllTables()
    {
        shiftChangeRequestRepository.deleteAll();
        shiftChangeRequestRepository.resetAutoIncrement();
    }

    public void init()
    {
        
    }
}