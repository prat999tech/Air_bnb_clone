package com.example.airbnb.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.airbnb.entity.Guest;

public interface GuestRepository extends JpaRepository<Guest, Long> {

}
