package com.example.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springboot.model.OverTimeRequest;
import com.example.springboot.model.Shift;
import com.example.springboot.model.ShiftListOverTime;

public interface ShiftListOverTimeRepository extends JpaRepository<ShiftListOverTime, Long>
{
    ShiftListOverTime findByOverTimeId(OverTimeRequest overTimeRequest);
    List<ShiftListOverTime> findByShiftListId(Shift shift);
    List<ShiftListOverTime> findByShiftListIdIn(List<Shift> shifts);
}
