package com.example.springboot.dto.input;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MonthlyInput
{
    private int year;
    private int month;
    private String requestComment;
    private String requestDate;
}
