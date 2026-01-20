package com.example.airbnb.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.airbnb.dto.RoomDto;
import com.example.airbnb.service.RoomService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@Slf4j

@RequestMapping("/admin/hotels/{hotelId}/rooms")
@RequiredArgsConstructor

public class RoomController {

    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<RoomDto> createNewRoom(@PathVariable Long hotelId, @RequestBody RoomDto roomDto) {
        log.info("attempting to create new room in hotel with id: {}", hotelId);
        RoomDto room = roomService.createNewRoom(hotelId, roomDto);
        return new ResponseEntity<>(room, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RoomDto>> getAllRoomsInHotel(@PathVariable Long hotelId) {
        return ResponseEntity.ok(roomService.getAllRoomsInHotel(hotelId));

    }

    @GetMapping("/{roomId}")
    public ResponseEntity<RoomDto> getRoomById(@PathVariable Long roomId, @PathVariable Long hotelId) {
        return ResponseEntity.ok(roomService.getRoomById(roomId));

    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<Boolean> deleteRoomById(@PathVariable Long roomId, @PathVariable Long hotelId) {
        roomService.deleteRoomById(roomId);
        return ResponseEntity.noContent().build();

    }

}
