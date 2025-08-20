package com.example.springboot.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.model.Attend;
import com.example.springboot.repository.AttendRepository;

@Service
public class AttendService
{
    @Autowired
    AttendRepository attendRepository;

    public Attend getAttendById(Long id)
    {
        return attendRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("出勤簿が見つかりません"));
    }

    public String save(Attend attend)
    {
        attendRepository.save(attend);
        return "ok";
    }

    @Transactional
    public void resetAllTables()
    {
        attendRepository.deleteAll();
        attendRepository.resetAutoIncrement();
    }
}
