package com.example.springboot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.model.VacationType;
import com.example.springboot.repository.VacationTypeRepository;

@Service
public class VacationTypeService
{
    @Autowired
    private VacationTypeRepository vacationTypeRepository;

    public VacationType findById(Long id)
    {
        return vacationTypeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("休暇種類が見つかりません"));
    }

    public List<VacationType> findAll()
    {
        return vacationTypeRepository.findAll();
    }
}
