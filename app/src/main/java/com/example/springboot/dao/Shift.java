package com.example.springboot.dao;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Shift
{
    private Long id;
    private LocalDateTime workStart;
    private LocalDateTime workEnd;
    private LocalDateTime breakStart;
    private LocalDateTime breakEnd;
    private Time lateness;
    private Time leaveEarly;
    private Time outing;
    private Time overWork;
    // コンストラクタ定義
    public Shift()
    {
        
    }
    // コンストラクタオーバーロード
    public Shift(Long id, LocalDateTime workStart, LocalDateTime workEnd, LocalDateTime breakStart,LocalDateTime breakEnd,
    Time lateness, Time leaveEarly, Time outing, Time overWork) {
        this.id = id;
        this.workStart = workStart;
        this.workEnd = workEnd;
        this.breakStart = breakStart;
        this.breakEnd = breakEnd;
        this.lateness = lateness;
        this.leaveEarly = leaveEarly;
        this.outing = outing;
        this.overWork = overWork;
    }
    public Shift processLine(String line)
    {
        String[] arrayLine = line.split(",");
        Long id = Long.valueOf(arrayLine[0]);
        LocalDateTime workStart = LocalDateTime.parse(arrayLine[1], DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm"));
        LocalDateTime workEnd = LocalDateTime.parse(arrayLine[2], DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm"));
        LocalDateTime breakStart = LocalDateTime.parse(arrayLine[3], DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm"));
        LocalDateTime breakEnd = LocalDateTime.parse(arrayLine[4], DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm"));
        Time lateness = Time.valueOf(arrayLine[5]);
        Time leaveEarly = Time.valueOf(arrayLine[6]);
        Time outing = Time.valueOf(arrayLine[7]);
        Time overWork = Time.valueOf(arrayLine[8]);

        Shift shift = new Shift(id, workStart, workEnd, breakStart, breakEnd, lateness, leaveEarly, outing, overWork);
        return shift;
    }
}
