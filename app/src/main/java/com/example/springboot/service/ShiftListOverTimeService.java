package com.example.springboot.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.model.OverTimeRequest;
import com.example.springboot.model.Shift;
import com.example.springboot.model.ShiftListOverTime;
import com.example.springboot.repository.ShiftListOverTimeRepository;

@Service
public class ShiftListOverTimeService
{
    @Autowired
    private ShiftListOverTimeRepository shiftListOverTimeRepository;

    public ShiftListOverTime findByOverTime(OverTimeRequest overTimeRequest)
    {
        return shiftListOverTimeRepository.findByOverTimeId(overTimeRequest);
    }

    public ShiftListOverTime findByOverTime(Long id)
    {
        OverTimeRequest overTimeRequest = new OverTimeRequest();
        overTimeRequest.setOverTimeId(id);
        return shiftListOverTimeRepository.findByOverTimeId(overTimeRequest);
    }

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

    @Transactional
    public ShiftListOverTime save(ShiftListOverTime shiftListOverTime)
    {
        return shiftListOverTimeRepository.save(shiftListOverTime);
    }

    @Transactional
    public void deleteByShiftListOverTime(ShiftListOverTime shiftListOverTime)
    {
        shiftListOverTimeRepository.delete(shiftListOverTime);
    }
}
