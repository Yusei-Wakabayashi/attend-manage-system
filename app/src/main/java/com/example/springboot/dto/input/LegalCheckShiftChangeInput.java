package com.example.springboot.dto.input;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LegalCheckShiftChangeInput
{
    private Long shiftId;
    private String beginWork;
    private String endWork;
    private String beginBreak;
    private String endBreak;
}
