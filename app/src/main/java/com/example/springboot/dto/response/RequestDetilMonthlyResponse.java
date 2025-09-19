package com.example.springboot.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RequestDetilMonthlyResponse
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
    private int approverId;
    private String approverName;
    private String approverComment;
    private String approvalTime;
}
