package com.example.springboot.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.model.Salt;
import com.example.springboot.repository.SaltRepository;

@Service
public class SaltService
{
    @Autowired
    SaltRepository saltRepository;

    public Salt getSaltById(Long id)
    {
        return saltRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("ソルトが見つかりません"));
    }

    public String save(Salt salt)
    {
        saltRepository.save(salt);
        return "ok";
    }

    @Transactional
    public void resetAllTables()
    {
        // データ削除
        saltRepository.deleteAll();
        // AUTO_INCREMENTリセット(順番はDELETEの後)
        saltRepository.resetAutoIncrement();
    }
}
