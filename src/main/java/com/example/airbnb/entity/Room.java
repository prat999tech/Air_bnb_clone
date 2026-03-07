package com.example.airbnb.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
// there will be many to one relationship with hotel entity i.e many rooms can
// belong to one hotel but one room cannot belong to many hotels so we will add
// many to one relationship in room entity later

public class Room { // in table Room is converted to room
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false) // this will create a foreign key column hotel_id in room table
                                                     // which references the primary key id of hotel table
    private Hotel hotel; // a new column hotel_id will be created in room table which will store the id
                         // of
                         // the hotel to which the room belongs
    @Column(nullable = false)
    private String Type;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal baseprice;

    @Column(columnDefinition = "TEXT[]")
    private String[] amenties;

    @CreationTimestamp
    @Column(updatable = false) // this field will not be updated once set
    private LocalDateTime createdAt;

    @UpdateTimestamp // whenever the entity is updated, this field will be updated with the current
                     // timestamp
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private Integer totalcount;

    @Column(nullable = false)
    private Integer capacity;

}
