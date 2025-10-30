package com.example.springboot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.model.Shift;
import com.example.springboot.model.ShiftListVacation;
import com.example.springboot.repository.ShiftListVacationRepository;

@Service
public class ShiftListVacationService
{
    @Autowired
    private ShiftListVacationRepository shiftListVacationRepository;
    public List<ShiftListVacation> findByShiftIdIn(List<Shift> shifts)
    {
        List<ShiftListVacation> shiftListVacations = shiftListVacationRepository.findByShiftIdIn(shifts);
        return shiftListVacations;
    }
}