package com.example.airbnb.dto;

import lombok.Data;

@Data
public class SignupRequestDto {
    private String email, password;
    private String name;

}
