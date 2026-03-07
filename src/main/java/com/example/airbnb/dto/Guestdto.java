package com.example.airbnb.dto;

import java.util.Set;

import com.example.airbnb.entity.booking;
import com.example.airbnb.entity.user;
import com.example.airbnb.entity.enumsrole.Gender;

import lombok.Data;

@Data

public class Guestdto {

    private Long id;

    private user user;

    private Gender gender;

    private Set<booking> bookings;

}
