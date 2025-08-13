package org.example.springkart.project.service;

import org.example.springkart.project.exception.APIException;
import org.example.springkart.project.exception.ResourceNotFoundException;
import org.example.springkart.project.model.Cart;
import org.example.springkart.project.model.Category;
import org.example.springkart.project.model.Product;
import org.example.springkart.project.model.User;
import org.example.springkart.project.payload.CartDTO;
import org.example.springkart.project.payload.ProductDTO;
import org.example.springkart.project.payload.ProductResponse;
import org.example.springkart.project.repository.CartRepository;
import org.example.springkart.project.repository.CategoryRepository;
import org.example.springkart.project.repository.ProductRepository;
import org.example.springkart.project.utils.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;

@Service
public class ProductServiceImplementation implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private FileService fileService;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    CartService cartService;
    @Autowired
    AuthUtil authUtil;


    @Value("${project.image}")
    private String path;


    @Value("${image.base.url}")
    private String imageBaseUrl;

    @Override
    public ProductDTO addProduct(ProductDTO productDto, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        boolean isProductNotPresent= true;
        List<Product> products= category.getProducts();
        for(int i=0;i<products.size();i++){
            if(products.get(i).getProductName().equalsIgnoreCase(productDto.getProductName())){
                isProductNotPresent=false;
                break;
            }
        }
        if(isProductNotPresent) {
            Product product = modelMapper.map(productDto, Product.class);
            product.setCategory(category);
            product.setUser(authUtil.loggedInUser());
            product.setImage("default.png");
            double specialPrice = product.getPrice() - ((product.getDiscount() * 0.01) * product.getPrice());
            product.setSpecialPrice(specialPrice);
            Product savedProduct = productRepository.save(product);
            ProductDTO savedProductDTO = modelMapper.map(savedProduct, ProductDTO.class);
            return savedProductDTO;
        }else{
            throw new APIException("Product with name " + productDto.getProductName() + " already exists!!!!");
        }
    }

    @Override
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String keyword, String category) {

        Sort sortByAndOrder;
        if (sortOrder.equalsIgnoreCase("asc")) {
            sortByAndOrder = Sort.by(sortBy).ascending();
        } else {
            sortByAndOrder = Sort.by(sortBy).descending();
        }
        Pageable pageDetails= PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Specification<Product> spec =   Specification.where(null);
        if (keyword != null && !keyword.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("productName")), "%" + keyword.toLowerCase() + "%"));
        }

        if (category != null && !category.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("category").get("categoryName"), category));
        }



        Page<Product> productPage= productRepository.findAll(spec,pageDetails);
        List<Product> productList= productPage.getContent();
        if(productList.isEmpty())
        {
            throw new APIException("No products found");
        }

        List<ProductDTO> productDtoList = productList.stream()
                .map(product-> {
                    ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
                    productDTO.setImage(constructImageUrl(productDTO.getImage()));
                    return productDTO;
                        })
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDtoList);

        //below here we are setting the page details for the response
        productResponse.setPageNumber(productPage.getNumber() + 1);
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setLastPage(productPage.isLast());
        productResponse.setSortBy(sortBy);
        productResponse.setSortOrder(sortOrder);

        return productResponse;
    }

    private String constructImageUrl(String imageName)
    {
        return imageBaseUrl.endsWith("/")? imageBaseUrl + imageName : imageBaseUrl+ "/" + imageName;
    }

    @Override
    public ProductResponse searchByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        Sort sortByAndOrder;
        if (sortOrder.equalsIgnoreCase("asc")) {
            sortByAndOrder = Sort.by(sortBy).ascending();
        } else {
            sortByAndOrder = Sort.by(sortBy).descending();
        }
        Pageable pageDetails= PageRequest.of(pageNumber-1, pageSize, sortByAndOrder);


        Page<Product> productPage= productRepository.findByCategoryOrderByPriceAsc(category, pageDetails);


        List<ProductDTO> productDtoList = productPage.stream()
                .map(product-> modelMapper.map(product, ProductDTO.class))
                .toList();
        ProductResponse productResponse = new ProductResponse();

        productResponse.setContent(productDtoList);
        productResponse.setPageNumber(productPage.getNumber() + 1);
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setLastPage(productPage.isLast());
        productResponse.setSortBy(sortBy);
        productResponse.setSortOrder(sortOrder);

        return productResponse;
    }

    @Override
    public ProductResponse searchByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sortByAndOrder;
        if (sortOrder.equalsIgnoreCase("asc")) {
            sortByAndOrder = Sort.by(sortBy).ascending();
        } else {
            sortByAndOrder = Sort.by(sortBy).descending();
        }
        Pageable pageDetails= PageRequest.of(pageNumber-1, pageSize, sortByAndOrder);

        Page<Product> productList= productRepository.findByProductNameContainingIgnoreCase( keyword,pageDetails);
        List<ProductDTO> productDtoList = productList.stream()
                .map(product-> modelMapper.map(product, ProductDTO.class)).toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDtoList);

        productResponse.setPageNumber(productList.getNumber() + 1);
        productResponse.setPageSize(productList.getSize());
        productResponse.setTotalElements(productList.getTotalElements());
        productResponse.setTotalPages(productList.getTotalPages());
        productResponse.setLastPage(productList.isLast());
        productResponse.setSortBy(sortBy);
        productResponse.setSortOrder(sortOrder);

        return productResponse;
    }

    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO productDto) {

        Product productFromDb = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        Product product = modelMapper.map(productDto, Product.class);

        productFromDb.setProductName(product.getProductName());
        productFromDb.setDescription(product.getDescription());
        productFromDb.setQuantity(product.getQuantity());
        productFromDb.setDiscount(product.getDiscount());
        productFromDb.setPrice(product.getPrice());
        productFromDb.setSpecialPrice(product.getSpecialPrice());

         Product savedProduct = productRepository.save(productFromDb);
         ProductDTO savedProductDTO = modelMapper.map(savedProduct, ProductDTO.class);

         List<Cart> carts = cartRepository.findCartsByProductId(productId);

         List<CartDTO> cartDTOS = carts.stream().map(cart->{
             CartDTO cartDTO= modelMapper.map(cart,CartDTO.class);
             List<ProductDTO> products = cart.getCartItems().stream()
                     .map(p-> modelMapper.map(p.getProduct(),ProductDTO.class))
                     .toList();
             cartDTO.setProducts(products);
             return cartDTO;
         }).toList();

        cartDTOS.forEach(cart-> cartService.updateProductInCarts(cart.getCartId(), productId));


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

        //DELETE
        List<Cart> carts = cartRepository.findCartsByProductId(productId);
        carts.forEach(cart->{cartService.deleteProductFromCart(cart.getCartId(),productId);});

        productRepository.delete(productToDelete);
        return modelMapper.map(productToDelete,ProductDTO.class);
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {

        //Steps to upload image to the server

        //Get the product from the database
        Product productFromDb= productRepository.findById(productId)
                .orElseThrow(()-> new ResourceNotFoundException("Product","productId",productId));
        //Upload the image to the server/ in the /image folder
        // Get the file name of uploaded image
        String path = "images/";//specifying path
        String fileName= fileService.uploadImage(path,image);
        // update the new file name in the product object
        productFromDb.setImage(fileName);
        //save updated product
        Product updatedProduct = productRepository.save(productFromDb);
        //return the DTO after mapping product to DTO
        ProductDTO updatedProductDTO = modelMapper.map(updatedProduct,ProductDTO.class);
        return updatedProductDTO;
    }


    @Override
    public ProductResponse getAllProductsForAdmin(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> pageProducts = productRepository.findAll(pageDetails);

        List<Product> products = pageProducts.getContent();

        List<ProductDTO> productDTOS = products.stream()
                .map(product -> {
                    ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
                    productDTO.setImage(constructImageUrl(product.getImage()));
                    return productDTO;
                })
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setLastPage(pageProducts.isLast());
        return productResponse;
    }

    @Override
    public ProductResponse getAllProductsForSeller(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        User user = authUtil.loggedInUser();
        Page<Product> pageProducts = productRepository.findByUser(user,pageDetails);

        List<Product> products = pageProducts.getContent();

        List<ProductDTO> productDTOS = products.stream()
                .map(product -> {
                    ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
                    productDTO.setImage(constructImageUrl(product.getImage()));
                    return productDTO;
                })
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setLastPage(pageProducts.isLast());
        return productResponse;

    }


}
