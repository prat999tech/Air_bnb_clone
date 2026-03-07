package com.example.airbnb.dto;

import java.time.LocalDate;

import lombok.Data;

@Data

public class Hotelsearch {

    private String city;
    private LocalDate startdate;
    private LocalDate enddate;
    private Integer roomsCount;
    private Integer page = 0;
    private Integer size = 10;

}
