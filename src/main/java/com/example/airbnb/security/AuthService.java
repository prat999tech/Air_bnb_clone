package com.example.airbnb.security;

import java.util.Set;

import com.example.airbnb.dto.LoginDto;
import com.example.airbnb.exception.resoucenotfoundexception;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

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

    public String[] login(LoginDto logindto) {
        //here wwe are returning array of 2 strings i.e. access token and refresh token
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(logindto.getEmail(), logindto.getPassword()));
        user users = (user) authentication.getPrincipal(); //in the jwt auth filter we are setting the user details in the security context and here we are getting that user details from the security context and then we are generating the access token and refresh token for that user i.e. "UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user,
        //null, user.getAuthorities());" with the help of this code we are setting user details in the security context folder
        String[] arr = new String[2];
        arr[0] = jwtService.generateAccessToken(users);
        arr[1] = jwtService.generateRefreshToken(users);
        return arr;


    }

    public String refreshToken(String refreshToken) {
        Long id = jwtService.getUserIdFromToken(refreshToken);
        user users = userRepository.findById(id).orElseThrow(() -> new resoucenotfoundexception("User not found with id: " + id));
        return jwtService.generateAccessToken(users);
    }
}



