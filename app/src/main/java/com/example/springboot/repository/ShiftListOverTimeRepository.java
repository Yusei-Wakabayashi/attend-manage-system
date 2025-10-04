package com.example.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springboot.model.Shift;
import com.example.springboot.model.ShiftListOverTime;

public interface ShiftListOverTimeRepository extends JpaRepository<ShiftListOverTime, Long>
{
    List<ShiftListOverTime> findByShiftListId(Shift shift);
}
