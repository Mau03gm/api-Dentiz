package com.dentiz.dentizapi.Service;

import com.dentiz.dentizapi.Components.Stripe.Plan;
import com.dentiz.dentizapi.Components.Stripe.Service.StripeService;
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
    @Autowired
    private StripeService stripeService;

    public void addDentistToDentistDetails(Dentist dentist, String token) {
        DentistDetails dentistDetails = new DentistDetails();
        dentistDetails.setDentist(dentist);
        dentistDetails.setCostumerId( stripeService.createCostumer(dentist, token));
       Plan plan = stripeService.getPlan("Basic");
       dentistDetails.setSubscriptionId(stripeService.createCostumerSubscription(dentistDetails.getCostumerId(),plan ));
        dentistDetailsRepository.save(dentistDetails);
    }

    public void addHoursToDentistDetails(Hour hour, Dentist dentist) {
        dentist.getDentistDetails().setHour(hour);
        dentistDetailsRepository.save(dentist.getDentistDetails());
    }
}
