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
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
@ToString
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productId;

    @NotBlank(message = "Product Name is mandatory")
    @Size(min=3,message = "Product Name should be at least 3 characters")
    private String productName;
    @Size(min=10,message = "Product Description should be at least 10 characters")
    private String description;
    private String image;
    private Integer quantity;
    private double price;
    private double discount;
    private double specialPrice;

    //RelationShips
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User user;


    @OneToMany(mappedBy = "product",cascade = { CascadeType.PERSIST,CascadeType.MERGE},fetch = FetchType.EAGER)//mapped by is actually by the field name okay go check in the cartItem there will be product field name
    private List<CartItem> products= new ArrayList<>();

    // Add this relationship
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

}
