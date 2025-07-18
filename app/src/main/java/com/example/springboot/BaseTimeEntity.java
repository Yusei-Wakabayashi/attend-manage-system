package com.example.springboot;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity
{
    @CreatedDate
    @Column(name = "create_date", nullable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "update_date", nullable = false)
    private LocalDateTime modifiedDate;
}
