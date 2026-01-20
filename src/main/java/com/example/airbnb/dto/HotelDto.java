package com.example.airbnb.dto;

import com.example.airbnb.entity.hotelcontactinfo;

import lombok.Data;

@Data

public class HotelDto {

    private Long id;

    private String name;

    private String city;

    private String[] photos;

    private String[] amenties;

    private hotelcontactinfo contactInfo;

    private Boolean active;

}
