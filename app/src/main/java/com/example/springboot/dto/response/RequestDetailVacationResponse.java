package com.example.springboot.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestDetailVacationResponse
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
