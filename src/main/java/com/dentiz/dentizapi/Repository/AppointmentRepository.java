package com.dentiz.dentizapi.Repository;

import com.dentiz.dentizapi.Entity.Appointment;
import com.dentiz.dentizapi.Entity.DentistDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByDentistDetails(DentistDetails dentistDetails);
}
