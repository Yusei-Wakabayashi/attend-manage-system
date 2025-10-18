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
@Table(name = "shift_list_overtimes", uniqueConstraints = {@UniqueConstraint(name = "overtime_requests", columnNames = {"over_time_id"})})
public class ShiftListOverTime
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shift_list_over_time_id", length = 20, nullable = false)
    private Long shiftListOverTimeId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shift_list_id", referencedColumnName = "shift_id", nullable = false)
    private Shift shiftListId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "over_time_id", referencedColumnName = "over_time_id", nullable = false)
    private OverTimeRequest overTimeId;
}
