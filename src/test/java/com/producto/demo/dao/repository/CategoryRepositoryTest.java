package com.producto.demo.dao.repository;

import com.producto.demo.dao.entity.CategoryEntity;
import com.producto.demo.dao.entity.ProductEntity;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Locale;
import java.util.Optional;

@Slf4j
@SpringBootTest
public class CategoryRepositoryTest {


    public static final String CATEGORY_1 = "CATEGORY 1";
    @Autowired
    private CategoryRepository categoryRepository;


    @BeforeEach
    public void init(){
        CategoryEntity category1 = new CategoryEntity();
        category1.setId(CATEGORY_1);
        category1.setDescription("DESC CATEGORY 1");


        CategoryEntity category2 = new CategoryEntity();
        category2.setId("CATEGORY 2");
        category2.setDescription("DESC CATEGORY 2");

        this.categoryRepository.saveAndFlush(category1);
        this.categoryRepository.saveAndFlush(category2);
    }

    @Test
    @Transactional
    void test_01_findAll() {
        log.info("test_01_findAll");
        categoryRepository.findAll().forEach(p-> log.info(p.toString()));
    }

    @Test
    @Transactional
    void test_02_findById(){
        log.info("test_02_findById");
        Optional<CategoryEntity> findResult = categoryRepository.findById(CATEGORY_1);

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
        Optional<CategoryEntity> findResult = categoryRepository.findById("NO EXIST");

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

        CategoryEntity categoryToSave = new CategoryEntity();

        categoryToSave.setId("producto nuevo");
        categoryToSave.setDescription("100.00");


        CategoryEntity userSaved = categoryRepository.saveAndFlush(categoryToSave);

        Optional<CategoryEntity> findResult = categoryRepository.findById(userSaved.getId());

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

        categoryRepository.deleteById(CATEGORY_1);

        Optional<CategoryEntity> findResult = categoryRepository.findById(CATEGORY_1);

        if(findResult.isPresent()){
            log.info("No se pudo borrar");
        }else{
            log.info("Se borro exitosamente");
        }

        Assertions.assertThat(findResult).isEmpty();
    }


}
