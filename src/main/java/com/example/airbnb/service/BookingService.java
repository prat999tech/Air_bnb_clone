package com.example.airbnb.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.example.airbnb.dto.BookingDto;
import com.example.airbnb.dto.BookingRequest;
import com.example.airbnb.dto.Guestdto;
import com.example.airbnb.dto.HotelReportDto;
import com.stripe.model.Event;

public interface BookingService {

    BookingDto initiateBooking(BookingRequest bookingRequest);

    BookingDto addGuest(Long bookingId, List<Guestdto> guestDto);
    String initiatePayments(Long bookingId);

    void capturePayment(Event event);

    void cancelBooking(Long bookingId);

     String getBookingStatus(Long bookingId);

    List<BookingDto> getAllBookingsByHotelId(Long hotelId);

    HotelReportDto getHotelReport(Long hotelId, LocalDate startDate, LocalDate endDate);

    List<BookingDto> getMyBookings();
}
