package com.example.springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.model.Account;
import com.example.springboot.model.VacationRequest;
import com.example.springboot.repository.VacationRequestRepository;

@Service
public class VacationRequestService
{
    @Autowired
    private VacationRequestRepository vacationRequestRepository;

    public VacationRequest findByAccountIdAndVacationId(Account account, Long id)
    {
        return vacationRequestRepository.findByAccountIdAndVacationId(account, id)
            .orElseThrow(() -> new RuntimeException("休暇申請が見つかりません"));
    }

    public VacationRequest findByAccountIdAndVacationId(Long accountId, Long id)
    {
        Account account = new Account();
        account.setId(accountId);
        return vacationRequestRepository.findByAccountIdAndVacationId(account, id)
            .orElseThrow(() -> new RuntimeException("休暇申請が見つかりません"));
    }
}