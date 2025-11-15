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

    public Department findDepartmentById(Long id)
    {
        return departmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("部署が見つかりません"));
    }

    public String save(Department department)
    {
        departmentRepository.save(department);
        return "ok";
    }

    @Transactional
    public void resetAllTables()
    {
        departmentRepository.deleteAll();
        departmentRepository.resetAutoIncrement();
    }
}