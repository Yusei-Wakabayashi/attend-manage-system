package com.example.springboot.dto;

import java.util.List;

import com.example.springboot.json.ArrayResponseSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonSerialize(using = ArrayResponseSerializer.class)
public class ArrayResponse<T>
{
    private int status;
    private List<T> list;
    private String key;

    public ArrayResponse(int status, List<T> list, String key) {
        this.status = status;
        this.list = list;
        this.key = key;
    }
}
