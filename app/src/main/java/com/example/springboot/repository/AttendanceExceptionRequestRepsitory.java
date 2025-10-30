package com.example.springboot.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springboot.model.Account;
import com.example.springboot.model.AttendanceExceptionRequest;
import com.example.springboot.model.AttendanceExceptionType;
import com.example.springboot.model.Shift;

public interface AttendanceExceptionRequestRepsitory extends JpaRepository<AttendanceExceptionRequest, Long>
{
    Optional<AttendanceExceptionRequest> findByAccountIdAndAttendanceExceptionId(Account accountId, Long id);
    List<AttendanceExceptionRequest> findByAccountId(Account account);
    List<AttendanceExceptionRequest> findByAccountIdIn(List<Account> accounts);
    List<AttendanceExceptionRequest> findByAccountIdAndRequestStatusAndBeginTimeBetween(Account account, int requestStatus, LocalDateTime startPeriod, LocalDateTime endPeriod);
    List<AttendanceExceptionRequest> findByAccountIdAndShiftIdAndAttendanceExceptionTypeIdAndBeginTimeBetweenAndRequestStatus(Account account, Shift shift, AttendanceExceptionType attendanceExceptionType, LocalDateTime startPeriod, LocalDateTime endPeriod, int requestStatus);
    List<AttendanceExceptionRequest> findByAccountIdAndShiftIdAndAttendanceExceptionTypeIdAndEndTimeBetweenAndRequestStatus(Account account, Shift shift, AttendanceExceptionType attendanceExceptionType, LocalDateTime startPeriod, LocalDateTime endPeriod, int requestStatus);
}
