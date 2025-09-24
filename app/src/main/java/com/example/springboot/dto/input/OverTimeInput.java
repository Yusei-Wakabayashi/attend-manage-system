package com.example.springboot.dto.input;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OverTimeInput
{
    private Long shiftId;
    private String beginOverTime;
    private String endOverTime;
    private String requestComment;
    private String requestDate;
}