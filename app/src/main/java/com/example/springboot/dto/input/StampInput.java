package com.example.springboot.dto.input;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StampInput
{
    private Long shiftId;
    private String beginWork;
    private String endWork;
    private String beginBreak;
    private String endBreak;
    private String requestComment;
    private String requestDate;
}
