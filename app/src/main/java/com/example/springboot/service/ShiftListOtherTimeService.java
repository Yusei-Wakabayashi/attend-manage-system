package com.example.springboot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.model.AttendanceExceptionRequest;
import com.example.springboot.model.Shift;
import com.example.springboot.model.ShiftListOtherTime;
import com.example.springboot.repository.ShiftListOtherTimeRepository;

@Service
public class ShiftListOtherTimeService
{
    @Autowired
    private ShiftListOtherTimeRepository shiftListOtherTimeRepository;

    public ShiftListOtherTime findByOtherTimeId(AttendanceExceptionRequest attendanceExceptionRequest)
    {
        return shiftListOtherTimeRepository.findByAttendanceExceptionId(attendanceExceptionRequest);
    }

    public ShiftListOtherTime findByOtherTimeId(Long id)
    {
        AttendanceExceptionRequest attendanceExceptionRequest = new AttendanceExceptionRequest();
        attendanceExceptionRequest.setAttendanceExceptionId(id);
        return shiftListOtherTimeRepository.findByAttendanceExceptionId(attendanceExceptionRequest);
    }

    public List<ShiftListOtherTime> findByShiftId(Long shiftId)
    {
        Shift shift = new Shift();
        shift.setShiftId(shiftId);
        List<ShiftListOtherTime> shiftListOtherTimes = shiftListOtherTimeRepository.findByShiftId(shift);
        return shiftListOtherTimes;
    }

    public List<ShiftListOtherTime> findByShiftId(Shift shift)
    {
        List<ShiftListOtherTime> shiftListOtherTimes = shiftListOtherTimeRepository.findByShiftId(shift);
        return shiftListOtherTimes;
    }

    public List<ShiftListOtherTime> findByShiftIdIn(List<Shift> shifts)
    {
        List<ShiftListOtherTime> shiftListOtherTimes = shiftListOtherTimeRepository.findByShiftIdIn(shifts);
        return shiftListOtherTimes;
    }

    public ShiftListOtherTime save(ShiftListOtherTime shiftListOtherTime)
    {
        return shiftListOtherTimeRepository.save(shiftListOtherTime);
    }

    public void deleteByShiftListOtherTime(ShiftListOtherTime shiftListOtherTime)
    {
        shiftListOtherTimeRepository.delete(shiftListOtherTime);
    }
}
