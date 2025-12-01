package com.example.springboot.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.dto.change.LocalDateTimeToString;
import com.example.springboot.dto.response.AttendListResponse;
import com.example.springboot.model.Account;
import com.example.springboot.model.Attend;
import com.example.springboot.repository.AttendRepository;

@Service
public class AttendService
{
    @Autowired
    AttendRepository attendRepository;

    public Attend findAttendById(Long id)
    {
        return attendRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("出勤簿が見つかりません"));
    }

    // 一月ごとの取得
    public List<Attend> findByAccountIdAndBeginWorkBetween(Long id, int year, int month)
    {
        Account accountId = new Account();
        accountId.setId(id);
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();
        LocalDateTime startPeriod = firstDay.atStartOfDay();
        LocalDateTime endPeriod = lastDay.atTime(23,59,59);
        List<Attend> attendList = attendRepository.findByAccountIdAndBeginWorkBetween(accountId, startPeriod, endPeriod);
        return attendList;
    }
    public List<Attend> findByAccountIdAndBeginWorkBetween(Account id, int year, int month)
    {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();
        LocalDateTime startPeriod = firstDay.atStartOfDay();
        LocalDateTime endPeriod = lastDay.atTime(23,59,59);
        List<Attend> attendList = attendRepository.findByAccountIdAndBeginWorkBetween(id, startPeriod, endPeriod);
        return attendList;
    }

    public AttendListResponse attendToAttendListResponse(Attend attend)
    {
        LocalDateTimeToString localDateTimeToString = new LocalDateTimeToString();
        AttendListResponse attendListResponse = new AttendListResponse();
        attendListResponse.setId(attend.getAttendanceId());
        attendListResponse.setBeginWork(localDateTimeToString.localDateTimeToString(attend.getBeginWork()));
        attendListResponse.setEndWork(localDateTimeToString.localDateTimeToString(attend.getEndWork()));
        attendListResponse.setBeginBreak(localDateTimeToString.localDateTimeToString(attend.getBeginBreak()));
        attendListResponse.setEndBreak(localDateTimeToString.localDateTimeToString(attend.getEndBreak()));
        attendListResponse.setWorkTime(attend.getWorkTime());
        attendListResponse.setBreakTime(attend.getBreakTime());
        attendListResponse.setLateness(attend.getLateness());
        attendListResponse.setLeaveEarly(attend.getLeaveEarly());
        attendListResponse.setOuting(attend.getOuting());
        attendListResponse.setOverWork(attend.getOverWork());
        attendListResponse.setHolidayWork(attend.getHolidayWork());
        attendListResponse.setLateNightWork(attend.getLateNightWork());
        attendListResponse.setVacationTime(attend.getVacationTime());
        attendListResponse.setAbsenceTime(attend.getAbsenceTime());
        return attendListResponse;
    }

    public String save(Attend attend)
    {
        attendRepository.save(attend);
        return "ok";
    }

    @Transactional
    public void resetAllTables()
    {
        attendRepository.deleteAll();
        attendRepository.resetAutoIncrement();
    }
}
