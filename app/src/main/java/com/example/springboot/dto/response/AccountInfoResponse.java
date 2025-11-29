package com.example.springboot.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountInfoResponse
{
    private int status;
    private String name;
    private String departmentName;
    private String roleName;
    private boolean admin; 
}
