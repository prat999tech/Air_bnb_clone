package com.example.airbnb.entity;

import java.util.Set;

import javax.management.relation.Role;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.GenerationType;

@Entity
@Getter
@Setter
 @Table(name="app_user")
public class user {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String name;
    
    @ElementCollection(fetch = FetchType.EAGER) // this annotation is used to define a collection of basic or embeddable types. here we are using it to store a set of enum values in a separate table and that table name will be app_user_roles as our main table is app_user and the collection field is roles 
    @Enumerated(EnumType.ORDINAL) // this will store the ordinal (position) of the enum value in the database i.e in enum Role class if guest id is 1 and hotel_manager id is 2 then in db it will store 1 for guest and 2 for hotel_manager
    private Set<Role> roles;



}
