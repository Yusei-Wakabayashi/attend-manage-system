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

import com.example.springboot.BaseTimeEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "shift_list_shift_requests", uniqueConstraints = {@UniqueConstraint(name = "shifts", columnNames = {"shift_id"}), @UniqueConstraint(name = "shift_requests", columnNames = {"shift_request_id"})})
public class ShiftListShiftRequest extends BaseTimeEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shift_list_shift_request_id", nullable = false, length = 20)
    private Long shiftListShiftRequestId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shift_id", referencedColumnName = "shift_id", nullable = false)
    private Shift shiftId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shift_request_id", referencedColumnName = "shift_request_id", nullable = false)
    private ShiftRequest shiftRequestId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shift_change_request_id", referencedColumnName = "shift_change_id", nullable = true)
    private ShiftChangeRequest shiftChangeRequestId;
}
