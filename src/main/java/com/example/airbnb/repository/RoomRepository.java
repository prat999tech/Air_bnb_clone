package com.example.airbnb.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.airbnb.entity.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {

}
