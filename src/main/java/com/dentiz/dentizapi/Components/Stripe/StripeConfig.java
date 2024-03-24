package com.dentiz.dentizapi.Components.Stripe;

import com.stripe.StripeClient;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class StripeConfig {
    @Value("${stripe.public-key}")
    private String publicKey;
    @Value("${stripe.secret-key}")
    private String secretKey;

<<<<<<< HEAD

=======
>>>>>>> b698e484c256f3d67faa98b260b7ad6eee012945
    public StripeClient getStripeClient() {
        return new StripeClient(
                secretKey
        );
    }
}
