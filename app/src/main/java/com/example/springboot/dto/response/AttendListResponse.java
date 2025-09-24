package com.example.springboot.dto.response;

import java.sql.Time;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttendListResponse
{
    private Long id;
    private String beginWork;
    private String endWork;
    private String beginBreak;
    private String endBreak;
    private Time workTime;
    private Time breakTime;
    private Time lateness;
    private Time leaveEarly;
    private Time outing;
    private Time overWork;
    private Time holidayWork;
    private Time lateNightWork;
    private Time vacationTime;
    private Time absenceTime;
}