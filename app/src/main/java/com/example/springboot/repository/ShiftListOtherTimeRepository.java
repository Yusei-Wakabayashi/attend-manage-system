package com.example.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springboot.model.Shift;
import com.example.springboot.model.ShiftListOtherTime;

public interface ShiftListOtherTimeRepository extends JpaRepository<ShiftListOtherTime, Long>
{
    List<ShiftListOtherTime> findByShiftId(Shift shift);
}
