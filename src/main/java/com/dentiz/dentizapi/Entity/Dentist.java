package com.dentiz.dentizapi.Entity;

import com.dentiz.dentizapi.Entity.DTO.DentistProfileDTO;
import com.dentiz.dentizapi.Entity.DTO.RegisterDentistDTO;
import com.dentiz.dentizapi.Entity.Enum.SubscriptionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table (name = "dentist", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")})
@NoArgsConstructor
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

    @OneToOne(mappedBy = "dentist", fetch = FetchType.LAZY)
    private DentistDetails dentistDetails;

    public Dentist(RegisterDentistDTO dentistDTO) {
        this.username = dentistDTO.getUsername();
        this.firstName = dentistDTO.getFirstName();
        this.lastName = dentistDTO.getLastName();
        this.email = dentistDTO.getEmail();
        this.phone = dentistDTO.getPhone();
    }

    public void updateDentistProfile(DentistProfileDTO dentistProfileDTO){
        this.license = dentistProfileDTO.getLicense();
        this.description = dentistProfileDTO.getDescription();
    }

}
