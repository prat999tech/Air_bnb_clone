package com.example.airbnb.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import com.example.airbnb.dto.HotelReportDto;
import com.example.airbnb.exception.Unauthorizedexception;
import com.example.airbnb.stratergy.PricingService;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.Refund;
import com.stripe.model.checkout.Session;
import com.stripe.param.RefundCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.airbnb.dto.BookingDto;
import com.example.airbnb.dto.BookingRequest;
import com.example.airbnb.dto.Guestdto;
import com.example.airbnb.entity.Guest;
import com.example.airbnb.entity.Hotel;
import com.example.airbnb.entity.Inventory;
import com.example.airbnb.entity.Room;
import com.example.airbnb.entity.booking;
import com.example.airbnb.entity.user;
import com.example.airbnb.entity.enumsrole.bookingStatus;
import com.example.airbnb.exception.resoucenotfoundexception;
import com.example.airbnb.repository.BookingRepository;
import com.example.airbnb.repository.GuestRepository;
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
        @Value("${Frontend.url}")
        private String frontendUrl;

        private final BookingRepository bookingRepository;
        private final HotelRepository hotelRepository;
        private final RoomRepository roomRepository;
        private final InventoryRepository inventoryRepository;
        private final ModelMapper modelMapper;
        private final GuestRepository guestRepository;
        private final CheckOutService checkOutService;
        private final PricingService pricingService;
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
                /*
                // reserve the rooms by updating the booked count of inventories
                for (Inventory inventory : inventories) {
                        inventory.setReservedCount(inventory.getReservedCount() + bookingRequest.getRoomsCount());
                }
                inventoryRepository.saveAll(inventories);
                rather then saving every time the reserved count by by calling saveall we can write the jpql query for it

                */
                inventoryRepository.initBooking(room.getId(),bookingRequest.getCheckInDate(),bookingRequest.getCheckOutDate(),bookingRequest.getRoomsCount());


                // create a booking

                //now calculating dynamic pricing
                //suppose we have booked the room for a 5 days so in each day room have a different pricing so we havr to update the price accordingly
                BigDecimal priceForOneRoom=pricingService.calculateTotalPrice(inventories);
                BigDecimal totalPrice=priceForOneRoom.multiply(BigDecimal.valueOf(bookingRequest.getRoomsCount()));



                // Assuming you have a User entity and you can get the user details

                booking bookings = booking.builder()
                                .bookingStatus(bookingStatus.COMPLETED)
                                .hotel(hotel)
                                .room(room)
                                .user(getCurrentUser())
                                .checkInDate(bookingRequest.getCheckInDate())
                                .checkOutDate(bookingRequest.getCheckOutDate())
                                .roomsCount(bookingRequest.getRoomsCount())
                                .amount(totalPrice)
                                .build();
                booking savedBooking = bookingRepository.save(bookings);
                log.info("Booking initiated successfully with id: {}", savedBooking.getId());

                return modelMapper.map(savedBooking, BookingDto.class);

        }// here we will lock the same price of the room and wait for user to book room
         // and out time out time will be 20 mintues and we used "reservedCount" in
         // inventory to lock the room

        @Override
        @Transactional
        public BookingDto addGuest(Long bookingId, List<Guestdto> guestDto) {
                log.info("Adding guests to booking with id: {}", bookingId);
                booking bookings = bookingRepository.findById(bookingId).orElseThrow(
                                () -> new resoucenotfoundexception("Booking not found with id: " + bookingId));
                user user=getCurrentUser();
                if(!user.equals(bookings.getUser())) { //we are getting getUser() from booking entity by the use of getter and setter and we are comparing it with the current user if both are same then only we will allow to add guest otherwise we will throw exception because only the user who has booked the room can add guest to that booking
                        log.error("Unauthorized attempt to add guests to booking with id: {} by user: {}", bookingId, user.getId());
                        throw new Unauthorizedexception("You are not authorized to add guests to this booking.");
                }

                // now before adding guest we have to check if booking is expired or not if
                // expired then we have to throw exception

                if (hasBookingExpired(bookings)) {
                        log.error("Booking with id: {} has expired.", bookingId);
                        throw new IllegalStateException("Booking has expired.");
                }

                if (bookings.getBookingStatus() != bookingStatus.COMPLETED) {
                        log.error("Cannot add guests to booking with id: {} as its status is not COMPLETED.",
                                        bookingId);
                        throw new IllegalStateException("Cannot add guests to a booking that is not COMPLETED.");
                }

                for (Guestdto guests : guestDto) {
                        Guest guest = modelMapper.map(guests, Guest.class);
                        guest.setUser(user);
                        guest = guestRepository.save(guest);
                        bookings.getGuests().add(guest);

                }
                booking updatedBooking = bookingRepository.save(bookings);
                log.info("Guests added successfully to booking with id: {}", bookingId);
                updatedBooking.setBookingStatus(bookingStatus.GUEST_ADDED);
                return modelMapper.map(updatedBooking, BookingDto.class);

        }

        @Override
        @Transactional
        public String initiatePayments(Long bookingId) {
                booking booking=bookingRepository.findById(bookingId).orElseThrow(()->new resoucenotfoundexception("Booking not found with id: "+bookingId));
                user user=getCurrentUser();
                if(!user.equals(booking.getUser())) { //we are getting getUser() from booking entity by the use of getter and setter and we are comparing it with the current user if both are same then only we will allow to initiate payment otherwise we will throw exception because only the user who has booked the room can initiate payment for that booking
                        log.error("Unauthorized attempt to initiate payment for booking with id: {} by user: {}", bookingId, user.getId());
                        throw new Unauthorizedexception("You are not authorized to initiate payment for this booking.");
                }
                if(hasBookingExpired(booking)) {
                        log.error("Booking with id: {} has expired. Cannot initiate payment.", bookingId);
                        throw new IllegalStateException("Booking has expired. Cannot initiate payment.");
                }
                //we are sepearing checkout service i.e. stripe payment gateway service so that in future if we want to change our payment gateway then we can easily do that by just changing the implementation of checkout service and we are not doing any change in booking service


                String SessionUrl=checkOutService.getCheckOutSession(booking, frontendUrl+"/payment/success",frontendUrl+"/payment/fail" );//we have taken frontend url as a success and fail url because after payment is successful or failed we have to redirect user to frontend and we have to show user that payment is successful or failed and for that we are taking frontend url as a parameter in this method and we are appending success and fail endpoint to it and in those endpoints we will handle the logic of showing user that payment is successful or failed

               booking.setBookingStatus(bookingStatus.PENDING);
               bookingRepository.save(booking);
                log.info("Payment initiated successfully for booking with id: {}", bookingId);

                return SessionUrl; // here my main motive is to make the session url for the stripe payment gateway and then calling the checkout service
        }

        @Override
        @Transactional
        public void capturePayment(Event event) {
                if ("checkout.session.completed".equals(event.getType())) {
                        Session session = (Session) event.getDataObjectDeserializer().getObject().orElse(null);
                        if (session == null) {
                                return;
                        }
                        String sessionId = session.getId();
                        booking booking = bookingRepository.findBySessionId(sessionId).orElseThrow(() -> new resoucenotfoundexception("Booking not found with session id: " + sessionId));
                        booking.setBookingStatus(bookingStatus.COMPLETED);
                        bookingRepository.save(booking);
                        log.info("Payment captured and booking status updated to COMPLETED for booking with id: {}", booking.getId());
                        inventoryRepository.findAndLockReservedInventory(booking.getRoom().getId(), booking.getCheckInDate(),booking.getCheckOutDate(),booking.getRoomsCount());
                        inventoryRepository.confirmBooking(booking.getRoom().getId(), booking.getCheckInDate(),booking.getCheckOutDate(),booking.getRoomsCount());
                        log.info("successfully confirmed booking for booking with id: {} and inventory updated accordingly", booking.getId());

                }
                else{
                        log.warn("Received unsupported event type: {}", event.getType());
                }
        }

        @Override
        @Transactional
        public void cancelBooking(Long bookingId) {
                booking booking=bookingRepository.findById(bookingId).orElseThrow(()->new resoucenotfoundexception("Booking not found with id: "+bookingId));
                user user=getCurrentUser();
                if(!user.equals(booking.getUser())) { //we are getting getUser() from booking entity by the use of getter and setter and we are comparing it with the current user if both are same then only we will allow to initiate payment otherwise we will throw exception because only the user who has booked the room can initiate payment for that booking
                        log.error("Unauthorized attempt to initiate payment for booking with id: {} by user: {}", bookingId, user.getId());
                        throw new Unauthorizedexception("You are not authorized to initiate payment for this booking.");
                }

                if(booking.getBookingStatus()!=bookingStatus.CONFIRMED) {
                        log.error(" only confirmed booking can cancel the booking.", bookingId);
                        throw new IllegalStateException("Booking has expired. Cannot initiate payment.");
                }
                booking.setBookingStatus(bookingStatus.CANCELLED);
                bookingRepository.save(booking);
                //now we have to update the inventory as we have new invvenrotry o that other customer can use that
                inventoryRepository.findAndLockReservedInventory(booking.getRoom().getId(), booking.getCheckInDate(),booking.getCheckOutDate(),booking.getRoomsCount());
                inventoryRepository.cancelBooking(booking.getRoom().getId(), booking.getCheckInDate(),booking.getCheckOutDate(),booking.getRoomsCount());
                //handle the refund
            try {
                Session session=Session.retrieve(booking.getSessionId());
                    RefundCreateParams refundparam=RefundCreateParams.builder()
                            .setPaymentIntent(session.getPaymentIntent())
                            .setReason(RefundCreateParams.Reason.REQUESTED_BY_CUSTOMER)
                            .build();
                    Refund.create(refundparam);
            } catch (StripeException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public String getBookingStatus(Long bookingId) {
                booking booking = bookingRepository.findById(bookingId).orElseThrow(
                        () -> new resoucenotfoundexception("Booking not found with id: "+bookingId)
                );
                user user = getCurrentUser();
                if (!user.equals(booking.getUser())) {
                        throw new Unauthorizedexception("Booking does not belong to this user with id: "+user.getId());
                }

                return booking.getBookingStatus().name();

        }

        @Override
        public List<BookingDto> getAllBookingsByHotelId(Long hotelId) {
                Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(
                        () -> new resoucenotfoundexception("Hotel not found with id: "+hotelId)
                );
                user user = getCurrentUser();
                log.info(
                        "Fetching all bookings for hotel with id: {} by user with id: {}",
                        hotelId,
                        user.getId()
                );
                if (!user.equals(hotel.getOwner())) {
                        throw new Unauthorizedexception("Hotel does not belong to this user with id: "+user.getId());
                }
                List<booking> bookings = bookingRepository.findByHotel(hotel);
                return bookings.stream().map(booking -> modelMapper.map(booking, BookingDto.class)).toList();
        }

        @Override
        public HotelReportDto getHotelReport(Long hotelId, LocalDate startDate, LocalDate endDate) {
                Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(
                        () -> new resoucenotfoundexception("Hotel not found with id: "+hotelId)
                );
                user user = getCurrentUser();
                log.info(
                        "generating report of hotel with id: {} by user with id: {}",
                        hotelId,
                        user.getId()
                );
                if (!user.equals(hotel.getOwner())) {
                        throw new Unauthorizedexception("Hotel does not belong to this user with id: "+user.getId());
                }

                LocalDateTime startDateTime=startDate.atStartOfDay();
                LocalDateTime endDateTime=endDate.atTime(LocalTime.MAX);
                List<booking> bookings = bookingRepository.findByHotelAndCreatedAtBetween(hotel, startDateTime, endDateTime);
                Long totalConfirmedBooking=bookings.stream().filter(booking -> booking.getBookingStatus()==bookingStatus.CONFIRMED).count();
                BigDecimal totalRevenue=bookings.stream().filter(booking -> booking.getBookingStatus()==bookingStatus.CONFIRMED).map(booking::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal avgRevenuePerBooking=totalConfirmedBooking>0? totalRevenue.divide(BigDecimal.valueOf(totalConfirmedBooking),2,BigDecimal.ROUND_HALF_UP):BigDecimal.ZERO;
                return new HotelReportDto(totalConfirmedBooking,totalRevenue, avgRevenuePerBooking);
        }

        @Override
        public List<BookingDto> getMyBookings() {
                user user= getCurrentUser();
                return bookingRepository.findByUser(user).stream().map((element) -> modelMapper.map(element, BookingDto.class)).collect(Collectors.toList());
        }

        public boolean hasBookingExpired(booking bookings) {
                return bookings.getCreatedAt().plusMinutes(20).isBefore(java.time.LocalDateTime.now());
        }

        public user getCurrentUser() {
                // Implement this method to retrieve the currently logged-in user
                //user users = new user(); // Assuming you have a User entity and you can get the user details
                //users.setId(1l);
               // return users;
                return (user)  SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        }
        //TODO: i have to understand what is the proper flow of my stripe and webhook code which i have written and the webhook controller and checkout session which i have made

}
