package com.producto.demo.exception;

//Exception          : try{....} catch { .. }       : Ventaja- más control por que sabes que la tienes que manejar , Desventaja- obliga a manejar la excepcion o declarar throw al metodo
//   |
//   v
//Runtime Exception  : No obliga a tener try catch  : Ventaja- es menos restrictivo que el exception, se puede usar en lamba , Desventaja: menos control, por que no sabes cuando ni donde puede ocurrir
public class ProductNotFoundException extends RuntimeException{

    public ProductNotFoundException(String message){
        super(message);
    }

}
