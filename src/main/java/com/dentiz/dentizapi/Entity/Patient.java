package com.dentiz.dentizapi.Entity;

import com.dentiz.dentizapi.Entity.DTO.AppointmentDTO;
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

    @Column(name = "phone", nullable = true, unique = false, length = 15)
    private String phone;

    public Patient(AppointmentDTO appointmentDTO) {
        this.nameComplete = appointmentDTO.getPatientName();
        this.email = appointmentDTO.getPatientEmail();
        this.phone = appointmentDTO.getPatientPhone();
    }
}
