package com.dentiz.dentizapi.Components.Stripe.Service.Services;

import com.dentiz.dentizapi.Components.Stripe.Repository.StripeRepository;
import com.dentiz.dentizapi.Components.Stripe.StripeConfig;
import com.dentiz.dentizapi.Entity.Dentist;
import com.dentiz.dentizapi.Entity.DentistDetails;
import com.dentiz.dentizapi.Entity.PriceService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.net.RequestOptions;
import com.stripe.param.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripeServices {
    private final StripeRepository stripeRepository;
    private final StripeConfig stripeConfig;

    @Autowired
    public StripeServices(StripeRepository stripeRepository, StripeConfig stripeConfig) {
        this.stripeRepository = stripeRepository;
        this.stripeConfig= stripeConfig;
    }

   public void createPaymentIntent(String paymentMethod, PriceService priceService, Dentist dentist){
       Stripe.apiKey = stripeConfig.getSecretKey();
        long amount = (long) (priceService.getPrice()*100);
        long applicationFee = amount * 10 / 100;
        long transferAmount = amount - applicationFee;
        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount(amount)
                        .setCurrency("mxn")
                        .setPaymentMethod(paymentMethod)
                        .setAutomaticPaymentMethods(PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                .setEnabled(true)
                                .build())
                        .setApplicationFeeAmount(applicationFee)
                        .setTransferData(PaymentIntentCreateParams.TransferData.builder()
                                .setDestination(dentist.getAccountStripeId())
                                .build())
                        .setReturnUrl("http://localhost:3000/" + dentist.getUsername())
                        .setConfirm(true)
                        .build();
        PaymentIntent paymentIntent;
        try {
            paymentIntent = stripeConfig.getStripeClient().paymentIntents().create(params);
//            if(getConnectAccountStatus(dentist)) {
//                payoutToDentist(dentist, transferAmount);
//            }
           // paymentIntent.confirm();
            //transferToDentist(dentist, transferAmount);
            //confirmPaymentIntent(paymentIntent, dentist);
        } catch (StripeException e) {
            throw new RuntimeException("Error al crear el intento de pago en Stripe"+ e.getMessage());
        }
   }

   private void confirmPaymentIntent(PaymentIntent paymentIntent, Dentist dentist) throws StripeException {
       PaymentIntent paymentIntentConfirm = PaymentIntent.retrieve(paymentIntent.getId());
       PaymentIntentConfirmParams params =
               PaymentIntentConfirmParams.builder()
                       .setPaymentMethod(paymentIntent.getPaymentMethod())
                       .setReturnUrl("http://localhost:3000/" + dentist.getUsername())
                       .build();
       PaymentIntent confirmPaymentIntent;
       try {
           confirmPaymentIntent = stripeConfig.getStripeClient().paymentIntents().confirm(paymentIntentConfirm.getId(), params);
          // payoutToDentist(dentist, confirmPaymentIntent.getAmount());
       } catch (StripeException e) {
           throw new RuntimeException("Error al confirmar el intento de pago en Stripe" + e.getMessage());
       }
   }

   private void transferToDentist(Dentist dentist, long amount) {
       Stripe.apiKey = stripeConfig.getSecretKey();
       Payout payout;
       PayoutCreateParams params =
               PayoutCreateParams.builder()
                       .setAmount(amount)
                       .setCurrency("mxn")
                       .build();
         RequestOptions requestOptions = RequestOptions.builder()
                    .setStripeAccount(dentist.getAccountStripeId())
                    .build();
       try {
           payout= Payout.create(params, requestOptions);
       } catch (StripeException e) {
           throw new RuntimeException("Error al realizar la transferencia en Stripe " + e.getMessage());
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

    public String UpdateAccountLink(Dentist dentist){
        AccountLinkCreateParams params =
                AccountLinkCreateParams.builder()
                        .setAccount(dentist.getAccountStripeId())
                        .setRefreshUrl("http://localhost:3000")
                        .setReturnUrl("http://localhost:3000")
                        .setType(AccountLinkCreateParams.Type.ACCOUNT_UPDATE)
                        .build();
        AccountLink accountLink;
        try {
            accountLink = stripeConfig.getStripeClient().accountLinks().create(params);
        } catch (StripeException e) {
            throw new RuntimeException("Error al crear el link de la cuenta en Stripe"+ e.getMessage());
        }

        return  accountLink.getUrl();
    }

    public String loginDashboardLink(Dentist dentist){
        LoginLinkCreateOnAccountParams params =
                LoginLinkCreateOnAccountParams.builder()
                        .build();
        LoginLink loginLink;
        try {
            loginLink = stripeConfig.getStripeClient().accounts().loginLinks().create(dentist.getAccountStripeId());
        } catch (StripeException e) {
            throw new RuntimeException("Error al crear el link de la cuenta en Stripe"+ e.getMessage());
        }

        return  loginLink.getUrl();
    }

    public void payoutToDentist(Dentist dentist, long amount){
        Stripe.apiKey = stripeConfig.getSecretKey();

        PayoutCreateParams params =
                PayoutCreateParams.builder()
                        .setAmount(amount)
                        .setCurrency("mxn")
                        .build();

        RequestOptions requestOptions = RequestOptions.builder()
                .setStripeAccount(dentist.getAccountStripeId())
                .build();

        try {
            Payout payout = Payout.create(params, requestOptions);
        } catch (StripeException e) {
            throw new RuntimeException("Error al realizar el pago en Stripe "+ e.getMessage());
        }

    }
    public Boolean getConnectAccountStatus(Dentist dentist){
        Account account;
        try {
            account = stripeConfig.getStripeClient().accounts().retrieve(dentist.getAccountStripeId());
        } catch (StripeException e) {
            throw new RuntimeException("Error al obtener el estado de la cuenta en Stripe"+ e.getMessage());
        }
        return account.getPayoutsEnabled();
    }

}
