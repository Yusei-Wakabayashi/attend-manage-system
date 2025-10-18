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
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "attendance_exception_types")
public class AttendanceExceptionType extends BaseTimeEntity
{
    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "attendance_exception_type_id", nullable = false, length = 20)
    private Long attedanceExceptionTypeId;
    @Column(name = "attendance_exception_type_name", nullable = false, length = 255)
    private String attednaceExceptionTypeName;

    @OneToMany(mappedBy = "attendanceExceptionTypeId", fetch = FetchType.LAZY)
    private List<AttendanceExceptionRequest> attedanceExceptionRequestFromAttendanceExceptionTypes;
}