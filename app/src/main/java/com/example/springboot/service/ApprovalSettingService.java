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
        return approvalSettings;
    }

    public List<ApprovalSetting> getApprovalSettingsByApprover(Role roleId)
    {
        List<ApprovalSetting> approvalSettings = approvalSettingRepository.findByApprovalId(roleId);
        return approvalSettings;
    }
    public String save(ApprovalSetting approvalSetting)
    {
        approvalSettingRepository.save(approvalSetting);
        return "ok";
    }
}
