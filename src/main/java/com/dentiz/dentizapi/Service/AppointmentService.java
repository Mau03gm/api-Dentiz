package com.dentiz.dentizapi.Service;

import com.dentiz.dentizapi.Entity.*;
import com.dentiz.dentizapi.Entity.DTO.AppointmentDTO;
import com.dentiz.dentizapi.Entity.DTO.HoursDTO;
import com.dentiz.dentizapi.Repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class AppointmentService {


    private PatientService patientService;
    private DentistService dentistService;
    private AppointmentRepository appointmentRepository;
    private ServicesService servicesService;

    @Autowired
    public AppointmentService(PatientService patientService, DentistService dentistService, AppointmentRepository appointmentRepository, ServicesService servicesService) {
        this.patientService = patientService;
        this.dentistService = dentistService;
        this.servicesService = servicesService;
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

    public HoursDTO getHoursByDentistAndDate(String username, AppointmentDTO appointmentDTO) throws Exception {
        Dentist dentist = dentistService.validateIfDentistExists(username, username);
        List<Appointment> appointments = appointmentRepository.findByDentistDetailsAndDate(dentist.getDentistDetails(), appointmentDTO.getDate());
        if (appointments.isEmpty()) {
            return HoursDTO.builder().hours(dentist.getDentistDetails().getHour().getHours()).build();
        }
        String[] hoursBusy = appointments.stream().map(Appointment::getHour).toArray(String[]::new);
        List<String> hoursDentist = deleteBusyHours(dentist.getDentistDetails().getHour(), hoursBusy);
        return HoursDTO.builder().hours(hoursDentist.toArray(String[]::new)).build();
    }

    public List<AppointmentDTO> getAppointmentsByDate(LocalDate date) {
        List<Appointment> appointments = appointmentRepository.findByDate(date);
        return appointments.stream().map(AppointmentDTO::new).toList();
    }

    public List<AppointmentDTO> getAppointmentsByDateAndDentist(String username, LocalDate date) throws Exception {
        Dentist dentist = dentistService.validateIfDentistExists(username, username);
        List<Appointment> appointments = appointmentRepository.findByDentistDetailsAndDate(dentist.getDentistDetails(), date);
        return appointments.stream().map(AppointmentDTO::new).toList();
    }

    public List<AppointmentDTO> getAppointmentsByMonthAndYearAndDentist(String username, AppointmentDTO appointmentDTO) throws Exception {
        Dentist dentist = dentistService.validateIfDentistExists(username, username);
        LocalDate date= appointmentDTO.getDate();
        List<Appointment> appointments = appointmentRepository.findByDentistDetailsAndYearAndMonth(dentist.getDentistDetails(), date.getYear(), date.getMonthValue());
        return appointments.stream().map(AppointmentDTO::new).toList();

    }

    private List<String> deleteBusyHours(Hour hours, String[] busyHours) {
        List<String> hoursDentist = new ArrayList<>(Arrays.asList(hours.getHours()));
        hoursDentist.removeAll(Arrays.asList(busyHours));
        return hoursDentist;
    }
}
