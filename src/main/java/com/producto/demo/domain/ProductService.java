package com.producto.demo.domain;

import com.producto.demo.dto.ProductDto;
import com.producto.demo.dto.ProductRequestDto;
import com.producto.demo.exception.ProductNotFoundException;

import java.util.List;

public interface ProductService {
    //CRUD
    //C - Create
    //R - Read
    //U - Update
    //D - Delete

    // Repository - Entity
    //
    //  |
    //  V
    //
    // Service    - Dto ==
    //
    //  |
    //  V
    // Controller - Dto ==

    //CREATE
    ProductDto save(ProductRequestDto productRequestDto);

    //READ
    List<ProductDto> findAll();

    ProductDto findById(Long id) throws ProductNotFoundException;

    //UPDATE
    ProductDto update(ProductDto productDto);

    //DELETE
    void delete(Long id) throws ProductNotFoundException;
}
