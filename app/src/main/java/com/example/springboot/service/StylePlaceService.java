package com.example.springboot.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.dto.AllStyleListResponse;
import com.example.springboot.model.StylePlace;
import com.example.springboot.repository.StylePlaceRepository;

@Service
public class StylePlaceService
{
    @Autowired
    StylePlaceRepository stylePlaceRepository;

    public StylePlace getStylePlaceById(Long id)
    {
        return stylePlaceRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("勤怠場所が見つかりません"));
    }

    public String save(StylePlace stylePlace)
    {
        stylePlaceRepository.save(stylePlace);
        return "ok";
    }

    public List<StylePlace> getAll()
    {
        return stylePlaceRepository.findAll();
    }

    public List<AllStyleListResponse> getStyleList()
    {
        List<StylePlace> stylePlaces = stylePlaceRepository.findAll();
        List<AllStyleListResponse> styleListResponse = new ArrayList();
        for(StylePlace stylePlace: stylePlaces)
        {
            AllStyleListResponse allStyleListResponse = new AllStyleListResponse
            (
                stylePlace.getId(),
                stylePlace.getName()
            );
            styleListResponse.add(allStyleListResponse);
        }
        return styleListResponse;
    }


    @Transactional
    public void resetAllTables()
    {
        stylePlaceRepository.deleteAll();
        stylePlaceRepository.resetAutoIncrement();
    }
}
