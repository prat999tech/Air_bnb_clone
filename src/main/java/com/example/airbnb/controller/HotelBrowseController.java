package com.example.airbnb.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.airbnb.dto.HotelDto;
import com.example.airbnb.dto.HotelPrice;
import com.example.airbnb.dto.HotelinfoDto;
import com.example.airbnb.dto.Hotelsearch;
import com.example.airbnb.service.HotelService;
import com.example.airbnb.service.InventoryService;

import org.springframework.data.domain.Page;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@RestController

@RequestMapping("/hotels")
@RequiredArgsConstructor

// for searching hotels we wiil look into inventories

public class HotelBrowseController {
    private final InventoryService inventoryService;
    private final HotelService hotelService;

    @GetMapping("/search")
    public ResponseEntity<Page<HotelPrice>> searchHotels(@RequestBody Hotelsearch hotelsearchrequest) {
        Page<HotelPrice> page = inventoryService.searchHotels(hotelsearchrequest);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{hotelId}/info")
    public ResponseEntity<HotelinfoDto> getHotelInfo(@PathVariable Long hotelId) {
        return ResponseEntity.ok(hotelService.getHotelInfoById(hotelId));
    }

}
