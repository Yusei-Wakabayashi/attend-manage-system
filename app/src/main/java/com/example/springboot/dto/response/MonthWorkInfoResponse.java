package com.example.springboot.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MonthWorkInfoResponse
{
    private int status;
    private String workTime;
    private String lateness;
    private String leaveEarly;
    private String outing;
    private String overWork;
    private String absenceTime;
    private String specialTime;
    private String lateNightWorkTime;
    private String holidayWorkTime;
    private String paydHolidayTime;
}
