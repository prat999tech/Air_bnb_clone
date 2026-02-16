package com.example.airbnb.security;

import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.airbnb.dto.SignupRequestDto;
import com.example.airbnb.dto.UserDto;
import com.example.airbnb.entity.user;
import com.example.airbnb.entity.enumsrole.roles;
import com.example.airbnb.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class AuthService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordencoder;

    public UserDto signUp(SignupRequestDto signuprequestdto) {
        user users = userRepository.findByEmail(signuprequestdto.getEmail()).orElse(null);
        if (users != null) {
            throw new RuntimeException("User is already present with same email id");

        }
        user newUser = modelMapper.map(signuprequestdto, user.class);
        newUser.setRoles(Set.of(roles.GUEST));
        newUser.setPassword(passwordencoder.encode(signuprequestdto.getPassword()));
        newUser = userRepository.save(newUser);
        return modelMapper.map(newUser, UserDto.class);

    }

}
