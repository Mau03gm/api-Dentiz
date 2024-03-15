package com.dentiz.dentizapi.Entity.DTO;

import lombok.Data;

@Data
public class StripeAccount {

    private String accountStripeId;
    private String accountStripeLink;

    public StripeAccount( String accountStripeLink) {
        this.accountStripeLink = accountStripeLink;
    }
}
