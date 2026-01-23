package com.example.springboot.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.dto.change.LocalDateTimeToString;
import com.example.springboot.dto.response.RequestDetailOverTimeResponse;
import com.example.springboot.model.Account;
import com.example.springboot.model.OverTimeRequest;
import com.example.springboot.model.Shift;
import com.example.springboot.repository.OverTimeRequestRepository;

@Service
public class OverTimeRequestService
{
    private final OverTimeRequestRepository overTimeRequestRepository;
    private final LocalDateTimeToString localDateTimeToString;

    @Autowired
    public OverTimeRequestService
    (
        OverTimeRequestRepository overTimeRequestRepository,
        LocalDateTimeToString localDateTimeToString
    )
    {
        this.overTimeRequestRepository = overTimeRequestRepository;
        this.localDateTimeToString = localDateTimeToString;
    }

    public OverTimeRequest findById(Long requestId)
    {
        return overTimeRequestRepository.findById(requestId)
            .orElseThrow(() -> new RuntimeException("残業申請が見つかりません"));
    }
    public OverTimeRequest findByAccountIdAndOverTimeRequestId(Long accountId, Long id)
    {
        Account account = new Account();
        account.setId(accountId);
        return overTimeRequestRepository.findByAccountIdAndOverTimeId(account, id);
    }

    public OverTimeRequest findByAccountIdAndOverTimeRequestId(Account account, Long id)
    {
        return overTimeRequestRepository.findByAccountIdAndOverTimeId(account, id);
    }

    public List<OverTimeRequest> findByAccountIdAndShiftIdAndRequestStatusWait(Long accountId, Long shiftId)
    {
        Account account = new Account();
        account.setId(accountId);
        Shift shift = new Shift();
        shift.setShiftId(shiftId);
        int requestStatus = 1;
        List<OverTimeRequest> overTimeRequests = overTimeRequestRepository.findByAccountIdAndShiftIdAndRequestStatus(account, shift, requestStatus);
        return overTimeRequests;
    }

    public List<OverTimeRequest> findByAccountIdAndShiftIdAndRequestStatusWait(Account account, Long shiftId)
    {
        Shift shift = new Shift();
        shift.setShiftId(shiftId);
        int requestStatus = 1;
        List<OverTimeRequest> overTimeRequests = overTimeRequestRepository.findByAccountIdAndShiftIdAndRequestStatus(account, shift, requestStatus);
        return overTimeRequests;
    }

    public List<OverTimeRequest> findByAccountIdAndShiftIdAndRequestStatusWait(Long accountId, Shift shift)
    {
        Account account = new Account();
        account.setId(accountId);
        int requestStatus = 1;
        List<OverTimeRequest> overTimeRequests = overTimeRequestRepository.findByAccountIdAndShiftIdAndRequestStatus(account, shift, requestStatus);
        return overTimeRequests;
    }

    public List<OverTimeRequest> findByAccountIdAndShiftIdAndRequestStatusWait(Account account, Shift shift)
    {
        int requestStatus = 1;
        List<OverTimeRequest> overTimeRequests = overTimeRequestRepository.findByAccountIdAndShiftIdAndRequestStatus(account, shift, requestStatus);
        return overTimeRequests;
    }

    public List<OverTimeRequest> findByAccountId(Long accountId)
    {
        Account account = new Account();
        account.setId(accountId);
        return overTimeRequestRepository.findByAccountId(account);
    }

    public List<OverTimeRequest> findByAccountId(Account account)
    {
        return overTimeRequestRepository.findByAccountId(account);
    }

    public List<OverTimeRequest> findByAccountIdIn(List<Account> accounts)
    {
        List<OverTimeRequest> overTimeRequests = overTimeRequestRepository.findByAccountIdIn(accounts);
        return overTimeRequests;
    }

