package com.example.airbnb.dto;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.example.airbnb.entity.hotelcontactinfo;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data

public class HotelDto {

    private Long id;

    private String name;

    private String city;

    private String[] photos;

    private String[] amenties;

    private hotelcontactinfo contactInfo;

    private Boolean active;

}
