package com.example.springboot.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Response
{
    private int status;
    public Response(int status)
    {
        this.status = status;
    }
}