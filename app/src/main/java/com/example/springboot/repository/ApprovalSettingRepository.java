package com.example.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springboot.model.ApprovalSetting;
import com.example.springboot.model.Role;

public interface ApprovalSettingRepository extends JpaRepository<ApprovalSetting, Long>
{
    List<ApprovalSetting> findByRoleId(Role id);
    List<ApprovalSetting> findByApprovalId(Role id);
}
