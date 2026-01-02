package com.example.airbnb.service;

import org.springframework.stereotype.Service;

import com.example.airbnb.dto.HotelDto;
import com.example.airbnb.entity.Hotel;
import com.example.airbnb.exception.resoucenotfoundexception;
import com.example.airbnb.repository.HotelRepository;
import org.modelmapper.ModelMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor

public class HotelServiceimpl implements HotelService {

    private final HotelRepository hotelRepository;
    // this is constructor dependency injection and
    // @RequiredArgsConstructor will create a constructor for this as
    // @RequiredArgsConstructor creates a constructor for all fields
    private final ModelMapper modelMapper;

    @Override
    public HotelDto createNewHotel(HotelDto hotelDto) {
        log.info("creating new hotel", hotelDto.getName());
        Hotel hotel = modelMapper.map(hotelDto, Hotel.class); // mapping HotelDto to Hotel entity
        hotel.setActive(false); // setting active to false by default
        hotel = hotelRepository.save(hotel); // saving hotel entity to database
        log.info("hotel created with id: {}", hotel.getId());
        return modelMapper.map(hotel, HotelDto.class); // mapping Hotel entity to HotelDto
    }

    @Override
    public HotelDto getHotelById(Long id) {
        log.info("getting hotel by id: {}", id);
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new resoucenotfoundexception("Hotel not found with id: " + id));
        return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    public HotelDto updateHotel(HotelDto hotelDto, Long id) {
        log.info("getting the hotel with the id: {}", id);
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new resoucenotfoundexception("Hotel not found with id: " + id));
        modelMapper.map(hotelDto, hotel);
        /*
         * What it does: This is the magic step. ModelMapper automatically copies all
         * the matching values from the hotelDto (the new info) onto the existingHotel
         * object we just found.
         * Simple terms:
         * "Automatically overwrite the old contact details with the new ones."
         * Example:
         * If hotelDto has name = "Grand Hyatt", it changes existingHotel.name to
         * "Grand Hyatt".
         * If hotelDto has city = "New York", it changes existingHotel.city to
         * "New York".
         * It does this for all matching fields, so you don't have to write setName(),
         * setCity(), etc., yourself.
         * 
         */

        hotel = hotelRepository.save(hotel);
        log.info("hotel updated with id: {}", hotel.getId());
        return modelMapper.map(hotel, HotelDto.class);

    }

    @Override
    public Boolean deleteHotelById(Long id) {
        log.info("deleting the hotel with the id: {}", id);
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new resoucenotfoundexception("Hotel not found with id: " + id));
        hotelRepository.delete(hotel);

        // todo:delete all rooms and inventories associated with this hotel
        log.info("hotel deleted with id: {}", id);
        return true;
    }

    @Override
    public void activateHotel(Long hotelId) {
        log.info("activating the hotel with the id: {}", hotelId);
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new resoucenotfoundexception("Hotel not found with id: " + hotelId));
        hotel.setActive(true);
        hotelRepository.save(hotel);
        log.info("hotel activated with id: {}", hotelId);
        // todo:create inventory fo all the room for thiis hotel

    }

}
