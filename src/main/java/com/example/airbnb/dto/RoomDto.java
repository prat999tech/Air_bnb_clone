package com.example.airbnb.dto;

import com.example.airbnb.entity.Hotel;
import com.example.airbnb.repository.HotelRepository;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import java.math.BigDecimal;

@Data

public class RoomDto {

    private Long id;

    @JsonIgnore // it is optional to ignore this field during serialization/deserialization

    private Hotel hotel;
    private String Type;

    private BigDecimal baseprice;

    private String[] amenties;

    private Integer totalcount;

    private Integer capacity;

}
