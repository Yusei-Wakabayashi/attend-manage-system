package com.example.springboot.dto.input;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ShiftInput
{
    private String beginWork;
    private String endWork;
    private String beginBreak;
    private String endBreak;
    private String requestComment;
    private String requestDate;
}
