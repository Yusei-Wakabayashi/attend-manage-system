package com.example.springboot.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.springboot.model.Account;
import com.example.springboot.model.Attend;

public interface AttendRepository extends JpaRepository<Attend, Long>
{
    List<Attend> findByAccountIdAndBeginWorkBetween(Account id, LocalDateTime startPeriod, LocalDateTime endPeriod);
    Optional<Attend> findByAccountIdAndAttendanceId(Account id, Long AttendanceId);
    @Modifying
    @Query(value = "ALTER TABLE attend_lists AUTO_INCREMENT = 1", nativeQuery = true)
    void resetAutoIncrement();
}
