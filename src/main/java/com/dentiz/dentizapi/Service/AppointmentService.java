package com.dentiz.dentizapi.Service;

import com.dentiz.dentizapi.Components.Mails.MailService.MailService;
import com.dentiz.dentizapi.Components.Mails.MailStructure;
import com.dentiz.dentizapi.Components.Stripe.Service.Services.StripeServices;
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
import java.util.logging.Logger;

@Service
public class AppointmentService {

    private PatientService patientService;
    private DentistService dentistService;
    private AppointmentRepository appointmentRepository;
    private ServicesService servicesService;
    private StripeServices stripeServices;
    private PriceServiceService priceServiceService;
    private MailService mailService;

    @Autowired
    public AppointmentService(PatientService patientService, DentistService dentistService, AppointmentRepository appointmentRepository,
                              ServicesService servicesService, StripeServices stripeServices, PriceServiceService priceServiceService,
                              MailService mailService) {
        this.patientService = patientService;
        this.dentistService = dentistService;
        this.servicesService = servicesService;
        this.appointmentRepository = appointmentRepository;
        this.stripeServices = stripeServices;
        this.priceServiceService = priceServiceService;
        this.mailService = mailService;
    }

    public AppointmentDTO addAppointment(AppointmentDTO appointmentDTO, String username) throws Exception {
        Patient patient = patientService.checkPatient(appointmentDTO);
        ServiceEntity service = servicesService.getService(appointmentDTO.getServiceId());;
        Dentist dentist = dentistService.validateIfDentistExists(username, username);
        DentistDetails dentistDetails = dentist.getDentistDetails();
        if (validateIfAppointmentExists(dentistDetails, appointmentDTO.getDate(), appointmentDTO.getHour())) {
            throw new Exception("Appointment already exists");
        }
        Appointment appointment = new Appointment(appointmentDTO, dentistDetails, patient, service);
        PriceService priceService =priceServiceService.getPriceService(service, dentistDetails);
        stripeServices.createPaymentIntent(appointmentDTO.getPaymentMethod(), priceService, dentist  );
        appointmentRepository.save(appointment);  
        MailStructure mailStructure = MailStructure.builder()
                .subject("Cita creada")
                .body("Tiene una cita programada para el dia " + appointmentDTO.getDate() + " a las " + appointmentDTO.getHour())
                .build();
        try {
            mailService.sendMail(dentist.getEmail(), mailStructure);
            mailService.sendMail(patient.getEmail(), mailStructure);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        List<Appointment> appointments = dentist.getDentistDetails().getAppointments();
        return appointments.stream().map(AppointmentDTO::new).toList();
    }

    public HoursDTO getHoursByDentistAndDate(String username, LocalDate date) throws Exception {
        Dentist dentist = dentistService.validateIfDentistExists(username, username);
        List<Appointment> appointments = appointmentRepository.findAppointmentsByDentistDetailsAndDate(dentist.getDentistDetails(), date);
        System.out.println(appointments.size());
        if (appointments.isEmpty()) {
            System.out.println("No hay citas");
            return HoursDTO.builder().hours(dentist.getDentistDetails().getHour().getHours()).build();
        }
        List<String> hoursBusy = appointments.stream().map(Appointment::getHour).toList();
        List<String> hoursDentist = deleteBusyHours(dentist.getDentistDetails().getHour(), hoursBusy);
        return HoursDTO.builder().hours(hoursDentist.toArray(String[]::new)).build();
    }

    public List<AppointmentDTO> getAppointmentsByDateAndDentist(String username, LocalDate date) throws Exception {
        Dentist dentist = dentistService.validateIfDentistExists(username, username);
        List<Appointment> appointments = appointmentRepository.findAppointmentsByDentistDetailsAndDate(dentist.getDentistDetails(), date);
        return appointments.stream().map(AppointmentDTO::new).toList();
    }

    public List<AppointmentDTO> getAppointmentsByMonthAndYearAndDentist(String username, AppointmentDTO appointmentDTO) throws Exception {
        Dentist dentist = dentistService.validateIfDentistExists(username, username);
        LocalDate date= appointmentDTO.getDate();
        List<Appointment> appointments = appointmentRepository.findByDentistDetailsAndYearAndMonth(dentist.getDentistDetails(), date.getYear(), date.getMonthValue());
        return appointments.stream().map(AppointmentDTO::new).toList();

    }

    private List<String> deleteBusyHours(Hour hours, List<String> busyHours) {
        List<String> hoursDentist = new ArrayList<>(Arrays.asList(hours.getHours()));
        hoursDentist.removeAll(busyHours);
        return hoursDentist;
    }

    private boolean validateIfAppointmentExists(DentistDetails dentistDetails, LocalDate date, String hour) throws Exception {
        return appointmentRepository.existsByDateAndDentistDetailsAndHour( date, dentistDetails, hour);
    }

}
