package com.example.springboot.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springboot.model.Account;
import com.example.springboot.model.Shift;
import com.example.springboot.model.VacationRequest;
import com.example.springboot.model.VacationType;

public interface VacationRequestRepository extends JpaRepository<VacationRequest, Long>
{
    Optional<VacationRequest> findByAccountIdAndVacationId(Account accountId, Long id);
    List<VacationRequest> findByAccountId(Account account);
    List<VacationRequest> findByAccountIdAndShiftId(Account account, Shift shift);
    List<VacationRequest> findByAccountIdAndRequestStatusAndVacationTypeId(Account account, int status, VacationType vacationType);
    List<VacationRequest> findByAccountIdAndVacationTypeIdAndRequestStatusAndBeginVacationBetween(Account account, VacationType vacationType, int requestStatus, LocalDateTime startPeriod, LocalDateTime endPeriod);
}
