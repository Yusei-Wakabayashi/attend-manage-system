package com.example.springboot.dto.input;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VacationInput
{
    private Long shiftId;
    private int vacationType;
    private String beginVacation;
    private String endVacation;
    private String requestComment;
    private String requestDate;
}