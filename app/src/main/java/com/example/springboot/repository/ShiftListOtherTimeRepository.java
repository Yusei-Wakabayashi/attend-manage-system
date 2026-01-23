package com.example.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springboot.model.AttendanceExceptionRequest;
import com.example.springboot.model.Shift;
import com.example.springboot.model.ShiftListOtherTime;

public interface ShiftListOtherTimeRepository extends JpaRepository<ShiftListOtherTime, Long>
{
    ShiftListOtherTime findByAttendanceExceptionId(AttendanceExceptionRequest attendanceExceptionRequest);
    List<ShiftListOtherTime> findByShiftId(Shift shift);
    List<ShiftListOtherTime> findByShiftIdIn(List<Shift> shifts);
}
