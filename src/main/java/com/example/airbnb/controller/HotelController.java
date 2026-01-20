package com.example.airbnb.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.airbnb.dto.HotelDto;
import com.example.airbnb.service.HotelService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/admin/hotels")
@RequiredArgsConstructor
@Slf4j

public class HotelController {
    private final HotelService hotelService;

    @PostMapping
    public ResponseEntity<HotelDto> createnewHotel(@RequestBody HotelDto hotelDto) {
        log.info("attempting to create new hotel: {}", hotelDto.getName());
        HotelDto hotel = hotelService.createNewHotel(hotelDto);
        return new ResponseEntity<>(hotel, HttpStatus.CREATED);

    }

    @GetMapping("/{hotelId}")
    public ResponseEntity<HotelDto> getHotelById(@PathVariable Long hotelId) {
        log.info("attempting to get hotel by id: {}", hotelId);
        HotelDto hotelDto = hotelService.getHotelById(hotelId);
        return new ResponseEntity<>(hotelDto, HttpStatus.OK);

    }

    @PutMapping("/{hotelId}")
    public ResponseEntity<HotelDto> updateHotel(@PathVariable Long hotelId, @RequestBody HotelDto hotelDto) {

        log.info("attempting to update hotel with id: {}", hotelId);
        HotelDto hotel = hotelService.updateHotel(hotelDto, hotelId);
        hotel.setId(hotelId);// sometimes id is not set in dto after mapping, so setting it explicitly
        return new ResponseEntity<>(hotel, HttpStatus.OK);

    }

    @DeleteMapping("/{hotelId}")
    public ResponseEntity<Boolean> deleteHotelById(@PathVariable Long hotelId) {
        log.info("attempting to delete hotel with id: {}", hotelId);
        Boolean deleted = hotelService.deleteHotelById(hotelId);
        return new ResponseEntity<>(deleted, HttpStatus.OK);

    }

    @PatchMapping("/{hotelId}")
    public ResponseEntity<HotelDto> activateHotel(@PathVariable Long hotelId) {
        log.info("attempting to activate hotel with id: {}", hotelId);
        hotelService.activateHotel(hotelId);
        return ResponseEntity.noContent().build();
    }

}
