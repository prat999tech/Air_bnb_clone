package com.example.airbnb.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.airbnb.dto.HotelPrice;
import com.example.airbnb.entity.Hotel;
import com.example.airbnb.entity.HotelMinPrice;

public interface HotelMinPriceRepository extends JpaRepository<HotelMinPrice, Long> {

    @Query("SELECT new com.example.airbnb.dto.HotelPrice(i.hotel, AVG(i.price)) FROM HotelMinPrice i"
            + " WHERE i.hotel.city = :city"
            + " AND i.date BETWEEN :startdate AND :enddate"
            + " AND i.hotel.active = true"
            + " GROUP BY i.hotel")
    Page<HotelPrice> findhotelwithavailableinventory(
            @Param("city") String city,
            @Param("startdate") LocalDate startdate,
            @Param("enddate") LocalDate enddate,
            @Param("roomsCount") Integer roomsCount,
            @Param("datecount") Integer datecount,
            Pageable peagble

    );

    Optional<HotelMinPrice> findByHotelAndDate(Hotel hotel, LocalDate date);

}
