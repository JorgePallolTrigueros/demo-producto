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

import java.util.Optional;




@Slf4j
@SpringBootTest
public class ProductRepositoryTest {

    public static final String CATEGORY_1 = "CATEGORY1";
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @Transactional
    public void testSaveProductEntity(){

        var categoryEntity = new CategoryEntity();
        categoryEntity.setId(CATEGORY_1);
        categoryEntity.setDescription("TEST");
        categoryRepository.saveAndFlush(categoryEntity);

        /* insertar mas product*/

        var product1 = new ProductEntity();
        product1.setName("Producto 1");
        product1.setDescription("P1");
        product1.setPrice(10.00);
        product1.setCategoryById(CATEGORY_1);

        var product2 = new ProductEntity();
        product2.setName("Producto 2");
        product2.setDescription("P2");
        product2.setPrice(10.00);
        product2.setCategoryById(CATEGORY_1);

        var product3 = new ProductEntity();
        product3.setName("Producto 3");
        product3.setDescription("P3");
        product3.setPrice(12.00);
        product3.setCategoryById(CATEGORY_1);

        var product4 = new ProductEntity();
        product4.setName("Producto 4");
        product4.setDescription("P4");
        product4.setPrice(11.50);
        product4.setCategoryById(CATEGORY_1);


        var product5 = new ProductEntity();
        product5.setName("Producto 5");
        product5.setDescription("P5");
        product5.setPrice(5.00);
        product5.setCategoryById(CATEGORY_1);

        var product6 = new ProductEntity();
        product6.setName("Producto 6");
        product6.setDescription("P1");
        product6.setPrice(7.00);
        product6.setCategoryById(CATEGORY_1);

        var product7 = new ProductEntity();
        product7.setName("Producto 7");
        product7.setDescription("P7");
        product7.setPrice(10.00);
        product7.setCategoryById(CATEGORY_1);



        log.info("inicializar la base de datos");
        productRepository.saveAndFlush(product1);
    }


    @Test
    @Transactional
    void test_01_findAll() {
        log.info("test_01_findAll");
        productRepository.findAll().forEach(p-> log.info(p.toString()));
    }


    @Test
    @Transactional
    void test_02_findById(){
        log.info("test_02_findById");
        Optional<ProductEntity> findResult = productRepository.findById(1L);

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
        Optional<ProductEntity> findResult = productRepository.findById(-1L);

        if(findResult.isPresent()){
            log.info("Existe");
            log.info(findResult.get().toString());
        }else{
            log.info("No existe");
        }

        Assertions.assertThat(findResult).isEmpty();
    }

    //TODO test de update delete

    @Test
    @Transactional
    void test_03_save(){
        log.info("test_03_save");

        ProductEntity productToSave = new ProductEntity();

        productToSave.setName("producto nuevo");
        productToSave.setPrice(100.00);
        productToSave.setDescription("producto a guardar");
        productToSave.setCategoryById(CATEGORY_1);


        ProductEntity userSaved = productRepository.saveAndFlush(productToSave);

        Optional<ProductEntity> findResult = productRepository.findById(userSaved.getId());

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

        productRepository.deleteById(1L);

        Optional<ProductEntity> findResult = productRepository.findById(1L);

        if(findResult.isPresent()){
            log.info("No se pudo borrar");
        }else{
            log.info("Se borro exitosamente");
        }

        Assertions.assertThat(findResult).isEmpty();
    }

}
