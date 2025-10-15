package com.example.springboot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.model.Account;
import com.example.springboot.model.AttendanceExceptionRequest;
import com.example.springboot.repository.AttendanceExceptionRequestRepsitory;

@Service
public class AttendanceExceptionRequestService
{
    @Autowired
    private AttendanceExceptionRequestRepsitory attendanceExceptionRequestRepsitory;

    public AttendanceExceptionRequest findByAccountIdAndAttendanceExceptionId(Long accountId, Long id)
    {
        Account account = new Account();
        account.setId(accountId);
        return attendanceExceptionRequestRepsitory.findByAccountIdAndAttendanceExceptionId(account, id)
            .orElseThrow(() -> new RuntimeException("勤怠例外申請が見つかりません"));
    }

    public AttendanceExceptionRequest findByAccountIdAndAttendanceExceptionId(Account account, Long id)
    {
        return attendanceExceptionRequestRepsitory.findByAccountIdAndAttendanceExceptionId(account, id)
            .orElseThrow(() -> new RuntimeException("勤怠例外申請が見つかりません"));
    }

    public List<AttendanceExceptionRequest> findByAccountId(Long accountId)
    {
        Account account = new Account();
        account.setId(accountId);
        return attendanceExceptionRequestRepsitory.findByAccountId(account);
    }

    public List<AttendanceExceptionRequest> findByAccountIdIn(List<Account> accounts)
    {
        List<AttendanceExceptionRequest> attendanceExceptionRequests = attendanceExceptionRequestRepsitory.findByAccountIdIn(accounts);
        return attendanceExceptionRequests;
    }

    public List<AttendanceExceptionRequest> findByAccountId(Account account)
    {
        return attendanceExceptionRequestRepsitory.findByAccountId(account);
    }
}