package com.example.springboot.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.model.LegalTime;
import com.example.springboot.repository.LegalTimeRepository;

@Service
public class LegalTimeService
{
    @Autowired
    private LegalTimeRepository legalTimeRepository;

    public LegalTime findFirstByOrderByBeginDesc()
    {
        return legalTimeRepository.findFirstByOrderByBeginDesc()
            .orElseThrow(() -> new RuntimeException("データが見つかりません"));
    }

    public LegalTime findLegalTimeByLegalTimeId(Long id)
    {
        return legalTimeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("データが見つかりません"));
    }

    public String save(LegalTime legalTime)
    {
        legalTimeRepository.save(legalTime);
        return "ok";
    }

    @Transactional
    public void resetAllTables()
    {
        legalTimeRepository.deleteAll();
        legalTimeRepository.resetAutoIncrement();
    }
}
