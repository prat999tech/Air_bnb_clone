package com.example.airbnb.entity;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass // this annotation is used to indicate that this class is a base class for other
                  // entities and its fields should be mapped to the database table of the derived
                  // entities.
@Getter
@Setter
public class Auditableclass {

    @CreatedDate
    private java.time.LocalDateTime createdAt;

    @LastModifiedBy
    private java.time.LocalDateTime updatedAt;

    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String lastModifiedBy;

}
