package com.example.springboot.model;

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
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "approval_settings")
public class ApprovalSetting extends BaseTimeEntity
{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "approval_setting_id", nullable = false, length = 20)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", referencedColumnName = "role_id", nullable = false)
    private Role roleId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approval_id", referencedColumnName = "role_id", nullable = false)
    private Role approvalId;
}
