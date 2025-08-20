package com.example.springboot.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApproverListResponse
{
    private Long id;
    private String name;
    private String departmentName;
    private String roleName;
    public ApproverListResponse ()
    {
        
    }
    public ApproverListResponse(Long id, String name, String departmentName, String roleName)
    {
        this.id = id;
        this.name = name;
        this.departmentName = departmentName;
        this.roleName = roleName;
    }
}
