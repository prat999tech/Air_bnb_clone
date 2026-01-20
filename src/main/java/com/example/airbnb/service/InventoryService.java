package com.example.airbnb.service;

import org.springframework.data.domain.Page;

import com.example.airbnb.dto.HotelDto;
import com.example.airbnb.dto.Hotelsearch;
import com.example.airbnb.entity.Room;

public interface InventoryService {

    void initiliazeroomforayear(Room room); // basically we have to initiaze a inventory for a room that means that
                                            // speciific room id will be with us just we have to initialize a inventory
                                            // for that room by passing that roomid entity

    void deletefutureinventoryofroom(Room room); // when a room is deactivated we have to delete all the future
                                                 // inventory of that room

    Page<HotelDto> searchHotels(Hotelsearch hotelsearchrequest);
}
