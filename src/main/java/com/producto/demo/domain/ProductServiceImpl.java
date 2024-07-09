package com.producto.demo.domain;

import com.producto.demo.dao.entity.GalleryEntity;
import com.producto.demo.dao.entity.ProductEntity;
import com.producto.demo.dao.repository.ProductRepository;
import com.producto.demo.dto.*;
import com.producto.demo.exception.ProductNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service // bean
@AllArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository repository;
    private final GalleryService galleryService;

    @Override
    @Transactional
    public ProductDto save(ProductRequestDto productRequestDto) {

        ProductEntity productToSave = new ProductEntity();
        productToSave.setName(productRequestDto.getName());
        productToSave.setCategoryById(productRequestDto.getCategory());
        productToSave.setPrice(productRequestDto.getPrice());
        productToSave.setDescription(productRequestDto.getDescription());
        productToSave.setQuantity(productRequestDto.getQuantity());


        productToSave.setGalleriesEntity(
                productRequestDto.getGalleries().stream().map(g->{
                    GalleryEntity galleryEntity = new GalleryEntity();
                    galleryEntity.setUrl(g.getUrl());
                    return galleryEntity;
                }).collect(Collectors.toList())
        );

        productToSave = repository.saveAndFlush(productToSave);



        return mapProductEntity2Dto(productToSave);
    }

    @Override
    public List<ProductDto> findAll() {
        return repository
                .findAll()
                .stream()
                .map(ProductServiceImpl::mapProductEntity2Dto)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDto findById(Long id) throws ProductNotFoundException {
        log.info("Calling findById: {}",id);
        return repository
                .findById(id)
                .map(ProductServiceImpl::mapProductEntity2Dto)
                .orElseThrow(()-> new ProductNotFoundException("Product not found: "+id));
    }



    @Override
    public ProductDto update(final ProductDto productDto) {
        return repository
                .findById(productDto.getId())
                .map(productEntity -> {

                    mapProductDto2Entity(productDto, productEntity);

                    return mapProductEntity2Dto(repository.saveAndFlush(productEntity));
                })
                .orElseThrow(()-> new ProductNotFoundException("Product not found: "+productDto.getId()));
    }



    @Override
    public void delete(final Long id) throws ProductNotFoundException {
        repository
                .findById(id)
                .ifPresentOrElse(
                        repository::delete, 
                        ()-> {throw new ProductNotFoundException("Product not found: "+id);}
                );
    }

    private static void mapProductDto2Entity(ProductDto productDto, ProductEntity productEntity) {
        productEntity.setName(productDto.getName());
        productEntity.setDescription(productDto.getDescription());
        productEntity.setCategoryById(productDto.getCategory());
        productEntity.setPrice(productDto.getPrice());
        productEntity.setQuantity(productDto.getQuantity());
    }

    private static ProductDto mapProductEntity2Dto(ProductEntity productEntity) {
        ProductDto productDto = new ProductDto();
        productDto.setId(productEntity.getId());
        productDto.setName(productEntity.getName());
        productDto.setDescription(productEntity.getDescription());
        productDto.setCategory(productEntity.getCategoryEntity().getId());
        productDto.setPrice(productEntity.getPrice());
        productDto.setQuantity(productEntity.getQuantity());
        productDto.setGalleries(productEntity.getGalleries().stream().map(g -> {
            GalleryDto galleryDto = new GalleryDto();
            galleryDto.setUrl(g.getUrl());
            galleryDto.setId(g.getId());
            galleryDto.setProductId(g.getProductEntity().getId());

            return galleryDto;
        }).collect(Collectors.toList()));
        return productDto;
    }
}
