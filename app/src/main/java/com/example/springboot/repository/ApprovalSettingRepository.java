package com.example.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.springboot.model.ApprovalSetting;
import com.example.springboot.model.Role;

public interface ApprovalSettingRepository extends JpaRepository<ApprovalSetting, Long>
{
    ApprovalSetting findByRoleIdAndApprovalId(Role accountRoleId, Role approverRoleId);
    List<ApprovalSetting> findByRoleId(Role id);
    List<ApprovalSetting> findByApprovalId(Role id);
    @Modifying
    @Query(value = "ALTER TABLE approval_settings AUTO_INCREMENT = 1", nativeQuery = true)
    void resetAutoIncrement();
}
