package com.producto.demo.service.notification;

import com.producto.demo.dto.ProductDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ActiveMQProductNotificationService implements ProductNotificationService {

    private final JmsTemplate jmsTemplate;
    private final String lowStockQueue;

    public ActiveMQProductNotificationService(JmsTemplate jmsTemplate,@Value("active-mq.product-low-stock-queue") String lowStockQueue) {
        this.jmsTemplate = jmsTemplate;
        this.lowStockQueue = lowStockQueue;
    }


    @Override
    public boolean notifyProductLowStock(ProductDto productDto) {
        try{

            jmsTemplate.convertAndSend(lowStockQueue,productDto);

            log.info("Se ha enviado notificacion de producto con bajo stock a la cola: {} , producto: {}",lowStockQueue,productDto);
            return true;
        }catch (final Exception exception){
            log.error("Ha ocurrido un erro al intentar enviar la cola: {}",lowStockQueue,exception);
        }
        return false;
    }
}
