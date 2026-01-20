package com.example.airbnb.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;

import com.example.airbnb.entity.enumsrole.Gender;

import java.util.Set;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.airbnb.entity.user;

@EntityListeners(AuditingEntityListener.class) // we have to create auditable base entity class if we want to
                                               // use auditing fields in multiple entities as it is not a good practice
                                               // to
                                               // repeat the same fields in multiple entities and we have to mke those
                                               // entities extend that base class and we have to implemet auditor aware
                                               // using spring security to get current logged in user for @CreatedBy and
                                               // @LastModifiedBy fields

@Entity
@Getter
@Setter
public class Guest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * @ManyToOne
     * 
     * @JoinColumn(name = "user_id")
     * private user user;
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private user user;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @ManyToMany(mappedBy = "guests")
    private Set<booking> bookings;

}
