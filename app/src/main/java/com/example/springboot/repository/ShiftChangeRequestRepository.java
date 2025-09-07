package com.example.springboot.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springboot.model.Account;
import com.example.springboot.model.ShiftChangeRequest;

public interface ShiftChangeRequestRepository extends JpaRepository<ShiftChangeRequest, Long>
{
    Optional<ShiftChangeRequest> findByAccountIdAndShiftChangeId(Account accountId, Long id);
}
