package org.example.springkart.project.service;

import jakarta.transaction.Transactional;
import org.example.springkart.project.exception.APIException;
import org.example.springkart.project.exception.ResourceNotFoundException;
import org.example.springkart.project.model.Cart;
import org.example.springkart.project.model.CartItem;
import org.example.springkart.project.model.Product;
import org.example.springkart.project.payload.CartDTO;
import org.example.springkart.project.payload.CartItemDTO;
import org.example.springkart.project.payload.ProductDTO;
import org.example.springkart.project.repository.CartItemRepository;
import org.example.springkart.project.repository.CartRepository;
import org.example.springkart.project.repository.ProductRepository;
import org.example.springkart.project.utils.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class CartServiceImplementation  implements CartService{


    @Autowired
    CartRepository cartRepository;

    @Autowired
    AuthUtil authUtil;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartItemRepository cartItemRepository;
    @Autowired
    private ModelMapper modelMapper;

    /**
     * Adds a product to the user's shopping cart. If the cart does not exist, it creates a new cart.
     * Validates the product availability and stock before adding it to the cart.
     *
     * @param productId the unique identifier of the product to be added
     * @param quantity the quantity of the product to be added to the cart
     * @return an updated CartDTO representing the state of the cart after the product is added
     * @throws ResourceNotFoundException if the product with the specified ID is not found
     * @throws APIException if the product already exists in the cart, is out of stock, or the requested quantity exceeds the available stock
     *
     */
    @Override
    public CartDTO addProductToCart(Long productId, Integer quantity) {
        //find existing cart or create one
        // retrieve product details
        //perform validation(like stalk exists or not)
        // create cart Item
        //save cart item
        // return updated cart

        //find an existing cart or create one
        Cart cart= createCart();

        // retrieve product details
        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new ResourceNotFoundException("Product","productId",productId));

        //perform validation(like stalk exists or not)
        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cart.getCartId(),productId);

        /* here I am checking that is there any product in the cart with this productId
         * and checking of cartId becoz there will be many cart of different users here cart.getCartId() is of particular user **/

        //here are the validatioms
        if(cartItem != null)
        {
            throw new APIException("Product "+ product.getProductName() +" already exists in your cart");
        }
        if(product.getQuantity()==0)
        {
            throw new APIException("Product "+ product.getProductName() +" is out of stock");
        }
        if(product.getQuantity()<quantity)
        {
            throw new APIException("please make an order of the "
                    + product.getProductName() +" for less than or equal to "+ product.getQuantity()+" quantity");
        }

        // create cart Item
        CartItem newCartItem= new CartItem();
        newCartItem.setProduct(product);
        newCartItem.setCart(cart);

        newCartItem.setQuantity(quantity);
        newCartItem.setDiscount(product.getDiscount());
        newCartItem.setProductPrice(product.getSpecialPrice());
        cartItemRepository.save(newCartItem);

        product.setQuantity(product.getQuantity()-quantity);//i am going to reduce the stalk also is the use has booked some quantity
        cart.setTotalPrice(cart.getTotalPrice()+(product.getSpecialPrice()*quantity));

        //save cart item
        cartRepository.save(cart);

        //return updated Cart
        CartDTO cartDTO= modelMapper.map(cart,CartDTO.class);

        List<CartItem> cartItems= cart.getCartItems();

        Stream<ProductDTO> productDTOStream = cartItems.stream().map( item->{
            ProductDTO map = modelMapper.map(item.getProduct(), ProductDTO.class);
            map.setQuantity(item.getQuantity());
            return map;
        });

        cartDTO.setProducts(productDTOStream.toList());

        return cartDTO;
    }

    @Override
    public List<CartDTO> getAllCarts() {

       List<Cart> carts = cartRepository.findAll();

       if(carts.isEmpty())
       {
           throw new APIException("No carts found");
       }

        List<CartDTO> cartDTOs = carts.stream().map(cart -> {
            CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

            List<ProductDTO> products = cart.getCartItems().stream().map(cartItem -> {
                ProductDTO productDTO = modelMapper.map(cartItem.getProduct(), ProductDTO.class);
                productDTO.setQuantity(cartItem.getQuantity()); // Set the quantity from CartItem
                return productDTO;
            }).collect(Collectors.toList());


            cartDTO.setProducts(products);

            return cartDTO;

        }).toList();

       return cartDTOs;
    }

    @Override
    public CartDTO getCart(String emailId, Long cartId) {

        Cart cart = cartRepository.findCartByEmailAndCartId(emailId,cartId);
        if(cart == null)
        {
            throw new ResourceNotFoundException("Cart","cartId",cartId);
        }
        CartDTO cartDTO = modelMapper.map(cart,CartDTO.class);

        //set the quantity of that is set by the user dont show the stock quantiy ( this has to be implememted )
//        cart.getCartItems().forEach(item-> item.setQuantity(item.getQuantity()));//here done !!
        cart.getCartItems().forEach(c ->
                c.getProduct().setQuantity(c.getQuantity()));


        List<ProductDTO> products = cart.getCartItems().stream()
                .map(p-> modelMapper.map(p.getProduct(),ProductDTO.class))
                .toList();
        cartDTO.setProducts(products);
        return cartDTO;
    }

    private Cart createCart(){
        Cart userCart= cartRepository.findCartByEmail(authUtil.loggedInEmail());
        if(userCart != null)
        {
            return userCart;
        }
        Cart newCart= new Cart();
        newCart.setTotalPrice(0.0);
        newCart.setUser(authUtil.loggedInUser());
        Cart newCartSaved= cartRepository.save(newCart);
        return newCartSaved;
    }

    @Transactional
    @Override
    public CartDTO updateProductQuantity(Long productId, Integer quantity) {

        String emailId = authUtil.loggedInEmail();
        Cart Usercart = cartRepository.findCartByEmail(emailId);
        Long cartId = Usercart.getCartId();// i have to cartId now from the loggedIn user
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(()-> new ResourceNotFoundException("Cart","cartId",cartId));

        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new ResourceNotFoundException("Product","productId",productId));

        if(product.getQuantity()==0){
            throw new APIException("Product "+ product.getProductName() +" is out of stock");
        }
        if(product.getQuantity() < quantity)
        {
            throw new APIException("please make an order of the "
                    + product.getProductName() +" for less than or equal to "+ product.getQuantity()+" quantity");
        }

        CartItem cartItem= cartItemRepository.findCartItemByProductIdAndCartId(cartId,productId);
        if(cartItem == null)
        {
            throw  new APIException("Product "+ product.getProductName() +" does not exist in your cart");
        }

        int newQuantity =cartItem.getQuantity()+quantity ;//calculating the new quantities
        //validation they should not go negative
        if(newQuantity < 0)
        {
            throw new APIException("Product "+ product.getProductName() +" quantity cannot be negative");
        }

        if(newQuantity == 0)
        {
            deleteProductFromCart(cartId,productId);
        }else
        {
            cartItem.setProductPrice(product.getSpecialPrice());
            cartItem.setQuantity(cartItem.getQuantity() + quantity );
            cartItem.setDiscount(product.getDiscount());
            cart.setTotalPrice(cart.getTotalPrice() + ( product.getSpecialPrice() * quantity) );
            cartRepository.save(cart);
        }

        CartItem updatedCartItem = cartItemRepository.save(cartItem);

        if(updatedCartItem.getQuantity() == 0)
        {
            cartItemRepository.delete(updatedCartItem);
        }
        CartDTO cartDTO= modelMapper.map(cart,CartDTO.class);
        List<CartItem>cartItems= cart.getCartItems();

        Stream<ProductDTO> productStream = cartItems.stream().map(item->{
            ProductDTO prd = modelMapper.map(item.getProduct(),ProductDTO.class);
            prd.setQuantity(item.getQuantity());
            return prd;
        });
        cartDTO.setProducts(productStream.toList());
        return cartDTO;
    }

    @Transactional
    @Override
    public String deleteProductFromCart(Long cartId, Long productId) {

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(()-> new ResourceNotFoundException("Cart","cartId",cartId));
        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId,productId);
        if(cartItem == null)
        {
            throw new ResourceNotFoundException("Product","productId",productId);
        }

        cart.setTotalPrice(cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity()));

        cartItemRepository.deleteCartItemByProductIdAndCartId(cartId,productId);

        return "Product"+ cartItem.getProduct().getProductName() +" has been removed from your cart !!!!..";
    }

    @Override
    public void updateProductInCarts(Long cartId, Long productId) {

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(()-> new ResourceNotFoundException("Cart","cartId",cartId));

        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new ResourceNotFoundException("Product","productId",productId));

        CartItem cartItem =cartItemRepository.findCartItemByProductIdAndCartId(cartId,productId);

        if(cartItem == null)
        {
            throw new APIException("Product "+ product.getProductName() +" does not exist in your cart");
        }

        //1000 - 100*2 = 800
        double cartPrice = cart.getTotalPrice() -
                (cartItem.getProductPrice() * cartItem.getQuantity());

        //price of product increase to 200 now due to some reason s
        cartItem.setProductPrice(product.getSpecialPrice());

        // 800 + (200*2)= 1200
        cart.setTotalPrice( cartPrice +
                (cartItem.getProductPrice() * cartItem.getQuantity())
        );
        cartItem = cartItemRepository.save(cartItem);
    }

    @Transactional
    @Override
    public String createOrUpdateCartWithItems(List<CartItemDTO> cartItems) {
        /* things i hve to do whch will be 
        1. get user's email 
        2. clear all current items in the existing cart
        3. process ech items in the request to add to the cart 
        4.find the product by ID
        5.directly update product stock and total price 
        6.create and sace cart items
        7. update the cart's total price and save
        * */

        //1 and 2
        String email= authUtil.loggedInEmail();
        Cart existingCart= cartRepository.findCartByEmail(email);
        if(existingCart==null)
        {
            existingCart= new Cart();
            existingCart.setTotalPrice(0.0);
            existingCart.setUser(authUtil.loggedInUser());
            existingCart= cartRepository.save(existingCart);
        }
        else {
            //3
            cartItemRepository.deleteAllByCartId(existingCart.getCartId());
        }
        double totalPrice=0.0;
        for(CartItemDTO cartItemDTO :cartItems )
        {
            //4
            Long productId= cartItemDTO.getProductId();//check it
            Integer quantity = cartItemDTO.getQuantity();

            //5
            Product product= productRepository.findById(productId)
                    .orElseThrow(()-> new ResourceNotFoundException("Product","productId",productId));

            //product.setQuantity(product.getQuantity()-quantity); //i have just commented for a development purpose
            totalPrice += product.getSpecialPrice()*quantity;

            CartItem cartItem= new CartItem();
            cartItem.setProduct(product);
            cartItem.setCart(existingCart);
            cartItem.setQuantity(quantity);
            cartItem.setDiscount(product.getDiscount());
            cartItem.setProductPrice(product.getSpecialPrice());
            cartItemRepository.save(cartItem);
        }
        existingCart.setTotalPrice(totalPrice);
        cartRepository.save(existingCart);
        return "Cart updated successfully !!!!..";
        
    }
}
