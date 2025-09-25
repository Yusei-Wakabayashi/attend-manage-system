package com.example.springboot.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.springboot.model.Account;
import com.example.springboot.model.Style;

public interface StyleRepository extends JpaRepository<Style, Long>
{
    Optional<Style> findByAccountId(Account id);
    @Modifying
    @Query(value = "ALTER TABLE styles AUTO_INCREMENT = 1", nativeQuery = true)
    void resetAutoIncrement();
}