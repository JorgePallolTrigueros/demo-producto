package com.producto.demo.exception;

public class GalleryNotFoundException extends RuntimeException{

    public GalleryNotFoundException(String message){
        super(message);
    }

}