package com.example.springboot.service;

import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.dto.response.ShiftListResponse;
import com.example.springboot.model.Account;
import com.example.springboot.model.Shift;
import com.example.springboot.repository.ShiftRepository;

@Service
public class ShiftService
{
    @Autowired
    private ShiftRepository shiftRepository;
    public List<Shift> findByAccountId(Long id)
    {
        Account accountId = new Account();
        accountId.setId(id);
        return shiftRepository.findByAccountId(accountId);
    }
    public List<Shift> findByAccountId(Account id)
    {
        return shiftRepository.findByAccountId(id);
    }

    public ShiftListResponse shiftToShiftListResponse(Shift shift)
    {
        ShiftListResponse shiftListResponse = new ShiftListResponse();
        shiftListResponse.setId(shift.getShiftId());
        shiftListResponse.setBeginWork(shift.getBeginWork().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + shift.getBeginWork().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        shiftListResponse.setEndWork(shift.getEndWork().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + shift.getEndWork().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        shiftListResponse.setBeginBreak(shift.getBeginBreak().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + shift.getBeginBreak().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        shiftListResponse.setEndBreak(shift.getEndBreak().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + shift.getEndBreak().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        shiftListResponse.setLateness(shift.getLateness());
        shiftListResponse.setLeaveEarly(shift.getLeaveEarly());
        shiftListResponse.setOuting(shift.getOuting());
        shiftListResponse.setOverWork(shift.getOverWork());
        return shiftListResponse;
    }
    @Transactional
    public void resetAllTables()
    {
        shiftRepository.deleteAll();
        shiftRepository.resetAutoIncrement();
    }

    public void init()
    {
        
    }
}
