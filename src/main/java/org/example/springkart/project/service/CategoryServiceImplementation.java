package org.example.springkart.project.service;

import org.example.springkart.project.model.Category;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;



@Service
public class CategoryServiceImplementation  implements CategoryService{

    private List<Category> categoryList= new ArrayList<>();
    private long nextId = 1L;

    @Override
    public List<Category> getAllCategory() {
        return categoryList;
    }

    @Override
    public void createCategory(Category category) {

        category.setCategoryId(nextId++);

        categoryList.add(category);


    }

    @Override
    public String deleteCategory(Long categoryId) {
        Category category= categoryList.stream()
                .filter(c-> c.getCategoryId().equals(categoryId))
                .findFirst()
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Dont have this ID"));

        categoryList.remove(category);
        return "category with ID " + categoryId + " deleted successfully";
    }

    @Override
    public String updateCategory(Long categoryId, Category category) {
        Category IdHasToBeUpdate = null;
        for(Category  c: categoryList){
            if(c.getCategoryId().equals(categoryId)){
             IdHasToBeUpdate=c;
             break;
            }
        }
        if(IdHasToBeUpdate!=null){
            IdHasToBeUpdate.setCategoryName(category.getCategoryName());
            return "category with ID " + categoryId + " updated successfully";
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category with ID " + categoryId + " not found");
        }
    }
}
