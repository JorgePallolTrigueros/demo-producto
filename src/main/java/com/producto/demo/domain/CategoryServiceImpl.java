package com.producto.demo.domain;

import com.producto.demo.dao.entity.CategoryEntity;
import com.producto.demo.dao.repository.CategoryRepository;
import com.producto.demo.dto.CategoryDto;
import com.producto.demo.dto.CategoryRequestDto;
import com.producto.demo.exception.CategoryNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service // bean
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;


    @Override
    public CategoryDto save(CategoryRequestDto categoryRequestDto) {

        CategoryEntity categoryToSave = new CategoryEntity();
        categoryToSave.setId(categoryRequestDto.getName());
        categoryToSave.setDescription(categoryRequestDto.getDescription());

        return mapCategoryEntity2Dto(repository.saveAndFlush(categoryToSave));
    }

    @Override
    public List<CategoryDto> findAll() {
        return repository
                .findAll()
                .stream()
                .map(CategoryServiceImpl::mapCategoryEntity2Dto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto findById(String id) throws CategoryNotFoundException {
        return repository
                .findById(id)
                .map(CategoryServiceImpl::mapCategoryEntity2Dto)
                .orElseThrow(() -> new CategoryNotFoundException("category not found: " + id));
    }


    @Override
    public CategoryDto update(final CategoryDto categoryDto) {
        return repository
                .findById(categoryDto.getId())
                .map(categoryEntity -> {

                    mapCategoryDto2Entity(categoryDto, categoryEntity);

                    return mapCategoryEntity2Dto(repository.saveAndFlush(categoryEntity));
                })
                .orElseThrow(() -> new CategoryNotFoundException("category not found: " + categoryDto.getId()));
    }


    @Override
    public void delete(final String id) throws CategoryNotFoundException {
        repository
                .findById(id)
                .ifPresentOrElse(
                        repository::delete,
                        () -> {
                            throw new CategoryNotFoundException("category not found: " + id);
                        }
                );
    }

    private static void mapCategoryDto2Entity(CategoryDto categoryDto, CategoryEntity categoryEntity) {
        categoryEntity.setDescription(categoryDto.getDescription());
    }

    private static CategoryDto mapCategoryEntity2Dto(CategoryEntity categoryEntity) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(categoryEntity.getId());
        categoryDto.setDescription(categoryEntity.getDescription());
        return categoryDto;
    }

}
