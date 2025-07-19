package com.example.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.springboot.model.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long>
{
    @Modifying
    @Query(value = "ALTER TABLE departments AUTO_INCREMENT = 1", nativeQuery = true)
    void resetAutoIncrement();
}
