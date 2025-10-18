package com.example.springboot.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.example.springboot.BaseTimeEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "month_requests")
public class MonthlyRequest extends BaseTimeEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "month_request_id", nullable = false, length = 20)
    private Long monthRequestId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "account_id", nullable = false)
    private Account accountId;
    @Column(name = "work_time", nullable = false)
    private String workTime;
    @Column(name = "over_time", nullable = false)
    private String overTime;
    @Column(name = "early_time", nullable = false)
    private String earlyTime;
    @Column(name = "leaving_time", nullable = false)
    private String leavingTime;
    @Column(name = "outing_time", nullable = false)
    private String outingTime;
    @Column(name = "absence_time", nullable = false)
    private String absenceTime;
    @Column(name = "payd_holiday_time", nullable = false)
    private String paydHolidayTime;
    @Column(name = "special_time", nullable = false)
    private String specialTime;
    @Column(name = "holiday_work_time", nullable = false)
    private String holidayWorkTime;
    @Column(name = "late_night_work_time", nullable = false)
    private String lateNightWorkTime;
    @Column(name = "year", nullable = false, length = 10)
    private int year;
    @Column(name = "month", nullable = false, length = 10)
    private int month;
    @Column(name = "request_comment", nullable = true, length = 255)
    private String requestComment;
    @Column(name = "request_date", nullable = false)
    private LocalDateTime requestDate;
    @Column(name = "request_status", nullable = false, length = 10)
    private int requestStatus;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver", referencedColumnName = "account_id", nullable = true)
    private Account approver;
    @Column(name = "approval_date", nullable = true)
    private LocalDateTime approvalDate;
    @Column(name = "approver_comment", nullable = true, length = 255)
    private String approverComment;
}
