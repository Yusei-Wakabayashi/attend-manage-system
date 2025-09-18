package com.example.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springboot.model.TimeCard;

public interface TimeCardRepository extends JpaRepository<TimeCard, Long>
{
    
}
