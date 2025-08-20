package com.example.springboot.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.example.springboot.BaseTimeEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "style_places")
public class StylePlace extends BaseTimeEntity
{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "style_place_id", nullable = false, length = 20)
    private Long id;
    @Column(name = "style_place_name", nullable = false, length = 255)
    private String name;
    @OneToMany(mappedBy = "stylePlaceId", fetch = FetchType.LAZY)
    private List<Style> styleFromStylePlace;
}