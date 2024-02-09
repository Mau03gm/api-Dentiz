package com.dentiz.dentizapi.Components.Stripe.Service;

import com.dentiz.dentizapi.Components.Stripe.Plan;
import com.dentiz.dentizapi.Components.Stripe.Repository.StripeRepository;
import com.dentiz.dentizapi.Components.Stripe.StripeConfig;
import com.dentiz.dentizapi.Entity.Dentist;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Subscription;
import com.stripe.param.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class StripeService {

    @Autowired
    private StripeRepository stripeRepository;
    @Autowired
    private StripeConfig stripeConfig;

    public String createCostumer(Dentist dentist, String token) {
        Map<String, Object> customerParams = Map.of(
                "name", dentist.getFirstName() + " " + dentist.getLastName(),
                "email", dentist.getEmail(),
                "source", token
        );
        Customer costumer;
        try{
          costumer = stripeConfig.getStripeClient().customers().create((CustomerCreateParams) customerParams);
        } catch (StripeException e) {
           throw new RuntimeException("Error al crear el cliente en Stripe");
        }
        return costumer.getId();
    }

    public void deleteCostumer(String costumerId) {
        try {
            stripeConfig.getStripeClient().customers().delete(costumerId);
        } catch (StripeException e) {
            throw new RuntimeException("Error al eliminar el cliente en Stripe");
        }
    }

    public void updateCostumer(Dentist dentist, String token) {
        Map<String, Object> customerParams = Map.of(
                "name", dentist.getFirstName() + " " + dentist.getLastName(),
                "email", dentist.getEmail(),
                "source", token
        );
        try {
            stripeConfig.getStripeClient().customers().update(dentist.getDentistDetails().getCostumerId(), (CustomerUpdateParams) customerParams);
        } catch (StripeException e) {
            throw new RuntimeException("Error al actualizar el cliente en Stripe");
        }
    }

    public void createCostumerSubscription(String costumerId, Plan plan) {
        Map<String, Object> subscriptionParams = Map.of(
                "customer", costumerId,
                "items", Map.of("plan", plan.getStripeId() ),
                "trial_period_days", plan.getFreeTrialDays()
        );
        try {
            stripeConfig.getStripeClient().subscriptions().create((SubscriptionCreateParams) subscriptionParams);
        } catch (StripeException e) {
            throw new RuntimeException("Error al crear la suscripción en Stripe");
        }
    }

    public void updateCostumerSubscription(String costumerId, Plan plan) {
        Map<String, Object> subscriptionParams = Map.of(
                "customer", costumerId,
                "items", Map.of("plan", plan.getStripeId() ),
                "trial_period_days", plan.getFreeTrialDays()
        );
        try {
            stripeConfig.getStripeClient().subscriptions().update(costumerId, (SubscriptionUpdateParams) subscriptionParams);
        } catch (StripeException e) {
            throw new RuntimeException("Error al actualizar la suscripción en Stripe");
        }
    }

    public void deleteCostumerSubscription(String costumerId) {
        try {
            stripeConfig.getStripeClient().subscriptions().cancel(costumerId, (SubscriptionCancelParams) null);
        } catch (StripeException e) {
            throw new RuntimeException("Error al eliminar la suscripción en Stripe");
        }
    }

}
