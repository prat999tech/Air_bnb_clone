package com.example.airbnb.service;

import org.springframework.stereotype.Service;

import com.example.airbnb.entity.user;
import com.example.airbnb.exception.resoucenotfoundexception;
import com.example.airbnb.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public user getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new resoucenotfoundexception("User not found"));
    }

}
