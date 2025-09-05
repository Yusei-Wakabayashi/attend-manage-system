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
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "shift_change_requests")
public class ShiftChangeRequest
{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "shift_change_id", nullable = false)
    private Long shiftChangeId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account accountId;
    @Column(name = "begin_work", nullable = false)
    private LocalDateTime beginWork;
    @Column(name = "end_work", nullable = false)
    private LocalDateTime endWork;
    @Column(name = "beginBreak", nullable = false)
    private LocalDateTime beginBreak;
    @Column(name = "endBreak", nullable = false)
    private LocalDateTime endBreak;
    @Column(name = "request_comment", nullable = false)
    private String requestComment;
    @Column(name = "request_date", nullable = false)
    private LocalDateTime requestDate;
    @Column(name = "request_status", nullable = false)
    private int requestStatus;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver", nullable = false)
    private Account approver;
    @Column(name = "approval_time", nullable = false)
    private LocalDateTime approvalTime;
    @Column(name = "approver_comment", nullable = false)
    private String approverComment;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shift_id", nullable = false)
    private Shift shiftId;
}
