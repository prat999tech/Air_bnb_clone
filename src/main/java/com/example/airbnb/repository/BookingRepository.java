package com.example.airbnb.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.airbnb.entity.booking;

public interface BookingRepository extends JpaRepository<booking, Long> {

}
