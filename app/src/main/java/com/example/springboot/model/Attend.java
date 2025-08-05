package com.example.springboot.model;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="attendance_lists")
public class Attend {
    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "attendance_id", nullable = false, length = 20)
    private Long attendanceId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "account_id", nullable = false)
    private Account accountId;
    @Column(name = "begin_work", nullable = false)
    private LocalDateTime beginWork;
    @Column(name = "end_work", nullable = false)
    private LocalDateTime endWork;
    @Column(name = "begin_break", nullable = false)
    private LocalDateTime beginBreak;
    @Column(name = "end_break", nullable = false)
    private LocalDateTime endBreak;
    @Column(name = "lateness", nullable = false)
    private Time lateness;
    @Column(name = "leave_early", nullable = false)
    private Time leaveEarly;
    @Column(name = "outing", nullable = false)
    private Time outing;
    @Column(name = "work_time", nullable = false)
    private Time workTime;
    @Column(name = "break_time", nullable = false)
    private Time breakTime;
    @Column(name = "over_work", nullable = false)
    private Time overWork;
    @Column(name = "holiday_work", nullable = false)
    private Time holidayWork;
    @Column(name = "late_night_work", nullable = false)
    private Time lateNightWork;
    // コンストラクタ定義
    public Attend()
    {
        
    }

    public Attend processLine(String line)
    {
        Attend attend = new Attend();
        String[] arrayLine = line.split(",");
        attend.setAttendanceId(Long.valueOf(arrayLine[0]));
        attend.setBeginWork(LocalDateTime.parse(arrayLine[1], DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        attend.setEndWork(LocalDateTime.parse(arrayLine[2], DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        attend.setBeginBreak(LocalDateTime.parse(arrayLine[3], DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        attend.setEndBreak(LocalDateTime.parse(arrayLine[4], DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        attend.setWorkTime(Time.valueOf(arrayLine[5]));
        attend.setBreakTime(Time.valueOf(arrayLine[6]));
        attend.setLateness(Time.valueOf(arrayLine[7]));
        attend.setLeaveEarly(Time.valueOf(arrayLine[8]));
        attend.setOuting(Time.valueOf(arrayLine[9]));
        attend.setOverWork(Time.valueOf(arrayLine[10]));
        attend.setHolidayWork(Time.valueOf(arrayLine[11]));
        attend.setLateNightWork(Time.valueOf(arrayLine[12]));
        return attend;
    }
}
