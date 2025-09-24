package com.example.springboot.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.model.Account;
import com.example.springboot.model.Vacation;
import com.example.springboot.model.VacationType;
import com.example.springboot.repository.VacationRepository;

@Service
public class VacationService
{
    @Autowired
    private VacationRepository vacationRepository;
    
    public List<Vacation> findByAccountIdAndBeginVacationBetweenWeek(Account account, LocalDateTime begin)
    {
        LocalDateTime beginVacation = begin.toLocalDate().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).atStartOfDay();
        LocalDateTime endVacation = begin.toLocalDate().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).atTime(LocalTime.MAX);
        List<Vacation> vacations = vacationRepository.findByAccountIdAndBeginVacationBetween(account, beginVacation, endVacation);
        return vacations;
    }
    public List<Vacation> findByAccountIdAndBeginVacationBetweenWeek(Long id, LocalDateTime begin)
    {
        Account account = new Account();
        account.setId(id);
        LocalDateTime beginVacation = begin.toLocalDate().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).atStartOfDay();
        LocalDateTime endVacation = begin.toLocalDate().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).atTime(LocalTime.MAX);
        List<Vacation> vacations = vacationRepository.findByAccountIdAndBeginVacationBetween(account, beginVacation, endVacation);
        return vacations;
    }

    public List<Vacation> findByAccountIdAndBeginVacationBetweenMonthAndPaydHoliday(Long id, int year, int month)
    {
        Account account = new Account();
        account.setId(id);
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();
        LocalDateTime startPeriod = firstDay.atStartOfDay();
        LocalDateTime endPeriod = lastDay.atTime(23,59,59);
        Long vacationTypeId = 1L;
        VacationType vacationType = new VacationType();
        vacationType.setVacationTypeId(vacationTypeId);
        List<Vacation> vacations = vacationRepository.findByAccountIdAndBeginVacationBetweenAndVacationTypeId(account, startPeriod, endPeriod, vacationType);
        return vacations;
    }

    public List<Vacation> findByAccountIdAndBeginVacationBetweenMonthAndPaydHoliday(Account account, int year, int month)
    {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();
        LocalDateTime startPeriod = firstDay.atStartOfDay();
        LocalDateTime endPeriod = lastDay.atTime(23,59,59);
        Long vacationTypeId = 1L;
        VacationType vacationType = new VacationType();
        vacationType.setVacationTypeId(vacationTypeId);
        List<Vacation> vacations = vacationRepository.findByAccountIdAndBeginVacationBetweenAndVacationTypeId(account, startPeriod, endPeriod, vacationType);
        return vacations;
    }
}
