package com.example.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springboot.model.Account;
import com.example.springboot.model.PaydHolidayUse;

public interface PaydHoildayUseRepository extends JpaRepository<PaydHolidayUse, Long>
{
    List<PaydHolidayUse> findByAccountId(Account account);
}
