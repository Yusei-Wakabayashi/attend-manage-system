package com.example.springboot.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.dto.change.LocalDateTimeToString;
import com.example.springboot.dto.response.RequestDetailOtherTimeResponse;
import com.example.springboot.model.Account;
import com.example.springboot.model.AttendanceExceptionRequest;
import com.example.springboot.model.AttendanceExceptionType;
import com.example.springboot.model.Shift;
import com.example.springboot.repository.AttendanceExceptionRequestRepsitory;

@Service
public class AttendanceExceptionRequestService
{
    private final AttendanceExceptionRequestRepsitory attendanceExceptionRequestRepsitory;
    private final LocalDateTimeToString localDateTimeToString;

    @Autowired
    public AttendanceExceptionRequestService
    (
        AttendanceExceptionRequestRepsitory attendanceExceptionRequestRepsitory,
        LocalDateTimeToString localDateTimeToString
    )
    {
        this.attendanceExceptionRequestRepsitory = attendanceExceptionRequestRepsitory;
        this.localDateTimeToString = localDateTimeToString;
    }

    public AttendanceExceptionRequest findById(Long id)
    {
        return attendanceExceptionRequestRepsitory.findById(id)
            .orElseThrow(() -> new RuntimeException("勤怠例外申請が見つかりません"));
    }

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

    public List<AttendanceExceptionRequest> findByAccountIdAndShiftIdAndOutingAndBeginTimeBetweenOrEndTimeBetweenAndRequestStatusWaitOrRequestStatusApproved(Long accountId, Long shiftId, LocalDateTime startPeriod, LocalDateTime endPeriod)
    {
        Account account = new Account();
        account.setId(accountId);
        Shift shift = new Shift();
        shift.setShiftId(shiftId);
        // 検索する際申請のリクエストが9時開始10時終了で既に10時開始11時終了があった際10時を含みたくないため1秒ずらしている
        startPeriod.plusSeconds(1);
        endPeriod.minusSeconds(1);
        AttendanceExceptionType outing = new AttendanceExceptionType();
        outing.setAttendanceExceptionTypeId(1L);
        int requestStatusWait = 1;
        int requestStatusApproved = 2;
        List<AttendanceExceptionRequest> beginTimeListWait = attendanceExceptionRequestRepsitory.findByAccountIdAndShiftIdAndAttendanceExceptionTypeIdAndBeginTimeBetweenAndRequestStatus(account, shift, outing, startPeriod, endPeriod, requestStatusWait);
        List<AttendanceExceptionRequest> endTimeListWait = attendanceExceptionRequestRepsitory.findByAccountIdAndShiftIdAndAttendanceExceptionTypeIdAndEndTimeBetweenAndRequestStatus(account, shift, outing, startPeriod, endPeriod, requestStatusWait);
        List<AttendanceExceptionRequest> beginTimeListApproved = attendanceExceptionRequestRepsitory.findByAccountIdAndShiftIdAndAttendanceExceptionTypeIdAndBeginTimeBetweenAndRequestStatus(account, shift, outing, startPeriod, endPeriod, requestStatusApproved);
        List<AttendanceExceptionRequest> endTimeListApproved = attendanceExceptionRequestRepsitory.findByAccountIdAndShiftIdAndAttendanceExceptionTypeIdAndEndTimeBetweenAndRequestStatus(account, shift, outing, startPeriod, endPeriod, requestStatusApproved);
        List<AttendanceExceptionRequest> allTimeListWait = Stream.concat(beginTimeListWait.stream(), endTimeListWait.stream()).distinct().collect(Collectors.toList());
        List<AttendanceExceptionRequest> allTimeListApproved = Stream.concat(beginTimeListApproved.stream(), endTimeListApproved.stream()).distinct().collect(Collectors.toList());
        List<AttendanceExceptionRequest> allTimeList = Stream.concat(allTimeListWait.stream(), allTimeListApproved.stream()).distinct().collect(Collectors.toList());
        return allTimeList;
    }

