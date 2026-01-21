package com.example.airbnb.service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.apache.catalina.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.airbnb.dto.BookingDto;
import com.example.airbnb.dto.BookingRequest;
import com.example.airbnb.entity.Hotel;
import com.example.airbnb.entity.Inventory;
import com.example.airbnb.entity.Room;
import com.example.airbnb.entity.booking;
import com.example.airbnb.entity.user;
import com.example.airbnb.entity.enumsrole.bookingStatus;
import com.example.airbnb.exception.resoucenotfoundexception;
import com.example.airbnb.repository.BookingRepository;
import com.example.airbnb.repository.HotelRepository;
import com.example.airbnb.repository.InventoryRepository;
import com.example.airbnb.repository.RoomRepository;
import org.modelmapper.ModelMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor

public class BookingServiceimpl implements BookingService {

        private final BookingRepository bookingRepository;
        private final HotelRepository hotelRepository;
        private final RoomRepository roomRepository;
        private final InventoryRepository inventoryRepository;
        private final ModelMapper modelMapper;

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
                                () -> new resoucenotfoundexception(
                                                "Hotel not found with id: " + bookingRequest.getHotelId()));
                Room room = roomRepository.findById(bookingRequest.getRoomId()).orElseThrow(
                                () -> new resoucenotfoundexception(
                                                "Room not found with id: " + bookingRequest.getRoomId()));
                List<Inventory> inventories = inventoryRepository.findAndLockAvailableInventory(
                                room.getId(),
                                bookingRequest.getCheckInDate(),
                                bookingRequest.getCheckOutDate(),
                                bookingRequest.getRoomsCount());
                long daysCount = ChronoUnit.DAYS.between(bookingRequest.getCheckInDate(),
                                bookingRequest.getCheckOutDate());

                if (inventories.size() != daysCount) {
                        log.error("Insufficient inventory for the requested dates.");
                        throw new IllegalStateException(
                                        "Insufficient inventory for the requested dates or Room is not available.");
                }
                // reserve the rooms by updating the booked count of inventories
                for (Inventory inventory : inventories) {
                        inventory.setBookedCount(inventory.getReservedCount() + bookingRequest.getRoomsCount());
                }
                inventoryRepository.saveAll(inventories);

                // create a booking

                user users = new user(); // Assuming you have a User entity and you can get the user details
                users.setId(1l); // TODO: remove the dummy user and get user using spring security
                // TODO:calculate the dynamic pricing

                booking bookings = booking.builder()
                                .bookingStatus(bookingStatus.COMPLETED)
                                .hotel(hotel)
                                .room(room)
                                .user(users)
                                .checkInDate(bookingRequest.getCheckInDate())
                                .checkOutDate(bookingRequest.getCheckOutDate())
                                .roomsCount(bookingRequest.getRoomsCount())
                                .amount(BigDecimal.TEN)
                                .build();
                booking savedBooking = bookingRepository.save(bookings);
                log.info("Booking initiated successfully with id: {}", savedBooking.getId());

                return modelMapper.map(savedBooking, BookingDto.class);

        }// here we will lock the same price of the room and wait for user to book room
         // and out time out time will be 20 mintues and we used "reservedCount" in
         // inventory to lock the room

}
