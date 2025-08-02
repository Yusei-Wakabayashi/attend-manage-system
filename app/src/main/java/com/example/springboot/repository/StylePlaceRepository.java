package com.example.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.springboot.model.StylePlace;

public interface StylePlaceRepository extends JpaRepository<StylePlace, Long>
{
    @Modifying
    @Query(value = "ALTER TABLE style_places AUTO_INCREMENT = 1", nativeQuery = true)
    void resetAutoIncrement();
}
