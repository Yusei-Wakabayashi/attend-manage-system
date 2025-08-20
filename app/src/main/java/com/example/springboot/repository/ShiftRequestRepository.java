package com.example.springboot.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.springboot.model.Account;
import com.example.springboot.model.ShiftRequest;

public interface ShiftRequestRepository extends JpaRepository<ShiftRequest, Long>
{
    List<ShiftRequest> findByAccountIdAndBeginWorkBetween(Account accountId, LocalDateTime beginWork, LocalDateTime endWork);
    List<ShiftRequest> findByBeginWorkBetween(LocalDateTime beginWork, LocalDateTime endWork);
    @Modifying
    @Query(value = "ALTER TABLE shift_requests AUTO_INCREMENT = 1", nativeQuery = true)
    void resetAutoIncrement();
}
