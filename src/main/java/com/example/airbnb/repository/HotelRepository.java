package com.example.airbnb.repository;

import com.example.airbnb.entity.user;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.airbnb.entity.Hotel;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Long> {

    List<Hotel> findByOwner(user user);
}
