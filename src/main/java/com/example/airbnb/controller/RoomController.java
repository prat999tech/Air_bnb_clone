package com.example.airbnb.controller;

import org.springframework.web.bind.annotation.*;

import com.example.airbnb.dto.RoomDto;
import com.example.airbnb.service.RoomService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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

    @PutMapping("/{roomId}")
    public ResponseEntity<RoomDto> updateRoomById(@PathVariable Long roomId, @PathVariable Long hotelId,
            @RequestBody RoomDto roomDto) {
        return ResponseEntity.ok(roomService.updateRoomById(hotelId, roomId, roomDto));
    }

}
