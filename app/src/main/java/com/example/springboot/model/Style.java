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
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "styles", uniqueConstraints = {@UniqueConstraint(name = "accounts", columnNames = {"account_id"})})
public class Style extends BaseTimeEntity
{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "style_id", nullable = false, length = 20)
    private Long styleId;
    @OneToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account accountId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "style_place_id", referencedColumnName = "style_place_id")
    private StylePlace stylePlaceId;
}