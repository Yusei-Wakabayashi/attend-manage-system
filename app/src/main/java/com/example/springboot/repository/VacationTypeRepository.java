package com.example.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.springboot.model.VacationType;

public interface VacationTypeRepository extends JpaRepository<VacationType, Long>
{
    @Modifying
    @Query(value = "ALTER TABLE accounts AUTO_INCREMENT = 1", nativeQuery = true)
    void resetAutoIncrement();
}