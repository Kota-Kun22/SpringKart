package org.example.springkart.project.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.CustomerSearchResult;
import com.stripe.model.PaymentIntent;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.CustomerSearchParams;
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

        Customer customer;
        //Retrive the customer exist
        CustomerSearchParams searchParams =
                CustomerSearchParams.builder()
                        .setQuery("email:'" + stripePaymentDto.getEmail() + "'")
                        .build();
        CustomerSearchResult customers = Customer.search(searchParams);

        if(customers.getData().isEmpty()){
            //if not create a new customer
            CustomerCreateParams customerParams= CustomerCreateParams.builder()
                    .setEmail(stripePaymentDto.getEmail())
                    .setName(stripePaymentDto.getName())
                    .setAddress(
                            CustomerCreateParams.Address.builder()
                                    .setLine1(stripePaymentDto.getAddress().getStreet())
                                    .setCity(stripePaymentDto.getAddress().getCity())
                                    .setState(stripePaymentDto.getAddress().getState())
                                    .setPostalCode(stripePaymentDto.getAddress().getPinCode())
                                    .setCountry(stripePaymentDto.getAddress().getCountry())
                                    .build()
                    )
                    .build();
            customer = Customer.create(customerParams);
        }else{
            //fetch the customer that exist
            customer = customers.getData().get(0);
        }

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
                            .setCustomer(customer.getId())
                            .setDescription(stripePaymentDto.getDescription())
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
