package com.example.shopapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class BaseEntity {
    @Column(name = "created_at")
    private LocalDate createdAt;
    @Column(name="updated_at")
    private LocalDate updatedAt;
    @PrePersist
    protected  void onCreate()
    {
        createdAt=LocalDate.now();
        updatedAt=LocalDate.now();
    }
    @PreUpdate
    protected void onUpdate()
    {
        updatedAt=LocalDate.now();
    }
}
