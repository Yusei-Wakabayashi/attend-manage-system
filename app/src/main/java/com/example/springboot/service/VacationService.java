package com.example.springboot.service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.model.Account;
import com.example.springboot.model.Vacation;
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
}
