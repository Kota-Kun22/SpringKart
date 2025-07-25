package org.example.springkart.project.Controller;

import jakarta.validation.Valid;
import org.example.springkart.project.config.AppConstants;
import org.example.springkart.project.model.Product;
import org.example.springkart.project.payload.ProductDTO;
import org.example.springkart.project.payload.ProductResponse;
import org.example.springkart.project.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@Valid  @RequestBody ProductDTO productDto, @PathVariable Long categoryId){

      ProductDTO savedProductDTO= productService.addProduct(productDto,categoryId);
      return new ResponseEntity<>(savedProductDTO, HttpStatus.CREATED);
    }

    @GetMapping("/public/product")
    public ResponseEntity<ProductResponse> getProductById(
            @RequestParam(name="keyword",required = false)String keyword,
            @RequestParam(name="category",required = false)String category,
            @RequestParam(name="pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false)Integer pageNumber,
            @RequestParam(name="pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false)Integer pageSize,
            @RequestParam(name="sort",defaultValue = AppConstants.PRODUCT_SORT_BY,required = false)String sortBy,
            @RequestParam(name="sortOrder",defaultValue = AppConstants.SORT_ORDER,required = false)String sortOrder
    ){

        ProductResponse productResponse= productService.getAllProducts(pageNumber,pageSize,sortBy,sortOrder,keyword,category);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductByCategoryId(
            @PathVariable Long categoryId,
            @RequestParam(name="pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false)Integer pageNumber,
            @RequestParam(name="pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false)Integer pageSize,
            @RequestParam(name="sort",defaultValue = AppConstants.PRODUCT_SORT_BY,required = false)String sortBy,
            @RequestParam(name="sortOrder",defaultValue = AppConstants.SORT_ORDER,required = false)String sortOrder)
    {
        ProductResponse response= productService.searchByCategory(categoryId,pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductByKeyword(
            @PathVariable String keyword,
            @RequestParam(name="pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false)Integer pageNumber,
            @RequestParam(name="pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false)Integer pageSize,
            @RequestParam(name="sort",defaultValue = AppConstants.PRODUCT_SORT_BY,required = false)String sortBy,
            @RequestParam(name="sortOrder",defaultValue = AppConstants.SORT_ORDER,required = false)String sortOrder
            )
    {
        ProductResponse response= productService.searchByKeyword(keyword,pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@Valid @RequestBody ProductDTO productDto,@PathVariable  Long productId ){

        ProductDTO updateProductDto= productService.updateProduct(productId,productDto);
        return new ResponseEntity<>(updateProductDto, HttpStatus.OK);

    }

    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable  Long productId ){
        ProductDTO deletedProductDto = productService.deleteProduct(productId);
        return new ResponseEntity<>(deletedProductDto, HttpStatus.OK);

    }

    @PutMapping("/products/{productId}/image")
    public ResponseEntity<ProductDTO> updateProductImage(@PathVariable Long productId,
                                                         @RequestParam("image") MultipartFile image)throws IOException
    {
       ProductDTO updatedProductWithImage= productService.updateProductImage(productId,image);
        return new ResponseEntity<>(updatedProductWithImage, HttpStatus.OK);
    }




}
