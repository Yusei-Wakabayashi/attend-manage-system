package com.example.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.springboot.model.Account;
import com.example.springboot.model.Shift;

public interface ShiftRepository extends JpaRepository<Shift, Long>
{
    List<Shift> findByAccountId(Account id);
    @Modifying
    @Query(value = "ALTER TABLE shift_lists AUTO_INCREMENT = 1", nativeQuery = true)
    void resetAutoIncrement();
}
