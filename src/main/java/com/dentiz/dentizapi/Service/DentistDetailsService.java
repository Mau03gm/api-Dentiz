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


    public void addDentistToDentistDetails(Dentist dentist, String costumerId, Plan plan, String subscriptionId) {
        DentistDetails dentistDetails = new DentistDetails();
        dentistDetails.setDentist(dentist);
        dentistDetails.setCostumerId( costumerId);
       dentistDetails.setSubscriptionId(subscriptionId);
        dentistDetailsRepository.save(dentistDetails);
    }

    public void addHoursToDentistDetails(Hour hour, Dentist dentist) {
         DentistDetails dentistDetails= dentist.getDentistDetails();
            dentistDetails.setHour(hour);
        dentistDetailsRepository.save(dentistDetails);
    }
}
