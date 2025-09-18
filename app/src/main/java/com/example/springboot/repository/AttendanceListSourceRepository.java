package com.example.springboot.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springboot.model.AttendanceListSource;
import com.example.springboot.model.Shift;

public interface AttendanceListSourceRepository extends JpaRepository<AttendanceListSource, Long>
{
    Optional<AttendanceListSource> findByShiftId(Shift id);
    List<AttendanceListSource> findByShiftIdIn(List<Shift> id);
}