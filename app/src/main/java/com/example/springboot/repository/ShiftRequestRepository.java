package com.example.springboot.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.springboot.model.Account;
import com.example.springboot.model.ShiftRequest;

public interface ShiftRequestRepository extends JpaRepository<ShiftRequest, Long>
{
    List<ShiftRequest> findByAccountIdAndBeginWorkBetween(Account accountId, LocalDateTime beginWork, LocalDateTime endWork);
    List<ShiftRequest> findByBeginWorkBetween(LocalDateTime beginWork, LocalDateTime endWork);
    List<ShiftRequest> findByAccountIdAndBeginWorkBetweenAndRequestStatus(Account accountId, LocalDateTime beginWork, LocalDateTime endWork, int requestStatus);
    Optional<ShiftRequest> findByAccountIdAndShiftRequestId(Account accountId, Long id);
    List<ShiftRequest> findByAccountId(Account account);
    @Modifying
    @Query(value = "ALTER TABLE shift_requests AUTO_INCREMENT = 1", nativeQuery = true)
    void resetAutoIncrement();
}
