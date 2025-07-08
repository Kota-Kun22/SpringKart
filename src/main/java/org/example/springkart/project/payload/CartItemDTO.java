package org.example.springkart.project.payload;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
//here we are representing a single cart item
public class CartItemDTO {

    private Long cartItemId;
    private CartDTO cart;
    private ProductDTO product;

    private Integer quantity;
    private double discount;
    private double productPrice;

}
