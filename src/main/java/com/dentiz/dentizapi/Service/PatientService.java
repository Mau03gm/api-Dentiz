package com.dentiz.dentizapi.Service;

import com.dentiz.dentizapi.Entity.DTO.AppointmentDTO;
import com.dentiz.dentizapi.Entity.Patient;
import com.dentiz.dentizapi.Repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    public Patient addPatient(Patient patient) {
        patientRepository.save(patient);
        return patient;
    }

    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }

    public void updatePatient(Patient patient) {
        patientRepository.save(patient);
    }

    public Patient getPatient(Long id) {
        return patientRepository.findById(id).orElse(null);
    }

    public Patient checkPatient(AppointmentDTO appointmentDTO) {
       Patient patientFound = patientRepository.findByEmail(appointmentDTO.getPatientEmail());
        if (patientFound == null) {
            Patient patient = new Patient(appointmentDTO);
           return addPatient(patient);
        }
        return patientFound;
    }
}
