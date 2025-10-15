package com.example.springboot.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserRequestListResponse
{
    private int id;
    private int accountId;
    private String accountName;
    private int type;
    private String requestDate;
    private int requestStatus;
}
