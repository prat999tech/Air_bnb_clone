package com.example.airbnb.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Embeddable // basically tells Hibernate "Hey, this class is not a full entity on its own,
            // but its fields can be embedded into other entities as part of their table
            // structure." so we use it to embedded it into Hotel entity

public class hotelcontactinfo {
    private String phoneNumber;
    private String address;
    private String email;
    private String location;

}
