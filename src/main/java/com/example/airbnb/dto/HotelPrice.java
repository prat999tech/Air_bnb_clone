package com.example.airbnb.dto;

import com.example.airbnb.entity.Hotel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class HotelPrice {
    private Hotel hotel;
    private Double price;

}
