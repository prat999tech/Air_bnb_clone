package com.example.airbnb.controller;

import com.example.airbnb.dto.BookingDto;
import com.example.airbnb.dto.Userprofile;
import com.example.airbnb.service.BookingService;
import com.example.airbnb.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")


public class UserController {

    private final UserService userService;
    private final BookingService bookingService;

    @PutMapping("/profile")
    public ResponseEntity<Void> updateUserProfile(@RequestBody Userprofile userprofile) {
        userService.updateProfile(userprofile);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/myBooking")
    public ResponseEntity<List<BookingDto>> getMyBookings(){
        return ResponseEntity.ok(bookingService.getMyBookings());
    }
}
