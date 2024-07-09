package com.producto.demo.domain;


import java.util.List;
import java.util.stream.Collectors;
import com.producto.demo.dao.entity.GalleryEntity;
import com.producto.demo.dao.repository.GalleryRepository;
import com.producto.demo.dto.GalleryDto;
import com.producto.demo.dto.GalleryRequestDto;
import com.producto.demo.exception.GalleryNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service // bean
@AllArgsConstructor
public class GalleryServiceImpl implements  GalleryService{

    private final  GalleryRepository repository;
    @Override
    public GalleryDto save(GalleryRequestDto  galleryRequestDto) {

        GalleryEntity  galleryToSave = new  GalleryEntity();
        galleryToSave.setUrl(galleryRequestDto.getUrl());
        galleryToSave.setProductEntityById( galleryRequestDto.getProductId());

        return mapGalleryEntity2Dto(repository.saveAndFlush(galleryToSave));
    }

    @Override
    public List< GalleryDto> findAll() {
        return repository
                .findAll()
                .stream()
                .map(GalleryServiceImpl::mapGalleryEntity2Dto)
                .collect(Collectors.toList());
    }

    @Override
    public  GalleryDto findById(Long id) throws  GalleryNotFoundException {
        return repository
                .findById(id)
                .map(GalleryServiceImpl::mapGalleryEntity2Dto)
                .orElseThrow(()-> new  GalleryNotFoundException(" Gallery not found: "+id));
    }



    @Override
    public  GalleryDto update(final  GalleryDto  galleryDto) {
        return repository
                .findById( galleryDto.getId())
                .map( GalleryEntity -> {

                    mapGalleryDto2Entity( galleryDto,  GalleryEntity);

                    return mapGalleryEntity2Dto(repository.saveAndFlush( GalleryEntity));
                })
                .orElseThrow(()-> new  GalleryNotFoundException(" Gallery not found: "+ galleryDto.getId()));
    }



    @Override
    public void delete(final Long id) throws  GalleryNotFoundException {
        repository
                .findById(id)
                .ifPresentOrElse(
                        repository::delete,
                        ()-> {throw new  GalleryNotFoundException(" Gallery not found: "+id);}
                );
    }

    private static void mapGalleryDto2Entity( GalleryDto  galleryDto,  GalleryEntity  galleryEntity) {
        galleryEntity.setUrl( galleryDto.getUrl());
        galleryEntity.setProductEntityById( galleryDto.getProductId());

    }

    private static  GalleryDto mapGalleryEntity2Dto( GalleryEntity  galleryEntity) {
        GalleryDto  galleryDto = new  GalleryDto();
        galleryDto.setId( galleryEntity.getId());
        galleryDto.setUrl(galleryEntity.getUrl());
        galleryDto.setProductId( galleryEntity.getProductEntity().getId());
        return  galleryDto;
    }
}