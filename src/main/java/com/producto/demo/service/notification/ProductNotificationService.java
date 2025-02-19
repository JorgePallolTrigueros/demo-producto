package com.producto.demo.service.notification;

import com.producto.demo.dto.ProductDto;

public interface ProductNotificationService {


    boolean notifyProductLowStock(ProductDto productDto);

}
