package com.example.springboot.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RequestDetilOtherTimeResponse
{
    private int status;
    private int shiftId;
    private int typeId;
    private String beginOtherTime;
    private String endOtherTime;
    private String requestComment;
    private String requestDate;
    private int requestStatus;
    private Integer approverId;
    private String approverName;
    private String approverComment;
    private String approvalTime;
}
