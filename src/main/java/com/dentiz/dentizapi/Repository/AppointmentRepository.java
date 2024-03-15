package com.dentiz.dentizapi.Repository;

import com.dentiz.dentizapi.Entity.Appointment;
import com.dentiz.dentizapi.Entity.DentistDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("SELECT a FROM Appointment a WHERE a.dentistDetails = :dentistDetails")
    List<Appointment> findAppointmentsByDentistDetails(@Param("dentistDetails") DentistDetails dentistDetails);
    List<Appointment> findAppointmentsByDentistDetailsAndDate(DentistDetails dentistDetails, LocalDate date);
    List<Appointment> findByDate(LocalDate date);

    @Query("SELECT a FROM Appointment a WHERE a.dentistDetails = ?1 AND YEAR(a.date) = ?2 AND MONTH(a.date) = ?3")
    List<Appointment> findByDentistDetailsAndYearAndMonth(DentistDetails dentistDetails, int year, int month);

    boolean existsByDateAndDentistDetailsAndHour(LocalDate date, DentistDetails dentistDetails, String hour);
}
