package com.example.springboot.service;

import java.util.Objects;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.model.Account;
import com.example.springboot.model.Style;
import com.example.springboot.model.StylePlace;
import com.example.springboot.repository.StyleRepository;

@Service
public class StyleService
{
    @Autowired
    StyleRepository styleRepository;

    @Autowired
    StylePlaceService stylePlaceService;

    @Autowired
    AccountService accountService;

    @Transactional
    public int updateStyle(Account account , Long newStylePlaceId)
    {
        Style style = findStyleByAccountId(account);
        StylePlace newStylePlace = stylePlaceService.findStylePlaceById(newStylePlaceId);
        style.setStylePlaceId(newStylePlace);
        Style resultStyle = save(style);
        if(Objects.isNull(resultStyle) || Objects.isNull(resultStyle.getStyleId()))
        {
            return 3;
        }
        return 1;
    }
    
    public Style findStyleById(Long id)
    {
        return styleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("スタイルが見つかりません"));
    }

    public Style findStyleByAccountId(Long id)
    {
        Account account = new Account();
        account.setId(id);
        return styleRepository.findByAccountId(account)
            .orElseThrow(() -> new RuntimeException("スタイルが見つかりません"));
    }

    public Style findStyleByAccountId(Account id)
    {
        return styleRepository.findByAccountId(id)
            .orElseThrow(() -> new RuntimeException("スタイルが見つかりません"));
    }

    public Style save(Style style)
    {
        return styleRepository.save(style);
    }

    @Transactional
    public void resetAllTables()
    {
        styleRepository.deleteAll();
        styleRepository.resetAutoIncrement();
    }
}
