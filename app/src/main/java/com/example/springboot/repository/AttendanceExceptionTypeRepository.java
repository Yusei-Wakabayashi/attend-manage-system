package com.example.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.springboot.model.AttendanceExceptionType;

public interface AttendanceExceptionTypeRepository extends JpaRepository<AttendanceExceptionType, Long>
{
    @Modifying
    @Query(value = "ALTER TABLE attendance_exception_types AUTO_INCREMENT = 1", nativeQuery = true)
    void resetAutoIncrement();
}
