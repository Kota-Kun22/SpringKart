package org.example.springkart.project.model;

public class Category {

    private Long CategoryId;
    private String CategoryName;

    public Category(String categoryName, Long categoryId) {
        CategoryName = categoryName;
        CategoryId = categoryId;
    }

    public Long getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(Long categoryId) {
        CategoryId = categoryId;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }
}
