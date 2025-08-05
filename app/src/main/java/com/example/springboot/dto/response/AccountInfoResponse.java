package com.example.springboot.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountInfoResponse
{
    private int status;
    private String name;
    private String departmentName;
    private String roleName;
    private boolean admin; 
}
