package com.example.springboot.dto.change;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeToString
{
    public String localDateTimeToString(LocalDateTime localDateTime)
    {
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "T" + localDateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }
}
