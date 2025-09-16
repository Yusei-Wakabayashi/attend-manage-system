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

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "attendance_exception_request")
public class AttendanceExceptionRequest
{
    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "attendance_exception_id", nullable = false, length = 20)
    private Long attendanceExceptionId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account accountId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attendance_exception_type_id", nullable = false)
    private AttendanceExceptionType attendanceExceptionTypeId;
    @Column(name = "begin_time", nullable = false)
    private LocalDateTime beginTime;
    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;
    @Column(name = "request_comment", nullable = false, length = 255)
    private String requestComment;
    @Column(name = "request_date", nullable = false)
    private LocalDateTime requestDate;
    @Column(name = "request_status", nullable = false, length = 10)
    private int requestStatus;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver", nullable = true)
    private Account approver;
    @Column(name = "approval_time", nullable = true)
    private LocalDateTime approvalTime;
    @Column(name = "approver_comment", nullable = true, length = 255)
    private String approverComment;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shift_id", nullable = false)
    private Shift shiftId;
}
