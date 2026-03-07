package com.example.airbnb.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.airbnb.dto.BookingDto;
import com.example.airbnb.dto.BookingRequest;
import com.example.airbnb.dto.Guestdto;
import com.example.airbnb.service.BookingService;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

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
    @PostMapping("/{bookingId}/payments")
    public ResponseEntity<Map<String,String>> initiatePayment(@PathVariable Long bookingId) {
        String sessionURL= bookingService.initiatePayments(bookingId);
        return ResponseEntity.ok(Map.of("sessionURL",sessionURL));
    }
    @PostMapping("/{bookingId}/cancel")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long bookingId) {
        bookingService.cancelBooking(bookingId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{bookingId}/bookingsttaus")
    public ResponseEntity<Map<String,String>> Bookingstatus(@PathVariable Long bookingId) {
        return ResponseEntity.ok(Map.of("status", bookingService.getBookingStatus(bookingId)));
    }
    //our frontend will keep calling this api to get the current status of the booking until the payment is successful or failed and then redirect to the appropriate page based on the status


}
