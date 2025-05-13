package org.example.springkart.project.Controller;


import org.example.springkart.project.model.Category;
import org.example.springkart.project.service.CategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CatergoryController {

    private CategoryService categoryService;



    public CatergoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/api/public/categories")
    public List<Category> getAllCategory(){
        return categoryService.getAllCategory();
    }

    @PostMapping("/api/public/categories")
    public String createCategory( @RequestBody Category category){
        categoryService.createCategory(category);

        return "Category Created Successfully";
    }



}
