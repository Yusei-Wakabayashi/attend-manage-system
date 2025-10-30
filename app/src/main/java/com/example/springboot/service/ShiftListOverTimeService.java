package com.example.springboot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.model.Shift;
import com.example.springboot.model.ShiftListOverTime;
import com.example.springboot.repository.ShiftListOverTimeRepository;

@Service
public class ShiftListOverTimeService
{
    @Autowired
    private ShiftListOverTimeRepository shiftListOverTimeRepository;

    public List<ShiftListOverTime> findByShiftId(Long shiftId)
    {
        Shift shift = new Shift();
        shift.setShiftId(shiftId);
        List<ShiftListOverTime> shiftListOverTimes = shiftListOverTimeRepository.findByShiftListId(shift);
        return shiftListOverTimes;
    }

    public List<ShiftListOverTime> findByShiftId(Shift shift)
    {
        List<ShiftListOverTime> shiftListOverTimes = shiftListOverTimeRepository.findByShiftListId(shift);
        return shiftListOverTimes;
    }
    public List<ShiftListOverTime> findByShiftIdIn(List<Shift> shifts)
    {
        List<ShiftListOverTime> shiftListOverTimes = shiftListOverTimeRepository.findByShiftListIdIn(shifts);
        return shiftListOverTimes;
    }
}
