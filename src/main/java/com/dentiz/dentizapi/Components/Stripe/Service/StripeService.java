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

    private final StripeRepository stripeRepository;

    private final StripeConfig stripeConfig;

    @Autowired
    public StripeService(StripeRepository stripeRepository, StripeConfig stripeConfig) {
        this.stripeRepository = stripeRepository;
        this.stripeConfig= stripeConfig;

    }

    public String createCostumer(Dentist dentist, String token) {
        CustomerCreateParams customerParams = CustomerCreateParams.builder()
                .setName(dentist.getFirstName() + " " + dentist.getLastName())
                .setEmail(dentist.getEmail())
                .setPaymentMethod(token)
                .build();
        Customer costumer= null;
        try{
          costumer = stripeConfig.getStripeClient().customers().create(customerParams);
        } catch (StripeException e) {
           throw new RuntimeException("Error al crear el cliente en Stripe"+e.getMessage());
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

    public void updateCostumerPaymentMethod(Dentist dentist, String token) {
        CustomerUpdateParams customerParams= CustomerUpdateParams.builder()
                .setName(dentist.getFirstName() + " " + dentist.getLastName())
                .setEmail(dentist.getEmail())
                .setDefaultSource(token)
                .build();
        try {
            stripeConfig.getStripeClient().customers().update(dentist.getDentistDetails().getCostumerId(),  customerParams);
        } catch (StripeException e) {
            throw new RuntimeException("Error al actualizar el cliente en Stripe");
        }
    }

    public String createCostumerSubscription(String costumerId, Plan plan) {
        SubscriptionCreateParams subscriptionParams = SubscriptionCreateParams.builder()
                .setCustomer(costumerId)
                .addItem(SubscriptionCreateParams.Item.builder().setPlan(plan.getStripeId()).build())
                .setTrialPeriodDays(plan.getFreeTrialDays())
                .build();
        Subscription subscription= null;
        try {
            subscription=stripeConfig.getStripeClient().subscriptions().create( subscriptionParams);
        } catch (StripeException e) {
            throw new RuntimeException("Error al crear la suscripción en Stripe" + e.getMessage());
        }
        if (subscription == null) {
            throw new RuntimeException("Error al crear la suscripción: la suscripción es nula");
        }
        return subscription.getId();
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

    public void deleteCostumerSubscription(String subscriptionId) {
        try {
            stripeConfig.getStripeClient().subscriptions().cancel(subscriptionId);
        } catch (StripeException e) {
            throw new RuntimeException("Error al eliminar la suscripción en Stripe");
        }
    }

    public Plan getPlan(String name) {
        return stripeRepository.findByName(name);
    }

}
