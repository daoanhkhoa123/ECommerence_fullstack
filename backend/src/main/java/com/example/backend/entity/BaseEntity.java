package com.example.backend.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {
    @Column(nullable = false)
    private Boolean isActive =true;

    @Column(nullable = false, updatable = false)
    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());

    @Column(nullable = false)
    private Timestamp updatedAt = new Timestamp(System.currentTimeMillis());

    @PrePersist
    protected void onCreate(){
        createdAt = new Timestamp(System.currentTimeMillis());
        updatedAt = createdAt;
        if (isActive==null) isActive=true;
    }

    @PreUpdate
    protected void onUpdate(){
        updatedAt = new Timestamp(System.currentTimeMillis());
    }

}
