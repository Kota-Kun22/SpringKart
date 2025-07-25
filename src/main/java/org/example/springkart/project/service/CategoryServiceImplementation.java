package org.example.springkart.project.service;

import org.example.springkart.project.exception.APIException;
import org.example.springkart.project.exception.ResourceNotFoundException;
import org.example.springkart.project.model.Category;
import org.example.springkart.project.payload.CategoryDTO;
import org.example.springkart.project.payload.CategoryResponse;
import org.example.springkart.project.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @Autowired
    private ModelMapper modelMapper;

    private long nextId = 1L;

    @Override
    public CategoryResponse getAllCategory(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sortByAndOrder;
        if (sortOrder.equalsIgnoreCase("asc")) {
            sortByAndOrder = Sort.by(sortBy).ascending();
        } else {
            sortByAndOrder = Sort.by(sortBy).descending();
        }

        Pageable pageDetails= PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Category> categoryPage = categoryRepository.findAll(pageDetails);//here i have the apage which the user requsted with the pageNo and pagesize where categroyPage is the paginated object help by the jpa to get the page no and pgsize  ;

        List<Category> categoryList =categoryPage.getContent();//here get content will get he
        if(categoryList.isEmpty()){
            throw new APIException("No categories created till now");
        }

        //below here for every category in the list we are mapping it to the that of category DTO for that we are using Stream here and this model mapping is done with the help of model mapper
        List<CategoryDTO> categoryDTOs= categoryList.stream()
                .map(category->modelMapper.map(category,CategoryDTO.class))
                .toList();

        CategoryResponse categoryResponse = new CategoryResponse();

        categoryResponse.setContent(categoryDTOs);
        //below here we are setting the page details for the response
        categoryResponse.setPageNumber(categoryPage.getNumber()+1);
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setLastPage(categoryPage.isLast());
        categoryResponse.setSortBy(sortBy);
        categoryResponse.setSortOrder(sortOrder);

        return categoryResponse;

    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {

//        category.setCategoryId(nextId++);since in my entity I have generationType.IDENTITY database is responsible for auto-incrementing the primary key
        Category category = modelMapper.map(categoryDTO,Category.class);

        Category CategoryFromDb = categoryRepository.findByCategoryName(category.getCategoryName());
        if(CategoryFromDb!=null){
            throw new APIException("Category with name " + category.getCategoryName() + " already exists!!!!");
        }
        Category savedCategory1= categoryRepository.save(category);
        CategoryDTO savedCategoryDTO= modelMapper.map(savedCategory1,CategoryDTO.class);
        return savedCategoryDTO;

    }

    @Override
    public CategoryDTO deleteCategory(Long categoryId) {

        List<Category> categoryList = categoryRepository.findAll();//here we are loading everything from the database which is not efficient
        //plus if we are just doing categoryRepository.findAll().stream().filter(...).findFirst() then we are streaming in-memory java object, not databse row

        Category category= categoryList.stream()
                .filter(c-> c.getCategoryId().equals(categoryId))
                .findFirst()
                .orElseThrow(()-> new ResourceNotFoundException("Category","categoryId",categoryId));

        categoryRepository.delete(category);
        return modelMapper.map(category,CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDto) {
//        Category IdHasToBeUpdate = null;
//        List<Category> categoryList = categoryRepository.findAll();
//        for(Category  c: categoryList){
//            if(c.getCategoryId().equals(categoryId)){
//             IdHasToBeUpdate=c;
//             break;
//            }
//        }
//        if(IdHasToBeUpdate!=null){
//            IdHasToBeUpdate.setCategoryName(category.getCategoryName());
//              categoryRepository.save(category);
//            return "category with ID  " + categoryId + " updated successfully";
//        }else{
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category with ID " + categoryId + " not found");
//        }

        Category categoryUpdation = modelMapper.map(categoryDto,Category.class);

         categoryUpdation = categoryRepository
                .findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category","categoryId",categoryId));



        categoryUpdation.setCategoryName(categoryDto.getCategoryName());

        Category updatedCategory= categoryRepository.save(categoryUpdation);
        CategoryDTO UpdateCategoryDto = modelMapper.map(updatedCategory,CategoryDTO.class);

//        return "category with ID " + categoryId + " updated successfully";
       return  UpdateCategoryDto;


    }
}
