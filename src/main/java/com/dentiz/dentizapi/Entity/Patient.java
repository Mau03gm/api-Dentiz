package com.dentiz.dentizapi.Entity;

import com.dentiz.dentizapi.Entity.DTO.PatientDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "patient")
@Getter
@Setter
@NoArgsConstructor
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name_complete", nullable = false, length = 100)
    private String nameComplete;

    @Column(name = "email", nullable = false, unique = true, length = 50)
    private String email;

    public Patient(PatientDTO patientDTO) {
        this.nameComplete = patientDTO.getNameComplete();
        this.email = patientDTO.getEmail();
    }
}
