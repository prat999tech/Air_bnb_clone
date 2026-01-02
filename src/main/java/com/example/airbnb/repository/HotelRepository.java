package com.example.airbnb.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.airbnb.entity.Hotel;

public interface HotelRepository extends JpaRepository<Hotel, Long> {

}
