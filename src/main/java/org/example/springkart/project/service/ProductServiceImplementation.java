package org.example.springkart.project.service;

import org.example.springkart.project.exception.ResourceNotFoundException;
import org.example.springkart.project.model.Category;
import org.example.springkart.project.model.Product;
import org.example.springkart.project.payload.ProductDTO;
import org.example.springkart.project.repository.CategoryRepository;
import org.example.springkart.project.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImplementation implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ProductDTO addProduct(Product product, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        product.setCategory(category);
        product.setImage("default.png");
        double specialPrice= product.getPrice() - ((product.getDiscount()*0.01)* product.getPrice());
        product.setSpecialPrice(specialPrice);
        Product savedProduct = productRepository.save(product);
        ProductDTO savedProductDTO = modelMapper.map(savedProduct, ProductDTO.class);
        return savedProductDTO;
    }
}
