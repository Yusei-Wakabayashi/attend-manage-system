package com.example.springboot.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.example.springboot.BaseTimeEntity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "roles")
public class Role extends BaseTimeEntity
{
    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "role_id", nullable = false, length = 20)
    private Long id;
    @Column(name = "role_name", nullable = false, length = 255)
    private String name;
    @Column(name = "power", nullable = false, length = 10)
    private int power;
    @OneToMany(mappedBy = "roleId")
    private List<Account> roleId;
    @OneToMany(mappedBy = "roleId")
    private List<ApprovalSetting> approvalSettings;
    @OneToMany(mappedBy = "approvalId")
    private List<ApprovalSetting> approvedBy;
}