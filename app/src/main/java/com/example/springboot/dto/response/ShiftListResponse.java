package com.example.springboot.dto.response;

import java.sql.Time;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
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