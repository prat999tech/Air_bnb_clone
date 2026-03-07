package com.example.airbnb.repository;

import com.example.airbnb.entity.Hotel;
import com.example.airbnb.entity.user;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.airbnb.entity.booking;

import java.lang.ScopedValue;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<booking, Long> {

    Optional<booking> findBySessionId(String sessionId);

    List<booking> findByHotel(Hotel hotel);

    List<booking> findByHotelAndCreatedAtBetween(Hotel hotel, LocalDateTime startDate, LocalDateTime endDate);

    Collection<booking> findByUser(user user);
}
