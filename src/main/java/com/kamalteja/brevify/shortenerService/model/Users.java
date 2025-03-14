package com.kamalteja.brevify.shortenerService.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class Users {

    @Id
    private String id;

    private String username;

    private String email;

    private String password;
}
