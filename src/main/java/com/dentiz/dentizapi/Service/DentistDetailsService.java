package com.dentiz.dentizapi.Service;

import com.dentiz.dentizapi.Entity.Dentist;
import com.dentiz.dentizapi.Entity.DentistDetails;
import com.dentiz.dentizapi.Entity.Hour;
import com.dentiz.dentizapi.Repository.DentistDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DentistDetailsService {

    @Autowired
    private DentistDetailsRepository dentistDetailsRepository;

    public void addDentistToDentistDetails(Dentist dentist) {
        DentistDetails dentistDetails = new DentistDetails();
        dentistDetails.setDentist(dentist);
        dentistDetailsRepository.save(dentistDetails);
    }

    public void addHoursToDentistDetails(Hour hour, Dentist dentist) {
        dentist.getDentistDetails().setHour(hour);
        dentistDetailsRepository.save(dentist.getDentistDetails());
    }
}
