package com.dentiz.dentizapi.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table (name = "dentist", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")})
public class Dentist {

    @Id
    private String username;

    @Column (name = "first_name", nullable = false )
    private String firstName;

    @Column (name = "last_name", nullable = false )
    private String lastName;

    @Column (name = "license", nullable = true )
    private String license;

    @Column (name = "email", nullable = false )
    private String email;

    @Column (name = "password", nullable = false )
    private String password;

    @Column (name = "description", nullable = true )
    private String description;

    public Dentist(String username, String firstName, String lastName, String license, String email, String password, String description) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.license = license;
        this.email = email;
        this.password = password;
        this.description = description;
    }

    public void updateDentist(){
        this.firstName = firstName;
        this.lastName = lastName;
        this.license = license;
        this.email = email;
        this.password = password;
        this.description = description;
    }

    public Dentist() {
    }
}
