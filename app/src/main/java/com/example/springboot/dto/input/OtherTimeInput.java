package com.example.springboot.dto.input;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OtherTimeInput
{
    private Long shiftId;
    private int otherType;
    private String beginOtherTime;
    private String endOtherTime;
    private String requestComment;
    private String requestDate;
}
