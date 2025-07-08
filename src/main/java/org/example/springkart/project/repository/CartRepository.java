package org.example.springkart.project.repository;

import org.example.springkart.project.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {


    @Query("SELECT c FROM Cart  c WHERE c.user.email = ?1")
    Cart findCartByEmail(String email);

    @Query("SELECT c FROM Cart  c WHERE c.user.email = ?1 AND c.cartId = ?2")
    Cart findCartByEmailAndCartId(String emailId, Long cartId);


    @Query(" SELECT c FROM  Cart c JOIN FETCH c.cartItems ci JOIN FETCH ci.product p WHERE p.productId=?1")
    List<Cart> findCartsByProductId(Long productId);
}
