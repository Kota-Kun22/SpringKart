package org.example.springkart.project.service;

import org.example.springkart.project.model.Category;

import java.util.List;

public interface CategoryService {

    List<Category> getAllCategory();
    void createCategory(Category category);

    String deleteCategory(Long categoryId);
}
