package com.example.springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.model.Account;
import com.example.springboot.model.ShiftChangeRequest;
import com.example.springboot.repository.ShiftChangeRequestRepository;

@Service
public class ShiftChangeRequestService
{
    @Autowired
    private ShiftChangeRequestRepository shiftChangeRequestRepository;

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
}