    public List<AttendanceExceptionRequest> findByAccountIdAndShiftIdAndOutingAndBeginTimeBetweenOrEndTimeBetweenAndRequestStatusWaitOrRequestStatusApproved(Account account, Long shiftId, LocalDateTime startPeriod, LocalDateTime endPeriod)
    {
        Shift shift = new Shift();
        shift.setShiftId(shiftId);
        // 検索する際申請のリクエストが9時開始10時終了で既に10時開始11時終了があった際10時を含みたくないため1秒ずらしている
        startPeriod.plusSeconds(1);
        endPeriod.minusSeconds(1);
        AttendanceExceptionType outing = new AttendanceExceptionType();
        outing.setAttendanceExceptionTypeId(1L);
        int requestStatusWait = 1;
        int requestStatusApproved = 2;
        List<AttendanceExceptionRequest> beginTimeListWait = attendanceExceptionRequestRepsitory.findByAccountIdAndShiftIdAndAttendanceExceptionTypeIdAndBeginTimeBetweenAndRequestStatus(account, shift, outing, startPeriod, endPeriod, requestStatusWait);
        List<AttendanceExceptionRequest> endTimeListWait = attendanceExceptionRequestRepsitory.findByAccountIdAndShiftIdAndAttendanceExceptionTypeIdAndEndTimeBetweenAndRequestStatus(account, shift, outing, startPeriod, endPeriod, requestStatusWait);
        List<AttendanceExceptionRequest> beginTimeListApproved = attendanceExceptionRequestRepsitory.findByAccountIdAndShiftIdAndAttendanceExceptionTypeIdAndBeginTimeBetweenAndRequestStatus(account, shift, outing, startPeriod, endPeriod, requestStatusApproved);
        List<AttendanceExceptionRequest> endTimeListApproved = attendanceExceptionRequestRepsitory.findByAccountIdAndShiftIdAndAttendanceExceptionTypeIdAndEndTimeBetweenAndRequestStatus(account, shift, outing, startPeriod, endPeriod, requestStatusApproved);
        List<AttendanceExceptionRequest> allTimeListWait = Stream.concat(beginTimeListWait.stream(), endTimeListWait.stream()).distinct().collect(Collectors.toList());
        List<AttendanceExceptionRequest> allTimeListApproved = Stream.concat(beginTimeListApproved.stream(), endTimeListApproved.stream()).distinct().collect(Collectors.toList());
        List<AttendanceExceptionRequest> allTimeList = Stream.concat(allTimeListWait.stream(), allTimeListApproved.stream()).distinct().collect(Collectors.toList());
        return allTimeList;
    }

    public List<AttendanceExceptionRequest> findByAccountIdAndShiftIdAndOutingAndBeginTimeBetweenOrEndTimeBetweenAndRequestStatusWaitOrRequestStatusApproved(Long accountId, Shift shift, LocalDateTime startPeriod, LocalDateTime endPeriod)
    {
        Account account = new Account();
        account.setId(accountId);
        // 検索する際申請のリクエストが9時開始10時終了で既に10時開始11時終了があった際10時を含みたくないため1秒ずらしている
        startPeriod.plusSeconds(1);
        endPeriod.minusSeconds(1);
        AttendanceExceptionType outing = new AttendanceExceptionType();
        outing.setAttendanceExceptionTypeId(1L);
        int requestStatusWait = 1;
        int requestStatusApproved = 2;
        List<AttendanceExceptionRequest> beginTimeListWait = attendanceExceptionRequestRepsitory.findByAccountIdAndShiftIdAndAttendanceExceptionTypeIdAndBeginTimeBetweenAndRequestStatus(account, shift, outing, startPeriod, endPeriod, requestStatusWait);
        List<AttendanceExceptionRequest> endTimeListWait = attendanceExceptionRequestRepsitory.findByAccountIdAndShiftIdAndAttendanceExceptionTypeIdAndEndTimeBetweenAndRequestStatus(account, shift, outing, startPeriod, endPeriod, requestStatusWait);
        List<AttendanceExceptionRequest> beginTimeListApproved = attendanceExceptionRequestRepsitory.findByAccountIdAndShiftIdAndAttendanceExceptionTypeIdAndBeginTimeBetweenAndRequestStatus(account, shift, outing, startPeriod, endPeriod, requestStatusApproved);
        List<AttendanceExceptionRequest> endTimeListApproved = attendanceExceptionRequestRepsitory.findByAccountIdAndShiftIdAndAttendanceExceptionTypeIdAndEndTimeBetweenAndRequestStatus(account, shift, outing, startPeriod, endPeriod, requestStatusApproved);
        List<AttendanceExceptionRequest> allTimeListWait = Stream.concat(beginTimeListWait.stream(), endTimeListWait.stream()).distinct().collect(Collectors.toList());
        List<AttendanceExceptionRequest> allTimeListApproved = Stream.concat(beginTimeListApproved.stream(), endTimeListApproved.stream()).distinct().collect(Collectors.toList());
        List<AttendanceExceptionRequest> allTimeList = Stream.concat(allTimeListWait.stream(), allTimeListApproved.stream()).distinct().collect(Collectors.toList());
        return allTimeList;
    }

