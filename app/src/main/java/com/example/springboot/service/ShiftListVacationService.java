package com.example.springboot.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.model.Shift;
import com.example.springboot.model.ShiftListVacation;
import com.example.springboot.model.VacationRequest;
import com.example.springboot.repository.ShiftListVacationRepository;

@Service
public class ShiftListVacationService
{
    private final ShiftListVacationRepository shiftListVacationRepository;

    @Autowired
    public ShiftListVacationService
    (
        ShiftListVacationRepository shiftListVacationRepository
    )
    {
        this.shiftListVacationRepository = shiftListVacationRepository;
    }

    public ShiftListVacation findById(Long id)
    {
        return shiftListVacationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException(""));
    }

    public List<ShiftListVacation> findByShiftId(Shift shift)
    {
        List<ShiftListVacation> shiftListVacations = shiftListVacationRepository.findByShiftId(shift);
        return shiftListVacations;
    }

    public List<ShiftListVacation> findByShiftIdIn(List<Shift> shifts)
    {
        List<ShiftListVacation> shiftListVacations = shiftListVacationRepository.findByShiftIdIn(shifts);
        return shiftListVacations;
    }

    public ShiftListVacation findByVacation(VacationRequest vacationRequest)
    {
        return shiftListVacationRepository.findByVacationId(vacationRequest);
    }

    public ShiftListVacation findByVacation(Long id)
    {
        VacationRequest vacationRequest = new VacationRequest();
        vacationRequest.setVacationId(id);
        return shiftListVacationRepository.findByVacationId(vacationRequest);
    }

    @Transactional
    public ShiftListVacation save(ShiftListVacation shiftListVacation)
    {
        return shiftListVacationRepository.save(shiftListVacation);
    }

    @Transactional
    public void deleteByShiftListVacationId(ShiftListVacation shiftListVacation)
    {
        shiftListVacationRepository.delete(shiftListVacation);
    }
}