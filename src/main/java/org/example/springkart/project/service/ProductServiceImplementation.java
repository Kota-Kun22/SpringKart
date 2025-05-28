package org.example.springkart.project.service;

import org.example.springkart.project.exception.ResourceNotFoundException;
import org.example.springkart.project.model.Category;
import org.example.springkart.project.model.Product;
import org.example.springkart.project.payload.ProductDTO;
import org.example.springkart.project.payload.ProductResponse;
import org.example.springkart.project.repository.CategoryRepository;
import org.example.springkart.project.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public ProductResponse getAllProducts() {

        List<Product> productList= productRepository.findAll();
        List<ProductDTO> productDtoList = productList.stream()
                .map(product-> modelMapper.map(product, ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDtoList);
        return productResponse;
    }

    @Override
    public ProductResponse searchByCategory(Long categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        List<Product> productList= productRepository.findByCategoryOrderByPriceAsc(category);


        List<ProductDTO> productDtoList = productList.stream()
                .map(product-> modelMapper.map(product, ProductDTO.class))
                .toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDtoList);

        return productResponse;
    }

    @Override
    public ProductResponse searchByKeyword(String keyword) {

        List<Product> productList= productRepository.findByProductNameContainingIgnoreCase( keyword);
        List<ProductDTO> productDtoList = productList.stream()
                .map(product-> modelMapper.map(product, ProductDTO.class)).toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDtoList);
        return productResponse;
    }

    @Override
    public ProductDTO updateProduct(Long productId, Product product) {

        Product productFromDb = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        productFromDb.setProductName(product.getProductName());
        productFromDb.setProductDescription(product.getProductDescription());
        productFromDb.setQuantity(product.getQuantity());
        productFromDb.setDiscount(product.getDiscount());
        productFromDb.setPrice(product.getPrice());
        productFromDb.setSpecialPrice(product.getSpecialPrice());

         Product savedProduct = productRepository.save(productFromDb);
         ProductDTO savedProductDTO = modelMapper.map(savedProduct, ProductDTO.class);
         return savedProductDTO;



    }

    @Override
    public ProductDTO deleteProduct(Long productId) {

        List<Product> productList = productRepository.findAll();
        // For-loop to find the product by ID
//        Product productToDelete = null;
//        for (Product product : productList) {
//            if (product.getProductId().equals(productId)) {
//                productToDelete = product;
//                break;
//            }
//        }
//        // If not found, throw exception
//        if (productToDelete == null) {
//            throw new ResourceNotFoundException("Product", "productId", productId);
        Product productToDelete= productList.stream()
                .filter(p-> p.getProductId().equals(productId))
                .findFirst()
                .orElseThrow(()-> new ResourceNotFoundException("Product","productId",productId));

        productRepository.delete(productToDelete);
        return modelMapper.map(productToDelete,ProductDTO.class);
    }


}
