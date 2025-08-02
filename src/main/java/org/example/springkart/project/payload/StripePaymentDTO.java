package org.example.springkart.project.payload;

import lombok.Data;
import org.example.springkart.project.model.Address;

import java.util.Map;

@Data
public class StripePaymentDTO {

    private Long  amount;
    private String currency;
    private String email;
    private String name;
    private Address address;
    private String description;
    private Map<String, String> metadata;
}
