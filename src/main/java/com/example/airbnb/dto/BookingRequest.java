package com.example.airbnb.dto;

import lombok.Data;

@Data
public class BookingRequest {
    private Long hotelId;
    private Long roomId;
    private String checkInDate;
    private String checkOutDate;
    private Integer roomsCount;

}
