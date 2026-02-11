package com.example.springboot.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.dto.response.VacationTypeListResponse;
import com.example.springboot.model.Account;
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

    public List<VacationTypeListResponse> returnAllVacationTypeListResponses()
    {
        List<VacationTypeListResponse> vacationTypeListResponses = new ArrayList<VacationTypeListResponse>();
        List<VacationType> vacationTypes = findAll();
        for(VacationType vacationType : vacationTypes)
        {
            VacationTypeListResponse vacationTypeListResponse = new VacationTypeListResponse();
            vacationTypeListResponse.setVacationTypeId(vacationType.getVacationTypeId().intValue());
            vacationTypeListResponse.setVacationTypeName(vacationType.getVacationName());
            vacationTypeListResponses.add(vacationTypeListResponse);
        }
        return vacationTypeListResponses;
    }

    public String save(VacationType vacationType)
    {
        vacationTypeRepository.save(vacationType);
        return "ok";
    }

    @Transactional
    public void resetAllTables()
    {
        vacationTypeRepository.deleteAll();
        vacationTypeRepository.resetAutoIncrement();
    }

    public List<VacationType> findAll()
    {
        return vacationTypeRepository.findAll();
    }
}
