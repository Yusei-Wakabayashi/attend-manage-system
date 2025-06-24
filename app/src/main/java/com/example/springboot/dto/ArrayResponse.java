package com.example.springboot.dto;

import java.util.ArrayList;

import com.example.springboot.json.ArrayResponseSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = ArrayResponseSerializer.class)
public class ArrayResponse<T> {
    private int status;
    private ArrayList<T> list;
    private String key;

    public ArrayResponse(int status, ArrayList<T> list, String key) {
        this.status = status;
        this.list = list;
        this.key = key;
    }

    public int getStatus()
    {
        return status;
    }

    public ArrayList<T> getList()
    {
        return list;
    }

    public String getKey()
    {
        return key;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public void setList(ArrayList<T> list)
    {
        this.list = list;
    }

    public void setKey(String key)
    {
        this.key = key;
    }
}
