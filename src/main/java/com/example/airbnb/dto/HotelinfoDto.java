package com.example.airbnb.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class HotelinfoDto {

    private HotelDto hotel;
    private List<RoomDto> rooms;

}
