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
@Table(name = "departments")
public class Department extends BaseTimeEntity
{
    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "department_id", nullable = false, length = 20)
    private Long id;
    @Column(name = "department_name", nullable = false, length = 255)
    private String name;
    @OneToMany(mappedBy = "departmentId")
    private List<Account> departmentId;
}