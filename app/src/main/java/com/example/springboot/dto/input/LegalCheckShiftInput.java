package com.example.springboot.dto.input;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LegalCheckShiftInput
{
    private String beginWork;
    private String endWork;
    private String beginBreak;
    private String endBreak;
}
