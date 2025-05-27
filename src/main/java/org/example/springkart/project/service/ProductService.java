package org.example.springkart.project.service;

import org.example.springkart.project.model.Product;
import org.example.springkart.project.payload.ProductDTO;

public interface ProductService {

    ProductDTO addProduct(Product product, Long categoryId);
}
