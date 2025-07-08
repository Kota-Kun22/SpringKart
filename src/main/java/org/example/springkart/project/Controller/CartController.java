package org.example.springkart.project.Controller;

import org.apache.coyote.Response;
import org.example.springkart.project.model.Cart;
import org.example.springkart.project.payload.CartDTO;
import org.example.springkart.project.repository.CartRepository;
import org.example.springkart.project.service.CartService;
import org.example.springkart.project.utils.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CartController {

    @Autowired
    private CartService cartService;
    @Autowired
    private AuthUtil authUtil;
    @Autowired
    private CartRepository cartRepository;

    @PostMapping("/carts/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> addProductToCart(@PathVariable Long productId, @PathVariable Integer quantity)
    {
        CartDTO cartDTO =  cartService.addProductToCart(productId, quantity);
        return new ResponseEntity<CartDTO>(cartDTO, HttpStatus.CREATED);

    }

    @GetMapping("/carts")
    public ResponseEntity<List<CartDTO>> getCarts()
    {
        List<CartDTO> cartDTOS= cartService.getAllCarts();
        return new ResponseEntity<List<CartDTO>>(cartDTOS,HttpStatus.OK);

    }

    @GetMapping("/carts/user/cart")
    public ResponseEntity<CartDTO> getCartById()
    {
        String email = authUtil.loggedInEmail();
        Cart cart = cartRepository.findCartByEmail(email);
        Long cartId = cart.getCartId();
        CartDTO cartDto= cartService.getCart(email,cartId);
        return new ResponseEntity<>(cartDto,HttpStatus.OK);

    }


    @PutMapping("/cart/products/{productId}/quantity/{operation}")
    public ResponseEntity<CartDTO> updateCartProduct(@PathVariable  Long productId, @PathVariable String operation)
    {
        CartDTO cartDto= cartService.updateProductQuantity(productId, operation.equalsIgnoreCase("delete")? -1: 1);
        return new ResponseEntity<CartDTO>(cartDto,HttpStatus.OK);
    }

    @DeleteMapping("/carts/{cartId}/products/{productId}")
    public ResponseEntity<String> deleteProductFromCart(@PathVariable Long cartId, @PathVariable  Long productId){

        String  status = cartService.deleteProductFromCart(cartId,productId);
        return new ResponseEntity<String>(status,HttpStatus.OK);
    }











}
