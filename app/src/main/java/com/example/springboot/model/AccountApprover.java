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
@Table(name = "account_approvers", uniqueConstraints = {@UniqueConstraint(name = "accounts", columnNames = "account_id")})
public class AccountApprover
{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "account_approver_id", nullable = false, length = 20)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account accountId;
    @ManyToOne
    @JoinColumn(name = "approver_id", nullable = false)
    private Account approverId;
}
