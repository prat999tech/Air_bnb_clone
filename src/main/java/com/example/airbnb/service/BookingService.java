package com.example.airbnb.service;

import java.util.List;

import com.example.airbnb.dto.BookingDto;
import com.example.airbnb.dto.BookingRequest;
import com.example.airbnb.dto.Guestdto;

public interface BookingService {

    BookingDto initiateBooking(BookingRequest bookingRequest);

    BookingDto addGuest(Long bookingId, List<Guestdto> guestDto);

}
