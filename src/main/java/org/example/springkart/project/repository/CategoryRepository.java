package org.example.springkart.project.repository;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.example.springkart.project.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {


    Category findByCategoryName(String categoryName);
    /**
     * here the power of JPA is that we as a developer need not to write the implementation of this EXTENDED method
     * here with the help of its camelCase convention it will automatically generate the implementation for us under the hood
     * */

}
