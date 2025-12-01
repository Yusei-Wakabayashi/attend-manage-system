package com.example.springboot.dto.change;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

// クラス変数を持たないただの変換ツールのためcomponent化
@Component
public class StringToLocalDateTime
{
    public LocalDateTime stringToLocalDateTime(String str)
    {
        // 受け取り形式で文字列からLocalDateTimeに変換、さらにLocalDateTimeで扱いたい形式で文字列に変換
        String beforeString = LocalDateTime.parse(str,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        // LocalDateTimeに変換
        return LocalDateTime.parse(beforeString,DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
    }
}
