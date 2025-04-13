package com.producto.demo.domain;


import com.producto.demo.dao.entity.ProductEntity;
import com.producto.demo.dao.repository.ProductRepository;
import com.producto.demo.dto.ProductDto;
import com.producto.demo.dto.ProductRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@SpringBootTest
public class ProductServiceTest {

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    public void init(){

        var product1 = new ProductEntity();
        product1.setId(1L);
        product1.setName("Producto 1");
        product1.setDescription("P1");
        product1.setPrice(10.00);
        product1.setCategoryById("Category1");

        var product2 = new ProductEntity();
        product2.setId(2L);
        product2.setName("Producto 2");
        product2.setDescription("P2");
        product2.setPrice(10.00);
        product2.setCategoryById("Category2");

        Mockito.when(productRepository.findAll()).thenReturn(List.of(product1,product2));
    }

    @Test
    void test_01_findAll() {
        log.info("test_01_findAll");

        // Action
        List<ProductDto> products = this.productService.findAll();

        // Assertion
        Assertions.assertThat(products).isNotEmpty();
        Assertions.assertThat(products).hasSize(2);
    }

    @Test
    void test_02_save() {
        log.info("test_02_save");

        // Data
        ProductRequestDto productRequestDto = ProductRequestDto
                .builder()
                .name("Producto insert")
                .price(10.00)
                .category("Category")
                .description("Description")
                .build();


        ProductEntity productEntityToInsert = new ProductEntity();
        productEntityToInsert.setName("Producto insert");
        productEntityToInsert.setDescription("Description");
        productEntityToInsert.setPrice(10.00);
        productEntityToInsert.setCategoryById("Category");


        ProductEntity productEntitySaved = new ProductEntity();
        productEntitySaved.setId(3L);
        productEntitySaved.setName("Producto insert");
        productEntitySaved.setDescription("Description");
        productEntitySaved.setPrice(10.00);
        productEntitySaved.setCategoryById("Category");

        // Mock
        Mockito.when(productRepository.saveAndFlush(productEntityToInsert)).thenReturn(productEntitySaved);

        MultipartFile[] files = new MultipartFile[0];

        // Action
        ProductDto productResult = productService.save(productRequestDto,files);

        // Assertion
        Assertions.assertThat(productResult).isNotNull();
    }

}
