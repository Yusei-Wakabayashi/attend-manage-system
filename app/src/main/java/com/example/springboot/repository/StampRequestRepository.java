package com.example.springboot.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.springboot.model.Account;
import com.example.springboot.model.Shift;
import com.example.springboot.model.StampRequest;

public interface StampRequestRepository extends JpaRepository<StampRequest, Long>
{
    StampRequest findByAccountIdAndStampId(Account accountId, Long Id);
    List<StampRequest> findByShiftIdAndRequestStatus(Shift id, int status);
    List<StampRequest> findByAccountIdAndBeginWorkBetweenAndRequestStatus(Account account, LocalDateTime startPeriod, LocalDateTime endPeriod, int requestStatus);
    List<StampRequest> findByAccountId(Account account);
    List<StampRequest> findByAccountIdIn(List<Account> accounts);
    @Modifying
    @Query(value = "ALTER TABLE stamp_requests AUTO_INCREMENT = 1", nativeQuery = true)
    void resetAutoIncrement();
}
