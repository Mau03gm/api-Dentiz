package com.dentiz.dentizapi.Components.Stripe.Service;

import com.dentiz.dentizapi.Components.Stripe.Plan;
import com.dentiz.dentizapi.Components.Stripe.Repository.StripeRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;

@Service
public class ConstructStripe {

    @Autowired
    private StripeRepository stripeRepository;

    @PostConstruct
    public void init() {
        if (stripeRepository.findByName("Basic") == null) {
            Plan plan = new Plan();
            plan.setName("Basic");
            plan.setDescription("Plan básico");
            plan.setStripeId("prod_PWcyVoDKedLxBV");
            plan.setSubscriptionId("price_1OhZiCJ3FiyOPn4xp1pSczl5");
            plan.setPrice(new BigDecimal(500).multiply(new BigDecimal(100))); // 500 pesos mexicanos en centavos
            plan.setCurrency("mxn");
            plan.setFreeTrialDays(14);
            stripeRepository.save(plan);
        }
    }
}
