package com.example.springboot.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.example.springboot.BaseTimeEntity;

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
public class LegalTime extends BaseTimeEntity
{
    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "legal_time_id", nullable = false, length = 20)
    private Long legalTimeId;
    @Column(name = "begin", nullable = false)
    private LocalDateTime begin;
    @Column(name = "end", nullable = true)
    private LocalDateTime end;
    @Column(name = "schedule_work_time", nullable = false, length = 255)
    private String scheduleWorkTime;
    @Column(name = "weekly_work_time", nullable = false, length = 255)
    private String weeklyWorkTime;
    @Column(name = "monthly_over_work_time", nullable = false, length = 255)
    private String monthlyOverWorkTime;
    @Column(name = "year_over_work_time", nullable = false, length = 255)
    private String yearOverWorkTime;
    @Column(name = "max_over_work_time", nullable = false, length = 255)
    private String maxOverWorkTime;
    @Column(name = "monthly_over_work_average", nullable = false, length = 255)
    private String monthlyOverWorkAverage;
    @Column(name = "late_night_work_begin", nullable = false, length = 255)
    private String lateNightWorkBegin;
    @Column(name = "late_night_work_end", nullable = false, length = 255)
    private String lateNightWorkEnd;
    @Column(name = "schedule_break_time", nullable = false, length = 255)
    private String scheduleBreakTime;
    @Column(name = "weekly_holiday", nullable = false, length = 10)
    private int weeklyHoliday;
}
