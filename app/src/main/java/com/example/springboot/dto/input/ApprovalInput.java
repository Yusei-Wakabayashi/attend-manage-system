package com.example.springboot.dto.input;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ApprovalInput
{
    private int requestId;
    private int requestType;
    private int approvalStatus;
    private String approvalComment;
    private String requestTime;
}
