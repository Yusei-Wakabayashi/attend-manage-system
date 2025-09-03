package com.example.springboot.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.dto.response.ShiftListResponse;
import com.example.springboot.model.Account;
import com.example.springboot.model.Shift;
import com.example.springboot.repository.ShiftRepository;

@Service
public class ShiftService
{
    @Autowired
    private ShiftRepository shiftRepository;
    public List<Shift> findByAccountId(Long id)
    {
        Account accountId = new Account();
        accountId.setId(id);
        return shiftRepository.findByAccountId(accountId);
    }
    public List<Shift> findByAccountId(Account id)
    {
        return shiftRepository.findByAccountId(id);
    }
    // 一月ごとの取得
    public List<Shift> findByAccountIdAndBeginWorkBetween(Long id, int year, int month)
    {
        Account accountId = new Account();
        accountId.setId(id);
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();
        LocalDateTime startPeriod = firstDay.atStartOfDay();
        LocalDateTime endPeriod = lastDay.atTime(23,59,59);
        List<Shift> shiftList = shiftRepository.findByAccountIdAndBeginWorkBetween(accountId, startPeriod, endPeriod);
        return shiftList;
    }
    public List<Shift> findByAccountIdAndBeginWorkBetween(Account id, int year, int month)
    {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();
        LocalDateTime startPeriod = firstDay.atStartOfDay();
        LocalDateTime endPeriod = lastDay.atTime(23,59,59);
        List<Shift> shiftList = shiftRepository.findByAccountIdAndBeginWorkBetween(id, startPeriod, endPeriod);
        return shiftList;
    }
    // 1日ごと
    public List<Shift> findByAccountIdAndDayBeginWorkBetween(Account id, LocalDateTime begin)
    {
        // 始業時間から1日の開始と終了を作成
        LocalDateTime beginWork = LocalDateTime.of(begin.toLocalDate(), LocalTime.MIN);
        LocalDateTime endWork = LocalDateTime.of(begin.toLocalDate(), LocalTime.MAX);
        List<Shift> shiftList = shiftRepository.findByAccountIdAndBeginWorkBetween(id, beginWork, endWork);
        return shiftList;
    }

    public List<Shift> findByAccountIdAndDayBeginWorkBetween(Long id, LocalDateTime begin)
    {
        Account account = new Account();
        account.setId(id);
        // 始業時間から1日の開始と終了を作成
        LocalDateTime beginWork = LocalDateTime.of(begin.toLocalDate(), LocalTime.MIN);
        LocalDateTime endWork = LocalDateTime.of(begin.toLocalDate(), LocalTime.MAX);
        List<Shift> shiftList = shiftRepository.findByAccountIdAndBeginWorkBetween(account, beginWork, endWork);
        return shiftList;
    }
    // 1週ごと
    public List<Shift> findByAccountIdAndBeginWorkBetweenWeek(Long id, LocalDateTime begin)
    {
        Account account = new Account();
        account.setId(id);
        LocalDateTime beginWork = begin.toLocalDate().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).atStartOfDay();
        LocalDateTime endWork = begin.toLocalDate().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).atTime(LocalTime.MAX);
        List<Shift> shifts = shiftRepository.findByAccountIdAndBeginWorkBetween(account, beginWork, endWork);
        return shifts;
    }

    public List<Shift> findByAccountIdAndBeginWorkBetweenWeek(Account id, LocalDateTime begin)
    {
        LocalDateTime beginWork = begin.toLocalDate().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).atStartOfDay();
        LocalDateTime endWork = begin.toLocalDate().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).atTime(LocalTime.MAX);
        List<Shift> shifts = shiftRepository.findByAccountIdAndBeginWorkBetween(id, beginWork, endWork);
        return shifts;
    }
    
    public ShiftListResponse shiftToShiftListResponse(Shift shift)
    {
        ShiftListResponse shiftListResponse = new ShiftListResponse();
        shiftListResponse.setId(shift.getShiftId());
        shiftListResponse.setBeginWork(shift.getBeginWork().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + shift.getBeginWork().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        shiftListResponse.setEndWork(shift.getEndWork().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + shift.getEndWork().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        shiftListResponse.setBeginBreak(shift.getBeginBreak().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + shift.getBeginBreak().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        shiftListResponse.setEndBreak(shift.getEndBreak().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + shift.getEndBreak().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        shiftListResponse.setLateness(shift.getLateness());
        shiftListResponse.setLeaveEarly(shift.getLeaveEarly());
        shiftListResponse.setOuting(shift.getOuting());
        shiftListResponse.setOverWork(shift.getOverWork());
        return shiftListResponse;
    }

    public String save(Shift shift)
    {
        shiftRepository.save(shift);
        return "ok";
    }

    @Transactional
    public void resetAllTables()
    {
        shiftRepository.deleteAll();
        shiftRepository.resetAutoIncrement();
    }

    public void init()
    {
        
    }
}
