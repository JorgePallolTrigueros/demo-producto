package com.producto.demo;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@Log4j2
@SpringBootApplication
@EnableFeignClients
public class DemoProductoApplication {

	public static void main(String[] args) {
		log.info("loading");
		SpringApplication.run(DemoProductoApplication.class, args);
	}

}
