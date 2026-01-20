package com.example.airbnb.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import com.example.airbnb.entity.Hotel;
import com.example.airbnb.entity.Room;
import com.example.airbnb.entity.user;
import com.example.airbnb.entity.enumsrole.bookingStatus;

import lombok.Data;

@Data

public class BookingDto {

    private long id;

    private Hotel hotel;

    private Room room;

    private user user;

    private Integer roomsCount;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private bookingStatus bookingStatus;

    private Set<Guestdto> guests;

}
