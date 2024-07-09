package com.producto.demo.domain;

import com.producto.demo.dto.CategoryDto;
import com.producto.demo.dto.CategoryRequestDto;
import com.producto.demo.exception.CategoryNotFoundException;

import java.util.List;

public interface CategoryService {

    CategoryDto save(CategoryRequestDto categoryRequestDto);

    //READ
    List<CategoryDto> findAll();

    CategoryDto findById(String id) throws CategoryNotFoundException;

    //UPDATE
    CategoryDto update(CategoryDto categoryDto);

    //DELETE
    void delete(String id) throws CategoryNotFoundException;


}