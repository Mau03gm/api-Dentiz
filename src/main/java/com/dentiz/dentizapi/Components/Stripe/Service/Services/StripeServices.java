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


   public void createPaymentIntent(String paymentMethod, PriceService priceService, Dentist dentist){
        long amount = (long) (priceService.getPrice()*100);
        long applicationFee = (long) (priceService.getPrice()*0.1*100);
        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount(amount)
                        .setCurrency("mxn")
                        .addPaymentMethodType("card")
                        .setPaymentMethod(paymentMethod)
                        .setAutomaticPaymentMethods(PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                .setEnabled(true)
                                .build())
                        .setApplicationFeeAmount(applicationFee)
                        .setTransferData(PaymentIntentCreateParams.TransferData.builder()
                                .setDestination(dentist.getAccountStripeId())
                                .build())
                        .build();
        PaymentIntent paymentIntent;
        try {
            paymentIntent = stripeConfig.getStripeClient().paymentIntents().create(params);
        } catch (StripeException e) {
            throw new RuntimeException("Error al crear el intento de pago en Stripe");
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
            account=stripeConfig.getStripeClient().accounts().create(params);
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
            accountLink = stripeConfig.getStripeClient().accountLinks().create(params);
        } catch (StripeException e) {
            throw new RuntimeException("Error al crear el link de la cuenta en Stripe"+ e.getMessage());
        }

        return  accountLink.getUrl();
    }


}
