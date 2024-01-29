package com.dentiz.dentizapi.Entity;

import com.dentiz.dentizapi.Entity.DTO.EditDentistProfileDTO;
import com.dentiz.dentizapi.Entity.DTO.RegisterDentistDTO;
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

    @Column (name = "first_name", nullable = false, length = 50 )
    private String firstName;

    @Column (name = "last_name", nullable = false, length = 50 )
    private String lastName;

    @Column (name = "license", unique = true, nullable = true, length = 50 )
    private String license;

    @Column (name = "phone", nullable = false, length = 15 )
    private String phone;

    @Column (name = "email", nullable = false )
    private String email;

    @Column (name = "password", nullable = false )
    private String password;

    @Column (name = "description", nullable = true, length = 255)
    private String description;

    @OneToOne(mappedBy = "dentist")
    private DentistService dentistService;

    public Dentist(RegisterDentistDTO dentistDTO) {
        this.username = dentistDTO.getUsername();
        this.firstName = dentistDTO.getFirstName();
        this.lastName = dentistDTO.getLastName();
        this.email = dentistDTO.getEmail();
        this.phone = dentistDTO.getPhone();
    }

    public void updateDentistProfile(EditDentistProfileDTO editDentistProfileDTO){
        this.username = editDentistProfileDTO.getUsername();
        this.firstName = editDentistProfileDTO.getFirstName();
        this.lastName = editDentistProfileDTO.getLastName();
        this.license = editDentistProfileDTO.getLicense();
        this.description = editDentistProfileDTO.getDescription();
    }

    public Dentist() {
    }
}
