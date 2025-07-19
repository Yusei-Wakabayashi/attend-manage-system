package com.example.springboot.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.model.Style;
import com.example.springboot.repository.StyleRepository;

@Service
public class StyleService
{
    @Autowired
    StyleRepository styleRepository;
    
    public Style getStyleById(Long id)
    {
        return styleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("ソルトが見つかりません"));
    }

    @Transactional
    public void resetAllTables()
    {
        styleRepository.deleteAll();
        styleRepository.resetAutoIncrement();
    }
}
