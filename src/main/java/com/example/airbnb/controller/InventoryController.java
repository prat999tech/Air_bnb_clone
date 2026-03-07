package com.example.airbnb.controller;

import com.example.airbnb.dto.InventoryDto;
import com.example.airbnb.dto.UpdateInventoryRequestDto;
import com.example.airbnb.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;


    @GetMapping("/rooms/{roomId}")
    public ResponseEntity<List<InventoryDto>> getAllInventory(@PathVariable long roomId){
        return ResponseEntity.ok(inventoryService.getAllInventory(roomId));

    }
    @PatchMapping("/rooms/{roomId}")
    public ResponseEntity<Void> updateInventory(@PathVariable Long roomId, @RequestBody UpdateInventoryRequestDto updateInventoryRequestDto){
        inventoryService.updateInventory(roomId, updateInventoryRequestDto);
        return ResponseEntity.noContent().build();
    }
}
