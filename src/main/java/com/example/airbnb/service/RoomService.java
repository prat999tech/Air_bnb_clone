package com.example.airbnb.service;

import java.util.List;

import com.example.airbnb.dto.RoomDto;

public interface RoomService {
    RoomDto createNewRoom(RoomDto roomDto);

    List<RoomDto> getAllRoomsInHotel(Long hotelId);

    RoomDto getRoomById(Long roomId);

    void deleteRoomById(Long roomId);

}
