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
import lombok.AllArgsConstructor;

@Getter
@Setter
@Entity
@AllArgsConstructor
@Table(name="shift_lists")
public class Shift
{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "shift_id")
    private Long shiftId;
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
    @Column(name = "over_work", nullable = false)
    private Time overWork;

    public Shift()
    {

    }

    public Shift processLine(String line)
    {
        Shift shift = new Shift();
        String[] arrayLine = line.split(",");
        shift.setShiftId(Long.valueOf(arrayLine[0]));
        shift.setBeginWork(LocalDateTime.parse(arrayLine[1], DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        shift.setEndWork(LocalDateTime.parse(arrayLine[2], DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        shift.setBeginBreak(LocalDateTime.parse(arrayLine[3], DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        shift.setEndBreak(LocalDateTime.parse(arrayLine[4], DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        shift.setLateness(Time.valueOf(arrayLine[5]));
        shift.setLeaveEarly(Time.valueOf(arrayLine[6]));
        shift.setOuting(Time.valueOf(arrayLine[7]));
        shift.setOverWork(Time.valueOf(arrayLine[8]));
        return shift;
    }
}
