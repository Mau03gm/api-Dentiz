package com.dentiz.dentizapi.Service;

import com.dentiz.dentizapi.Entity.Appointment;
import com.dentiz.dentizapi.Entity.DTO.AppointmentDTO;
import com.dentiz.dentizapi.Entity.Dentist;
import com.dentiz.dentizapi.Entity.Patient;
import com.dentiz.dentizapi.Entity.ServiceEntity;
import com.dentiz.dentizapi.Repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class AppointmentService {


    private PatientService patientService;
    private DentistService dentistService;
    private AppointmentRepository appointmentRepository;
    private ServicesService servicesService;

    @Autowired
    public AppointmentService(PatientService patientService, DentistService dentistService, AppointmentRepository appointmentRepository) {
        this.patientService = patientService;
        this.dentistService = dentistService;
        this.appointmentRepository = appointmentRepository;
    }

    public AppointmentDTO addAppointment(AppointmentDTO appointmentDTO, String username) throws Exception {
        Patient patient = new Patient(appointmentDTO);
        patient = patientService.checkPatient(patient);
        ServiceEntity service = servicesService.getService(appointmentDTO.getServiceId());
        Dentist dentist = dentistService.validateIfDentistExists(username, username);
        Appointment appointment = new Appointment(appointmentDTO, dentist.getDentistDetails(), patient, service);
        return appointmentDTO;
    }

    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }

    public AppointmentDTO updateAppointment(AppointmentDTO appointmentDTO) throws Exception {
        Appointment appointment = appointmentRepository.findById(appointmentDTO.getId()).orElse(null);
        if (appointment == null) {
            throw  new Exception("Cita no encontrada");
        }
       appointment=  appointmentRepository.save(appointment);

        return new AppointmentDTO(appointment);
    }

    public AppointmentDTO getAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id).orElse(null);
        if (appointment == null) {
            return null;
        }
        return new AppointmentDTO(appointment);
    }

    public List<AppointmentDTO> getAppointmentsByDentist(String username) throws Exception {
        Dentist dentist = dentistService.validateIfDentistExists(username, username);
        List<Appointment> appointments = appointmentRepository.findByDentistDetails(dentist.getDentistDetails());
        return appointments.stream().map(AppointmentDTO::new).toList();

    }
}
