package com.example.springboot.dto.response;

import java.util.List;

import com.example.springboot.json.ArrayResponseSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonSerialize(using = ArrayResponseSerializer.class)
public class ArrayResponse<T>
{
    private int status;
    private List<T> list;
    private String key;

    public ArrayResponse(int status, List<T> list, String key)
    {
        this.status = status;
        this.list = list;
        this.key = key;
    }
}
