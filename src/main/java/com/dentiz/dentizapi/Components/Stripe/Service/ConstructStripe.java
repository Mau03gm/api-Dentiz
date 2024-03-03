package com.dentiz.dentizapi.Components.Stripe.Service;

import com.dentiz.dentizapi.Components.Stripe.Plan;
import com.dentiz.dentizapi.Components.Stripe.Repository.StripeRepository;
import com.dentiz.dentizapi.Components.Stripe.Service.Subscription.StripeService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;

@Service
public class ConstructStripe {

    @Autowired
    private StripeRepository stripeRepository;
    @Autowired
    private StripeService stripeService;

    @PostConstruct
    public void init() {
        if (stripeRepository.findByName("Dentiz Basic") == null) {
            Plan plan = new Plan();
            plan.setName("Dentiz Basic");
            plan.setDescription("Plan básico");
            plan.setPrice(50000L); // 500 pesos mexicanos en centavos
            plan.setCurrency("mxn");
            plan.setFreeTrialDays(14L);
            stripeService.createPlan(plan);
            stripeRepository.save(plan);
        }
    }
}
