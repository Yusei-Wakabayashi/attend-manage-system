package com.example.springboot.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.springboot.model.Account;
import com.example.springboot.model.Role;

public interface AccountRepository extends JpaRepository<Account, Long>
{
    Optional<Account> findByUsername(String name);
    List<Account> findByRoleId(Role roleId); // 単一のroleidを元に同じroleidを持つレコードを取得する
    List<Account> findByRoleIdIn(List<Role> roles); // 複数のroleidを元に同じroleidを持つレコードを取得する
    @Modifying
    @Query(value = "ALTER TABLE accounts AUTO_INCREMENT = 1", nativeQuery = true)
    void resetAutoIncrement();
}