package com.example.springboot.model;

import java.sql.Time;
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
import javax.persistence.UniqueConstraint;

import com.example.springboot.BaseTimeEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "payd_holiday_uses", uniqueConstraints = {@UniqueConstraint(name = "vacation_requets", columnNames = {"vacation_id"})})
public class PaydHolidayUse extends BaseTimeEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payd_holiday_use_id", length = 20, nullable = false)
    private Long paydHolidayUseId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "account_id", nullable = false)
    private Account accountId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vacation_id", referencedColumnName = "vacation_id", nullable = false)
    private VacationRequest vacationId;
    @Column(name = "time", nullable = false)
    private Time time;
    @Column(name = "use_date", nullable = false)
    private LocalDateTime useDate;
}