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
    public ResponseEntity<ProductDTO> addProduct(@RequestBody ProductDTO productDto, @PathVariable Long categoryId){

      ProductDTO savedProductDTO= productService.addProduct(productDto,categoryId);
      return new ResponseEntity<>(savedProductDTO, HttpStatus.CREATED);
    }

    @GetMapping("/public/product")
    public ResponseEntity<ProductResponse> getProductById(){

        ProductResponse productResponse= productService.getAllProducts();
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductByCategoryId(@PathVariable Long categoryId){

        ProductResponse response= productService.searchByCategory(categoryId);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductByKeyword(@PathVariable String keyword){
        ProductResponse response= productService.searchByKeyword(keyword);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@RequestBody ProductDTO productDto,@PathVariable  Long productId ){

        ProductDTO updateProductDto= productService.updateProduct(productId,productDto);
        return new ResponseEntity<>(updateProductDto, HttpStatus.OK);

    }

    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable  Long productId ){
        ProductDTO deletedProductDto = productService.deleteProduct(productId);
        return new ResponseEntity<>(deletedProductDto, HttpStatus.OK);

    }


}
