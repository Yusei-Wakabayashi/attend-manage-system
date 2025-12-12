package com.example.springboot.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestDetailMonthlyResponse
{
    private int status;
    private String workTime;
    private String overTime;
    private String earlyTime;
    private String leavingTime;
    private String outingTime;
    private String absenceTime;
    private String paydHolidayTime;
    private String specialTime;
    private String holidayWorkTime;
    private String lateNightWorkTime;
    private int year;
    private int month;
    private String requestComment;
    private String requestDate;
    private int requestStatus;
    private Integer approverId;
    private String approverName;
    private String approverComment;
    private String approvalTime;
}
