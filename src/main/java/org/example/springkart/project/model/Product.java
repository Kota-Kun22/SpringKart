package org.example.springkart.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productId;

    @NotBlank(message = "Product Name is mandatory")
    @Size(min=3,message = "Product Name should be at least 3 characters")
    private String productName;
    @Size(min=10,message = "Product Description should be at least 10 characters")
    private String productDescription;
    private String image;
    private Integer quantity;
    private double price;
    private double discount;
    private double specialPrice;

    //RelationShips
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}
