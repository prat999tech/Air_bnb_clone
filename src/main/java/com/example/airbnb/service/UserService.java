package com.example.airbnb.service;

import com.example.airbnb.dto.Userprofile;
import com.example.airbnb.entity.user;

public interface UserService {

    user getUserById(Long id);

    void updateProfile(Userprofile userprofile);
}
