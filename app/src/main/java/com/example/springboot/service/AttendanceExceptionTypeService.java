package com.example.springboot.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.model.AttendanceExceptionType;
import com.example.springboot.repository.AttendanceExceptionTypeRepository;

@Service
public class AttendanceExceptionTypeService
{
    @Autowired
    AttendanceExceptionTypeRepository attendanceExceptionTypeRepository;

    public List<AttendanceExceptionType> findAll()
    {
        return attendanceExceptionTypeRepository.findAll();
    }

    @Transactional
    public void resetAllTables()
    {
        attendanceExceptionTypeRepository.deleteAll();
        attendanceExceptionTypeRepository.resetAutoIncrement();
    }

    public String save (AttendanceExceptionType attendanceExceptionType)
    {
        attendanceExceptionTypeRepository.save(attendanceExceptionType);
        return "ok";
    }

    public void init()
    {
        
    }
}
