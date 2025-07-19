package com.example.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.springboot.model.Salt;

public interface SaltRepository extends JpaRepository<Salt, Long>
{
    @Modifying
    @Query(value = "ALTER TABLE salts AUTO_INCREMENT = 1", nativeQuery = true)
    void resetAutoIncrement();
}
