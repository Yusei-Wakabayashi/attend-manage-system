package com.example.springboot.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "approval_settings", uniqueConstraints = {@UniqueConstraint(name = "roles", columnNames = {"role_id"})}) // 外部キーにユニーク制約追加
public class ApprovalSetting
{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "approval_setting_id", nullable = false, length = 20)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role roleId;
    @ManyToOne
    @JoinColumn(name = "approval_id", nullable = false)
    private Role approvalId;
}
