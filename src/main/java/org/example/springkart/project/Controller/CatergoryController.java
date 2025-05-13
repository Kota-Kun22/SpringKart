package org.example.springkart.project.Controller;


import org.example.springkart.project.model.Category;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CatergoryController {

    private List<Category> categoryList= new ArrayList<>();


    @GetMapping("/api/public/categories")
    public List<Category> getAllCategory(){
        return categoryList;
    }

    @PostMapping("/api/public/categories")
    public String createCategory( @RequestBody Category category){
        categoryList.add(category);
        System.out.println(categoryList);
        return "Category Created Successfully";
    }



}
