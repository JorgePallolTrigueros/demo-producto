package com.producto.demo.controller;

import com.producto.demo.domain.CategoryService;
import com.producto.demo.domain.ProductService;
import com.producto.demo.dto.CategoryDto;
import com.producto.demo.dto.CategoryRequestDto;
import com.producto.demo.dto.ProductDto;
import com.producto.demo.dto.ProductRequestDto;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/v1/category")
@AllArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostConstruct
    void init(){

        var category1 = new CategoryRequestDto();

        category1.setName("Category 1");
        category1.setDescription("C1");

        var category2 = new CategoryRequestDto();
        category2.setName("Category 2");
        category2.setDescription("C2");

        categoryService.save(category1);
        categoryService.save(category2);
    }

    @GetMapping("/")
    public List<CategoryDto> findAll(){
        return categoryService.findAll();
    }
    @GetMapping("/{id}")
    public CategoryDto findCategorytById(@PathVariable String id){
        return categoryService.findById(id);
    }


    @PostMapping("/")
    public CategoryDto saveCategory(@RequestBody @Valid CategoryRequestDto request){
        return categoryService.save(request);
    }

    @PatchMapping("/")
    public CategoryDto updateCategory(@RequestBody CategoryDto categoryDto){
        return categoryService.update(categoryDto);
    }

    @DeleteMapping("/{id}")
    void deleteCategory(String id){
        categoryService.delete(id);
    }



}
