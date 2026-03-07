package com.example.airbnb.Auth;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;

public class Auditorawareimpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        // Here, you would typically fetch the currently authenticated user's username
        // or ID from the security context.
        // For simplicity, we'll return a fixed username.
        return Optional.of("system_user");
    }

}
