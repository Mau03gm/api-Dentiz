package com.dentiz.dentizapi.Repository;

import com.dentiz.dentizapi.Entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    Patient findByEmail(String email);
}
