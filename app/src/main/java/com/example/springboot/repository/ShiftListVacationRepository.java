package com.example.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springboot.model.Shift;
import com.example.springboot.model.ShiftListVacation;

public interface ShiftListVacationRepository extends JpaRepository<ShiftListVacation, Long>
{
    List<ShiftListVacation> findByShiftIdIn(List<Shift> shifts);
}
