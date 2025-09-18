package com.example.springboot.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.model.AttendanceListSource;
import com.example.springboot.model.Shift;
import com.example.springboot.repository.AttendanceListSourceRepository;

@Service
public class AttendanceListSourceService
{
    @Autowired
    private AttendanceListSourceRepository attendanceListSourceRepository;

    public AttendanceListSource findByShiftId(Shift shift)
    {
        return attendanceListSourceRepository.findByShiftId(shift)
            .orElseThrow(() -> new RuntimeException("シフトに対応する勤怠情報が見つかりません"));
    }
    public AttendanceListSource findByShiftId(Long id)
    {
        Shift shift = new Shift();
        shift.setShiftId(id);
        return attendanceListSourceRepository.findByShiftId(shift)
            .orElseThrow(() -> new RuntimeException("シフトに対応する勤怠情報が見つかりません"));
    }
    public List<AttendanceListSource> findByShiftIdIn(List<Shift> shifts)
    {
        List<AttendanceListSource> attendanceListSources = attendanceListSourceRepository.findByShiftIdIn(shifts);
        return attendanceListSources;
    }
    public List<AttendanceListSource> findByShiftIdIn(Shift shift)
    {
        List<Shift> shifts = new ArrayList<Shift>();
        shifts.add(shift);
        List<AttendanceListSource> attendanceListSources = attendanceListSourceRepository.findByShiftIdIn(shifts);
        return attendanceListSources;
    }
}