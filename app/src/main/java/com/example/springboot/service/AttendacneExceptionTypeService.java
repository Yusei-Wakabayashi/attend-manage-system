package com.example.springboot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.repository.AttendanceExceptionTypeRepository;

import com.example.springboot.model.AttendanceExceptionType;

@Service
public class AttendacneExceptionTypeService
{
    @Autowired
    AttendanceExceptionTypeRepository attendanceExceptionTypeRepository;

    public List<AttendanceExceptionType> findAll()
    {
        return attendanceExceptionTypeRepository.findAll();
    }
}
