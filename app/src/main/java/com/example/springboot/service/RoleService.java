package com.example.springboot.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.model.Role;
import com.example.springboot.repository.RoleRepository;

@Service
public class RoleService
{
    @Autowired
    RoleRepository roleRepository;
    public Role getRoleById(Long id)
    {
        return roleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("役職が見つかりません"));
    }

    public String save(Role role)
    {
        roleRepository.save(role);
        return "ok";
    }
    @Transactional
    public void resetAllTables()
    {
        roleRepository.deleteAll();
        roleRepository.resetAutoIncrement();
    }
}
