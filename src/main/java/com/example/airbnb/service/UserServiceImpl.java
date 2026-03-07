package com.example.airbnb.service;

import com.example.airbnb.dto.Userprofile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.airbnb.entity.user;
import com.example.airbnb.exception.resoucenotfoundexception;
import com.example.airbnb.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService { // we need this UserDetailsService to load user by username for authentication which is used by authentication manager
    private final UserRepository userRepository;

    public user getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new resoucenotfoundexception("User not found"));
    }

    @Override
    public void updateProfile(Userprofile userprofile) {
        user user= getCurrentUser();
        if(userprofile.getName()!=null) {
            user.setName(userprofile.getName());
        }
        if(userprofile.getDateofBirth()!=null) {
            user.setBirthdate(userprofile.getDateofBirth());
        }
        if(userprofile.getGender()!=null) {
            user.setGender(userprofile.getGender());
        }
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }

    public user getCurrentUser() {

        return (user)  SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    }
}
