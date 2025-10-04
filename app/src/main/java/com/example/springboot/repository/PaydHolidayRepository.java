package com.example.springboot.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springboot.model.Account;
import com.example.springboot.model.PaydHoliday;

public interface PaydHolidayRepository extends JpaRepository<PaydHoliday, Long>
{
    // アカウントと特定の日付以降で検索
    List<PaydHoliday> findByAccountIdAndLimitAfter(Account account, LocalDateTime date);
}