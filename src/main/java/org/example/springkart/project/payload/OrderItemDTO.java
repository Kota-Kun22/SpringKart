package org.example.springkart.project.payload;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.springkart.project.model.Order;
import org.example.springkart.project.model.Product;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {

    private Long orderItemId;
    private ProductDTO product;

    private Integer quantity;
    private double discount;
    private double orderedProductPrice;

}
