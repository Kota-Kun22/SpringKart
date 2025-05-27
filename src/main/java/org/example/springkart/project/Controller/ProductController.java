package org.example.springkart.project.Controller;

import org.example.springkart.project.model.Product;
import org.example.springkart.project.payload.ProductDTO;
import org.example.springkart.project.payload.ProductResponse;
import org.example.springkart.project.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@RequestBody Product product, @PathVariable Long categoryId){

      ProductDTO savedProductDTO= productService.addProduct(product,categoryId);
      return new ResponseEntity<>(savedProductDTO, HttpStatus.CREATED);
    }
}
