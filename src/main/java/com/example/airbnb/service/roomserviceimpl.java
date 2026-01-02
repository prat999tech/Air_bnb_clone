package com.example.airbnb.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.airbnb.dto.RoomDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j

public class roomserviceimpl implements RoomService {

    @Override
    public RoomDto createNewRoom(RoomDto roomDto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createNewRoom'");
    }

    @Override
    public List<RoomDto> getAllRoomsInHotel(Long hotelId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllRoomsInHotel'");
    }

    @Override
    public RoomDto getRoomById(Long roomId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRoomById'");
    }

    @Override
    public void deleteRoomById(Long roomId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteRoomById'");
    }

}
