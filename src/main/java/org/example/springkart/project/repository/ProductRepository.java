package org.example.springkart.project.repository;

import org.example.springkart.project.model.Category;
import org.example.springkart.project.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    List<Product> findByCategoryOrderByPriceAsc(Category category);

    List<Product> findByProductNameContainingIgnoreCase(String keyword);
}
