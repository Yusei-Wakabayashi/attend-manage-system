package com.example.springboot.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="shift_lists")
public class Shift {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "shift_id")
    private Long shiftId;
    
}
