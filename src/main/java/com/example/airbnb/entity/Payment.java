package com.example.airbnb.entity;

import java.math.BigDecimal;

import com.example.airbnb.entity.enumsrole.Paymentstatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.GenerationType;

@Entity
@Getter
@Setter

public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String transactionId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Paymentstatus status;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

}
