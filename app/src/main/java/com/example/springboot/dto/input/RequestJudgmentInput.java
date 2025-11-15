package com.example.springboot.dto.input;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RequestJudgmentInput
{
    private Long requestId;
    private int requestType;
    private String approvalComment;
    private String requestTime;
}
