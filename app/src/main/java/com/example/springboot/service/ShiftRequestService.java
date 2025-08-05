package com.example.springboot.service;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.model.Account;
import com.example.springboot.model.ShiftRequest;
import com.example.springboot.repository.ShiftRequestRepository;

@Service
public class ShiftRequestService
{
    @Autowired
    private ShiftRequestRepository shiftRequestRepository;

    public List<ShiftRequest> getAllShiftRequest()
    {
        return shiftRequestRepository.findAll();
    }

    public List<ShiftRequest> getAccountIdAndBeginWorkBetween(Account accountId, LocalDateTime beginWork, LocalDateTime endWork)
    {
        return shiftRequestRepository.findByAccountIdAndBeginWorkBetween(accountId, beginWork, endWork);
    }

    public String save(ShiftRequest shiftRequest)
    {
        shiftRequestRepository.save(shiftRequest);
        return "ok";
    }

    @Transactional
    public void resetAllTables()
    {
        shiftRequestRepository.deleteAll();
        shiftRequestRepository.resetAutoIncrement();
    }
}