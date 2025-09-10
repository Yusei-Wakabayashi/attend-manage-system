package com.example.springboot.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RequestDetilVacationResponse
{
    int status;
    int shiftId;
    int vacationType;
    String beginVacation;
    String endVacation;
    String requestComment;
    String requestDate;
    int requestStatus;
    int approverId;
    String approverName;
    String approverComment;
    String approvalTime;
}
