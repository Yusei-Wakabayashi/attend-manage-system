package com.example.springboot.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springboot.model.Account;
import com.example.springboot.model.Shift;
import com.example.springboot.model.ShiftChangeRequest;

public interface ShiftChangeRequestRepository extends JpaRepository<ShiftChangeRequest, Long>
{
    Optional<ShiftChangeRequest> findByAccountIdAndShiftChangeId(Account accountId, Long id);
    List<ShiftChangeRequest> findByAccountIdAndShiftIdAndRequestStatus(Account accountId, Shift id, int requestStatus);
    List<ShiftChangeRequest> findByAccountIdAndBeginWorkBetweenAndRequestStatus(Account accountId, LocalDateTime beginWork, LocalDateTime endWork, int requestStatus);
    List<ShiftChangeRequest> findByAccountIdAndShiftIdInAndRequestStatus(Account accountId, List<Shift> shiftIds, int requestStatus);
    List<ShiftChangeRequest> findByAccountId(Account account);
    List<ShiftChangeRequest> findByAccountIdIn(List<Account> accounts);
}
