package com.example.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.springboot.model.Attend;

public interface AttendRepository extends JpaRepository<Attend, Long>
{
    @Modifying
    @Query(value = "ALTER TABLE attend_lists AUTO_INCREMENT = 1", nativeQuery = true)
    void resetAutoIncrement();
}
