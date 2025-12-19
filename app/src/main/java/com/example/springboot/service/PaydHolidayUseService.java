package com.example.springboot.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.model.Account;
import com.example.springboot.model.PaydHolidayUse;
import com.example.springboot.model.VacationRequest;
import com.example.springboot.repository.PaydHoildayUseRepository;

@Service
public class PaydHolidayUseService
{
    @Autowired
    private PaydHoildayUseRepository paydHoildayUseRepository;

    public PaydHolidayUse findByPaydHolidayUseId(Long id)
    {
        return paydHoildayUseRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("有給消化の履歴が見つかりません"));
    }

    public PaydHolidayUse findByVacationId(VacationRequest vacationRequest)
    {
        return paydHoildayUseRepository.findByVacationId(vacationRequest);
    }

    public PaydHolidayUse findByVacationId(Long id)
    {
        VacationRequest vacationRequest = new VacationRequest();
        vacationRequest.setVacationId(id);
        return paydHoildayUseRepository.findByVacationId(vacationRequest);
    }

    public List<PaydHolidayUse> findByAccountId(Long accountId)
    {
        Account account = new Account();
        account.setId(accountId);
        List<PaydHolidayUse> paydHolidayUses = paydHoildayUseRepository.findByAccountId(account);
        return paydHolidayUses;
    }

    public List<PaydHolidayUse> findByAccountId(Account account)
    {
        List<PaydHolidayUse> paydHolidayUses = paydHoildayUseRepository.findByAccountId(account);
        return paydHolidayUses;
    }

    @Transactional
    public PaydHolidayUse save(PaydHolidayUse paydHolidayUse)
    {
        return paydHoildayUseRepository.save(paydHolidayUse);
    }

    @Transactional
    public void delete(PaydHolidayUse paydHolidayUse)
    {
        paydHoildayUseRepository.delete(paydHolidayUse);
    }
}
