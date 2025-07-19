package com.example.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.springboot.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long>
{
    @Modifying
    @Query(value = "ALTER TABLE roles AUTO_INCREMENT = 1", nativeQuery = true)
    void resetAutoIncrement();
}
