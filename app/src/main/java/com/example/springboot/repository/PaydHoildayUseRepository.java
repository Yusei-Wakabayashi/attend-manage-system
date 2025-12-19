package com.example.springboot.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springboot.model.Account;
import com.example.springboot.model.PaydHolidayUse;
import com.example.springboot.model.VacationRequest;

public interface PaydHoildayUseRepository extends JpaRepository<PaydHolidayUse, Long>
{
    PaydHolidayUse findByVacationId(VacationRequest vacationRequest);
    List<PaydHolidayUse> findByAccountId(Account account);
    List<PaydHolidayUse> findByAccountIdAndUseDateBetween(Account account, LocalDateTime startPeriod, LocalDateTime endPeriod);
}
