package com.example.springboot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.model.ApprovalSetting;
import com.example.springboot.model.Role;
import com.example.springboot.repository.ApprovalSettingRepository;

@Service
public class ApprovalSettingService
{
    @Autowired
    ApprovalSettingRepository approvalSettingRepository;
    public List<ApprovalSetting> getApprovalSettings(Role roleId)
    {
        List<ApprovalSetting> approvalSettings = approvalSettingRepository.findByRoleId(roleId);
        if (approvalSettings.isEmpty())
        {
            throw new RuntimeException("対象が見つかりませんでした");
        }
        return approvalSettings;
    }
}
