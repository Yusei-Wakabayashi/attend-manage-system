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
@Table(name = "time_cards")
public class TimeCard extends BaseTimeEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "time_card_id", nullable = false, length = 20)
    private Long timeCardId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "account_id", nullable = false)
    private Account accountId;
    @Column(name = "begin_work", nullable = false)
    private LocalDateTime beginWork;
    @Column(name = "end_work", nullable = true)
    private LocalDateTime endWork;
    @Column(name = "begin_break", nullable = true)
    private LocalDateTime beginBreak;
    @Column(name = "end_break", nullable = true)
    private LocalDateTime endBreak;
}
