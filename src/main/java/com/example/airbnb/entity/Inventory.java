package com.example.airbnb.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Getter
@Setter
@Table(uniqueConstraints = @UniqueConstraint(name = "unique_hotel_room_date", columnNames = { "hotel_id", "room_id",
        "date" })) // this unique constraint ensures that for a given hotel, room and date
                   // combination, there is only one inventory record. This prevents duplicate
                   // entries for the same hotel room on the same date. mtlb eek hi din pr eek hi
                   // hotel ma eek hi room ka inventory hona chhaie
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER) // EAGER fetch type is used here because whenever we fetch inventory details, we
                                        // would always need to know which hotel it belongs to, so fetching it eagerly
                                        // makes sense and if we did not add this line then we have to explicitly (in a
                                        // code) fetch hotel details later which is not efficient. agar humlog inventory
                                        // fetch kar rahe ho agar id=2 ka toh hume hotel ka detail ekdum se mil jayega
                                        // bina kisi .hotel call ke humare code ke andar
    @JoinColumn(name = "hotel_id", nullable = false) // there will be many to one relationship between inventory and
                                                     // hotel beacuse one hotel needs to track its availability and
                                                     // stock across different dates and room types
    private Hotel hotel;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id", nullable = false) // there will be many to one relationship between inventory and room
                                                    // because one room type can have multiple inventory records for
                                                    // different dates
    private Room room;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer bookedCount;

    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer reservedCount;

    @Column(nullable = false)
    private Integer totalCount;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal surgefactor;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;// baseprice*surgefactor

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private Boolean closed;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
