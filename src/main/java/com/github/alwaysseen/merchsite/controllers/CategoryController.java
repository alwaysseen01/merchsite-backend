package com.github.alwaysseen.merchsite.controllers;

import com.github.alwaysseen.merchsite.entities.Category;
import com.github.alwaysseen.merchsite.repositories.CategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("api/category")
public class CategoryController {
    private CategoryRepository categoryRepository;

    record NewCategoryRequest(
            String name
    ){}

    @GetMapping
    public ResponseEntity<List<Category>> getAll(){
        return new ResponseEntity<>(categoryRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/id/{category_id}")
    public ResponseEntity<Category> getById(@PathVariable("category_id") Integer id){
        Category category = categoryRepository.findById(id).get();
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Category> create(@RequestBody NewCategoryRequest request){
        Category category = new Category();
        category.setName(request.name());
        categoryRepository.save(category);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/update/{category_id}")
    public ResponseEntity<Category> update(@RequestBody NewCategoryRequest request,
                                           @PathVariable("category_id") Integer id){
        Category category = categoryRepository.findById(id).get();
        category.setName(request.name());
        categoryRepository.save(category);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{category_id}")
    public ResponseEntity<Category> delete(@PathVariable("category_id") Integer id){
        categoryRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