    public List<AttendanceExceptionRequest> findByAccountIdAndShiftIdAndOutingAndBeginTimeBetweenOrEndTimeBetweenAndRequestStatusWaitOrRequestStatusApproved(Account account, Shift shift, LocalDateTime startPeriod, LocalDateTime endPeriod)
    {
        // 検索する際申請のリクエストが9時開始10時終了で既に10時開始11時終了があった際10時を含みたくないため1秒ずらしている
        startPeriod.plusSeconds(1);
        endPeriod.minusSeconds(1);
        AttendanceExceptionType outing = new AttendanceExceptionType();
        outing.setAttendanceExceptionTypeId(1L);
        int requestStatusWait = 1;
        int requestStatusApproved = 2;
        List<AttendanceExceptionRequest> beginTimeListWait = attendanceExceptionRequestRepsitory.findByAccountIdAndShiftIdAndAttendanceExceptionTypeIdAndBeginTimeBetweenAndRequestStatus(account, shift, outing, startPeriod, endPeriod, requestStatusWait);
        List<AttendanceExceptionRequest> endTimeListWait = attendanceExceptionRequestRepsitory.findByAccountIdAndShiftIdAndAttendanceExceptionTypeIdAndEndTimeBetweenAndRequestStatus(account, shift, outing, startPeriod, endPeriod, requestStatusWait);
        List<AttendanceExceptionRequest> beginTimeListApproved = attendanceExceptionRequestRepsitory.findByAccountIdAndShiftIdAndAttendanceExceptionTypeIdAndBeginTimeBetweenAndRequestStatus(account, shift, outing, startPeriod, endPeriod, requestStatusApproved);
        List<AttendanceExceptionRequest> endTimeListApproved = attendanceExceptionRequestRepsitory.findByAccountIdAndShiftIdAndAttendanceExceptionTypeIdAndEndTimeBetweenAndRequestStatus(account, shift, outing, startPeriod, endPeriod, requestStatusApproved);
        List<AttendanceExceptionRequest> allTimeListWait = Stream.concat(beginTimeListWait.stream(), endTimeListWait.stream()).distinct().collect(Collectors.toList());
        List<AttendanceExceptionRequest> allTimeListApproved = Stream.concat(beginTimeListApproved.stream(), endTimeListApproved.stream()).distinct().collect(Collectors.toList());
        List<AttendanceExceptionRequest> allTimeList = Stream.concat(allTimeListWait.stream(), allTimeListApproved.stream()).distinct().collect(Collectors.toList());
        return allTimeList;
    }
    
    public List<AttendanceExceptionRequest> findByAccountIdAndBeginTimeBetweenAndRequestStatusWait(Long accountId, int year, int month)
    {
        Account account = new Account();
        account.setId(accountId);
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();
        LocalDateTime startPeriod = firstDay.atStartOfDay();
        LocalDateTime endPeriod = lastDay.atTime(23,59,59);
        int wait = 1;
        List<AttendanceExceptionRequest> attendanceExceptionRequests = attendanceExceptionRequestRepsitory.findByAccountIdAndRequestStatusAndBeginTimeBetween(account, wait, startPeriod, endPeriod);
        return attendanceExceptionRequests;
    }

    public List<AttendanceExceptionRequest> findByAccountIdAndBeginTimeBetweenAndRequestStatusWait(Account account, int year, int month)
    {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();
        LocalDateTime startPeriod = firstDay.atStartOfDay();
        LocalDateTime endPeriod = lastDay.atTime(23,59,59);
        int wait = 1;
        List<AttendanceExceptionRequest> attendanceExceptionRequests = attendanceExceptionRequestRepsitory.findByAccountIdAndRequestStatusAndBeginTimeBetween(account, wait, startPeriod, endPeriod);
        return attendanceExceptionRequests;
    }
    
    public RequestDetailOtherTimeResponse mapTorequestDetail(AttendanceExceptionRequest attendanceExceptionRequest)
    {
        RequestDetailOtherTimeResponse requestDetailOtherTimeResponse = new RequestDetailOtherTimeResponse
        (
            1,
            attendanceExceptionRequest.getShiftId().getShiftId().intValue(),
            attendanceExceptionRequest.getAttendanceExceptionTypeId().getAttendanceExceptionTypeId().intValue(),
            localDateTimeToString.localDateTimeToString(attendanceExceptionRequest.getBeginTime()),
            localDateTimeToString.localDateTimeToString(attendanceExceptionRequest.getEndTime()),
            attendanceExceptionRequest.getRequestComment(),
            localDateTimeToString.localDateTimeToString(attendanceExceptionRequest.getRequestDate()),
            attendanceExceptionRequest.getRequestStatus(),
            Objects.isNull(attendanceExceptionRequest.getApprover()) ? null : attendanceExceptionRequest.getApprover().getId().intValue(),
            Objects.isNull(attendanceExceptionRequest.getApprover()) ? "" : attendanceExceptionRequest.getApprover().getName(),
            attendanceExceptionRequest.getApproverComment(),
            Objects.isNull(attendanceExceptionRequest.getApprovalTime()) ? "" : localDateTimeToString.localDateTimeToString(attendanceExceptionRequest.getApprovalTime())
        );
        return requestDetailOtherTimeResponse;
    }

    public AttendanceExceptionRequest save(AttendanceExceptionRequest attendanceExceptionRequest)
    {
        return attendanceExceptionRequestRepsitory.save(attendanceExceptionRequest);
    }
}