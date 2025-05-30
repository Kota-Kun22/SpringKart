package org.example.springkart.project.service;

import org.example.springkart.project.payload.ProductDTO;
import org.example.springkart.project.payload.ProductResponse;

public interface ProductService {

    ProductDTO addProduct(ProductDTO product, Long categoryId);

    ProductResponse getAllProducts();

    ProductResponse searchByCategory(Long categoryId);

    ProductResponse searchByKeyword(String keyword);

    ProductDTO updateProduct(Long productId, ProductDTO productDto);

    ProductDTO deleteProduct(Long productId);
}
