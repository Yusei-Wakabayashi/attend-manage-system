package com.example.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.springboot.model.Account;
import com.example.springboot.model.AccountApprover;

public interface AccountApproverRepository extends JpaRepository<AccountApprover, Long>
{
    AccountApprover findByAccountId(Account id);
    AccountApprover findByAccountIdAndApproverId(Account accountId, Account approverId);
    List<AccountApprover> findByApproverId(Account id);
    @Modifying
    @Query(value = "ALTER TABLE account_approvers AUTO_INCREMENT = 1", nativeQuery = true)
    void resetAutoIncrement();
}
