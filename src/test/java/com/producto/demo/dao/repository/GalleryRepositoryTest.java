package com.producto.demo.dao.repository;

import com.producto.demo.dao.entity.CategoryEntity;
import com.producto.demo.dao.entity.GalleryEntity;
import com.producto.demo.dao.entity.ProductEntity;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@Slf4j
@SpringBootTest
public class GalleryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private GalleryRepository galleryRepository;

    @Autowired
    private ProductRepository productRepository;

    public static boolean saved = false;


    @BeforeEach
    @Transactional
    public void init(){

        if(saved){
            return;
        }


            var categoryEntity = new CategoryEntity();
            categoryEntity.setId("CATEGORY_1");
            categoryEntity.setDescription("TEST");
            categoryRepository.saveAndFlush(categoryEntity);




            var product1 = new ProductEntity();
            product1.setName("Producto 1");
            product1.setDescription("P1");
            product1.setPrice(10.00);
            product1.setCategoryById("CATEGORY_1");

            product1 = productRepository.saveAndFlush(product1);



        GalleryEntity gallery1 = new GalleryEntity();
        gallery1.setUrl("URL 1");
        gallery1.setProductEntityById(1L);


        GalleryEntity gallery2 = new GalleryEntity();
        gallery2.setUrl("URL 1");
        gallery2.setProductEntityById(1L);

        this.galleryRepository.saveAndFlush(gallery1);
        this.galleryRepository.saveAndFlush(gallery2);


        saved= true;
    }

    @Test
    @Transactional
    void test_01_findAll() {
        log.info("test_01_findAll");
        galleryRepository.findAll().forEach(p-> log.info(p.toString()));
    }

    @Test
    @Transactional
    void test_02_findById(){
        log.info("test_02_findById");
        Optional<GalleryEntity> findResult = galleryRepository.findById(1L);

        if(findResult.isPresent()){
            log.info("Existe");
            log.info(findResult.get().toString());
        }else{
            log.info("No existe");
        }

        Assertions.assertThat(findResult).isNotEmpty();
    }


    @Test
    void test_02_findById_ExpectedNotExist(){
        log.info("test_02_findById");
        Optional<GalleryEntity> findResult = galleryRepository.findById(-1L);

        if(findResult.isPresent()){
            log.info("Existe");
            log.info(findResult.get().toString());
        }else{
            log.info("No existe");
        }

        Assertions.assertThat(findResult).isEmpty();
    }



    @Test
    @Transactional
    void test_03_save(){
        log.info("test_03_save");


        var categoryEntity = new CategoryEntity();
        categoryEntity.setId("CATEGORY_1");
        categoryEntity.setDescription("TEST");
        categoryRepository.saveAndFlush(categoryEntity);




        var product1 = new ProductEntity();
        product1.setName("Producto 1");
        product1.setDescription("P1");
        product1.setPrice(10.00);
        product1.setCategoryById("CATEGORY_1");

        product1 = productRepository.saveAndFlush(product1);

        GalleryEntity galleryToSave = new GalleryEntity();

        galleryToSave.setUrl("galeria ");
        galleryToSave.setProductEntityById(product1.getId());


        GalleryEntity gallerySaved = galleryRepository.saveAndFlush(galleryToSave);

        Optional<GalleryEntity> findResult = galleryRepository.findById(gallerySaved.getId());

        if(findResult.isPresent()){
            log.info("Existe");
            log.info(findResult.get().toString());
        }else{
            log.info("No existe");
        }

        Assertions.assertThat(findResult).isNotEmpty();
    }



    @Test
    void test_04_delete(){
        log.info("test_04_delete");

        galleryRepository.deleteById(1L);

        Optional<GalleryEntity> findResult = galleryRepository.findById(1L);

        if(findResult.isPresent()){
            log.info("No se pudo borrar");
        }else{
            log.info("Se borro exitosamente");
        }

        Assertions.assertThat(findResult).isEmpty();
    }

}

