package com.example.springboot.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springboot.model.Account;
import com.example.springboot.model.AttendanceExceptionRequest;

public interface AttendanceExceptionRequestRepsitory extends JpaRepository<AttendanceExceptionRequest, Long>
{
    Optional<AttendanceExceptionRequest> findByAccountIdAndAttendanceExceptionId(Account accountId, Long id);
    List<AttendanceExceptionRequest> findByAccountId(Account account);
    List<AttendanceExceptionRequest> findByAccountIdIn(List<Account> accounts);
}
