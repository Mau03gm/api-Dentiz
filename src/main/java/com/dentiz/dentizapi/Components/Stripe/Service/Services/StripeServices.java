package com.dentiz.dentizapi.Components.Stripe.Service.Services;

import com.dentiz.dentizapi.Components.Stripe.Repository.StripeRepository;
import com.dentiz.dentizapi.Components.Stripe.StripeConfig;
import com.dentiz.dentizapi.Entity.Dentist;
import com.dentiz.dentizapi.Entity.PriceService;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.param.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripeServices {
    private final StripeRepository stripeRepository;
    private final StripeConfig stripeConfig;

    @Value("stripe.stripe-id")
    private String PLATFORM_ACCOUNT_ID ;

    @Autowired
    public StripeServices(StripeRepository stripeRepository, StripeConfig stripeConfig) {
        this.stripeRepository = stripeRepository;
        this.stripeConfig= stripeConfig;
    }


    public void createCharge(Dentist dentist, PriceService priceService, String paymentMethodId){
        double doctorPercentage = 0.9;
        long priceInCents = (long)(priceService.getPrice() * 100);
        long doctorAmount = (long) (priceInCents * doctorPercentage);
        long platformAmount = priceInCents - doctorAmount;

        ChargeCreateParams chargeParams = ChargeCreateParams.builder()
                .setAmount(priceInCents)
                .setCurrency("mxn")
                .setSource(paymentMethodId)
                .setDescription(priceService.getService().getName())
                .setDestination(ChargeCreateParams.Destination.builder()
                        .setAccount(dentist.getAccountStripeId())
                        .setAmount(doctorAmount)
                        .build())
                .setDestination(ChargeCreateParams.Destination.builder()
                        .setAccount(PLATFORM_ACCOUNT_ID)
                        .setAmount(platformAmount)
                        .build())
                .build();

        Charge charge;
        try {
            charge = Charge.create(chargeParams);
        } catch (StripeException e) {
            throw new RuntimeException("Error al cargar el cliente en Stripe");
        }
        CreateTransfer(charge, priceInCents);
    }


    public void CreateTransfer(Charge charge, long amount){
        TransferCreateParams params =
                TransferCreateParams.builder()
                        .setCurrency("mxn")
                        .setAmount(amount)
                        .setSourceTransaction(charge.getId())
                        .build();

        try {
            Transfer.create(params);
        } catch (StripeException e) {
            throw new RuntimeException("Error al crear la transferencia en Stripe"+ e.getMessage());

        }

    }

    public String createAccountStripeConnect(Dentist dentist){
        AccountCreateParams params =
                AccountCreateParams.builder()
                        .setType(AccountCreateParams.Type.EXPRESS)
                        .setCountry("MX")
                        .setEmail(dentist.getEmail())
                        .setCapabilities(
                                AccountCreateParams.Capabilities.builder()
                                        .setCardPayments(
                                                AccountCreateParams.Capabilities.CardPayments.builder()
                                                        .setRequested(true)
                                                        .build()
                                        )
                                        .setTransfers(AccountCreateParams.Capabilities.Transfers.builder()
                                                .setRequested(true)
                                                .build())
                                        .build()
                        )
                        .build();
        Account account;

        try {
            account=Account.create(params);
        } catch (StripeException e) {
            throw new RuntimeException("Error al crear la cuenta en Stripe");
        }
       return account.getId();
    }

    public String createAccountLink(Dentist dentist){
        AccountLinkCreateParams params =
                AccountLinkCreateParams.builder()
                        .setAccount(dentist.getAccountStripeId())
                        .setRefreshUrl("http://localhost:3000")
                        .setReturnUrl("http://localhost:3000")
                        .setType(AccountLinkCreateParams.Type.ACCOUNT_ONBOARDING)
                        .build();
        AccountLink accountLink;
        try {
            accountLink = AccountLink.create(params);
        } catch (StripeException e) {
            throw new RuntimeException("Error al crear el link de la cuenta en Stripe"+ e.getMessage());
        }

        return  accountLink.getUrl();
    }


}
