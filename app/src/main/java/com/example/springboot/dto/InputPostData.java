package com.example.springboot.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InputPostData<T>
{
    private T data;
    private String text;
}