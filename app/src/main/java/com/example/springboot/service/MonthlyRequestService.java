package com.example.springboot.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.dto.change.LocalDateTimeToString;
import com.example.springboot.dto.response.RequestDetailMonthlyResponse;
import com.example.springboot.model.Account;
import com.example.springboot.model.MonthlyRequest;
import com.example.springboot.repository.MonthlyRequestRepository;

@Service
public class MonthlyRequestService
{
    private final MonthlyRequestRepository monthlyRequestRepository;
    private final LocalDateTimeToString localDateTimeToString;

    @Autowired
    public MonthlyRequestService
    (
        MonthlyRequestRepository monthlyRequestRepository,
        LocalDateTimeToString localDateTimeToString
    )
    {
        this.monthlyRequestRepository = monthlyRequestRepository;
        this.localDateTimeToString = localDateTimeToString;
    }

    public MonthlyRequest findById(Long id)
    {
        return monthlyRequestRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("月次申請が見つかりません"));
    }

    public MonthlyRequest findByAccountIdAndMothlyRequestId(Long accountId, Long id)
    {
        Account account = new Account();
        account.setId(accountId);
        return monthlyRequestRepository.findByAccountIdAndMonthRequestId(account, id);
    }

    public MonthlyRequest findByAccountIdAndMothlyRequestId(Account account, Long id)
    {
        return monthlyRequestRepository.findByAccountIdAndMonthRequestId(account, id);
    }

    public List<MonthlyRequest> findByAccountId(Long accountId)
    {
        Account account = new Account();
        account.setId(accountId);
        return monthlyRequestRepository.findByAccountId(account);
    }

    public List<MonthlyRequest> findByAccountIdAndYearAndMonth(Long accountId, int year, int month)
    {
        Account account = new Account();
        account.setId(accountId);
        List<MonthlyRequest> monthlyRequests = monthlyRequestRepository.findByAccountIdAndYearAndMonth(account, year, month);
        return monthlyRequests;
    }

    public List<MonthlyRequest> findByAccountIdAndYearAndMonth(Account account, int year, int month)
    {
        List<MonthlyRequest> monthlyRequests = monthlyRequestRepository.findByAccountIdAndYearAndMonth(account, year, month);
        return monthlyRequests;
    }

    public List<MonthlyRequest> findByAccountId(Account account)
    {
        return monthlyRequestRepository.findByAccountId(account);
    }

    public List<MonthlyRequest> findByAccountIdIn(List<Account> accounts)
    {
        List<MonthlyRequest> monthlyRequests = monthlyRequestRepository.findByAccountIdIn(accounts);
        return monthlyRequests;
    }

    public List<MonthlyRequest> findAll()
    {
        return monthlyRequestRepository.findAll();
    }

    public RequestDetailMonthlyResponse mapToDetailResponse(MonthlyRequest monthlyRequest)
    {
        RequestDetailMonthlyResponse requestDetailMonthlyResponse = new RequestDetailMonthlyResponse
        (
            1,
            monthlyRequest.getWorkTime(),
            monthlyRequest.getOverTime(),
            monthlyRequest.getEarlyTime(),
            monthlyRequest.getLeavingTime(),
            monthlyRequest.getOutingTime(),
            monthlyRequest.getAbsenceTime(),
            monthlyRequest.getPaydHolidayTime(),
            monthlyRequest.getSpecialTime(),
            monthlyRequest.getHolidayWorkTime(),
            monthlyRequest.getLateNightWorkTime(),
            monthlyRequest.getYear(),
            monthlyRequest.getMonth(),
            monthlyRequest.getRequestComment(),
            localDateTimeToString.localDateTimeToString(monthlyRequest.getRequestDate()),
            monthlyRequest.getRequestStatus(),
            Objects.isNull(monthlyRequest.getApprover()) ? null : monthlyRequest.getApprover().getId().intValue(),
            Objects.isNull(monthlyRequest.getApprover()) ? "" : monthlyRequest.getApprover().getName(),
            monthlyRequest.getApproverComment(),
            Objects.isNull(monthlyRequest.getApprovalDate()) ? "" : localDateTimeToString.localDateTimeToString(monthlyRequest.getApprovalDate())
        );
        return requestDetailMonthlyResponse;
    }

    public MonthlyRequest save(MonthlyRequest monthlyRequest)
    {
        return monthlyRequestRepository.save(monthlyRequest);
    }
}
