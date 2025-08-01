package org.example.springkart.project.service;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.example.springkart.project.payload.StripePaymentDTO;

public interface StripeService {

    PaymentIntent paymentIntent(StripePaymentDTO stripePaymentDto) throws StripeException;
}