    public List<OverTimeRequest> findByAccounIdAndRequestStatusWaitOrApprovedAndBeginWorkOrEndWorkBetween(Long accountId, LocalDateTime startPeriod, LocalDateTime endPeriod)
    {
        Account account = new Account();
        account.setId(accountId);
        int wait = 1;
        int approved = 2;
        // 検索する際申請のリクエストが19時開始20時終了で残業申請が18時開始19時終了の際18時を含みたくないため1秒ずらしている
        startPeriod.plusSeconds(1);
        endPeriod.minusSeconds(1);
        List<OverTimeRequest> waitOverTimeRequestBeginList = overTimeRequestRepository.findByAccountIdAndRequestStatusAndBeginWorkBetween(account, wait, startPeriod, endPeriod);
        List<OverTimeRequest> waitOverTimeRequestEndList = overTimeRequestRepository.findByAccountIdAndRequestStatusAndEndWorkBetween(account, wait, startPeriod, endPeriod);
        List<OverTimeRequest> approvedOverTimeRequestBeginList = overTimeRequestRepository.findByAccountIdAndRequestStatusAndBeginWorkBetween(account, approved, startPeriod, endPeriod);
        List<OverTimeRequest> approvedOverTimeRequestEndList = overTimeRequestRepository.findByAccountIdAndRequestStatusAndEndWorkBetween(account, approved, startPeriod, endPeriod);
        List<OverTimeRequest> waitOverTimeRequestList = Stream.concat(waitOverTimeRequestBeginList.stream(), waitOverTimeRequestEndList.stream()).distinct().collect(Collectors.toList());
        List<OverTimeRequest> approvedOverTimeRequestList = Stream.concat(approvedOverTimeRequestBeginList.stream(), approvedOverTimeRequestEndList.stream()).distinct().collect(Collectors.toList());
        List<OverTimeRequest> overTimeRequestList = Stream.concat(waitOverTimeRequestList.stream(), approvedOverTimeRequestList.stream()).distinct().collect(Collectors.toList());
        return overTimeRequestList;
    }

    public List<OverTimeRequest> findByAccounIdAndRequestStatusWaitOrApprovedAndBeginWorkOrEndWorkBetween(Account account, LocalDateTime startPeriod, LocalDateTime endPeriod)
    {
        int wait = 1;
        int approved = 2;
        // 検索する際申請のリクエストが19時開始20時終了で残業申請が18時開始19時終了の際18時を含みたくないため1秒ずらしている
        startPeriod.plusSeconds(1);
        endPeriod.minusSeconds(1);
        List<OverTimeRequest> waitOverTimeRequestBeginList = overTimeRequestRepository.findByAccountIdAndRequestStatusAndBeginWorkBetween(account, wait, startPeriod, endPeriod);
        List<OverTimeRequest> waitOverTimeRequestEndList = overTimeRequestRepository.findByAccountIdAndRequestStatusAndEndWorkBetween(account, wait, startPeriod, endPeriod);
        List<OverTimeRequest> approvedOverTimeRequestBeginList = overTimeRequestRepository.findByAccountIdAndRequestStatusAndBeginWorkBetween(account, approved, startPeriod, endPeriod);
        List<OverTimeRequest> approvedOverTimeRequestEndList = overTimeRequestRepository.findByAccountIdAndRequestStatusAndEndWorkBetween(account, approved, startPeriod, endPeriod);
        List<OverTimeRequest> waitOverTimeRequestList = Stream.concat(waitOverTimeRequestBeginList.stream(), waitOverTimeRequestEndList.stream()).distinct().collect(Collectors.toList());
        List<OverTimeRequest> approvedOverTimeRequestList = Stream.concat(approvedOverTimeRequestBeginList.stream(), approvedOverTimeRequestEndList.stream()).distinct().collect(Collectors.toList());
        List<OverTimeRequest> overTimeRequestList = Stream.concat(waitOverTimeRequestList.stream(), approvedOverTimeRequestList.stream()).distinct().collect(Collectors.toList());
        return overTimeRequestList;
    }

