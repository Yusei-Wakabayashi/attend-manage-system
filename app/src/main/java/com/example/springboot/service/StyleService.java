package com.example.springboot.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.model.Account;
import com.example.springboot.model.Style;
import com.example.springboot.repository.StyleRepository;

@Service
public class StyleService
{
    @Autowired
    StyleRepository styleRepository;

    @Autowired
    StylePlaceService stylePlaceService;
    
    public Style getStyleById(Long id)
    {
        return styleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("スタイルが見つかりません"));
    }

    public Style getStyleByAccountId(Long id)
    {
        Account account = new Account();
        account.setId(id);
        return styleRepository.findByAccountId(account)
            .orElseThrow(() -> new RuntimeException("スタイルが見つかりません"));
    }

    public Style getStyleByAccountId(Account id)
    {
        return styleRepository.findByAccountId(id)
            .orElseThrow(() -> new RuntimeException("スタイルが見つかりません"));
    }

    public String save(Style style)
    {
        styleRepository.save(style);
        return "ok";
    }

    @Transactional
    public void resetAllTables()
    {
        styleRepository.deleteAll();
        styleRepository.resetAutoIncrement();
    }
}
