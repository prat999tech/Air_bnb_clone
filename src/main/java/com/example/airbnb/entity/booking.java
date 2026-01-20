package com.example.airbnb.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.airbnb.entity.enumsrole.bookingStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import java.util.Set;

@EntityListeners(AuditingEntityListener.class)

@Getter
@Setter
@Entity
@Table(name = "bookings")

public class booking extends Auditableclass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private user user;

    @Column(nullable = false)
    private Integer roomsCount;
    @Column(nullable = false)
    private LocalDate checkInDate;
    @Column(nullable = false)
    private LocalDate checkOutDate;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp // whenever the entity is updated, this field will be updated with the current
                     // timestamp
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private bookingStatus bookingStatus;

   /*
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;
     */ 

    @ManyToMany
    @JoinTable(name = "booking_guests", joinColumns = @JoinColumn(name = "booking_id"), inverseJoinColumns = @JoinColumn(name = "guest_id"))
    private Set<Guest> guests; // we use here set because one booking can have multiple guests and one guest
                               // can have multiple bookings and set will avoid duplicate entries of same guest
                               // in one booking

}
// we use many to many relationship between guest and booking because one guest
// can have multiple bookings and one booking can have multiple guests and for
// that we use join table not join column because join column is used for many
// to one and one to one relationship only