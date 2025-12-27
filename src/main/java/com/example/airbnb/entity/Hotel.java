package com.example.airbnb.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "hotels")

public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Think of it like a "Take a Number" machine at a deli or a
                                                        // bank. When a new customer (a new Hotel record) walks in, the
                                                        // machine automatically gives them the next number in line ($1,
                                                        // 2, 3...$) without you having to manually assign it.

    private Long id;

    @Column(nullable = false)
    private String name;

    private String city;

    @Column(columnDefinition = "TEXT[]") // hibernate will map this to a text array in postgres as hibernate only does
                                         // not have native support for array types
    private String[] photos;

    @Column(columnDefinition = "TEXT[]")
    private String[] amenties;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp // whenever the entity is updated, this field will be updated with the current
                     // timestamp
    private LocalDateTime updatedAt;

    @Embedded // this annotation is used to embed a class (hotelcontactinfo) into an entity
              // (Hotel). It allows us to include the fields of the embedded class as part of
              // the entity's table structure in the database. so in hotel table it is like
              // phoneNumber, address, email, website columns will be there

    /*
     * Imagine you have a Hotel entity. Every hotel has an address (Street, City,
     * State, Zip Code).
     * 
     * Option A (Messy): Put all 4 address fields directly inside the Hotel class.
     * This makes the class huge and cluttered.
     * 
     * Option B (Better): Create an Address class. Since an address doesn't usually
     * exist on its own without a building, you don't want a separate "Addresses"
     * table. You just want the Hotel table to have those columns.
     * 
     */

    private hotelcontactinfo contactInfo;

    @Column(nullable = false)
    private Boolean active;

}
