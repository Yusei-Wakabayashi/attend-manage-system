package com.example.springboot.dto;

public class ShiftRequest { 
    private int year;
    private int month;
    // 入力された年のゲッター
    public int getYear()
    {
        return this.year;
    }
    // 入力された月のゲッター
    public int getMonth()
    {
        return this.month;
    }
    //
    public void setYear(int year)
    {
        this.year = year;
    }
    //
    public void setMonth(int month)
    {
        this.month = month;
    }
}
