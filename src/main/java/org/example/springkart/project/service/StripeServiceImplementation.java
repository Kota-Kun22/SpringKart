package org.example.springkart.project.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.example.springkart.project.exception.InvalidPaymentException;
import org.example.springkart.project.payload.StripePaymentDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class StripeServiceImplementation implements StripeService {

    @Value("${stripe.secret.key}")
    private String stripeApiKey;

    @PostConstruct
    public void init(){
        Stripe.apiKey= stripeApiKey;
        System.out.println("Stripe initialized with the key  "+stripeApiKey);
    }

    @Override
    public PaymentIntent paymentIntent(StripePaymentDTO stripePaymentDto) throws StripeException {
        try {
            //  MINIMUM CHECK: ₹50 = 5000 paise
            // ✅ Check for minimum amount in INR (because Stripe account is in EUR)
            if (stripePaymentDto.getCurrency().equalsIgnoreCase("inr") &&
                    stripePaymentDto.getAmount() < 5000) {
                throw new InvalidPaymentException(
                        "The minimum amount allowed is ₹50.00 due to Stripe's global processing requirement."

                );
            }

            PaymentIntentCreateParams params =
                    PaymentIntentCreateParams.builder()
                            .setAmount(stripePaymentDto.getAmount())
                            .setCurrency(stripePaymentDto.getCurrency())
                            .setAutomaticPaymentMethods(
                                    PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                            .setEnabled(true)
                                            .build()
                            )
                            .build();

            return PaymentIntent.create(params);

        } catch (StripeException e) {
            throw new RuntimeException("Stripe payment creation failed: " + e.getMessage());
        }
    }

}
