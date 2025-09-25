package com.example.springboot.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ApproverResponse
{
    private int status;
    private int approverId;
    private String approverName;
    private String approverDepartment;
    private String approverRole;
}
