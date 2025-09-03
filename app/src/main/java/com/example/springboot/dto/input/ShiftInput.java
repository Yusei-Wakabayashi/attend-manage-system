package com.example.springboot.dto.input;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ShiftInput
{
    private String beginWork;
    private String endWork;
    private String beginBreak;
    private String endBreak;
    private String requestComment;
    private String requestDate;
}
