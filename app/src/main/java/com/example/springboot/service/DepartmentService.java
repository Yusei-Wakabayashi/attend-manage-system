package com.example.springboot.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.model.Department;
import com.example.springboot.repository.DepartmentRepository;

@Service
public class DepartmentService
{
    @Autowired
    DepartmentRepository departmentRepository;

    public Department getDepartmentById(Long id)
    {
        return departmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("部署が見つかりません"));
    }

    @Transactional
    public void resetAllTables()
    {
        departmentRepository.deleteAll();
        departmentRepository.resetAutoIncrement();
    }
}