package org.example.springkart.project.service;

import org.example.springkart.project.model.Category;
import org.example.springkart.project.payload.CategoryDTO;
import org.example.springkart.project.payload.CategoryResponse;

import java.util.List;

public interface CategoryService {

    CategoryResponse getAllCategory();

    CategoryDTO createCategory(CategoryDTO categoryDTO);

    String deleteCategory(Long categoryId);

    String updateCategory(Long categoryId, Category category);
}
