package com.producto.demo.domain;

import com.producto.demo.dao.entity.CategoryEntity;
import com.producto.demo.dao.repository.CategoryRepository;
import com.producto.demo.dto.CategoryRequestDto;
import com.producto.demo.dto.CategoryDto;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.List;
import java.util.Locale;

@Slf4j
@SpringBootTest
public class CategoryServiceTest {


    public static final String CATEGORY_1 = "CATEGORY1";
    public static final String CATEGORY_2 = "CATEGORY2";
    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Mock
    private CategoryRepository categoryRepository;


    @BeforeEach
    public void init() {

        var category1 = new CategoryEntity();
        category1.setId(CATEGORY_1);
        category1.setDescription("P1");

        Mockito.when(categoryRepository.findAll()).thenReturn(List.of(category1));
    }


    @Test
    void test_02_save() {
        log.info("test_02_save");

        // Data
        CategoryRequestDto categoryRequestDto = CategoryRequestDto
                .builder()
                .name(CATEGORY_1)
                .description("Description")
                .build();


        // Datos de comprobacion
        CategoryEntity categoryEntityToInsert = new CategoryEntity();
        categoryEntityToInsert.setId(CATEGORY_1);
        categoryEntityToInsert.setDescription("Description");


        CategoryEntity categoryEntitySaved= new CategoryEntity();
        categoryEntitySaved.setId(CATEGORY_1);
        categoryEntitySaved.setDescription("Description");


        // Mock
        Mockito.when(categoryRepository.saveAndFlush(categoryEntityToInsert)).thenReturn(categoryEntitySaved);

        // Action
        CategoryDto categoryResult = categoryService.save(categoryRequestDto);

        // Assertion
        Assertions.assertThat(categoryResult).isNotNull();
    }



}
