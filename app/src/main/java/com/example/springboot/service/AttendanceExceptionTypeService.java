package com.example.springboot.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.dto.response.OtherTypeListResponse;
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

    public List<OtherTypeListResponse> returnOtherTypeListResponses()
    {
        List<OtherTypeListResponse> otherTypeListResponses = new ArrayList<OtherTypeListResponse>();
        List<AttendanceExceptionType> attendanceExceptionTypes = findAll();
        for(AttendanceExceptionType attendanceExceptionType : attendanceExceptionTypes)
        {
            OtherTypeListResponse otherTypeListResponse = new OtherTypeListResponse();
            otherTypeListResponse.setOtherTypeId(attendanceExceptionType.getAttendanceExceptionTypeId().intValue());
            otherTypeListResponse.setOtherTypeName(attendanceExceptionType.getAttednaceExceptionTypeName());
            otherTypeListResponses.add(otherTypeListResponse);
        }
        return otherTypeListResponses;
    }

    public AttendanceExceptionType findByAttendanceExceptionTypeId(Long attendanceExceptionType)
    {
        return attendanceExceptionTypeRepository.findById(attendanceExceptionType)
            .orElseThrow(() -> new RuntimeException("勤怠例外種類が見つかりません"));
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
