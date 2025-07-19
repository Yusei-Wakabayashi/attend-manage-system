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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.example.springboot.BaseTimeEntity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "accounts", uniqueConstraints = {@UniqueConstraint(name = "salts", columnNames = {"salt_id"})}) // 外部キーにユニーク制約追加
public class Account extends BaseTimeEntity
{
    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "account_id", nullable = false, length = 20)
    private Long id;
    @Column(name = "username", nullable = false, length = 255)
    private String username;
    @OneToOne
    @JoinColumn(name = "salt_id", nullable = false)
    private Salt saltId;
    @Column(name = "password", nullable = false)
    private byte[] password;
    @Column(name = "name", nullable = false, length = 255)
    private String name;
    @Column(name = "gender", nullable = true, length = 255)
    private String gender;
    @Column(name = "age", nullable = false, length = 10)
    private int age;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", referencedColumnName = "role_id", nullable = false)
    private Role roleId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", referencedColumnName = "department_id", nullable = false)
    private Department departmentId;
    @Column(name = "join_date", nullable = false)
    private LocalDateTime joinDate;
}