package com.producto.demo.domain;

import com.producto.demo.dao.entity.GalleryEntity;
import com.producto.demo.dao.entity.ProductEntity;
import com.producto.demo.dao.repository.ProductRepository;
import com.producto.demo.dto.*;
import com.producto.demo.exception.ProductNotFoundException;
import com.producto.demo.service.notification.ProductNotificationService;
import com.producto.demo.service.storage.FileStorageService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service // bean
@AllArgsConstructor
public class ProductServiceImpl implements ProductService{

    public static final int MIN_LOW_STOCK = 10;
    private final ProductRepository repository;
    private final GalleryService galleryService;
    private final ProductNotificationService productNotificationService;
    private final FileStorageService fileStorageService;

    @Override
    @Transactional
    public ProductDto save(ProductRequestDto productRequestDto) {



        ProductEntity productToSave = new ProductEntity();
        productToSave.setName(productRequestDto.getName());
        productToSave.setCategoryById(productRequestDto.getCategory());
        productToSave.setPrice(productRequestDto.getPrice());
        productToSave.setDescription(productRequestDto.getDescription());
        productToSave.setQuantity(productRequestDto.getQuantity());

        productToSave = repository.saveAndFlush(productToSave);



        return mapProductEntity2Dto(productToSave);
    }

    @Override
    public ProductDto saveImages(Long id,MultipartFile[] files) {

        if(files==null || files.length==0){
            log.info("No hay imagenes que guardar");
            throw new IllegalArgumentException("No hay datos de imagen que guardar");
        }

        ProductEntity productEntity = repository
                .findById(id)
                .orElseThrow(()-> new ProductNotFoundException("Product not found: "+id));

        log.info("Guardando: "+files.length+" imagenes");
        final List<String> imgs = new ArrayList<>();
        for(MultipartFile file : files){
            if(file!=null && !file.isEmpty()){
                imgs.add(fileStorageService.save(String.valueOf(id),file));
            }
        }

        for(final String resource:imgs){
            try {
                String imgPath = resource;
                log.info("Imagen guardada en: {}", imgPath);
                if(productEntity.getGalleries()==null){
                    productEntity.setGalleries(new ArrayList<>());
                }
                productEntity.getGalleries().add(new GalleryEntity(imgPath,productEntity));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        productEntity = repository.saveAndFlush(productEntity);


        return mapProductEntity2Dto(productEntity);
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

    @Override
    public boolean reduceStock(Long productId, Long quantity) {

        final Optional<ProductEntity> optionalProductEntity = repository.findById(productId);
        if(optionalProductEntity.isEmpty()){
            throw new ProductNotFoundException("Product not found: "+productId);
        }
        ProductEntity productEntity = optionalProductEntity.get();// (dato) -> get

        // 100  >= 5   = true    1

        // 10    >= 10 = true    0
        // 10    >= 7  = false  -1

        if(productEntity.getQuantity().compareTo(BigDecimal.valueOf(quantity) )>=0){
            productEntity.setQuantity(productEntity
                    .getQuantity()
                    .subtract(BigDecimal.valueOf(quantity)));

            repository.saveAndFlush(productEntity);

            //TODO si hay pocos productos es decir hay menos de X productos
            //TODO en producEntity.getQuantity si es menor ejemplo a 10 debe enviar una notificacion por cola
            if(productEntity.getQuantity().compareTo(BigDecimal.valueOf(MIN_LOW_STOCK))<=0){
                productNotificationService.notifyProductLowStock( mapProductEntity2Dto(productEntity) );
            }
            return true;
        }

        return false;
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
        productDto.setCreatedAt(productEntity.getCreatedAt());
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
