package com.dentiz.dentizapi.Controller;


import com.dentiz.dentizapi.Application.Application;
import com.dentiz.dentizapi.Entity.DTO.AppointmentDTO;
import com.dentiz.dentizapi.Entity.DTO.HoursDTO;
import com.dentiz.dentizapi.Service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Application.API_BASE_PATH+"/appointment")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping("/{username}")
    public ResponseEntity<AppointmentDTO> addAppointment(@PathVariable String username, @RequestBody AppointmentDTO appointmentDTO) throws Exception {
        return ResponseEntity.ok().body(appointmentService.addAppointment(appointmentDTO, username));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
       return ResponseEntity.ok().build();
    }

    @GetMapping("/{username}")
    public ResponseEntity <List<AppointmentDTO>> getAppointments(@PathVariable String username) throws Exception {
        return ResponseEntity.ok().body( appointmentService.getAppointmentsByDentist(username));
    }

    @GetMapping("/{username}/andDate")
    public ResponseEntity <List<AppointmentDTO>> getAppointmentsByDate(@PathVariable String username, @RequestParam AppointmentDTO appointmentDTO) throws Exception {
        return ResponseEntity.ok().body( appointmentService.getAppointmentsByDateAndDentist(username, appointmentDTO.getDate()));
    }

    @GetMapping("/{username}/andMonth")
    public ResponseEntity <List<AppointmentDTO>> getAppointmentsByMonth(@PathVariable String username, @RequestParam AppointmentDTO appointmentDTO) throws Exception {
        return ResponseEntity.ok().body( appointmentService.getAppointmentsByMonthAndYearAndDentist(username, appointmentDTO));
    }

    @GetMapping("/{username}/hours")
    public ResponseEntity <HoursDTO> getHours(@PathVariable String username, @RequestParam AppointmentDTO appointmentDTO) throws Exception {
        return ResponseEntity.ok().body(appointmentService.getHoursByDentistAndDate(username, appointmentDTO));
    }
}

