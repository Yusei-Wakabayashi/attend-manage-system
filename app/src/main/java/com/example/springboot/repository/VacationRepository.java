package com.example.springboot.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springboot.model.Account;
import com.example.springboot.model.Vacation;
import com.example.springboot.model.VacationType;

public interface VacationRepository extends JpaRepository<Vacation, Long>
{
    List<Vacation> findByAccountIdAndBeginVacationBetween(Account id, LocalDateTime startPeriod, LocalDateTime endPeriod);
    List<Vacation> findByAccountIdAndBeginVacationBetweenAndVacationTypeId(Account id, LocalDateTime startPeriod, LocalDateTime endPeriod, VacationType vacationTypeId);
}
