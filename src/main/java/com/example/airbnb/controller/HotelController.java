package com.example.airbnb.controller;

import com.example.airbnb.dto.BookingDto;
import com.example.airbnb.dto.HotelReportDto;
import com.example.airbnb.service.BookingService;
import org.springframework.web.bind.annotation.*;

import com.example.airbnb.dto.HotelDto;
import com.example.airbnb.service.HotelService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/admin/hotels")
@RequiredArgsConstructor
@Slf4j

public class HotelController {
    private final HotelService hotelService;
    private final BookingService bookingService;

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

    @GetMapping
    public ResponseEntity<List<HotelDto>> getAllHotels(){
        log.info("attempting to get all hotels details");
        return ResponseEntity.ok(hotelService.getAllHotels());
    }

    @GetMapping("/{hotelId}/bookings")
    public ResponseEntity<List<BookingDto>> getAllBookingByHotelId(@PathVariable Long hotelId){
        log.info("attempting to get all bookings for hotel with id: {}", hotelId);
        return ResponseEntity.ok(bookingService.getAllBookingsByHotelId(hotelId));
    }

    @GetMapping("/{hotelId}/reports")
    public ResponseEntity<HotelReportDto> getHotelReport(@PathVariable Long hotelId, @RequestParam(required = false)LocalDate startDate, @RequestParam(required = false) LocalDate endDate){
        if(startDate==null) {
            startDate=LocalDate.now().minusMonths(1); // default to last 1 month
        }
        if(endDate==null) {
            endDate=LocalDate.now(); // default to current date
        }
        log.info("attempting to get hotel report for hotel with id: {} from {} to {}", hotelId, startDate, endDate);
        return ResponseEntity.ok(bookingService.getHotelReport(hotelId, startDate, endDate));
    }







}
