package com.example.airbnb.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.example.airbnb.Auth.Auditorawareimpl;

@Configuration

@EnableJpaAuditing(auditorAwareRef="getauditor") // this is required to enable JPA Auditing for @CreatedDate, @LastModifiedDate,
                   // etc. and it is done in only config class only
                   // auditorAwareRef is used to specify the bean name of AuditorAware implementation class.


public class MapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    AuditorAware<String> getauditor() {
        return new Auditorawareimpl();
    }

}
