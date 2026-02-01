package com.example.airbnb.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.airbnb.dto.BookingDto;
import com.example.airbnb.dto.BookingRequest;
import com.example.airbnb.dto.Guestdto;
import com.example.airbnb.service.BookingService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class HotelBookingController {
    private final BookingService bookingService;

    @PostMapping("/init")
    public ResponseEntity<BookingDto> initialiseBooking(@RequestBody BookingRequest bookingRequest) {
        return ResponseEntity.ok(bookingService.initiateBooking(bookingRequest));

    }

    @PostMapping("/{bookingId}/addGuest")
    public ResponseEntity<BookingDto> addGuest(@PathVariable Long bookingId, @RequestBody List<Guestdto> guestDto) {
        return ResponseEntity.ok(bookingService.addGuest(bookingId, guestDto));
    }

}
