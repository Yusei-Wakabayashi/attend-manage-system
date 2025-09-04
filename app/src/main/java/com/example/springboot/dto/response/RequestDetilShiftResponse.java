package com.example.springboot.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestDetilShiftResponse
{
    private int status;
    private String beginWork;
    private String endWork;
    private String beginBreak;
    private String endBreak;
    private String requestComment;
    private String requestDate;
    private int requestStatus;
    private int approverId;
    private String approverName;
    private String approverComment;
    private String approvalTime;
}
