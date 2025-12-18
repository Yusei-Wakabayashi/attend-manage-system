package com.example.springboot.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.dto.change.LocalDateTimeToString;
import com.example.springboot.dto.response.RequestDetailShiftChangeResponse;
import com.example.springboot.model.Account;
import com.example.springboot.model.Shift;
import com.example.springboot.model.ShiftChangeRequest;
import com.example.springboot.repository.ShiftChangeRequestRepository;

@Service
public class ShiftChangeRequestService
{
    // 再代入不可で定義
    private final ShiftChangeRequestRepository shiftChangeRequestRepository;
    private final LocalDateTimeToString localDateTimeToString;

    // コンストラクタで依存性注入
    @Autowired
    public ShiftChangeRequestService
    (
        ShiftChangeRequestRepository shiftChangeRequestRepository,
        LocalDateTimeToString localDateTimeToString
    )
    {
        this.shiftChangeRequestRepository = shiftChangeRequestRepository;
        this.localDateTimeToString = localDateTimeToString;
    }

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
        return shiftChangeRequestRepository.findByAccountIdAndShiftChangeId(account, id);
    }
    public ShiftChangeRequest findByAccountIdAndShiftChangeRequestId(Long accountId, Long id)
    {
        Account account = new Account();
        account.setId(accountId);
        return shiftChangeRequestRepository.findByAccountIdAndShiftChangeId(account, id);
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

    public List<ShiftChangeRequest> findAccountIdAndBeginWorkBetweenAndRequestStatusWaitWeek(Account account, LocalDateTime begin)
    {
        LocalDateTime beginWork = begin.toLocalDate().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).atStartOfDay();
        LocalDateTime endWork = begin.toLocalDate().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).atTime(LocalTime.MAX);
        int requestStatus = 1;
        List<ShiftChangeRequest> shiftChangeRequests = shiftChangeRequestRepository.findByAccountIdAndBeginWorkBetweenAndRequestStatus(account, beginWork, endWork, requestStatus);
        return shiftChangeRequests;
    }

    public List<ShiftChangeRequest> findAccountIdAndBeginWorkBetweenAndRequestStatusWaitWeek(Long id, LocalDateTime begin)
    {
        Account account = new Account();
        account.setId(id);
        LocalDateTime beginWork = begin.toLocalDate().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).atStartOfDay();
        LocalDateTime endWork = begin.toLocalDate().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).atTime(LocalTime.MAX);
        int requestStatus = 1;
        List<ShiftChangeRequest> shiftChangeRequests = shiftChangeRequestRepository.findByAccountIdAndBeginWorkBetweenAndRequestStatus(account, beginWork, endWork, requestStatus);
        return shiftChangeRequests;
    }

    public List<ShiftChangeRequest> findAccountIdAndShiftIdInAndRequestStatusWait(Account account, List<Shift> shifts)
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

    public List<ShiftChangeRequest> findByAccountIdAndBeginWorkBetweenAndRequestStatusWait(Long accountId, int year, int month)
    {
        Account account = new Account();
        account.setId(accountId);
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();
        LocalDateTime startPeriod = firstDay.atStartOfDay();
        LocalDateTime endPeriod = lastDay.atTime(23,59,59);
        int wait = 1;
        List<ShiftChangeRequest> shiftChangeRequests = shiftChangeRequestRepository.findByAccountIdAndBeginWorkBetweenAndRequestStatus(account, startPeriod, endPeriod, wait);
        return shiftChangeRequests;
    }

    public List<ShiftChangeRequest> findByAccountIdAndBeginWorkBetweenAndRequestStatusWait(Account account, int year, int month)
    {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();
        LocalDateTime startPeriod = firstDay.atStartOfDay();
        LocalDateTime endPeriod = lastDay.atTime(23,59,59);
        int wait = 1;
        List<ShiftChangeRequest> shiftChangeRequests = shiftChangeRequestRepository.findByAccountIdAndBeginWorkBetweenAndRequestStatus(account, startPeriod, endPeriod, wait);
        return shiftChangeRequests;
    }

    public Shift shiftChangeRequestToShift(ShiftChangeRequest shiftChangeRequest)
    {
        Shift shift = shiftChangeRequest.getShiftId();
        shift.setBeginWork(shiftChangeRequest.getBeginWork());
        shift.setEndWork(shiftChangeRequest.getEndWork());
        shift.setBeginBreak(shiftChangeRequest.getBeginBreak());
        shift.setEndBreak(shiftChangeRequest.getEndBreak());
        return shift;
    } 

    public RequestDetailShiftChangeResponse mapToDetailResponse(ShiftChangeRequest shiftChangeRequest)
    {
        RequestDetailShiftChangeResponse requestDetailShiftChangeResponse =
        new RequestDetailShiftChangeResponse
        (
            1,
            shiftChangeRequest.getShiftId().getShiftId().intValue(),
            localDateTimeToString.localDateTimeToString(shiftChangeRequest.getBeginWork()),
            localDateTimeToString.localDateTimeToString(shiftChangeRequest.getEndWork()),
            localDateTimeToString.localDateTimeToString(shiftChangeRequest.getBeginBreak()),
            localDateTimeToString.localDateTimeToString(shiftChangeRequest.getEndBreak()),
            shiftChangeRequest.isVacationWork(),
            shiftChangeRequest.getRequestComment(),
            localDateTimeToString.localDateTimeToString(shiftChangeRequest.getRequestDate()),
            shiftChangeRequest.getRequestStatus(),
            shiftChangeRequest.getApprover().getId().intValue(),
            shiftChangeRequest.getApprover().getName(),
            shiftChangeRequest.getApproverComment(),
            shiftChangeRequest.getApprovalTime() == null ? "" : localDateTimeToString.localDateTimeToString(shiftChangeRequest.getApprovalTime())
        );
        return requestDetailShiftChangeResponse;
    }

    public ShiftChangeRequest save(ShiftChangeRequest shiftChangeRequest)
    {
        return shiftChangeRequestRepository.save(shiftChangeRequest);
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