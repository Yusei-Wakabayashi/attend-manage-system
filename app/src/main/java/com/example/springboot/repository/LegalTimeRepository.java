package com.example.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.springboot.model.LegalTime;

public interface LegalTimeRepository extends JpaRepository<LegalTime, Long>
{
    // orderbyで最大(最新)のものを一件取得
    LegalTime findFirstByOrderByBeginDesc();
    @Modifying
    @Query(value = "ALTER TABLE legal_times AUTO_INCREMENT = 1", nativeQuery = true)
    void resetAutoIncrement();
}