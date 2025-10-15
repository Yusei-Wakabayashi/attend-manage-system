package com.example.springboot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.model.Account;
import com.example.springboot.model.OverTimeRequest;
import com.example.springboot.model.Shift;
import com.example.springboot.repository.OverTimeRequestRepository;


@Service
public class OverTimeRequestService
{
    @Autowired
    private OverTimeRequestRepository overTimeRequestRepository;
    
    public OverTimeRequest findByAccountIdAndOverTimeRequestId(Long accountId, Long id)
    {
        Account account = new Account();
        account.setId(accountId);
        return overTimeRequestRepository.findByAccountIdAndOverTimeId(account, id)
            .orElseThrow(() -> new RuntimeException("残業申請が見つかりません"));
    }

    public OverTimeRequest findByAccountIdAndOverTimeRequestId(Account account, Long id)
    {
        return overTimeRequestRepository.findByAccountIdAndOverTimeId(account, id)
            .orElseThrow(() -> new RuntimeException("残業申請が見つかりません"));
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

    public String save(OverTimeRequest overTimeRequest)
    {
        overTimeRequestRepository.save(overTimeRequest);
        return "ok";
    }
}