package org.example.springkart.project.service;

import org.example.springkart.project.model.Category;
import org.example.springkart.project.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;



@Service
public class CategoryServiceImplementation  implements CategoryService{

    //private List<Category> categoryList= new ArrayList<>();
    @Autowired
    CategoryRepository categoryRepository;

    private long nextId = 1L;

    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    @Override
    public void createCategory(Category category) {

//        category.setCategoryId(nextId++);since in my entity I have generationType.IDENTITY database is responsible for auto-incrementing the primary key

        categoryRepository.save(category);


    }

    @Override
    public String deleteCategory(Long categoryId) {
        List<Category> categoryList = categoryRepository.findAll();//here we are loading everything from the database which is not efficient
        //plus if we are just doing categoryRepository.findAll().stream().filter(...).findFirst() then we are streaming in-memory java object, not databse row
        Category category= categoryList.stream()
                .filter(c-> c.getCategoryId().equals(categoryId))
                .findFirst()
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Dont have this ID"));

        categoryRepository.delete(category);
        return "category with ID " + categoryId + " deleted successfully";
    }

    @Override
    public String updateCategory(Long categoryId, Category category) {
//        Category IdHasToBeUpdate = null;
//        List<Category> categoryList = categoryRepository.findAll();
//        for(Category  c: categoryList){
//            if(c.getCategoryId().equals(categoryId)){
//             IdHasToBeUpdate=c;
//             break;
//            }
//        }
//        if(IdHasToBeUpdate!=null){
////            IdHasToBeUpdate.setCategoryName(category.getCategoryName());
//              categoryRepository.save(category);
//            return "category with ID  " + categoryId + " updated successfully";
//        }else{
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category with ID " + categoryId + " not found");
//        }
        Category existingCategory = categoryRepository.findById(categoryId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Category with ID " + categoryId + " not found"));
        existingCategory.setCategoryName(category.getCategoryName());
        categoryRepository.save(existingCategory);
        return "category with ID " + categoryId + " updated successfully";


    }
}
