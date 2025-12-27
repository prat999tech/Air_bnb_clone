package com.example.airbnb.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;

import com.example.airbnb.entity.enumsrole.Gender;

import java.util.Set;

import com.example.airbnb.entity.user;

@Entity
@Getter
@Setter
public class Guest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * @ManyToOne
     * 
     * @JoinColumn(name = "user_id")
     * private user user;
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private user user;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @ManyToMany(mappedBy = "guests")
    private Set<booking> bookings;

}
