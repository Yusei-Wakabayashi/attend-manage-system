package com.example.springboot.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springboot.model.Account;
import com.example.springboot.model.VacationRequest;

public interface VacationRequestRepository extends JpaRepository<VacationRequest, Long>
{
    Optional<VacationRequest> findByAccountIdAndVacationId(Account accountId, Long id);
    List<VacationRequest> findByAccountId(Account account);
}
