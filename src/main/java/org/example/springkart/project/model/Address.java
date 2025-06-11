package org.example.springkart.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long addressId;

    @NotBlank
    @Size(min = 5,message = "Street should be at least 5 characters")
    private String street;


    @NotBlank
    @Size(min = 5,message = "Building name is required")
    private String buildingName;

    @NotBlank
    @Size(min = 4,message = "City is required")
    private String city;

    @NotBlank
    @Size(min = 2,message = "State is required")
    private String state;

    @NotBlank
    @Size(min = 2,message = "Country is required")
    private String country;

    @NotBlank
    @Size(min = 6,message = "Zip code is required")
    private String pinCode;

    @ToString.Exclude
    @ManyToMany(mappedBy = "addresses")
    private List<User> users= new ArrayList<>();

    public Address(String street, String buildingName, String city, String state, String country, String pinCode) {
        this.street = street;
        this.buildingName = buildingName;
        this.city = city;
        this.state = state;
        this.country = country;
        this.pinCode = pinCode;
    }
}
