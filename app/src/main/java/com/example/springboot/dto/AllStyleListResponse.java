package com.example.springboot.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AllStyleListResponse
{
    private Long id;
    private String name;
    // 引数なしコンストラクタ
    public AllStyleListResponse()
    {

    }
    public AllStyleListResponse(Long id, String name)
    {
        this.id = id;
        this.name = name;
    }
}
