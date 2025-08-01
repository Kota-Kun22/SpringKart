package org.example.springkart.project.service;

import jakarta.transaction.Transactional;
import org.example.springkart.project.payload.CartDTO;
import org.example.springkart.project.payload.CartItemDTO;

import java.util.List;


public interface CartService {

    CartDTO addProductToCart(Long productId, Integer quantity);

    List<CartDTO> getAllCarts();

    CartDTO getCart(String email, Long cartId);

    @Transactional
    CartDTO updateProductQuantity(Long productId, Integer quantity);

    String deleteProductFromCart(Long cartId, Long productId);

    void updateProductInCarts(Long cartId, Long productId);

    String createOrUpdateCartWithItems(List<CartItemDTO> cartItems);
}
