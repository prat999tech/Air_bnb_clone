package com.example.airbnb.service;

import org.jspecify.annotations.Nullable;

import com.example.airbnb.dto.BookingDto;
import com.example.airbnb.dto.BookingRequest;

public interface BookingService {

    BookingDto initiateBooking(BookingRequest bookingRequest);

}
