package org.example.springkart.project.service;

import org.example.springkart.project.model.Category;
import org.example.springkart.project.payload.CategoryDTO;
import org.example.springkart.project.payload.CategoryResponse;

import java.util.List;

public interface CategoryService {

    CategoryResponse getAllCategory(Integer pageNumber , Integer pageSize, String sortBy, String sortOrder);

    CategoryDTO createCategory(CategoryDTO categoryDTO);

    CategoryDTO deleteCategory(Long categoryId);

     CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDto);
}
