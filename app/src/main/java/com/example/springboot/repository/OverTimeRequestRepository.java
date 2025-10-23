package com.example.springboot.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springboot.model.Account;
import com.example.springboot.model.OverTimeRequest;
import com.example.springboot.model.Shift;

public interface OverTimeRequestRepository extends JpaRepository<OverTimeRequest, Long>
{
    Optional<OverTimeRequest> findByAccountIdAndOverTimeId(Account accountId, Long Id);
    List<OverTimeRequest> findByAccountIdAndShiftIdAndRequestStatus(Account account, Shift shift, int requestStatus);
    List<OverTimeRequest> findByAccountId(Account account);
    List<OverTimeRequest> findByAccountIdIn(List<Account> accounts);
    List<OverTimeRequest> findByAccountIdAndRequestStatusAndBeginWorkBetween(Account account, int requestStatus, LocalDateTime startPeriod, LocalDateTime endPeriod);
    List<OverTimeRequest> findByAccountIdAndRequestStatusAndEndWorkBetween(Account account, int requestStatus, LocalDateTime startPeriod, LocalDateTime endPeriod);
}