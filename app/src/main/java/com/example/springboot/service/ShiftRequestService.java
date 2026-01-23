package com.example.springboot.service;

import java.sql.Time;
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
import com.example.springboot.dto.response.RequestDetailShiftResponse;
import com.example.springboot.model.Account;
import com.example.springboot.model.Shift;
import com.example.springboot.model.ShiftRequest;
import com.example.springboot.repository.ShiftRequestRepository;

@Service
public class ShiftRequestService
{
    // 再代入不可で定義
    private final ShiftRequestRepository shiftRequestRepository;
    private final LocalDateTimeToString localDateTimeToString;
    // コンストラクタ定義(依存性注入)
    @Autowired // 念のため記載
    public ShiftRequestService
    (
        ShiftRequestRepository shiftRequestRepository,
        LocalDateTimeToString localDateTimeToString
    )
    {
        this.shiftRequestRepository = shiftRequestRepository;
        this.localDateTimeToString = localDateTimeToString;
    }

    public List<ShiftRequest> findAllShiftRequest()
    {
        return shiftRequestRepository.findAll();
    }

    public ShiftRequest findById(Long id)
    {
        return shiftRequestRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("シフト申請が見つかりません"));
    }

    public List<ShiftRequest> findAccountIdAndBeginWorkBetween(Account accountId, LocalDateTime beginWork, LocalDateTime endWork)
    {
        return shiftRequestRepository.findByAccountIdAndBeginWorkBetween(accountId, beginWork, endWork);
    }

    public List<ShiftRequest> findAccountIdAndBeginWorkBetweenDay(Account accountId, LocalDateTime begin)
    {
        // 始業時間から1日の開始と終了を作成
        LocalDateTime beginWork = LocalDateTime.of(begin.toLocalDate(), LocalTime.MIN);
        LocalDateTime endWork = LocalDateTime.of(begin.toLocalDate(), LocalTime.MAX);
        return shiftRequestRepository.findByAccountIdAndBeginWorkBetween(accountId, beginWork, endWork);
    }

    public List<ShiftRequest> findAccountIdAndBeginWorkBetweenAndRequestStatusWaitWeek(Long id, LocalDateTime begin)
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

    public List<ShiftRequest> findAccountIdAndBeginWorkBetweenAndRequestStatusWaitWeek(Account account, LocalDateTime begin)
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
        return shiftRequestRepository.findByAccountIdAndShiftRequestId(account, id);
    }

    public ShiftRequest findByAccountIdAndShiftRequestId(Long accountId, Long id)
    {
        Account account = new Account();
        account.setId(accountId);
        return shiftRequestRepository.findByAccountIdAndShiftRequestId(account, id);
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

    public List<ShiftRequest> findByAccountIdAndBeginWorkBetweenAndRequestStatusWait(Long accountId, int year, int month)
    {
        Account account = new Account();
        account.setId(accountId);
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();
        LocalDateTime startPeriod = firstDay.atStartOfDay();
        LocalDateTime endPeriod = lastDay.atTime(23,59,59);
        int wait = 1;
        List<ShiftRequest> shiftRequests = shiftRequestRepository.findByAccountIdAndBeginWorkBetweenAndRequestStatus(account, startPeriod, endPeriod, wait);
        return shiftRequests;
    }

    public List<ShiftRequest> findByAccountIdAndBeginWorkBetweenAndRequestStatusWait(Account account, int year, int month)
    {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();
        LocalDateTime startPeriod = firstDay.atStartOfDay();
        LocalDateTime endPeriod = lastDay.atTime(23,59,59);
        int wait = 1;
        List<ShiftRequest> shiftRequests = shiftRequestRepository.findByAccountIdAndBeginWorkBetweenAndRequestStatus(account, startPeriod, endPeriod, wait);
        return shiftRequests;
    }

    // modelに関するものは入力値で考える
    public Shift shiftRequestToShift(ShiftRequest shiftRequest)
    {
        Shift shift = new Shift();
        shift.setAccountId(shiftRequest.getAccountId());
        shift.setBeginWork(shiftRequest.getBeginWork());
        shift.setEndWork(shiftRequest.getEndWork());
        shift.setBeginBreak(shiftRequest.getBeginBreak());
        shift.setEndBreak(shiftRequest.getEndBreak());
        shift.setLateness(Time.valueOf("00:00:00"));
        shift.setLeaveEarly(Time.valueOf("00:00:00"));
        shift.setOuting(Time.valueOf("00:00:00"));
        shift.setOverWork(Time.valueOf("00:00:00"));
        return shift;
    }

    public RequestDetailShiftResponse mapToDetailResponse(ShiftRequest shiftRequest) 
    {
        RequestDetailShiftResponse requestDetailShiftResponse =
        new RequestDetailShiftResponse
        (
            1,
            localDateTimeToString.localDateTimeToString(shiftRequest.getBeginWork()),
            localDateTimeToString.localDateTimeToString(shiftRequest.getEndWork()),
            localDateTimeToString.localDateTimeToString(shiftRequest.getBeginBreak()),
            localDateTimeToString.localDateTimeToString(shiftRequest.getEndBreak()),
            shiftRequest.isVacationWork(),
            shiftRequest.getRequestComment(),
            localDateTimeToString.localDateTimeToString(shiftRequest.getRequestDate()),
            shiftRequest.getRequestStatus(),
            shiftRequest.getApprover().getId().intValue(),
            shiftRequest.getApprover().getName(),
            shiftRequest.getApproverComment(),
            shiftRequest.getApprovalTime() == null ? "" : localDateTimeToString.localDateTimeToString(shiftRequest.getApprovalTime())
        );
        return requestDetailShiftResponse;
    }

    public ShiftRequest save(ShiftRequest shiftRequest)
    {
        return shiftRequestRepository.save(shiftRequest);
    }

    @Transactional
    public void resetAllTables()
    {
        shiftRequestRepository.deleteAll();
        shiftRequestRepository.resetAutoIncrement();
    }
}