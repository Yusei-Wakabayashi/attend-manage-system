package com.example.springboot.dao;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Shift {
    private int id;
    private LocalDateTime workStart;
    private LocalDateTime workEnd;
    private LocalDateTime breakStart;
    private LocalDateTime breakEnd;
    private int lateness;
    private int leaveEarly;
    private int outing;
    private int overWork;
    // コンストラクタ定義
    public Shift()
    {
        
    }
    // コンストラクタオーバーロード
    public Shift(int id, LocalDateTime workStart, LocalDateTime workEnd, LocalDateTime breakStart,LocalDateTime breakEnd,
    int lateness, int leaveEarly, int outing, int overWork) {
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
        int id = Integer.valueOf(arrayLine[0]);
        LocalDateTime workStart = LocalDateTime.parse(arrayLine[1], DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm"));
        LocalDateTime workEnd = LocalDateTime.parse(arrayLine[2], DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm"));
        LocalDateTime breakStart = LocalDateTime.parse(arrayLine[3], DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm"));
        LocalDateTime breakEnd = LocalDateTime.parse(arrayLine[4], DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm"));
        int lateness = Integer.valueOf(arrayLine[5]);
        int leaveEarly = Integer.valueOf(arrayLine[6]);
        int outing = Integer.valueOf(arrayLine[7]);
        int overWork = Integer.valueOf(arrayLine[8]);

        Shift shift = new Shift(id, workStart, workEnd, breakStart, breakEnd, lateness, leaveEarly, outing, overWork);
        return shift;
    }

    // Getter, Setter
    public int getId() { return id; }
    public LocalDateTime getWorkStart() { return workStart; }
    public LocalDateTime getWorkEnd() { return workEnd; }
    public LocalDateTime getBreakStart() { return breakStart; }
    public LocalDateTime getBreakEnd() { return breakEnd; }
    public int getLateness() { return lateness; }
    public int getLeaveEarly() { return leaveEarly; }
    public int getOuting() { return outing; }
    public int getOverWork() { return overWork; }


    public void setId(int id) { this.id = id; }
    public void setWorkStart(LocalDateTime workStart) { this.workStart = workStart; }
    public void setWorkEnd(LocalDateTime workEnd) { this.workEnd = workEnd; }
    public void setBreakStart(LocalDateTime breakStart) { this.breakStart = breakStart; }
    public void setBreakEnd(LocalDateTime breakEnd) { this.breakEnd = breakEnd; }
    public void setLateness(int lateness) { this.lateness = lateness;}
    public void setLeaveEarly(int lateness) { this.lateness = lateness;}
    public void setOuting(int lateness) { this.lateness = lateness;}
    public void setOverWork(int lateness) { this.lateness = lateness;}
}
