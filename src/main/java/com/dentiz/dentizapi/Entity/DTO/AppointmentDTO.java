package com.dentiz.dentizapi.Entity.DTO;

import com.dentiz.dentizapi.Entity.Appointment;
import jakarta.annotation.Nullable;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AppointmentDTO {

    @Nullable
    private Long id;

    private Integer serviceId;

    private String patientName;

    private String patientEmail;

    private String patientPhone;

    private String service;

    private LocalDate date;

    private String hour;

    public AppointmentDTO(Appointment appointment) {
        this.id = appointment.getId();
        this.patientName = appointment.getPatient().getNameComplete();
        this.patientEmail = appointment.getPatient().getEmail();
        this.patientPhone = appointment.getPatient().getPhone();
        this.service = appointment.getService().getName();
        this.date = appointment.getDate();
        this.hour = appointment.getHour();
    }

    public AppointmentDTO() {
    }
}
