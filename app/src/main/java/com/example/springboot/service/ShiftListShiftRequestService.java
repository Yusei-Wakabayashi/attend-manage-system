package com.example.springboot.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.model.Shift;
import com.example.springboot.model.ShiftListShiftRequest;
import com.example.springboot.model.ShiftRequest;
import com.example.springboot.repository.ShiftListShiftRequestRepository;

@Service
public class ShiftListShiftRequestService
{
    @Autowired
    private ShiftListShiftRequestRepository shiftListShiftRequestRepository;

    public ShiftListShiftRequest findByShiftId(Long id)
    {
        Shift shift = new Shift();
        shift.setShiftId(id);
        return shiftListShiftRequestRepository.findByShiftId(shift);
    }

    public ShiftListShiftRequest findByShiftId(Shift shift)
    {
        return shiftListShiftRequestRepository.findByShiftId(shift);
    }

    public ShiftListShiftRequest findByShiftRequest(ShiftRequest shiftRequest)
    {
        return shiftListShiftRequestRepository.findByShiftRequestId(shiftRequest);
    }

    public ShiftListShiftRequest findByShiftRequest(Long id)
    {
        ShiftRequest shiftRequest = new ShiftRequest();
        shiftRequest.setShiftRequestId(id);
        return shiftListShiftRequestRepository.findByShiftRequestId(shiftRequest);
    }

    public List<ShiftListShiftRequest> findByShiftIdIn(List<Shift> shifts)
    {
        List<ShiftListShiftRequest> shiftListShiftRequests = shiftListShiftRequestRepository.findByShiftIdIn(shifts);
        return shiftListShiftRequests;
    }

    @Transactional
    public ShiftListShiftRequest save(ShiftListShiftRequest shiftListShiftRequest)
    {
        return shiftListShiftRequestRepository.save(shiftListShiftRequest);
    }

    @Transactional
    public void deleteByShiftListShiftRequest(ShiftListShiftRequest shiftListShiftRequest)
    {
        shiftListShiftRequestRepository.delete(shiftListShiftRequest);
    }
}
