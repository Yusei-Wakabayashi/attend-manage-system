package com.example.springboot.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaydHolidayHistoryListResponse
{
    private String type;
    private String date;
    private String time;
}
