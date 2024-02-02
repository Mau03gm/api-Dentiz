package com.dentiz.dentizapi.Service;

import com.dentiz.dentizapi.Entity.DTO.HoursDTO;
import com.dentiz.dentizapi.Entity.Dentist;
import com.dentiz.dentizapi.Entity.Hour;
import com.dentiz.dentizapi.Repository.HourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class HourService {

    @Autowired
    private HourRepository hourRepository;
    @Autowired
    private DentistService dentistService;
    @Autowired
    private DentistDetailsService dentistDetailsService;

    public HoursDTO addHourToDentist( String username, String[] hours) throws Exception {
        Dentist dentist = dentistService.validateIfDentistExists(username, username);
        Hour hour= new Hour(hours);
        dentistDetailsService.addHoursToDentistDetails(hour, dentist);
        return HoursDTO.builder()
                .hours(hour.getHours())
                .build();
    }

    public HoursDTO getHoursByDentist(String username) throws Exception {
        Dentist dentist = dentistService.validateIfDentistExists(username, username);
        Hour hour= dentist.getDentistDetails().getHour();
        HoursDTO hours;
        return hours= HoursDTO.builder()
                .hours(hour.getHours())
                .build();
    }

    public HoursDTO editHours(String username, String[] hours) throws Exception {
        Dentist dentist = dentistService.validateIfDentistExists(username, username);
        Hour hour= dentist.getDentistDetails().getHour();
        hour.setHours(hours);
        hourRepository.save(hour);
        return HoursDTO.builder()
                .hours(hour.getHours())
                .build();
    }


}
