package com.example.springboot.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "shift_requests")
public class ShiftRequest
{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "shift_request_id", nullable = false)
    private Long shiftRequestId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account accountId;
    @Column(name = "begin_work", nullable = false)
    private LocalDateTime beginWork;
    @Column(name = "end_work", nullable = false)
    private LocalDateTime endWork;
    @Column(name = "begin_break", nullable = false)
    private LocalDateTime beginBreak;
    @Column(name = "end_Break", nullable = false)
    private LocalDateTime endBreak;
    @Column(name = "request_comment", nullable = true, length = 255)
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
    @OneToMany(mappedBy = "shiftRequestId", fetch = FetchType.LAZY)
    private List<ShiftListShiftRequest> shiftListShiftRequestFromShiftRequests;
}