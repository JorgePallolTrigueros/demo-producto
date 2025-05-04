package com.producto.demo.dao.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Entity
@Table(name = "gallery")
public class GalleryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty
    private String url;
    @ManyToOne(optional = false)
    @JoinColumn(name="product_id", nullable=false, updatable=false)
    private ProductEntity productEntity;

    public GalleryEntity() {
    }

    public GalleryEntity(String url, ProductEntity productEntity) {
        this.url = url;
        this.productEntity = productEntity;
    }

    public void setProductEntityById(Long id){
        this.productEntity = new ProductEntity();
        productEntity.setId(id);
    }
}
