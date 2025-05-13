package org.example.springkart.project.service;

import org.example.springkart.project.model.Category;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImplementation  implements CategoryService{

    private List<Category> categoryList= new ArrayList<>();

    @Override
    public List<Category> getAllCategory() {
        return categoryList;
    }

    @Override
    public void createCategory(Category category) {
        categoryList.add(category);

    }
}
