package com.dentiz.dentizapi.Entity;

import com.dentiz.dentizapi.Entity.DTO.AppointmentDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "appointment")
@Getter
@Setter
@NoArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date", nullable = false)
    private LocalDate date;
    @Column(name = "hour", nullable = false)
    private String hour;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dentist_details_id")
    private DentistDetails dentistDetails;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    private ServiceEntity service;

    public Appointment(AppointmentDTO appointmentDTO, DentistDetails dentistDetails, Patient patient, ServiceEntity service) {
        this.date = appointmentDTO.getDate();
        this.hour = appointmentDTO.getHour();
        this.dentistDetails =  dentistDetails;
        this.patient = patient;
        this.service = service;
    }
}
