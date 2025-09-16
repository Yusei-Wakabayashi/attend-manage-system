package com.example.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springboot.model.AttendanceExceptionType;

public interface AttendanceExceptionTypeRepository extends JpaRepository<AttendanceExceptionType, Long>
{

}
