package com.example.airbnb.service;

import com.example.airbnb.dto.HotelDto;
import com.example.airbnb.dto.HotelinfoDto;

public interface HotelService {

    public HotelDto createNewHotel(HotelDto hotelDto);

    public HotelDto getHotelById(Long id);

    public HotelDto updateHotel(HotelDto hotelDto, Long id);

    public Boolean deleteHotelById(Long id);

    void activateHotel(Long hotelId); // Method to activate a hotel as we have activate field in Hotel entity

    HotelinfoDto getHotelInfoById(Long hotelId);

}