    public List<OverTimeRequest> findByAccountIdAndRequestStatusWaitOrApprovedAndBeginWorkBetweenMonth(Long accountId, int year, int month)
    {
        Account account = new Account();
        account.setId(accountId);
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();
        LocalDateTime startPeriod = firstDay.atStartOfDay();
        LocalDateTime endPeriod = lastDay.atTime(23,59,59);
        int wait = 1;
        int approved = 2;
        List<OverTimeRequest> overTimeRequestWait = overTimeRequestRepository.findByAccountIdAndRequestStatusAndBeginWorkBetween(account, wait, startPeriod, endPeriod);
        List<OverTimeRequest> overTimeRequestApproved = overTimeRequestRepository.findByAccountIdAndRequestStatusAndBeginWorkBetween(account, approved, startPeriod, endPeriod);
        List<OverTimeRequest> overTimeRequests = Stream.concat(overTimeRequestWait.stream(), overTimeRequestApproved.stream()).distinct().collect(Collectors.toList());
        return overTimeRequests;
    }

    public List<OverTimeRequest> findByAccountIdAndRequestStatusWaitOrApprovedAndBeginWorkBetweenMonth(Account account, int year, int month)
    {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();
        LocalDateTime startPeriod = firstDay.atStartOfDay();
        LocalDateTime endPeriod = lastDay.atTime(23,59,59);
        int wait = 1;
        int approved = 2;
        List<OverTimeRequest> overTimeRequestWait = overTimeRequestRepository.findByAccountIdAndRequestStatusAndBeginWorkBetween(account, wait, startPeriod, endPeriod);
        List<OverTimeRequest> overTimeRequestApproved = overTimeRequestRepository.findByAccountIdAndRequestStatusAndBeginWorkBetween(account, approved, startPeriod, endPeriod);
        List<OverTimeRequest> overTimeRequests = Stream.concat(overTimeRequestWait.stream(), overTimeRequestApproved.stream()).distinct().collect(Collectors.toList());
        return overTimeRequests;
    }

    public List<OverTimeRequest> findByAccountIdAndBeginWorkAndRequstStatusWait(Long accountId, int year, int month)
    {
        Account account = new Account();
        account.setId(accountId);
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();
        LocalDateTime startPeriod = firstDay.atStartOfDay();
        LocalDateTime endPeriod = lastDay.atTime(23,59,59);
        int wait = 1;
        List<OverTimeRequest> overTimeRequests = overTimeRequestRepository.findByAccountIdAndRequestStatusAndBeginWorkBetween(account, wait, startPeriod, endPeriod);
        return overTimeRequests;
    }

    public List<OverTimeRequest> findByAccountIdAndBeginWorkAndRequstStatusWait(Account account, int year, int month)
    {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();
        LocalDateTime startPeriod = firstDay.atStartOfDay();
        LocalDateTime endPeriod = lastDay.atTime(23,59,59);
        int wait = 1;
        List<OverTimeRequest> overTimeRequests = overTimeRequestRepository.findByAccountIdAndRequestStatusAndBeginWorkBetween(account, wait, startPeriod, endPeriod);
        return overTimeRequests;
    }

    public RequestDetailOverTimeResponse mapToDetailResponse(OverTimeRequest overTimeRequest)
    {
        RequestDetailOverTimeResponse requestDetailOverTimeResponse = new RequestDetailOverTimeResponse
        (
            1,
            overTimeRequest.getShiftId().getShiftId().intValue(),
            localDateTimeToString.localDateTimeToString(overTimeRequest.getBeginWork()),
            localDateTimeToString.localDateTimeToString(overTimeRequest.getEndWork()),
            overTimeRequest.getRequestComment(),
            localDateTimeToString.localDateTimeToString(overTimeRequest.getRequestDate()),
            overTimeRequest.getRequestStatus(),
            Objects.isNull(overTimeRequest.getApprover()) ? null : overTimeRequest.getApprover().getId().intValue(),
            Objects.isNull(overTimeRequest.getApprover()) ? "" : overTimeRequest.getApprover().getName(),
            overTimeRequest.getApproverComment(),
            Objects.isNull(overTimeRequest.getApprovalTime()) ? "" : localDateTimeToString.localDateTimeToString(overTimeRequest.getApprovalTime())
        );
        return requestDetailOverTimeResponse;
    }

    public OverTimeRequest save(OverTimeRequest overTimeRequest)
    {
        return overTimeRequestRepository.save(overTimeRequest);
    }

    @Transactional
    public void resetAllTables()
    {
        overTimeRequestRepository.deleteAll();;
        overTimeRequestRepository.resetAutoIncrement();
    }
}