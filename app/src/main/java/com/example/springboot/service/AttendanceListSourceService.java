package com.example.springboot.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.model.Attend;
import com.example.springboot.model.AttendanceListSource;
import com.example.springboot.model.Shift;
import com.example.springboot.repository.AttendanceListSourceRepository;

@Service
public class AttendanceListSourceService
{
    private final AttendanceListSourceRepository attendanceListSourceRepository;

    @Autowired
    public AttendanceListSourceService
    (
        AttendanceListSourceRepository attendanceListSourceRepository
    )
    {
        this.attendanceListSourceRepository = attendanceListSourceRepository;
    }

    public AttendanceListSource findByShiftId(Shift shift)
    {
        return attendanceListSourceRepository.findByShiftId(shift);
    }
    public AttendanceListSource findByShiftId(Long id)
    {
        Shift shift = new Shift();
        shift.setShiftId(id);
        return attendanceListSourceRepository.findByShiftId(shift);
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

    public List<AttendanceListSource> findByAttendIdIn(List<Attend> attends)
    {
        List<AttendanceListSource> attendanceListSources = attendanceListSourceRepository.findByAttendanceIdIn(attends);
        return attendanceListSources;
    }

    public AttendanceListSource save(AttendanceListSource attendanceListSource)
    {
        return attendanceListSourceRepository.save(attendanceListSource);
    }
}