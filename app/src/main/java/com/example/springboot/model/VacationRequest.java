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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.example.springboot.BaseTimeEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "vacation_requests")
public class VacationRequest extends BaseTimeEntity
{
    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "vacation_id", nullable = false, length = 20)
    private Long vacationId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "account_id", nullable = false)
    private Account accountId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vacation_type_id", referencedColumnName = "vacation_type_id", nullable = false)
    private VacationType vacationTypeId;
    @Column(name = "begin_vacation", nullable = false)
    private LocalDateTime beginVacation;
    @Column(name = "end_vacation", nullable = false)
    private LocalDateTime endVacation;
    @Column(name = "request_comment", nullable = true, length = 255)
    private String requestComment;
    @Column(name = "request_date", nullable = false)
    private LocalDateTime requestDate;
    @Column(name = "request_status", nullable = false, length = 10)
    private int requestStatus;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver", referencedColumnName = "account_id", nullable = true)
    private Account approver;
    @Column(name = "approval_time", nullable = true)
    private LocalDateTime approvalTime;
    @Column(name = "approver_comment", nullable = true, length = 255)
    private String approverComment;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shift_id", referencedColumnName = "shift_id", nullable = false)
    private Shift shiftId;

    @OneToOne(fetch = FetchType.LAZY)
    private PaydHolidayUse paydHolidayUseFromVacationRequest;
    @OneToOne(fetch = FetchType.LAZY)
    private ShiftListVacation shiftListVacationFromVacationRequest;
}
