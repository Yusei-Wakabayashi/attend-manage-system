package com.example.springboot.model;

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
import javax.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "attendance_list_sources", uniqueConstraints = {@UniqueConstraint(name = "attendance_list", columnNames = {"attendance_id"}), @UniqueConstraint(name = "shift_list", columnNames = {"shift_id"})})
public class AttendanceListSource
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attendance_list_source_id", nullable = false, length = 20)
    private Long attendanceListSourceId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attendance_id", nullable = false)
    private Attend attendanceId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shift_id", nullable = false)
    private Shift shiftId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "time_card_id", nullable = false)
    private TimeCard timeCardId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stamp_request_id", nullable = false)
    private StampRequest stampRequestId;
}