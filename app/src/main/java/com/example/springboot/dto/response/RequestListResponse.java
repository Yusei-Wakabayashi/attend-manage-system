package com.example.springboot.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RequestListResponse
{
    private int id;
    private int requestType;
    private String requestDate;
    private int requestStatus;
}
