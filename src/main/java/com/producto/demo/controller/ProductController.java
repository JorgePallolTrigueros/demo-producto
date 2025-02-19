package com.producto.demo.controller;

import com.producto.demo.dao.entity.ProductEntity;
import com.producto.demo.domain.ProductService;
import com.producto.demo.dto.ProductDto;
import com.producto.demo.dto.ProductRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/product")
@AllArgsConstructor
public class ProductController {


    private final ProductService productService;

    @PostConstruct
    void init(){

        var product1 = new ProductRequestDto();

        product1.setName("Producto 1");
        product1.setDescription("P1");
        product1.setPrice(10.00);
        product1.setCategory("Category1");

        var product2 = new ProductRequestDto();
        product2.setName("Producto 2");
        product2.setDescription("P2");
        product2.setPrice(10.00);
        product2.setCategory("Category2");

        //productService.save(product1);
        //productService.save(product2);
    }

    @GetMapping("/")
    public List<ProductDto> findAll(){
        return productService.findAll();
    }
    @GetMapping("/{id}")
    public ProductDto findProductById(@PathVariable  Long id){
        return productService.findById(id);
    }

    @PostMapping("/")
    public ProductDto saveProduct(@RequestBody @Valid ProductRequestDto request){
        return productService.save(request);
    }

    @PatchMapping("/")
    public ProductDto updateProduct(@RequestBody ProductDto productDto){
        return productService.update(productDto);
    }

    @DeleteMapping("/{id}")
    void deleteProduct(Long id){
        productService.delete(id);
    }


    @PatchMapping("/reduce-stock/product-id/{productId}/quantity/{quantity}")
    public boolean reduceStock(@PathVariable Long productId,@PathVariable Long quantity){
        return productService.reduceStock(productId,quantity);
    }






}
