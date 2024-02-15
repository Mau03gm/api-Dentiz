package com.dentiz.dentizapi.Components.Stripe.Repository;

import com.dentiz.dentizapi.Components.Stripe.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StripeRepository extends JpaRepository<Plan, Integer> {
    Plan findByStripeId(String stripeId);
    Plan findByName(String name);
}