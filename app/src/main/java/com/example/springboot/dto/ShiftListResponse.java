package com.example.springboot.dto;

import java.sql.Time;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShiftListResponse
{
    private Long id;
    private String beginWork;
    private String endWork;
    private String beginBreak;
    private String endBreak;
    private Time lateness;
    private Time leaveEarly;
    private Time outing;
    private Time overWork;

    public ShiftListResponse()
    {
        
    }
}
