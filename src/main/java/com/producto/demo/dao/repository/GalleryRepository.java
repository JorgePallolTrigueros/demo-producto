package com.producto.demo.dao.repository;

import com.producto.demo.dao.entity.GalleryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GalleryRepository extends JpaRepository<GalleryEntity,Long> {

}
