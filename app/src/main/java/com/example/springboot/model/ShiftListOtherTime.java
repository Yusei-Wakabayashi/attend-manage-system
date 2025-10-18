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
@Table(name = "shift_list_other_times", uniqueConstraints = {@UniqueConstraint(name = "attendance_exception_request", columnNames = {"attendance_exception_id"})}) // 外部キーにユニーク制約追加
public class ShiftListOtherTime
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shift_list_other_time_id", length = 20, nullable = false)
    private Long shiftListOtherTimeId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shift_id", referencedColumnName = "shift_id", nullable = false)
    private Shift shiftId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attendance_exception_id", referencedColumnName = "attendance_exception_id", nullable = false)
    private AttendanceExceptionRequest attendanceExceptionId;
}
