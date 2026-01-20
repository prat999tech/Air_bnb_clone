package com.example.airbnb.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.airbnb.dto.BookingDto;
import com.example.airbnb.dto.BookingRequest;
import com.example.airbnb.entity.Hotel;
import com.example.airbnb.entity.Room;
import com.example.airbnb.exception.resoucenotfoundexception;
import com.example.airbnb.repository.BookingRepository;
import com.example.airbnb.repository.HotelRepository;
import com.example.airbnb.repository.RoomRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor

public class BookingServiceimpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;

    @Override
    @Transactional
    public BookingDto initiateBooking(BookingRequest bookingRequest) {
        log.info("Initiating booking for hotelId: {}, roomId: {}, checkInDate: {}, checkOutDate: {}, roomsCount: {}",
                bookingRequest.getHotelId(),
                bookingRequest.getRoomId(),
                bookingRequest.getCheckInDate(),
                bookingRequest.getCheckOutDate(),
                bookingRequest.getRoomsCount());

        Hotel hotel = hotelRepository.findById(bookingRequest.getHotelId()).orElseThrow(
                () -> new resoucenotfoundexception("Hotel not found with id: " + bookingRequest.getHotelId()));
        Room room = roomRepository.findById(bookingRequest.getRoomId()).orElseThrow(
                () -> new resoucenotfoundexception("Room not found with id: " + bookingRequest.getRoomId()));

        return null;

    }// here we will lock the same price of the room and wait for user to book room
     // and out time out time will be 20 mintues and we used "reservedCount" in
     // inventory to lock the room

}
