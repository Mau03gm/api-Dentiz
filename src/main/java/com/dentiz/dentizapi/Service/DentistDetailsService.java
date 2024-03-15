package com.dentiz.dentizapi.Service;

import com.dentiz.dentizapi.Components.Stripe.Plan;
import com.dentiz.dentizapi.Components.Stripe.Service.Subscription.StripeSubscriptions;
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
    private StripeSubscriptions stripeSubscriptions;



    public void addDentistToDentistDetails(Dentist dentist, String token) {
        DentistDetails dentistDetails = new DentistDetails();
        dentistDetails.setDentist(dentist);
        dentistDetails.setCostumerId( stripeSubscriptions.createCostumer(dentist, token));
       Plan plan = stripeSubscriptions.getPlan("Basic");
       dentistDetails.setSubscriptionId(stripeSubscriptions.createCostumerSubscription(dentistDetails.getCostumerId(),plan ,token));
        dentistDetailsRepository.save(dentistDetails);
    }

    public void addHoursToDentistDetails(Hour hour, Dentist dentist) {
        dentist.getDentistDetails().setHour(hour);
        dentistDetailsRepository.save(dentist.getDentistDetails());
    }
}
