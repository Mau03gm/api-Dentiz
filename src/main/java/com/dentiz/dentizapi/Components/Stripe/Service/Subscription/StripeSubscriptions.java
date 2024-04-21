package com.dentiz.dentizapi.Components.Stripe.Service.Subscription;

import com.dentiz.dentizapi.Components.Stripe.Plan;
import com.dentiz.dentizapi.Components.Stripe.Repository.StripeRepository;
import com.dentiz.dentizapi.Components.Stripe.StripeConfig;
import com.dentiz.dentizapi.Entity.Dentist;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentMethod;
import com.stripe.model.Product;
import com.stripe.model.Subscription;
import com.stripe.param.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class StripeSubscriptions {

    private final StripeRepository stripeRepository;

    private final StripeConfig stripeConfig;

    @Autowired
    public StripeSubscriptions(StripeRepository stripeRepository, StripeConfig stripeConfig) {
        this.stripeRepository = stripeRepository;
        this.stripeConfig= stripeConfig;

    }

    public void createPlan(Plan plan) {
        ProductCreateParams productParams = ProductCreateParams.builder()
                .setName(plan.getName())
                .build();

        Product product;
        try {
            product = stripeConfig.getStripeClient().products().create(productParams);
            plan.setStripeId(product.getId());
        } catch (StripeException e) {
            throw new RuntimeException("Error al crear el producto en Stripe");
        }

        PriceCreateParams priceParams;

        if(Objects.equals(plan.getInterval(), "month")){
            priceParams = PriceCreateParams.builder()
                    .setProduct(product.getId())
                    .setCurrency("mxn")
                    .setUnitAmount(plan.getPrice())
                    .setRecurring(PriceCreateParams.Recurring.builder()
                            .setInterval(PriceCreateParams.Recurring.Interval.MONTH)
                            .build())
                    .setProduct(product.getId())
                    .build();
        } else {
             priceParams = PriceCreateParams.builder()
                    .setProduct(product.getId())
                    .setCurrency("mxn")
                    .setUnitAmount(plan.getPrice())
                    .setRecurring(PriceCreateParams.Recurring.builder()
                            .setInterval(PriceCreateParams.Recurring.Interval.YEAR)
                            .build())
                    .setProduct(product.getId())
                    .build();
        }

        try {
            com.stripe.model.Price price = stripeConfig.getStripeClient().prices().create(priceParams);
            plan.setSubscriptionId(price.getId());
        } catch (StripeException e) {
            throw new RuntimeException("Error al crear el precio en Stripe");
        }
    }



    public String createCostumer(Dentist dentist, String token) {
        CustomerCreateParams customerParams = CustomerCreateParams.builder()
                .setName(dentist.getFirstName() + " " + dentist.getLastName())
                .setEmail(dentist.getEmail())
                .setPaymentMethod(token)
                .build();
        Customer costumer= null;
        try{
          costumer = stripeConfig.getStripeClient().customers().create(customerParams);
        } catch (StripeException e) {
           throw new RuntimeException("Error al crear el cliente en Stripe"+e.getMessage());
        }
        return costumer.getId();
    }

    public void deleteCostumer(String costumerId) {
        try {
            stripeConfig.getStripeClient().customers().delete(costumerId);
        } catch (StripeException e) {
            throw new RuntimeException("Error al eliminar el cliente en Stripe");
        }
    }

    public void updateCostumerPaymentMethod(Dentist dentist, String paymentMethodId) throws StripeException {
        Customer customer= Customer.retrieve(dentist.getDentistDetails().getCostumerId());
        CustomerUpdateParams customerParams = CustomerUpdateParams.builder()
                .setInvoiceSettings(
                        CustomerUpdateParams.InvoiceSettings.builder()
                                .setDefaultPaymentMethod(paymentMethodId)
                                .build()
                )
                .build();
        try {
            stripeConfig.getStripeClient().customers().update(dentist.getDentistDetails().getCostumerId(),  customerParams);
        } catch (StripeException e) {
            throw new RuntimeException("Error al actualizar el cliente en Stripe");
        }
    }

    public String createCostumerSubscription(String costumerId, Plan plan, String token) {
        SubscriptionCreateParams subscriptionParams = SubscriptionCreateParams.builder()
                .setCustomer(costumerId)
                .setDefaultPaymentMethod(token)
                .addItem(
                        SubscriptionCreateParams.Item.builder().setPlan(plan.getSubscriptionId()).build()
                )
                .setTrialPeriodDays(plan.getFreeTrialDays())

                .build();
        Subscription subscription= null;
        try {
            subscription=stripeConfig.getStripeClient().subscriptions().create( subscriptionParams);
        } catch (StripeException e) {
            throw new RuntimeException("Error al crear la suscripción en Stripe" + e.getMessage());
        }
        if (subscription == null) {
            throw new RuntimeException("Error al crear la suscripción: la suscripción es nula");
        }
        return subscription.getId();
    }

    public void updateCostumerSubscription(String susbcriptionId, Plan plan) {
        SubscriptionUpdateParams subscriptionParams = SubscriptionUpdateParams.builder()
                .addItem(SubscriptionUpdateParams.Item.builder().setId(plan.getStripeId()).build())
                .build();
        try {
            stripeConfig.getStripeClient().subscriptions().update(susbcriptionId,  subscriptionParams);
        } catch (StripeException e) {
            throw new RuntimeException("Error al actualizar la suscripción en Stripe");
        }
    }

    public void deleteCostumerSubscription(String subscriptionId) {
        try {
            stripeConfig.getStripeClient().subscriptions().cancel(subscriptionId);
        } catch (StripeException e) {
            throw new RuntimeException("Error al eliminar la suscripción en Stripe");
        }
    }

    public boolean validateDentistSubscription(Dentist dentist) throws StripeException {
        String state;
            Subscription subscription = stripeConfig.getStripeClient().subscriptions().retrieve(dentist.getDentistDetails().getSubscriptionId());
            if(subscription.getStatus().equals("active")) {
                return true;
            } else if (subscription.getStatus().equals("trialing")) {
                return true;
            } else {
                return false;
            }

    }

    public Plan getPlan(String name) {
        return stripeRepository.findByName(name);
    }



}
