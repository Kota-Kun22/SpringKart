package org.example.springkart.project.Controller;


import jakarta.validation.Valid;
import org.example.springkart.project.model.Category;
import org.example.springkart.project.payload.CategoryDTO;
import org.example.springkart.project.payload.CategoryResponse;
import org.example.springkart.project.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/** RequestMapping let the developer set the URL patterns  */
@RestController
@RequestMapping("/api")//this is the request mapping on the class or controller level which let us not to repeat /api in every address
public class CatergoryController {


    private CategoryService categoryService;



    public CatergoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    //@GetMapping("/api/public/categories")
    /**
     * with the help of Request mapping, I don't have to write the specific Mapping like get and post this is on method level
     * */
    @RequestMapping(value = "/public/categories",method = RequestMethod.GET)
    public ResponseEntity<List<Category>> getAllCategory(){

        CategoryResponse categoriesResponse= categoryService.getAllCategory();
       return  new ResponseEntity(categoriesResponse,HttpStatus.OK);
    }

    //@PostMapping("/api/public/categories")
    @RequestMapping(value = "/public/categories",method = RequestMethod.POST)
    public ResponseEntity<CategoryDTO> createCategory( @Valid @RequestBody CategoryDTO categoryDto){


        CategoryDTO savedCategoryDTO= categoryService.createCategory(categoryDto);

        return  new ResponseEntity<>(savedCategoryDTO, HttpStatus.CREATED);
    }

    //@DeleteMapping("/api/admin/categories/{categoryId}")
    @RequestMapping(value = "/admin/categories/{categoryId}",method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteCategory(@PathVariable  Long categoryId){
            String status= categoryService.deleteCategory(categoryId);
            return new ResponseEntity<>(status, HttpStatus.OK);
        }


    //@PutMapping("/api/admin/categories/{categoryId}")
    @RequestMapping(value = "/admin/categories/{categoryId}",method = RequestMethod.PUT)
    public ResponseEntity<String> updateCategory(@PathVariable Long categoryId,@RequestBody Category category){
//        categoryService.updateCategory(categoryId,category);
//        return new ResponseEntity<>("Category updated successfully",HttpStatus.OK);
            String status= categoryService.updateCategory(categoryId,category);
            return new ResponseEntity<>(status, HttpStatus.OK);
    }


}
