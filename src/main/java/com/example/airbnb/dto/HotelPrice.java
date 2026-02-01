package com.example.airbnb.dto;

import com.example.airbnb.entity.Hotel;

import lombok.Data;

@Data

public class HotelPrice {
    private Hotel hotel;
    private Double price;

}
