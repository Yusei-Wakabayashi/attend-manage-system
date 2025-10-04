package com.example.springboot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.model.Account;
import com.example.springboot.model.Shift;
import com.example.springboot.model.VacationRequest;
import com.example.springboot.model.VacationType;
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

    public List<VacationRequest> findByAccountIdAndShiftId(Long accountId, Long shiftId)
    {
        Account account = new Account();
        account.setId(accountId);
        Shift shift = new Shift();
        shift.setShiftId(shiftId);
        List<VacationRequest> vacationRequests = vacationRequestRepository.findByAccountIdAndShiftId(account, shift);
        return vacationRequests;
    }

    public List<VacationRequest> findByAccountIdAndShiftId(Account account, Long shiftId)
    {
        Shift shift = new Shift();
        shift.setShiftId(shiftId);
        List<VacationRequest> vacationRequests = vacationRequestRepository.findByAccountIdAndShiftId(account, shift);
        return vacationRequests;
    }

    public List<VacationRequest> findByAccountIdAndShiftId(Long accountId, Shift shift)
    {
        Account account = new Account();
        account.setId(accountId);
        List<VacationRequest> vacationRequests = vacationRequestRepository.findByAccountIdAndShiftId(account, shift);
        return vacationRequests;
    }

    public List<VacationRequest> findByAccountIdAndShiftId(Account account, Shift shift)
    {
        List<VacationRequest> vacationRequests = vacationRequestRepository.findByAccountIdAndShiftId(account, shift);
        return vacationRequests;
    }

    public List<VacationRequest> findByAccountIdAndRequestStatusWaitAndVacationTypePaydHoliday(Long accountId)
    {
        Account account = new Account();
        account.setId(accountId);
        // 承認待ちのidで検索
        VacationType vacationType = new VacationType();
        // 有給のidをセット
        vacationType.setVacationTypeId(1L);
        List<VacationRequest> vacationRequests = vacationRequestRepository.findByAccountIdAndRequestStatusAndVacationTypeId(account, 1, vacationType);
        return vacationRequests;
    }

    public List<VacationRequest> findByAccountIdAndRequestStatusWaitAndVacationTypePaydHoiday(Account account)
    {
        // 有給のidをセット
        VacationType vacationType = new VacationType();
        vacationType.setVacationTypeId(1L);
        // 承認待ちのidで検索
        List<VacationRequest> vacationRequests = vacationRequestRepository.findByAccountIdAndRequestStatusAndVacationTypeId(account, 1, vacationType);
        return vacationRequests;
    }

    public List<VacationRequest> findByAccountId(Long accountId)
    {
        Account account = new Account();
        account.setId(accountId);
        return vacationRequestRepository.findByAccountId(account);
    }

    public List<VacationRequest> findByAccountId(Account account)
    {
        return vacationRequestRepository.findByAccountId(account);
    }
}