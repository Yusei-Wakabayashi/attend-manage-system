package com.example.springboot.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.example.springboot.BaseTimeEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="salts")
public class Salt extends BaseTimeEntity
{
    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "salt_id", nullable = false, length = 20)
    private Long id;
    @Column(name = "text", nullable = false, length=255)
    private String text;
    @OneToOne(mappedBy = "saltId", fetch = FetchType.LAZY)
    private Account accountFromSalts;
}