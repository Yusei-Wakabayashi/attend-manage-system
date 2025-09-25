package com.example.springboot.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VacationTypeListResponse
{
    private int vacationTypeId;
    private String vacationTypeName;
}