package com.example.airbnb.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.airbnb.entity.user;
import java.util.*;

public interface UserRepository extends JpaRepository<user, Long> {

    Optional<user> findByEmail(String email);

}
