package com.example.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springboot.model.Shift;
import com.example.springboot.model.ShiftListVacation;
import com.example.springboot.model.VacationRequest;

public interface ShiftListVacationRepository extends JpaRepository<ShiftListVacation, Long>
{
    List<ShiftListVacation> findByShiftId(Shift shift);
    List<ShiftListVacation> findByShiftIdIn(List<Shift> shifts);
    ShiftListVacation findByVacationId(VacationRequest vacationRequest);
}
