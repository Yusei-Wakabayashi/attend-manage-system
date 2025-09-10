package com.example.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springboot.model.VacationType;

public interface VacationTypeRepository extends JpaRepository<VacationType, Long>
{

}