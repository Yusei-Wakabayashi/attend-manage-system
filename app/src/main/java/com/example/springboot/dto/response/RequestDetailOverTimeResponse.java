package com.example.springboot.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestDetailOverTimeResponse
{
    private int status;
    private int shiftId;
    private String beginOverWork;
    private String endOverWork;
    private String requestComment;
    private String requestDate;
    private int requestStatus;
    private Integer approverId;
    private String approverName;
    private String approverComment;
    private String approvalTime;
}
