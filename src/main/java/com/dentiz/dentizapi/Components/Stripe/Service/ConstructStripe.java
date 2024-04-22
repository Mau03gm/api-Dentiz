package com.dentiz.dentizapi.Components.Stripe.Service;

import com.dentiz.dentizapi.Components.Stripe.Plan;
import com.dentiz.dentizapi.Components.Stripe.Repository.StripeRepository;
import com.dentiz.dentizapi.Components.Stripe.Service.Subscription.StripeSubscriptions;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConstructStripe {

    @Autowired
    private StripeRepository stripeRepository;
    @Autowired
    private StripeSubscriptions stripeSubscriptions;

    @PostConstruct
    public void init() {
        if (stripeRepository.findByName("Dentiz Mensual") == null) {
            Plan plan = new Plan();
            plan.setName("Dentiz Mensual");
            plan.setDescription("Plan de subscripcion mensual para la plataforma Dentiz");
            plan.setPrice(49900L); // 499 pesos mexicanos en centavos
            plan.setCurrency("mxn");
            plan.setFreeTrialDays(14L);
            plan.setInterval("month");
            stripeSubscriptions.createPlan(plan);
            stripeRepository.save(plan);
        }
        if (stripeRepository.findByName("Dentiz Anual") == null) {
            Plan plan = new Plan();
            plan.setName("Dentiz Anual");
            plan.setDescription("Plan de subscripcion anual para la plataforma Dentiz");
            plan.setPrice(520000L); // 5200 pesos mexicanos en centavos
            plan.setCurrency("mxn");
            plan.setFreeTrialDays(14L);
            plan.setInterval("year");
            stripeSubscriptions.createPlan(plan);
            stripeRepository.save(plan);
        }
    }
}
