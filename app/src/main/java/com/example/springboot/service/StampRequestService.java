package com.example.springboot.service;

import java.util.List;

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

    public List<StampRequest> findByAccountId(Account account)
    {
        return stampRequestRepository.findByAccountId(account);
    }

    public String save(StampRequest stampRequest)
    {
        stampRequestRepository.save(stampRequest);
        return "ok";
    }
}
