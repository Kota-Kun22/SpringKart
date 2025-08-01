package org.example.springkart.project.payload;

import lombok.Data;

@Data
public class StripePaymentDTO {

    private Long  amount;
    private String currency;
}
