package com.example.springboot.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.springboot.model.Account;
import com.example.springboot.model.Shift;

public interface ShiftRepository extends JpaRepository<Shift, Long>
{
    List<Shift> findByAccountId(Account id);
    List<Shift> findByAccountIdAndBeginWorkBetween(Account id, LocalDateTime startPeriod, LocalDateTime endPeriod);
    Shift findByAccountIdAndShiftId(Account id, Long ShiftId);
    List<Shift> findByAccountIdAndEndWorkBetween(Account id, LocalDateTime startPeriod, LocalDateTime endPeriod);

    @Modifying
    @Query(value = "ALTER TABLE shift_list AUTO_INCREMENT = 1", nativeQuery = true)
    void resetAutoIncrement();
}
