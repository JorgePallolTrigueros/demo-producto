package com.producto.demo.domain;

import com.producto.demo.dto.GalleryDto;
import com.producto.demo.dto.GalleryRequestDto;
import com.producto.demo.exception.GalleryNotFoundException;

import java.util.List;

public interface GalleryService {


    GalleryDto save(GalleryRequestDto galleryRequestDto);

    //READ
    List<GalleryDto> findAll();

    GalleryDto findById(Long id) throws GalleryNotFoundException;

    //UPDATE
    GalleryDto update(GalleryDto galleryDto);

    //DELETE
    void delete(Long id) throws GalleryNotFoundException;


}
