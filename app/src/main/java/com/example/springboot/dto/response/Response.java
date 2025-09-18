package com.example.springboot.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Response
{
    private int status;
    public Response(int status)
    {
        this.status = status;
    }
}