package com.dentiz.dentizapi.Components.Stripe;

import com.stripe.StripeClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripeConfig {
    @Value("${stripe.public-key}")
    private String publicKey;
    @Value("${stripe.secret-key}")
    private String secretKey;

    public StripeClient getStripeClient() {
        return new StripeClient(
                secretKey
        );
    }
}
