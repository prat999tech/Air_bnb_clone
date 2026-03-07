package com.example.airbnb.dto;

import com.example.airbnb.entity.enumsrole.Gender;
import lombok.Data;

import java.time.LocalDate;
@Data

public class Userprofile {
    private String name;
    private LocalDate dateofBirth;
    private Gender gender;
}
