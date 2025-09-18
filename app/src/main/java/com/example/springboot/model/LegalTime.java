package com.example.springboot.model;

import java.sql.Time;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "legal_times")
public class LegalTime
{
    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "legal_time_id", nullable = false, length = 20)
    private Long legalTimeId;
    @Column(name = "begin", nullable = false)
    private LocalDateTime begin;
    @Column(name = "end", nullable = true)
    private LocalDateTime end;
    @Column(name = "schedule_work_time", nullable = false)
    private Time scheduleWorkTime;
    @Column(name = "weekly_work_time", nullable = false)
    private Time weeklyWorkTime;
    @Column(name = "monthly_over_work_time", nullable = false)
    private Time monthlyOverWorkTime;
    @Column(name = "year_over_work_time", nullable = false)
    private Time yearOverWorkTime;
    @Column(name = "max_over_work_time", nullable = false)
    private Time maxOverWorkTime;
    @Column(name = "monthly_over_work_average", nullable = false)
    private Time monthlyOverWorkAverage;
    @Column(name = "late_night_work_begin", nullable = false)
    private Time lateNightWorkBegin;
    @Column(name = "late_night_work_end", nullable = false)
    private Time lateNightWorkEnd;
    @Column(name = "schedule_break_time", nullable = false)
    private Time scheduleBreakTime;
    @Column(name = "weekly_holiday", nullable = false, length = 10)
    private int weeklyHoliday;
}
